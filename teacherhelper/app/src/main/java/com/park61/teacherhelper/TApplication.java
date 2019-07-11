package com.park61.teacherhelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okserver.OkDownload;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.park61.teacherhelper.common.set.AppInit;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.service.LocationService;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 应用程序application
 *
 * @author super
 */
public class TApplication extends MultiDexApplication {

    public LocationService locationService;
    private static final String TAG = "TApplication";
    public static boolean isShowNetDialog = true;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * app初始化
         */
        AppInit.initEnvironment(this, true, true);

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());

        //下载相关设置-----------start-------------//
        DownloadDAO.init(this);
        AliyunDownloadConfig config = new AliyunDownloadConfig();
        config.setMaxNums(3);
        config.setDownloadDir(getExternalCacheDir() + "/download/");
        config.setSecretImagePath(Environment.getExternalStorageDirectory() + "/test_save/");
        AliyunDownloadManager.getInstance(this).setDownloadConfig(config);
        //音视频下载 初始化
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        OkGo.getInstance().init(this).setOkHttpClient(builder.build());
        OkDownload.getInstance().setFolder(getExternalCacheDir() + "/addition/");
        //下载相关设置-----------end-------------//

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        // Init Umeng Push Service
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "07ab3e24e402d71a8c892f7b337d06bb");
        /**
         * 是否开启日志与普通测试
         * 参数：boolean 默认为false
         */
        UMConfigure.setLogEnabled(true);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //mPushAgent.setNotificationClickHandler(new CommonNotificationClickHandler());

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                GlobalParam.YOUMENG_DEVICE_TOKEN = deviceToken;
                Log.d(TAG, "deviceToken =======> " + deviceToken);
                SharedPreferences youmengRunSp = getSharedPreferences("YOUMENG_DEVICE_TOKEN", Activity.MODE_PRIVATE);
                youmengRunSp.edit().putString("deviceToken", deviceToken).commit();
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d(TAG, "deviceToken.failure, s =======> " + s + ", s1=" + s1);
            }
        });

        MiPushRegistar.register(this, "2882303761517616009", "5251761676009");

        HuaWeiRegister.register(this);
    }
}
