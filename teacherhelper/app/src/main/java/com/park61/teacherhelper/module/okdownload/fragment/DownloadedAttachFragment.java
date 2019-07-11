package com.park61.teacherhelper.module.okdownload.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okserver.OkDownload;
import com.park61.teacherhelper.AudioBaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.databinding.FragmentDownloadedAttachBinding;
import com.park61.teacherhelper.module.okdownload.DownloadListActivity;
import com.park61.teacherhelper.module.okdownload.adapter.DownloadedFileAdapter;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.module.okdownload.widget.SimpleTask;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/1/25.
 * <p>
 * 已下载列表
 */

public class DownloadedAttachFragment extends AudioBaseFragment {

    public static final int DOWNLOADED_REFRESH = 0x0007;
    FragmentDownloadedAttachBinding binding;

    private List<com.lzy.okserver.download.DownloadTask> files = new ArrayList<>();
    LRecyclerViewAdapter mFileAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloaded_attach, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        //查询课件下载数据
        List<com.lzy.okserver.download.DownloadTask> fileTmps = OkDownload.restore(DownloadManager.getInstance().getFinished());
        for (com.lzy.okserver.download.DownloadTask t : fileTmps) {
            if (t.progress.extra1 != null) {
                files.add(t);
            } else {
                t.remove(true);
            }
        }
        DownloadedFileAdapter adapter1 = new DownloadedFileAdapter(getActivity(), files);
        mFileAdapter = new LRecyclerViewAdapter(adapter1);
        binding.filesLv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.filesLv.setAdapter(mFileAdapter);
    }

    @Override
    public void initListener() {
        binding.filesLv.setPullRefreshEnabled(false);
        setListNum(files.size());
        binding.setManageClick(v -> {
            //管理课件
            if (files.size() > 0) {
                goFileListActivity();
            }
        });
    }

    /**
     * ... 弹窗后删除 点击项
     */
    public void delete(int position) {
        //课件
        com.lzy.okserver.download.DownloadTask task = files.get(position);
        task.remove(true);
        files.remove(position);
        setListNum(files.size());
        mFileAdapter.notifyDataSetChanged();
    }

    /**
     * 分享到微信
     */
    public void toWx(int position) {
        if (files != null) {

            // 实例化
            IWXAPI wxApi = WXAPIFactory.createWXAPI(getContext(), GlobalParam.WX_APP_ID);
            wxApi.registerApp(GlobalParam.WX_APP_ID);
            if (!wxApi.isWXAppInstalled()) {
                showShortToast("您还没有安装微信，请下载安装后再试！");
                return;
            }
            com.lzy.okserver.download.DownloadTask task = files.get(position);
            if (task.progress.currentSize > 1024 * 1024 * 25) {
                showShortToast("文件大小超过25M");
                return;
            }
            WXFileObject fileObject = new WXFileObject();
            fileObject.setContentLengthLimit(1024 * 1024 * 25);
            fileObject.setFilePath(task.progress.filePath);//设置文件本地地址
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = fileObject;
            msg.title = ((FileModel) task.progress.extra1).getName();

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            wxApi.sendReq(req);
        }
    }

    /**
     * 分享到QQ
     */
    public void toQQ(int position) {
        com.lzy.okserver.download.DownloadTask task = files.get(position);
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            ComponentName component = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            share.setComponent(component);
            File file = new File(task.progress.filePath);//这里是文件位置
            String name = ((FileModel) task.progress.extra1).getName();
            FilesManager.copyFile(file, name);
            File newFile = new File(OkDownload.getInstance().getFolder(), name);
            if (newFile.exists()) {
                file = newFile;
            }
            //大于7.1
            Uri shareUri = null;
            if (Build.VERSION.SDK_INT > 25) {
                shareUri = FileProvider.getUriForFile(parentActivity, "com.park61.teacherhelper.fileprovider", file);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                shareUri = Uri.fromFile(file);
            }
            share.putExtra(Intent.EXTRA_STREAM, shareUri);
            share.setType("*/*");
            startActivity(Intent.createChooser(share, "发送"));
        } catch (Exception e) {
            showShortToast(e.getMessage());
        }
    }

    private void setListNum(int num) {
        if (num > 0) {
            //隐藏缺省图
            binding.lvEmpty.setVisibility(View.GONE);
        } else {
            //显示缺省图
            binding.lvEmpty.setVisibility(View.VISIBLE);
        }
        binding.numTv.setText("(共" + num + "个)");
    }

    private void goFileListActivity() {
        List<SimpleTask> simpleTasks = new ArrayList<>();
        for (com.lzy.okserver.download.DownloadTask t : files) {
            SimpleTask s = new SimpleTask();
            FileModel model = (FileModel) t.progress.extra1;
            //url作为下载tag
            s.setVid(model.getUrl());
            s.setTitle(model.getName());
            s.setSize(model.getSize());
            simpleTasks.add(s);
        }

        Intent it = new Intent(getActivity(), DownloadListActivity.class);
        it.putExtra("flag", DOWNLOADED_REFRESH);
        it.putExtra("list", (Serializable) simpleTasks);
        //是否是课件
        it.putExtra("isFile", true);
        it.putExtra("fileNum", files.size());
        getActivity().startActivity(it);
    }

    /**
     * 管理界面删除后刷新界面
     */
    public void refresh() {
        //刷新课件列表
        files.clear();
        List<com.lzy.okserver.download.DownloadTask> fileTmps = OkDownload.restore(DownloadManager.getInstance().getFinished());
        for (com.lzy.okserver.download.DownloadTask t : fileTmps) {
            if (t.progress.extra1 != null) {
                files.add(t);
            } else {
                t.remove(true);
            }
        }
        setListNum(files.size());
        mFileAdapter.notifyDataSetChanged();
    }

}
