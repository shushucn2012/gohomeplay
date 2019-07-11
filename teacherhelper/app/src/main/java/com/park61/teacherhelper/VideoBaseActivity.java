package com.park61.teacherhelper;

import android.content.res.Configuration;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.park61.teacherhelper.common.tool.ScreenStatusController;

/**
 * Created by shubei on 2018/1/31.
 */

public abstract class VideoBaseActivity extends BaseActivity {

    public AliyunVodPlayerView mAliyunVodPlayerView;
    public ScreenStatusController mScreenStatusController = null;
    private AliyunPlayAuth.AliyunPlayAuthBuilder aliyunAuthInfoBuilder;

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }

    public boolean isStrangePhone() {
        boolean strangePhone = Build.DEVICE.equalsIgnoreCase("mx5")
                || Build.DEVICE.equalsIgnoreCase("Redmi Note2")
                || Build.DEVICE.equalsIgnoreCase("Z00A_1")
                || Build.DEVICE.equalsIgnoreCase("hwH60-L02")
                || Build.DEVICE.equalsIgnoreCase("hermes")
                || (Build.DEVICE.equalsIgnoreCase("V4") && Build.MANUFACTURER.equalsIgnoreCase("Meitu"));

        return strangePhone;
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }
        if (mScreenStatusController != null)
            mScreenStatusController.stopListen();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        updatePlayerViewMode();
    }

    public void initStatusLsner() {
        //锁屏监听
        mScreenStatusController = new ScreenStatusController(this);
        mScreenStatusController.setScreenStatusListener(new ScreenStatusController.ScreenStatusListener() {
            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {
            }
        });
        mScreenStatusController.startListen();
    }

    public abstract void updatePlayerViewMode();

    /**
     * 初始化视频播放器
     */
    public void initAliyunVodPlayerView(String videoId, String authInfo) {
        if (mAliyunVodPlayerView == null)
            return;
        aliyunAuthInfoBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunAuthInfoBuilder.setVid(videoId);
        aliyunAuthInfoBuilder.setPlayAuth(authInfo);
        aliyunAuthInfoBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL);
        mAliyunVodPlayerView.setAuthInfo(aliyunAuthInfoBuilder.build());
        mAliyunVodPlayerView.setControlBarCanShow(true);
        mAliyunVodPlayerView.setAutoPlay(true);
    }

    public void showBar(){
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            logout("Turning immersive mode mode off. ");


            //先取 非 后再 与， 把对应位置的1 置成0，原本为0的还是0

            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }


            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
            }


            if (Build.VERSION.SDK_INT >= 18) {
                newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        }
    }


    public void hideBar() {
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (!isImmersiveModeEnabled) {
            logout("Turning immersive mode mode on. ");


            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }


            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            if (Build.VERSION.SDK_INT >= 18) {
                newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        }
    }

}
