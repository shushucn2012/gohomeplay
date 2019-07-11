package com.park61.teacherhelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.RequestPermissionUtil;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.login.bean.UserManager;
import com.park61.teacherhelper.service.LocationService;

/**
 * 推送通知点击进入app的启动页
 */
public class LoadingPushActivity extends Activity {
    private static final int REQUEST_CODE = 1001; // 请求码
    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,//百度定位需要读取用户手机状态
            Manifest.permission.ACCESS_COARSE_LOCATION,//精准定位权限
            Manifest.permission.ACCESS_FINE_LOCATION,//精准定位权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Context mContext;
    private boolean userPermission = false;
    private LocationService locationService;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mContext = this;

        webUrl = getIntent().getStringExtra("WEB_VIEW_URL");
        Log.out("==========================webUrl============================" + webUrl);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (userPermission) {
            afterReadPhonePermission();
        }
    }

    private void afterReadPhonePermission() {
        startHomePage();
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
                    LoadingPushActivity.this.finish();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                if (GlobalParam.userToken != null) {
                    Intent it = new Intent(mContext, MainTabActivity.class);
                    it.putExtra("isPushIn", true);
                    it.putExtra("WEB_VIEW_URL", webUrl);
                    startActivity(it);
                } else {
                    startActivity(new Intent(LoadingPushActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }

}
