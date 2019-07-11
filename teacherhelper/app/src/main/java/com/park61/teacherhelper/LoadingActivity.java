package com.park61.teacherhelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ListDataSave;
import com.park61.teacherhelper.common.tool.RequestPermissionUtil;
import com.park61.teacherhelper.module.home.bean.CheckVersionBean;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.login.bean.UserManager;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.service.LocationService;
import com.park61.teacherhelper.widget.dialog.AlipayWaitProgressDialog;
import com.park61.teacherhelper.widget.dialog.AppUpdateDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoadingActivity extends Activity {
    private static final int REQUEST_CODE = 1001; // 请求码
    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,//百度定位需要读取用户手机状态
            Manifest.permission.ACCESS_COARSE_LOCATION,//精准定位权限
            Manifest.permission.ACCESS_FINE_LOCATION,//精准定位权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean isFirstFlag = false;// 是否是首次启动
    private boolean isShowAdvertising = false;// 是否展示广告页
    // private MyCPCheckUpdateCallback mMyCPCheckUpdateCallback;
    // private AppUpdateModule updateModule;
    // private DoubleDialog doubleDialog;// 提示更新对话框
    private AppUpdateDialog doubleDialog;// 提示更新对话框
    private AlipayWaitProgressDialog aPDialog;// 下载更新包进度框
    private Context mContext;
    private View loading_root;
    private boolean userPermission = false;
    private LocationService locationService;

    public static final String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mContext = this;
        loading_root = findViewById(R.id.loading_root);
        // 判断是否是第一次进入APP
        SharedPreferences isFirstRunSp = getSharedPreferences("IsFirstFlag", Activity.MODE_PRIVATE);
        isFirstFlag = isFirstRunSp.getBoolean("IsFirstRun", true);
        String appVersion = isFirstRunSp.getString("AppVersion", "");

        if (GlobalParam.versionName != null && appVersion != null &&
                !appVersion.equals(GlobalParam.versionName)) {
            //当前版本号和保存的版本号不一样，代表是第一次安装此版本，展示引导图
            isFirstFlag = true;
        }

        // 判断是否展示广告页
        ListDataSave mListDataSave = new ListDataSave(mContext);
        isShowAdvertising = mListDataSave.getAdvertiseShow();
        Log.out("=================isShowAdvertising=====================" + isShowAdvertising);

        doubleDialog = new AppUpdateDialog(this);
        aPDialog = new AlipayWaitProgressDialog(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        userPermission = RequestPermissionUtil.getRequestPermissionUtilInstance().checkPermissions(
                this, REQUEST_CODE, PERMISSIONS);
        if (userPermission) {
            afterLocationPermission();
        }
    }

    private void afterLocationPermission() {
        locationService = ((TApplication) getApplication()).locationService;
        // 获取实例，建议应用中只初始化1个location实例
        locationService.registerListener(mListener);
        // 注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                GlobalParam.latitude = location.getLatitude();
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                GlobalParam.longitude = location.getLongitude();
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                GlobalParam.locationCityStr = location.getCity();
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                GlobalParam.locationCountryStr = location.getDistrict();
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.out(sb.toString());
            }
            if (GlobalParam.longitude != 0 && GlobalParam.longitude != 4.9e-324) {
                if (locationService != null) {
                    locationService.unregisterListener(mListener); // 注销掉监听
                    locationService.stop(); // 停止定位服务
                }
            }
        }
    };

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
       /* if (locationService != null) {
            locationService.unregisterListener(mListener); // 注销掉监听
            locationService.stop(); // 停止定位服务
        }*/
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userPermission) {
            afterReadPhonePermission();
        }
    }

    private void afterReadPhonePermission() {
       /* if (mMyCPCheckUpdateCallback == null) {
            mMyCPCheckUpdateCallback = new MyCPCheckUpdateCallback();
            Log.out("======开始检测更新======");
            BDAutoUpdateSDK.cpUpdateCheck(this, mMyCPCheckUpdateCallback);
        }*/
        asyncCheckUpdateStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    afterLocationPermission();
                    afterReadPhonePermission();
                } else {
                    Toast.makeText(this, "手机存储权限被拒绝,不能正常使用,请进行授权!", Toast.LENGTH_LONG).show();
                    LoadingActivity.this.finish();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//    /**
//     * 检测更新监听
//     */
//    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {
//
//        @Override
//        public void onCheckUpdateCallback(final AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
//            Log.out("------------onCheckUpdateCallback------------");
//            Log.out("------------infoForInstall------------" + infoForInstall);
//            // 安装已存在本地
//            if (infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
//                String changeLog = infoForInstall.getAppChangeLog().replace("<br>", "\n");
//                // 如果更新log里说明是强制更新，那么必须更新
//                final boolean isForce = changeLog.contains("强制更新") ? true : false;
//                String installStr = "";
//                if (isForce) {
//                    installStr = "更新包已下载，必须安装才能使用\n" + changeLog;
//                } else {
//                    installStr = "更新包已下载，请安装\n" + changeLog;
//                }
//                final String apkPath = infoForInstall.getInstallPath();
//                doubleDialog.showDialog("提示", installStr, "取消", "确定",
//                        new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                doubleDialog.dismissDialog();
//                                if (isForce) {
//                                    finish();
//                                } else {
//                                    startHomePage();
//                                }
//                            }
//                        }, new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                doubleDialog.dismissDialog();
//                                BDAutoUpdateSDK.cpUpdateInstall(mContext, apkPath);
//                            }
//                        });
//            } else if (info != null) {
//                String changeLog = info.getAppChangeLog().replace("<br>", "\n");
//                Log.out("changeLog======" + changeLog);
//                GlobalParam.versionNameNext = info.getAppVersionName();
//                // 如果更新log里说明是强制更新，那么必须更新
//                final boolean isForce = changeLog.contains("强制更新") ? true : false;
//                String installStr = "";
//                if (isForce) {
//                    installStr = "发现新版本，必须更新才能使用\n" + changeLog;
//                } else {
//                    installStr = "发现新版本，请更新\n" + changeLog;
//                }
//                doubleDialog.setCancelable(false);
//                doubleDialog.showDialog("提示", installStr, "取消", "确定",
//                        new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                doubleDialog.dismissDialog();
//                                if (isForce) {
//                                    finish();
//                                } else {
//                                    startHomePage();
//                                }
//                            }
//                        }, new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                doubleDialog.dismissDialog();
//                                BDAutoUpdateSDK.cpUpdateDownload(mContext, info, new UpdateDownloadCallback());
//                            }
//                        });
//
//            } else {
//                startHomePage();
//            }
//        }
//    }
//    *
//     * 更新进度监听
//
//    private class UpdateDownloadCallback implements CPUpdateDownloadCallback {
//
//        @Override
//        public void onDownloadComplete(String apkPath) {
//            aPDialog.dialogDismiss();
//            BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);
//        }
//
//        @Override
//        public void onStart() {
//            aPDialog.setCancelable(false);
//            aPDialog.showDialog("正在下载更新包，已完成0%,请稍候...");
//        }
//
//        @Override
//        public void onPercent(int percent, long rcvLen, long fileSize) {
//            aPDialog.showDialog("正在下载更新包，已完成" + percent + "%,请稍候...");
//        }
//
//        @Override
//        public void onFail(Throwable error, String content) {
//            Toast.makeText(mContext, "下载失败！请检查应用的存储权限是否开启", Toast.LENGTH_SHORT).show();
//            aPDialog.dialogDismiss();
//            finish();
//        }
//
//        @Override
//        public void onStop() {
//            aPDialog.dialogDismiss();
//        }
//    }

    /**
     * 进入主页面
     */
    public void startHomePage() {
        // 判断是否需要重新登陆
        SharedPreferences spf = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String logindate = spf.getString("logindate", "");
        // 如果没有保存用户，则需要重新登陆
        if (TextUtils.isEmpty(logindate)) {
            GlobalParam.userToken = null;
        } else {
            GlobalParam.userToken = spf.getString("usertoken", "");
            GlobalParam.currentUser = UserManager.getInstance().getAccountInfo(mContext);
            // 如果这两个有一个为空，代表数据被清除，需要重新登陆
            if (TextUtils.isEmpty(GlobalParam.userToken)) {
                GlobalParam.userToken = null;
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isFirstFlag) {// 判断是否是首次运行，首次运行进入引导页
                    startActivity(new Intent(LoadingActivity.this, GuideActivity.class));
                    //startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                } else {
                    if (isShowAdvertising) {//显示广告
                        if (GlobalParam.userToken != null) { // 已登录
                            startActivity(new Intent(LoadingActivity.this, AdvertisingActivity.class));
                        } else {//未登录
                            startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                        }
                    } else {
                        if (GlobalParam.userToken != null) {
                            startActivity(new Intent(mContext, MainTabActivity.class));
                        } else {
                            startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                        }
                    }
                }
                finish();
            }
        }, 1000);
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
        OkHttpUtils.getInstance().setMainActivity(LoadingActivity.this).startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            startHomePage();
        }

        @Override
        public void onSuccess(int requestId, final String url, JSONObject jsonResult) {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            final CheckVersionBean checkVersionBean = gson.fromJson(jsonResult.toString(), CheckVersionBean.class);
            if (checkVersionBean.getUpgradeType() == 0) {//0不需更新 1提示更新 2强制更新
                startHomePage();
            } else if (checkVersionBean.getUpgradeType() == 1 || checkVersionBean.getUpgradeType() == 2) {// 1提示更新 2强制更新
                final boolean isForce = checkVersionBean.getUpgradeType() == 2 ? true : false;
                doubleDialog.showDialog("发现新版本", checkVersionBean.getChangeLog().replace(";", "\n").replace("；", "\n"), isForce,
                        v -> {
                            doubleDialog.dismissDialog();
                            startHomePage();
                        }, v -> {
                            doubleDialog.dismissDialog();
                            //打开外链下载app
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkVersionBean.getUpgradeUrl()));
                            startActivity(intent);
                        });
            } else {//出错
                startHomePage();
            }
        }
    };

}
