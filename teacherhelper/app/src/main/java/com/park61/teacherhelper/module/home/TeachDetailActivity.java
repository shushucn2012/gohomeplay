package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.module.course.bean.ContentRecommendBean;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nieyu on 2017/10/20.
 */

public class TeachDetailActivity extends BaseActivity {
    private WebView wvContent;
    private WebSettings websettings;
    ImageView iv_back;
    private ProgressBar pb;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_teachdetail);
    }

    @Override
    public void initView() {
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wvContent = (WebView) findViewById(R.id.wvContent);
        websettings = wvContent.getSettings();
        //websettings.setJavaScriptEnabled(true);
        //websettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        websettings.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);
//         websettings.setSupportMultipleWindows(true);
        //websettings.setDomStorageEnabled(true);
        //websettings.setAppCacheEnabled(true);
//        websettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
//        websettings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
//        websettings.setUseWideViewPort(true);// 可任意比例缩放
        //websettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
//        websettings.setSavePassword(true);
//        websettings.setSaveFormData(true);// 保存表单数据
        //wvContent.loadDataWithBaseURL("http://t.61park.cn/html-sui/activity/teacher-detail.html?teacherid=11 \n", getIntent().getStringExtra("h5Content"), "text/html", "UTF-8", "");

        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(getIntent().getStringExtra("h5Content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        wvContent.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "UTF-8", "");

        wvContent.setWebChromeClient(new WebChromeClient() {
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
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
//                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        });

    }

    @Override
    public void initData() {
//        asynGetTeacherDetail();
    }

    @Override
    public void initListener() {

    }

//    public void asynGetTeacherDetail(){
//        String wholeUrl = AppUrl.host + AppUrl.addTeachdetail;
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("trainerId", getIntent().getIntExtra("teachId",-1));
//        String requestBodyData = ParamBuild.buildParams(map);
//        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,teachdetaillistener);
//    }

    BaseRequestListener teachdetaillistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
           /* if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }*/
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
////            iv_attent.setImageDrawable(getResources().getDrawable(R.mipmap.comment_attend));
//            contentRecommendBean = new Gson().fromJson(jsonResult.toString(), ContentRecommendBean.class);
//            ContentRecomendAdapter contentRecomendAdapter =new ContentRecomendAdapter(CourseDetailsActivity.this,contentRecommendBean.getRows());
//            gv_recommend.setAdapter(contentRecomendAdapter);
//            gv_recommend.addItemDecoration(new CourseDetailsActivity.SpaceItemDecoration(-10));
//            contentRecomendAdapter.setOnItemClickListener(new ContentRecomendAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
////                    Toast.makeText(CourseDetailsActivity.this,"我被点了"+position,Toast.LENGTH_LONG).show();
//                    Intent it = new Intent(mContext, CourseDetailsActivity.class);
//                    it.putExtra("coursePlanId",contentRecommendBean.getRows().get(position).getId());
//                    mContext.startActivity(it);
//                    finish();
//                }
//            });
        }
    };
}
