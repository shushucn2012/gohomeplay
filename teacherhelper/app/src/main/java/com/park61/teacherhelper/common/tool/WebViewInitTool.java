package com.park61.teacherhelper.common.tool;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;

/**
 * webview 初始化配置工具类
 * Created by shubei on 2018/9/4.
 */

public class WebViewInitTool {

    /**
     * 初始化web设置
     */
    public static void initWebViewSetting(WebView wv) {
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置缓存模式
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //修改ua让后台识别
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + "; 61park/android");

        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

    /**
     * 设置浏览器cookie值
     *
     * @param url
     */
    public static void syncCookie(Context mContext, String url) {
        try {
            Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.out("Nat: webView.syncCookieOutter.oldCookie-======" + oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("ut=%s", GlobalParam.userToken));
            //sbCookie.append(";domain=" + AppUrl.BUILD_CHART_DOMAIN);
            //sbCookie.append(";path=/");

            String cookieValue = sbCookie.toString();
            Log.i("--cookieValue", cookieValue);
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.out("Nat: webView.syncCookie.newCookie-======" + newCookie);
            }
        } catch (Exception e) {
            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }
}
