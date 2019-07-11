package com.park61.teacherhelper.module.okdownload.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.databinding.FragmentDownloadingBinding;
import com.park61.teacherhelper.module.okdownload.DownloadListActivity;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.MyDownLoadAttachActivity;
import com.park61.teacherhelper.module.okdownload.MyDownLoadVAdioActivity;
import com.park61.teacherhelper.module.okdownload.adapter.DownloadingAdapter;
import com.park61.teacherhelper.module.okdownload.adapter.DownloadingFileAdapter;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.module.okdownload.widget.SimpleTask;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/1/25.
 * <p>
 * 下载中任务列表
 * <p>
 * 分为音视频下载 和 附件下载
 */

public class DownloadingFragment extends BaseFragment implements DownloadingAdapter.ReDownloadListener {

    public static final int DOWNLOADING_REFRESH = 0x0008;
    FragmentDownloadingBinding binding;
    private boolean isPrepare = false;
    private boolean isDownload = true;
    private List<AliyunDownloadMediaInfo> tasks = new ArrayList<>();
    private List<DownloadTask> files = new ArrayList<>();
    DownloadingAdapter mAdapter;
    DownloadingFileAdapter mFileAdapter;
    private AliyunDownloadManager downloadManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloading, container, false);
        parentView = binding.getRoot();
        isPrepare = true;
        downloadManager = AliyunDownloadManager.getInstance(parentActivity);
        if (getActivity() instanceof MyDownLoadAttachActivity) {//附件中隐藏音视频下载列表
            binding.downloadingLv.setVisibility(View.GONE);
        } else if (getActivity() instanceof MyDownLoadVAdioActivity) {//音视频中隐藏附件下载列表
            binding.downloadingFileLv.setVisibility(View.GONE);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData();
        }
    }

    private void loadData() {
        if (isPrepare) {
            isPrepare = false;
            //阿里云音视频下载任务
            List<AliyunDownloadMediaInfo> list = downloadManager.getUnfinishedDownload();
            if (list != null) {
                for (AliyunDownloadMediaInfo info : list) {
                    if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                        if (info.getProgress() < 100 && DownloadDAO.getInstance().hasTask(info.getVid())) {
                            tasks.add(info);
                        }
                    } else {
                        //下载完成但是本地数据库没存
                        com.park61.teacherhelper.module.okdownload.db.DownloadTask t = DownloadDAO.getInstance().selectTask(info.getVid());
                        if (t != null && t.getTask_status() != 1) {
                            DownloadDAO.getInstance().delete(info.getVid());
                            downloadManager.removeDownloadMedia(info);
                        }
                    }
                }
            }
            mAdapter = new DownloadingAdapter(getActivity(), tasks);
            mAdapter.setReDownloadListener(this);
            binding.downloadingLv.setAdapter(mAdapter);
            binding.downloadingLv.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (getActivity() instanceof MyDownLoadAttachActivity) {
                //进度监听
                ((MyDownLoadAttachActivity) getActivity()).setProgressListener(mAdapter);
            } else if (getActivity() instanceof MyDownLoadVAdioActivity) {
                //进度监听
                ((MyDownLoadVAdioActivity) getActivity()).setProgressListener(mAdapter);
            }
            //课件url下载任务
            List<DownloadTask> fileDatas = OkDownload.restore(DownloadManager.getInstance().getDownloading());
            for (DownloadTask t : fileDatas) {
                if (t.progress.fraction < 1) {
                    files.add(t);
                }
            }
            mFileAdapter = new DownloadingFileAdapter(getActivity(), files);
            binding.downloadingFileLv.setAdapter(mFileAdapter);
            binding.downloadingFileLv.setLayoutManager(new LinearLayoutManager(getActivity()));

            refresh();
        }
    }

    @Override
    public void reDownload(String vid) {
        String sid = DownloadDAO.getInstance().selectSid(vid);
        if (!TextUtils.isEmpty(sid)) {
            getPlayAuth(sid);
        }
    }

    /**
     * 退出应用重新进入时，重新下载需要凭证
     */
    public void getPlayAuth(String sid) {
        String wholeUrl = AppUrl.host + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", sid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alister);
    }

    BaseRequestListener alister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String vid = jsonResult.optString("videoId");
            String playAuth = jsonResult.optString("videoPlayAuth");

            com.park61.teacherhelper.module.okdownload.db.DownloadTask t = DownloadDAO.getInstance().selectTask(vid);
            if (t != null) {
                Intent it = new Intent(getActivity(), DownloadService.class);
                it.putExtra("title", t.getTask_name());
                it.putExtra("sid", FU.paseLong(t.getSourceId()));
                it.putExtra("vid", t.getTask_vid());
                it.putExtra("contentId", FU.paseLong(t.getContentId()));
                it.putExtra("type", t.getTask_type());
                it.putExtra("icon", t.getTask_icon());
                it.putExtra("playAuth", playAuth);
                getActivity().startService(it);
            }
        }
    };

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        binding.setPauseClick(v -> {
            //切换下载状态
            if (isDownload) {
                //全部暂停
                binding.downloadState.setText(getText(R.string.download_all));
                binding.downloadIcon.setImageResource(R.mipmap.icon_caching);
                downloadManager.stopDownloadMedias(tasks);
                OkDownload.getInstance().pauseAll();
            } else {
                //开始下载全部
                binding.downloadIcon.setImageResource(R.mipmap.icon_waiting);
                binding.downloadState.setText(getText(R.string.pause_all));
                for (AliyunDownloadMediaInfo info : tasks) {
                    int tmp = downloadManager.startDownloadMedia(info);
                    //如果退出应用重新打开app时，tmp不为0无法继续下载，重新添加到service
                    if (tmp != AliyunErrorCode.ALIVC_SUCCESS.getCode()) {
                        reDownload(info.getVid());
                    }
                }
                OkDownload.getInstance().startAll();
            }
            isDownload = !isDownload;
        });

        binding.setManageClick(v -> {
            //跳转管理界面
            if (tasks.size() + files.size() > 0) {
                goDownListActivity();
            }
        });
    }

    private void goDownListActivity() {
        List<SimpleTask> simpleTasks = new ArrayList<>();
        //aliyun音视频
        for (AliyunDownloadMediaInfo info : tasks) {
            SimpleTask s = new SimpleTask();
            s.setVid(info.getVid());
            s.setTitle(info.getTitle());
            s.setSize(Formatter.formatFileSize(getActivity(), info.getSize()));
            simpleTasks.add(s);
        }
        //课件列表
        for (DownloadTask d : files) {
            SimpleTask s = new SimpleTask();
            s.setVid(d.progress.tag);
            s.setTitle(((FileModel) d.progress.extra1).getName());
            s.setSize(((FileModel) d.progress.extra1).getSize());
            simpleTasks.add(s);
        }
        Intent it = new Intent(getActivity(), DownloadListActivity.class);
        it.putExtra("flag", DOWNLOADING_REFRESH);
        it.putExtra("list", (Serializable) simpleTasks);
        //是否有课件
        if (files.size() > 0) {
            it.putExtra("isFile", true);
            it.putExtra("fileNum", files.size());
        }
        getActivity().startActivity(it);
    }

    /**
     * 管理界面删除刷新
     */
    public void deleteDownload() {
        tasks.clear();
        List<AliyunDownloadMediaInfo> list = downloadManager.getUnfinishedDownload();
        if (list != null) {
            for (AliyunDownloadMediaInfo info : list) {
                if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                    if (info.getProgress() < 100 && DownloadDAO.getInstance().hasTask(info.getVid()))
                        tasks.add(info);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        //课件刷新
        files.clear();
        List<DownloadTask> fileDatas = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        for (DownloadTask t : fileDatas) {
            if (t.progress.fraction < 1) {
                files.add(t);
            }
        }
        mFileAdapter.notifyDataSetChanged();
        refresh();
    }

    /**
     * 下载完成刷新
     */
    public void refresh() {
        /*if (tasks.size() == 0 && files.size() == 0) {
            binding.lvEmpty.setVisibility(View.VISIBLE);
            binding.manageRylt.setVisibility(View.GONE);
        } else {
            binding.lvEmpty.setVisibility(View.GONE);
            binding.manageRylt.setVisibility(View.VISIBLE);
        }*/

        if (getActivity() instanceof MyDownLoadAttachActivity && files.size() == 0) {//附件中隐藏音视频下载列表
            binding.lvEmpty.setVisibility(View.VISIBLE);
            binding.manageRylt.setVisibility(View.GONE);
        } else if (getActivity() instanceof MyDownLoadVAdioActivity && tasks.size() == 0) {//音视频中隐藏附件下载列表
            binding.lvEmpty.setVisibility(View.VISIBLE);
            binding.manageRylt.setVisibility(View.GONE);
        } else {
            binding.lvEmpty.setVisibility(View.GONE);
            binding.manageRylt.setVisibility(View.VISIBLE);
        }
    }
}
