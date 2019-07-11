package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.ScrollLsnWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 圣诞活动主页
 */
public class ChristmasActMainActivity extends BaseActivity {

    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    private TextView tv_page_title;
    private ScrollLsnWebView wv;
    private View area_no_pay, area_to_pay, area_free_in;
    private ProgressBar pb;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private ActivityDetailsBean mActivityDetailsBean;
    private int cActivityId;
    private String url_share;
    private ShareInfoBean mShareInfoBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_cris_main);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.page_title);
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (ScrollLsnWebView) findViewById(R.id.wv);
        area_no_pay = findViewById(R.id.area_no_pay);
        area_to_pay = findViewById(R.id.area_to_pay);
        area_free_in = findViewById(R.id.area_free_in);
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
        cActivityId = getIntent().getIntExtra("activityId", -1);
        String url = "http://m.61park.cn/teach/#/activity/activityindex/" + cActivityId;
        url_share = url;
        logout("url======" + url);
        syncCookie(url);

        wv.setOnScrollChangeListener(new ScrollLsnWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int l, int scrollY, int oldl, int oldt) {
                if (scrollY > DevAttr.dip2px(mContext, 185)) {
                    findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                    ((ImageView) findViewById(R.id.img_back)).setImageResource(R.mipmap.icon_red_backimg);
                    ((ImageView) findViewById(R.id.img_share)).setImageResource(R.mipmap.icon_to_share_red);
                } else if (scrollY > DevAttr.dip2px(mContext, 145)) {
                    findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
                } else {
                    findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#00000000"));
                    ((ImageView) findViewById(R.id.img_back)).setImageResource(R.mipmap.icon_content_backimg);
                    ((ImageView) findViewById(R.id.img_share)).setImageResource(R.mipmap.icon_shadow_share);
                }
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
            }

            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
            }
        });
        wv.loadUrl(url);
        mShareInfoBean = new ShareInfoBean(url, getIntent().getStringExtra("picUrl"), "圣诞活动", "更多精彩，尽在61学院！");
        asyncGetActivityDetail();
    }

    private void asyncGetActivityDetail() {
        String wholeUrl = AppUrl.host + AppUrl.christmasActivityDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", cActivityId);//activityId=1
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

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
        public void onSuccess(int requestId, String url0, JSONObject jsonResult) {
            dismissDialog();
            mActivityDetailsBean = gson.fromJson(jsonResult.toString(), ActivityDetailsBean.class);
            mShareInfoBean = new ShareInfoBean(url_share, mActivityDetailsBean.getCoverImg(), mActivityDetailsBean.getName(), mActivityDetailsBean.getIntroduction());
        }
    };

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
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(mShareInfoBean);
            }
        });
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
                String isfree = param.optString("isfree");//0 付费 1 免费
                logout("content_id======" + content_id);
                if (mActivityDetailsBean.getPaymentStatus() == 1) {//已下单
                    Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                    intent.putExtra("coursePlanId", FU.paseInt(content_id));
                    startActivity(intent);
                } else if (mActivityDetailsBean.getPaymentStatus() == 0) {//未下单
                    if ("1".equals(isfree)) {
                        Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                        intent.putExtra("coursePlanId", FU.paseInt(content_id));
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "此内容为活动付费内容，打赏付费后可查看所有活动内容", Toast.LENGTH_LONG).show();
                    }
                }
            } else if ("introduce".equals(forwardtype)) {//内容详情
                JSONObject param = data.optJSONObject("params");
                String activityId = param.optString("activityId");
                logout("activityId======" + activityId);
                Intent it = new Intent(mContext, ActIntroWebViewActivity.class);
                it.putExtra("activityId", activityId);
                it.putExtra("PAGE_TITLE", mActivityDetailsBean.getName());
                it.putExtra("picUrl", mActivityDetailsBean.getCoverImg());
                startActivity(it);
            } else if ("activityList".equals(forwardtype)) {//圣诞活动列表
                JSONObject param = data.optJSONObject("params");
                Intent it = new Intent(mContext, ChristmasActListActivity.class);
                it.putExtra("activityId", cActivityId);
                it.putExtra("shareTitle", mShareInfoBean.getTitle());
                it.putExtra("shareUrl", mShareInfoBean.getShareUrl());
                it.putExtra("sharePic", mShareInfoBean.getPicUrl());
                it.putExtra("shareDescription", mShareInfoBean.getDescription());
                startActivity(it);
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
