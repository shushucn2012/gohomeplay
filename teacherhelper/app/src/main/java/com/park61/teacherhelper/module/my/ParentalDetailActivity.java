package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.databinding.ActivityParentalDetailBinding;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.SharePopWin;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlie on 2017/12/26.
 * <p>
 * 亲子活动详情页
 */

public class ParentalDetailActivity extends BaseActivity {

    private String baseUrl = "file:///android_asset";
    ActivityParentalDetailBinding binding;
    private int activityId, classId;
    private String imgUrl;
    private String activityName;
    private String description;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parental_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //透明状态栏
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void initView() {

        WebSettings ws1 = binding.wvNotice.getSettings();
        WebSettings ws2 = binding.wvDetail.getSettings();
        ws1.setJavaScriptEnabled(true);
        ws2.setJavaScriptEnabled(true);
        ws1.setJavaScriptCanOpenWindowsAutomatically(true);
        ws2.setJavaScriptCanOpenWindowsAutomatically(true);
        ws1.setDomStorageEnabled(true);
        ws2.setDomStorageEnabled(true);
        ws1.setAppCacheEnabled(true);
        ws2.setAppCacheEnabled(true);
        // setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws1.setLoadWithOverviewMode(true);
        ws2.setLoadWithOverviewMode(true);

    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("id", -1);
        classId = getIntent().getIntExtra("classId", -1);
        //获取活动详情
        asyncActivityDetail();

        //获取活动装备列表
        asyncActivityToys();
    }

    /**
     * "content": "<p>亲子游活动</p>", 详情
     * "coverUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20171016183207850_390.jpg",
     * "id": 1,
     * "intro": "啊啊啊",  简介
     * "name": "亲子游活动",
     * "notice": "啊啊啊notice",  须知
     * "schoolName": "测试1228",
     * "showStatus": 0,
     * "startDateStr": "2017/12/28 周四 10:21"
     */
    private void asyncActivityDetail() {

        String url = AppUrl.host + AppUrl.childActivityDetail;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        map.put("classId", classId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) {
            imgUrl = j.optString("coverUrl");
            activityName = j.optString("name");
            description = j.optString("intro");
            ImageManager.getInstance().displayImg(binding.topImg, imgUrl);
            binding.activityTitle.setText(activityName);
            binding.topBarTitle.setText(activityName);
            binding.activityIntro.setText(description);
            binding.activityTime.setText(j.optString("startDateStr"));
            binding.activitySite.setText(j.optString("schoolName"));
            int state = j.optInt("showStatus") ;
            if(state != 3)
            binding.activityState.setImageResource(state == 0 ? R.mipmap.icon_signing:
                    (state==1 ? R.mipmap.icon_activitying : R.mipmap.icon_sign_close));
            setWebData(binding.wvNotice, j.optString("notice"));
            setWebData(binding.wvDetail, j.optString("content"));
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }
    };

    private void asyncActivityToys() {

        String url = AppUrl.host + AppUrl.childActivityToys;
        Map<String, Object> map = new HashMap<>();
        map.put("id", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, toysListener);
    }

    BaseRequestListener toysListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray list = jsonResult.optJSONArray("list");
            if(list !=null && list.length()>0){
                setToysData(list);
            }else{
                binding.toyLlyt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }
    };

    /**
     * {
     * "currentUnifyPrice": 188,
     * "id": 66,
     * "marketPrice": 198,
     * "productCname": "北极熊女生礼物可爱",
     * "productPicUrl": "http://ghpprod.oss-cn-qingdao.aliyuncs.com/product/20170604152222899_224.png?x-oss-process=style/compress_nologo"
     * }
     */
    private void setToysData(JSONArray arr) {
        if (arr.length() > 0) {
            JSONObject j1 = arr.optJSONObject(0);
            binding.arms1.setVisibility(View.VISIBLE);
            //图片展示
            ImageManager.getInstance().displayImg(binding.arms1Img, j1.optString("productPicUrl"));
            binding.arms1Name.setText(j1.optString("productCname"));
            binding.arms1CurrPrice.setText("￥" + j1.optDouble("currentUnifyPrice"));
            if(j1.optDouble("marketPrice") != 0){
                binding.arms1OrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                binding.arms1OrigPrice.setText("￥" + j1.optDouble("marketPrice"));
            }
        }
        if(arr.length() > 1){
            JSONObject j2 = arr.optJSONObject(1);
            binding.arms2.setVisibility(View.VISIBLE);
            //图片展示
            ImageManager.getInstance().displayImg(binding.arms2Img, j2.optString("productPicUrl"));
            binding.arms2Name.setText(j2.optString("productCname"));
            binding.arms2CurrPrice.setText("￥" + j2.optDouble("currentUnifyPrice"));
            if(j2.optDouble("marketPrice") != 0){
                binding.arms2OrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                binding.arms2OrigPrice.setText("￥" + j2.optDouble("marketPrice"));
            }
        }

        if(arr.length()>2){
            binding.armsMore.setVisibility(View.VISIBLE);
            binding.armsMore.setOnClickListener(v->{
                Intent it = new Intent(mContext, ToysListActivity.class);
                it.putExtra("list", arr.toString());
                startActivity(it);
            });
        }
    }

    private void setWebData(ShowImageWebView wv, String webContent) {
        ViewInitTool.initShowimgWebview(wv);
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(webContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wv.loadDataWithBaseURL(baseUrl, htmlDetailsStr, "text/html", "UTF-8", "");
    }

    @Override
    public void initListener() {
        binding.setBackClick(v -> finish());
        binding.setShareClick(v -> toShare());
        binding.setSignDataClick(v -> {
            Intent it = new Intent(mContext, SignDataActivity.class);
            it.putExtra("id", activityId);
            it.putExtra("classId", classId);
            mContext.startActivity(it);
        });
        int h = DevAttr.dip2px(mContext, 130);
        binding.contentSroll.setScrollViewListener((x, y, oldx, oldy) -> {
                    int alpha = y * 255 / h;
                    if (alpha > 255) {
                        alpha = 255;
                        binding.backImg.setImageResource(R.mipmap.icon_red_backimg);
//                        binding.shareImg.setImageResource(R.drawable.red_contentshare);
                        binding.topBarTitle.setVisibility(View.VISIBLE);
                    } else {
                        binding.backImg.setImageResource(R.mipmap.icon_content_backimg);
//                        binding.shareImg.setImageResource(R.drawable.contents_share);
                        binding.topBarTitle.setVisibility(View.GONE);
                    }
                    binding.topBar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    binding.titleStatusBar.setBackgroundColor(Color.argb(alpha, 254, 105, 139));
                }
        );
    }

    /**
     * 右上角分享按钮
     */
    public void toShare() {
        Intent its = new Intent(this, SharePopWin.class);
        String shareUrl = "http://m.61park.cn/#/fm/fmindex/"+activityId+"/"+classId;
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", imgUrl);
        its.putExtra("title", activityName);
        its.putExtra("description", description);
        startActivity(its);
    }
}
