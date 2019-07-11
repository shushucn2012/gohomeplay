package com.park61.teacherhelper.module.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DataCleanManager;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.module.home.bean.CheckVersionBean;
import com.park61.teacherhelper.module.login.ForgetPwdInputPhoneActivity;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.AlipayWaitProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/7/15.
 */
public class SettingActivity extends BaseActivity {

    private View area_aboutme, version_area, area_clear_cache, area_pwd;
    private Button btn_logout;
    private TextView tv_clear_cache, tv_version_name, tv_phone;
    private AlipayWaitProgressDialog aPDialog;// 下载更新包进度框

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        setPagTitle("设置");
        area_aboutme = findViewById(R.id.area_aboutme);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tv_clear_cache = (TextView) findViewById(R.id.tv_clear_cache);
        area_clear_cache = findViewById(R.id.area_clear_cache);
        version_area = findViewById(R.id.version_area);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        area_pwd = findViewById(R.id.area_pwd);
        aPDialog = new AlipayWaitProgressDialog(this);
    }

    @Override
    public void initData() {
        try {
            tv_clear_cache.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
            tv_clear_cache.setText("15.8M");
        }
        tv_version_name.setText("当前版本：" + GlobalParam.versionName);
        tv_phone.setText(GlobalParam.currentUser.getMobile().substring(0, 5) + "****" + GlobalParam.currentUser.getMobile().substring(9));
    }

    @Override
    public void initListener() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginOutDialog();
            }
        });
        area_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.clearAllCache(mContext);
                tv_clear_cache.setText("0K");
                showShortToast("缓存清理成功！");
            }
        });
        version_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BDAutoUpdateSDK.cpUpdateCheck(mContext, new MyCPCheckUpdateCallback());
                asyncCheckUpdateStatus();
            }
        });
        area_aboutme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
        area_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ForgetPwdInputPhoneActivity.class);
                it.putExtra("from", "setting");
                startActivity(it);
               /* Intent it = new Intent(mContext, ForgetPwdGetVCodeActivity.class);
                it.putExtra("userMobile", GlobalParam.currentUser.getMobile());
                startActivity(it);*/
            }
        });
    }

    /**
     * 检测更新
     */
    private void asyncCheckUpdateStatus() {
        String wholeUrl = AppUrl.host + AppUrl.checkUpdateStatus;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appVersionName", GlobalParam.versionName);
        map.put("bundleId", GlobalParam.BUNDLE_ID);
        map.put("appType", 0);//0 android;1 ios;
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, final String url, JSONObject jsonResult) {
            dismissDialog();
            final CheckVersionBean checkVersionBean = gson.fromJson(jsonResult.toString(), CheckVersionBean.class);
            if (checkVersionBean.getUpgradeType() == 0) {//0不需更新 1提示更新 2强制更新
                showShortToast("已经是最新版本了");
            } else if (checkVersionBean.getUpgradeType() == 1 || checkVersionBean.getUpgradeType() == 2) {// 1提示更新 2强制更新
                final boolean isForce = checkVersionBean.getUpgradeType() == 2 ? true : false;
                String installStr = "";
                if (isForce) {
                    installStr = "发现新版本，必须更新才能使用\n" + checkVersionBean.getChangeLog();
                } else {
                    installStr = "发现新版本，请更新\n" + checkVersionBean.getChangeLog();
                }
                dDialog.setCancelable(false);
                dDialog.showDialog("提示", installStr, "取消", "确定",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                            }
                        }, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                //打开外链下载app
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkVersionBean.getUpgradeUrl()));
                                startActivity(intent);
                            }
                        });
            } else {//出错
                showShortToast("已经是最新版本了");
            }
        }
    };

    /**
     * 检测更新监听
     */
    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(final AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
            dismissDialog();
            if (info != null) {
                String changeLog = info.getAppChangeLog();
                GlobalParam.versionNameNext = info.getAppVersionName();
                // 如果更新log里说明是强制更新，那么必须更新
                final boolean isForce = changeLog.contains("强制更新") ? true : false;
                String installStr = "";
                if (isForce) {
                    installStr = "发现新版本，必须更新才能使用\n" + changeLog;
                } else {
                    installStr = "发现新版本，请更新\n" + changeLog;
                }
                dDialog.showDialog("提示", installStr, "确定", "取消",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                BDAutoUpdateSDK.cpUpdateDownload(mContext, info, new UpdateDownloadCallback());
                            }
                        }, null);
            } else {
                showShortToast("已经是最新版本了");
            }
        }
    }

    /**
     * 更新进度监听
     */
    private class UpdateDownloadCallback implements CPUpdateDownloadCallback {

        @Override
        public void onDownloadComplete(String apkPath) {
            aPDialog.dialogDismiss();
            BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);
        }

        @Override
        public void onStart() {
            aPDialog.setCancelable(false);
            aPDialog.showDialog("正在下载更新包，已完成0%,请稍候...");
        }

        @Override
        public void onPercent(int percent, long rcvLen, long fileSize) {
            aPDialog.showDialog("正在下载更新包，已完成" + percent + "%,请稍后...");
        }

        @Override
        public void onFail(Throwable error, String content) {
            Toast.makeText(mContext, "下载失败！", Toast.LENGTH_SHORT).show();
            aPDialog.dialogDismiss();
        }

        @Override
        public void onStop() {
            aPDialog.dialogDismiss();
        }
    }

    public void showLoginOutDialog() {
        dDialog.showDialog("提示", "确定要退出吗？", "取消", "确定", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dDialog.dismissDialog();

            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dDialog.dismissDialog();
                asyncLoginOut();
            }
        });
    }

    /**
     * 退出登录
     */
    private void asyncLoginOut() {
        String wholeUrl = AppUrl.host + AppUrl.LOGIN_OUT;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, loginOutLsner);
    }

    BaseRequestListener loginOutLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("已退出，请重新登录！");
            GlobalParam.userToken = null;
            // ---清除用户登录信息缓存---start---//
            SharedPreferences spf = getSharedPreferences(LoginActivity.FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("logindate", "");
            editor.putString("usertoken", "");
            editor.commit();
            // ---清除用户登录信息缓存---end---//
            for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
                if (mActivity != null) {// 关闭所有页面
                    mActivity.finish();
                }
            }
            startActivity(new Intent(mContext, LoginActivity.class));
            /*Intent changeIt = new Intent();
            changeIt.setAction("ACTION_TAB_CHANGE");
            changeIt.putExtra("TAB_NAME", "tab_main");
            sendBroadcast(changeIt);*/
        }
    };

}
