package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通用网页展示页, 回退按钮可以回退h5页面
 */
public class ActIntroWebViewActivity extends BaseActivity {

    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    private static final int FOR_ASK = 2;
    private TextView tv_page_title;
    private WebView wv;
    private ProgressBar pb;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private int activityId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_free_content_webview);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.page_title);
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        initWebViewSetting();
    }

    /**
     * 初始化web设置
     */
    private void initWebViewSetting() {
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

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
        //关闭顶部右侧按钮
        String title = getIntent().getStringExtra("PAGE_TITLE");
        if (!TextUtils.isEmpty(title)) {
            tv_page_title.setText(title);
        }

        //String url = "http://t.61park.cn/html-sui/activity/activity-detail.html?activityId=" + getIntent().getStringExtra("activityId");
        String url = "http://m.61park.cn/t/html-sui/activity/activity-detail.html?activityId=" + getIntent().getStringExtra("activityId");
        logout("url======" + url);
        syncCookie(url);
        wv.loadUrl(url);
    }

    @Override
    public void initListener() {
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        // 在js中调用本地java方法
        wv.addJavascriptInterface(new JsInterface(), "Android");
        //加载进度
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pb.setVisibility(View.GONE);
                } else {
                    // 加载中
                    pb.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //tv_page_title.setText(title);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        });
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    finish();
                }
            }
        });
        area_right.setVisibility(View.INVISIBLE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    /**
     * 处理js回调传回的json数据
     */
    private class JsInterface {
        // 在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void call(String jsonStr) {
            logout("jsonStr====" + jsonStr);
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject data = jsonObject.optJSONObject("data");
            String forwardtype = data.optString("forwardtype");
            logout("forwardtype======" + forwardtype);
            if ("teacher_main".equals(forwardtype)) {//培训师详情
                JSONObject param = data.optJSONObject("params");
                String teacher_id = param.optString("teacher_id");
                logout("teacher_id======" + teacher_id);
                Intent it = new Intent(mContext, TeachHouseActivity.class);
                it.putExtra("teachId", FU.paseInt(teacher_id));
                startActivity(it);
            } else if ("content_detail".equals(forwardtype)) {//内容详情
                JSONObject param = data.optJSONObject("params");
                String content_id = param.optString("content_id");
                logout("content_id======" + content_id);
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.putExtra("coursePlanId", FU.paseInt(content_id));
                startActivity(intent);
            } else if ("ask".equals(forwardtype)) {//提问
                Intent intent = new Intent(mContext, ActQuestionPublish.class);
                intent.putExtra("activityId", activityId);
                startActivityForResult(intent, FOR_ASK);
            }
        }
    }

    /**
     * 设置浏览器cookie值
     *
     * @param url
     */
    private void syncCookie(String url) {
        try {
            Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                logout("Nat: webView.syncCookieOutter.oldCookie-======" + oldCookie);
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
                logout("Nat: webView.syncCookie.newCookie-======" + newCookie);
            }
        } catch (Exception e) {
            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }

}
