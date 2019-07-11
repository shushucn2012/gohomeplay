package com.park61.teacherhelper.module.course;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.adapter.FileSourcesAdapter;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.MyDownLoadAttachActivity;
import com.park61.teacherhelper.module.okdownload.fragment.DownloadedAttachFragment;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;

public class AttachmentDLActivity extends BaseActivity {

    private ListView lv_files;
    private TextView tv_files_num;

    private List<FileItemSource> fList = new ArrayList<>();
    private FileSourcesAdapter fileSourcesAdapter;
    private boolean isShowed;//是否显示下载提示
    private int coursePlanId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_attachment_dl);
    }

    @Override
    public void initView() {

        lv_files = (ListView) findViewById(R.id.lv_files);
        tv_files_num = findViewById(R.id.tv_files_num);
    }

    @Override
    public void initData() {
        setPagTitle("相关课件");
        coursePlanId = getIntent().getIntExtra("coursePlanId", -1);

        fileSourcesAdapter = new FileSourcesAdapter(AttachmentDLActivity.this, fList);
        lv_files.setAdapter(fileSourcesAdapter);

        ansycGetFileList();
    }

    @Override
    public void initListener() {
        ((TextView) findViewById(R.id.tv_area_right)).setText("我的下载");
        findViewById(R.id.tv_area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyDownLoadAttachActivity.class));
            }
        });
        findViewById(R.id.area_how_to_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                it.putExtra("url", "http://m.61park.cn/teach/#/servicedict/index/355");
                startActivity(it);
            }
        });

        findViewById(R.id.img_download_all).setOnClickListener(v -> {
            if (!CommonMethod.checkUserLogin(mContext))
                return;
            //下载全部 添加进度框
            showDownLoadTip();
            showDialog();
            for (int i = 0; i < fList.size(); i++) {
                FileItemSource fileItemSource = fList.get(i);
                if (fileItemSource.getStatus() != -1 || HttpUrl.parse(fileItemSource.getSourceUrl()) == null) {//已经开始下载了就跳过
                    continue;
                }

                //下载附件
                FileModel fileModel = new FileModel();
                fileModel.setUrl(fileItemSource.getSourceUrl());
                fileModel.setName(fileItemSource.getSourceName());
                fileModel.setCreateTime(DownloadService.fomatCurrentTime());
                fileModel.setPath(OkDownload.getInstance().getFolder() + fileItemSource.getSourceName());
                fileModel.setSize(fileItemSource.getShowFileSize());
                fileModel.setIconUrl(fileItemSource.getIconUrl());
                GetRequest<File> request = OkGo.<File>get(fileModel.getUrl());
                OkDownload.request(fileModel.getUrl(), request).extra1(fileModel).save();

                if (OkDownload.getInstance().getTask(fileItemSource.getSourceUrl()) != null) {
                    DownloadTask task = OkDownload.getInstance().getTask(fileItemSource.getSourceUrl());
                    task.register(new ListDownloadListener(fileItemSource));
                }

                fileItemSource.setStatus(0);//未下载完成状态
                fileItemSource.setTotalDownloadNum(FU.addDownLoadNumStr(fileItemSource.getTotalDownloadNum()));
                addDowLoadNum(fileItemSource.getId());
            }
            fileSourcesAdapter.notifyDataSetChanged();
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    OkDownload.getInstance().startAll();
                    dismissDialog();
                }
            }.sendEmptyMessageDelayed(0, 1500);
        });
    }

    /**
     * 获取附件列表
     */
    public void ansycGetFileList() {
        String wholeUrl = AppUrl.host + AppUrl.getAttachmentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, flister);
    }

    BaseRequestListener flister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0) {
                return;
            }
            fList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                FileItemSource source = gson.fromJson(jot.toString(), FileItemSource.class);
                source.setStatus(ViewInitTool.getFilesItemDownLoadStatus(source.getSourceUrl()));//下载状态
                fList.add(source);
            }
            tv_files_num.setText("(共" + fList.size() + "个)");
            fileSourcesAdapter.notifyDataSetChanged();

            fileSourcesAdapter.setOnDownLoadClickedLsner(index -> {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                FileItemSource fileItemSource = fList.get(index);
                if (fileItemSource.getStatus() != -1) {
                    return;
                }
                if (HttpUrl.parse(fileItemSource.getSourceUrl()) == null) {
                    showShortToast("文件地址有误，无法下载");
                    return;
                }

                showDownLoadTip();

                //下载附件
                FileModel fileModel = new FileModel();
                fileModel.setUrl(fileItemSource.getSourceUrl());
                fileModel.setName(fileItemSource.getSourceName());
                fileModel.setCreateTime(DownloadService.fomatCurrentTime());
                fileModel.setPath(OkDownload.getInstance().getFolder() + fileItemSource.getSourceName());
                fileModel.setSize(fileItemSource.getShowFileSize());
                fileModel.setIconUrl(fileItemSource.getIconUrl());
                GetRequest<File> request = OkGo.<File>get(fileModel.getUrl());
                OkDownload.request(fileModel.getUrl(), request).extra1(fileModel).save().start();

                if (OkDownload.getInstance().getTask(fileItemSource.getSourceUrl()) != null) {
                    DownloadTask task = OkDownload.getInstance().getTask(fileItemSource.getSourceUrl());
                    task.register(new ListDownloadListener(fileItemSource));
                }

                fileItemSource.setStatus(0);//未下载完成状态
                fileItemSource.setTotalDownloadNum(FU.addDownLoadNumStr(fileItemSource.getTotalDownloadNum()));
                fileSourcesAdapter.notifyDataSetChanged();
                addDowLoadNum(fileItemSource.getId());
            });
        }
    };

    public void addDowLoadNum(int id) {
        String wholeUrl = AppUrl.host + AppUrl.addDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    private class ListDownloadListener extends DownloadListener {

        public ListDownloadListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //更新列表
            for (int i = 0; i < fList.size(); i++) {
                FileItemSource source = fList.get(i);
                if (source == tag) {
                    source.setStatus(1);
                    break;
                }
            }
            fileSourcesAdapter.notifyDataSetChanged();
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    /**
     * 显示下载提示
     */
    private void showDownLoadTip() {
        if (!isShowed) {
            showShortToast("已加入下载列表，可在我的-我的下载中查看");
            isShowed = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DownloadedAttachFragment.DOWNLOADED_REFRESH) {
            if (data != null) {
                String operate = data.getStringExtra("operate");
                if ("toWx".equals(operate)) {
                    toWx(data.getIntExtra("position", -1));
                } else if ("toQQ".equals(operate)) {
                    toQQ(data.getIntExtra("position", -1));
                }
            }
        }
    }

    /**
     * 分享到微信
     */
    public void toWx(int position) {
        String attachMemtUrl = fList.get(position).getSourceUrl();
        com.lzy.okserver.download.DownloadTask task = null;
        //查询课件下载数据
        List<com.lzy.okserver.download.DownloadTask> fileTmps = OkDownload.restore(DownloadManager.getInstance().getFinished());
        for (com.lzy.okserver.download.DownloadTask t : fileTmps) {
            if (t.progress.extra1 != null && attachMemtUrl.equals(((FileModel)t.progress.extra1).getUrl())) {
                task = t;
            }
        }

        // 实例化
        IWXAPI wxApi = WXAPIFactory.createWXAPI(mContext, GlobalParam.WX_APP_ID);
        wxApi.registerApp(GlobalParam.WX_APP_ID);
        if (!wxApi.isWXAppInstalled()) {
            showShortToast("您还没有安装微信，请下载安装后再试！");
            return;
        }

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

    /**
     * 分享到QQ
     */
    public void toQQ(int position) {
        String attachMemtUrl = fList.get(position).getSourceUrl();
        com.lzy.okserver.download.DownloadTask task = null;
        //查询课件下载数据
        List<com.lzy.okserver.download.DownloadTask> fileTmps = OkDownload.restore(DownloadManager.getInstance().getFinished());
        for (com.lzy.okserver.download.DownloadTask t : fileTmps) {
            if (t.progress.extra1 != null && attachMemtUrl.equals(((FileModel)t.progress.extra1).getUrl())) {
                task = t;
            }
        }

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
                shareUri = FileProvider.getUriForFile(mContext, "com.park61.teacherhelper.fileprovider", file);
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
}
