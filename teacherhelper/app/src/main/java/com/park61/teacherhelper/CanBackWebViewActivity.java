package com.park61.teacherhelper;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.teacherhelper.common.tool.ComJsInterface;
import com.park61.teacherhelper.common.tool.WebViewInitTool;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;

/**
 * 通用网页展示页, 回退按钮可以回退h5页面
 */
public class CanBackWebViewActivity extends BaseActivity {

    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public TextView tv_page_title;
    public WebView wv;
    public ProgressBar pb;
    public View area_close;

    public ShareInfoBean mShareInfoBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_canback_webview);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.page_title);
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        area_close = findViewById(R.id.area_close);
        WebViewInitTool.initWebViewSetting(wv);
    }

    @Override
    public void initData() {
        //关闭顶部右侧按钮
        String title = getIntent().getStringExtra("PAGE_TITLE");
        if (!TextUtils.isEmpty(title)) {
            tv_page_title.setText(title);
        }
        String url = getIntent().getStringExtra("url");
        logout("url======" + url);
        WebViewInitTool.syncCookie(mContext, url);
        wv.loadUrl(url);
        mShareInfoBean = new ShareInfoBean(url, getIntent().getStringExtra("picUrl"), title, "更多精彩，尽在61学院！");
    }

    @Override
    public void initListener() {
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    //shareUrl = url;
                    mShareInfoBean.setShareUrl(url);
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                return true;
            }
        });
        // 在js中调用本地java方法
        wv.addJavascriptInterface(new ComJsInterface(CanBackWebViewActivity.this, mShareInfoBean), "Android");
        //加载进度
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pb.setVisibility(View.GONE);
                } else {
                    // 加载中
                    if (pb.getVisibility() == View.GONE) {//如果进度条隐藏了，再显示出来
                        pb.setVisibility(View.VISIBLE);
                    }
                    pb.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String mtitle) {
                setShareAndNowTitle(mtitle);
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
        area_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(mShareInfoBean);
            }
        });
    }

    protected void setShareAndNowTitle(String mtitle) {
        //shareTitle = mtitle;
        mShareInfoBean.setTitle(mtitle);
       /* if (TextUtils.isEmpty(title)) {
            tv_page_title.setText(mtitle);
        }*/
        logout("====================mtitle====================" + mtitle);
        if (!mtitle.contains("m.61park.cn")) {
            tv_page_title.setText(mtitle);
        }
    }

    protected void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("video/*;image/*");
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

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            finish();
        }
    }

}
