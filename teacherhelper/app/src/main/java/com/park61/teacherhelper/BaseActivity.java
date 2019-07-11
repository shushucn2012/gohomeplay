package com.park61.teacherhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayerview.utils.NetWatchdog;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okserver.OkDownload;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.home.HomeModularListActivity;
import com.park61.teacherhelper.module.my.MyMainActivity;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;
import com.park61.teacherhelper.module.workplan.WorkPlanMainWholeActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.CommonProgressDialog;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;
import com.park61.teacherhelper.widget.pw.SharePopWin;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础activity类
 *
 * @author super
 */
public abstract class BaseActivity extends FragmentActivity {
    public Context mContext;
    /**
     * log打印需要用到的标签
     */
    public String Tag = "BaseActivity";
    public String actTag;// 网络请求标记，用于标识请求以便取消
    /**
     * 传入参数为字符串拼接方式的网络请求实例
     */
    public OkHttpUtils netRequest;
    public CommonProgressDialog commonProgressDialog;
    public DoubleDialog dDialog;
    public AliyunDownloadManager downloadManager;
    public NetWatchdog netWatchdog;
    public Gson gson;

    public View area_left, area_right;
    public TextView page_title;

    //public String shareUrl = "", sharePic = "", shareTitle = "", shareDescription = "";

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //友盟推送
        PushAgent.getInstance(mContext).onAppStart();

        setLayout();
        ExitAppUtils.getInstance().addActivity(this);
        Tag = this.getClass().getSimpleName();
        logout("Tag------onCreate-----" + Tag);
        actTag = Tag + System.currentTimeMillis();
        netRequest = OkHttpUtils.getInstance();
        netRequest.setActTag(actTag);
        netRequest.setMainActivity(this);
        commonProgressDialog = new CommonProgressDialog(mContext);
        // 进度框不可取消
        setCancelable(false);
        downloadManager = AliyunDownloadManager.getInstance(getApplicationContext());
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetChangeListener(new NetWatchdog.NetChangeListener() {
            @Override
            public void onWifiTo4G() {
                if (TApplication.isShowNetDialog && GlobalParam.userToken != null && hasDownloading()) {
                    showNetChangeDialog();
                }
            }

            @Override
            public void on4GToWifi() {

            }

            @Override
            public void onNetDisconnected() {

            }
        });
        netWatchdog.startWatch();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        dDialog = new DoubleDialog(mContext);
        initView();
        initTopBar();
        initData();
        initListener();
    }

    private boolean hasDownloading() {
        List<AliyunDownloadMediaInfo> downloadingMedias = downloadManager.getUnfinishedDownload();
        if (downloadingMedias != null) {
            for (AliyunDownloadMediaInfo info : downloadingMedias) {
                if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete && info.getProgress() < 100) {
                    return true;
                }
            }
        }
        return DownloadManager.getInstance().getDownloading().size() > 0;
    }

    private AlertDialog netChangeDialog = null;

    private void showNetChangeDialog() {
        if (netChangeDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialogBuilder.setTitle(R.string.net_change_tip);
            dialogBuilder.setMessage(R.string.continue_to_download);
            dialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                netChangeDialog = null;
                dialog.dismiss();
                TApplication.isShowNetDialog = false;
                List<AliyunDownloadMediaInfo> downloadMediaInfos = downloadManager.getUnfinishedDownload();
                if (downloadMediaInfos != null) {
                    for (AliyunDownloadMediaInfo info : downloadMediaInfos) {
                        if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                            //重新下载
                            int tmp = downloadManager.startDownloadMedia(info);
                            Log.e(Tag, "继续下载:" + tmp);
                            //如果退出应用重新打开app时，tmp不为0无法继续下载，重新添加到service
                            if (tmp != AliyunErrorCode.ALIVC_SUCCESS.getCode()) {
                                Log.e(Tag, "error, renew add");
                                reDownload(info.getVid());
                            }
                        }
                    }
                }
                //下载课件
                OkDownload.restore(DownloadManager.getInstance().getDownloading());
                OkDownload.getInstance().startAll();
            });
            dialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
                netChangeDialog = null;
                dialog.dismiss();
                TApplication.isShowNetDialog = false;
                List<AliyunDownloadMediaInfo> downloadMediaInfos = downloadManager.getUnfinishedDownload();
                if (downloadMediaInfos != null) {
                    for (AliyunDownloadMediaInfo info : downloadMediaInfos) {
                        if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                            //暂停
                            downloadManager.stopDownloadMedia(info);
                        }
                    }
                }
                OkDownload.restore(DownloadManager.getInstance().getDownloading());
                OkDownload.getInstance().pauseAll();
            });
            dialogBuilder.setCancelable(false);
            netChangeDialog = dialogBuilder.create();
        }
        if (!netChangeDialog.isShowing()) {
            netChangeDialog.show();
        }
    }

    public void reDownload(String vid) {
        String sid = DownloadDAO.getInstance().selectSid(vid);
        if (!TextUtils.isEmpty(sid))
            getPlayAuth(sid);
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
                Intent it = new Intent(mContext, DownloadService.class);
                it.putExtra("title", t.getTask_name());
                it.putExtra("sid", FU.paseLong(t.getSourceId()));
                it.putExtra("vid", t.getTask_vid());
                it.putExtra("contentId", FU.paseLong(t.getContentId()));
                it.putExtra("type", t.getTask_type());
                it.putExtra("icon", t.getTask_icon());
                it.putExtra("playAuth", playAuth);
                startService(it);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Tag);
        MobclickAgent.onResume(mContext);
        netRequest.setActTag(actTag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Tag);
        MobclickAgent.onPause(mContext);
        logout("Tag------onPause-----" + Tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        netRequest.setActTag(actTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netRequest.cancelAllRequest(actTag);
        netWatchdog.stopWatch();
        logout("---onDestroy---netRequest---cancelAllRequest---" + actTag);
        ExitAppUtils.getInstance().delActivity(this);
    }

    /**
     * 设置布局
     */
    public abstract void setLayout();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    private void initTopBar() {
        area_left = findViewById(R.id.area_left);
        if (area_left != null) {
            area_left.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    finish();
                }
            });
        }
        area_right = findViewById(R.id.area_right);
    }

    /**
     * 设置页面标题
     */
    public void setPagTitle(String str) {
        page_title = (TextView) findViewById(R.id.page_title);
        page_title.setText(str);
    }

    /**
     * 打印日志
     */
    public void logout(String msg) {
        Log.out(msg);
    }

    /**
     * 显示分享
     *
     * @param shareUrl    分享链接
     * @param picUrl      分享图像
     * @param title       分享标题
     * @param description 分享描述
     */
    public void showShareDialog(String shareUrl, String picUrl, String title, String description) {
        Intent it = new Intent(mContext, SharePopWin.class);
        it.putExtra("shareUrl", shareUrl);
        it.putExtra("picUrl", picUrl);
        it.putExtra("title", title);
        it.putExtra("description", description);
        startActivity(it);
    }

    /**
     * 显示分享
     * shareInfoBean
     */
    public void showShareDialog(ShareInfoBean shareInfoBean) {
        if (shareInfoBean == null)
            return;
        Intent it = new Intent(mContext, SharePopWin.class);
        it.putExtra("shareUrl", shareInfoBean.getShareUrl());
        it.putExtra("picUrl", shareInfoBean.getPicUrl());
        it.putExtra("title", shareInfoBean.getTitle());
        it.putExtra("description", shareInfoBean.getDescription());
        startActivity(it);
    }

    /**
     * toast短提示
     */
    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String msg) {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.setMessage(msg);
            } else {
                commonProgressDialog.showDialog(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelable(boolean is) {
        commonProgressDialog.setCancelable(is);
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断点击区域是否在评论输入框内
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null) {// && (v instanceof EditText)
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 返回首页
     */
    public void backToMe() {
        for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
            if (!(mActivity instanceof MainTabActivity)
                    && !(mActivity instanceof HomeModularListActivity)
                    && !(mActivity instanceof WorkPlanMainWholeActivity)
                    && !(mActivity instanceof MyMainActivity)) {
                if (mActivity != null) {// 关闭所有页面，置回首页
                    mActivity.finish();
                    Intent changeIt = new Intent();
                    changeIt.setAction("ACTION_TAB_CHANGE");
                    changeIt.putExtra("TAB_NAME", "tab_me");
                    sendBroadcast(changeIt);
                }
            }
        }
    }

}
