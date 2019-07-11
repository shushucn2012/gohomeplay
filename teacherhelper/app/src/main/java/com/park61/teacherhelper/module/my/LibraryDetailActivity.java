package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.databinding.ActivityLibraryDetailBinding;
import com.park61.teacherhelper.module.my.adapter.ToysAdapter;
import com.park61.teacherhelper.module.my.bean.ToysBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2017/12/26.
 * <p>
 * 亲子活动详情页
 */

public class LibraryDetailActivity extends BaseActivity {

    private String baseUrl = "file:///android_asset";
    ActivityLibraryDetailBinding binding;
    private int activityId, classId;
    private String activityName;
    private List<ToysBean> datas;
    private List<ToysBean> rawData;
    ToysAdapter adapter;
    boolean isShowAll = false;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_library_detail);

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

    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("id", -1);
        classId = getIntent().getIntExtra("classId", -1);
        //获取活动详情
        asyncActivityDetail();
        rawData = new ArrayList<>();
        datas = new ArrayList<>();
        adapter = new ToysAdapter(this, datas);
        binding.toyLv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.toyLv.setNestedScrollingEnabled(false);
        binding.toyLv.setAdapter(adapter);

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

        String url = AppUrl.host + AppUrl.myLibDetail;
        Map<String, Object> map = new HashMap<>();
        map.put("partyTemplateId", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) {
            activityName = j.optString("name");
            ImageManager.getInstance().displayImg(binding.topImg, j.optString("coverUrl"));
            binding.activityTitle.setText(activityName);
            binding.topBarTitle.setText(activityName);
            binding.activityIntro.setText(j.optString("intro"));
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

        String url = AppUrl.host + AppUrl.myLibToyList;
        Map<String, Object> map = new HashMap<>();
        map.put("partyTemplateId", activityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, toysListener);
    }

    BaseRequestListener toysListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray list = jsonResult.optJSONArray("list");
            if (list != null && list.length() > 0) {
                for (int i = 0; i < list.length(); i++) {
                    ToysBean b = gson.fromJson(list.optJSONObject(i).toString(), ToysBean.class);
                    rawData.add(b);
                }
                if (rawData.size() > 3) {
                    binding.showAllArea.setVisibility(View.VISIBLE);
                    datas.addAll(rawData.subList(0, 3));
                } else {
                    datas.addAll(rawData);
                }
                adapter.notifyDataSetChanged();
            } else {
                binding.toyArea.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            binding.toyArea.setVisibility(View.GONE);
        }
    };


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
        binding.setPublishClick(v -> {
            Intent it = new Intent(mContext, PublishLibActivity.class);
            it.putExtra("id", activityId);
            it.putExtra("classId", classId);
            mContext.startActivity(it);
            finish();
        });

        binding.showAllArea.setOnClickListener(v -> {
            datas.clear();
            if (isShowAll) {
                binding.showAllBt.setText("查看全部");
                binding.showAllArrow.setRotation(0);
                datas.addAll(rawData.subList(0, 3));
            } else {
                binding.showAllBt.setText("收起内容");
                binding.showAllArrow.setRotation(180);
                datas.addAll(rawData);
            }
            isShowAll = !isShowAll;
            adapter.notifyDataSetChanged();
        });

        int h = DevAttr.dip2px(mContext, 130);
        binding.scrollContent.setScrollViewListener((x, y, oldx, oldy) -> {
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

}
