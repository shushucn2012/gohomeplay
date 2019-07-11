package com.park61.teacherhelper;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.ExpertCourseBean;
import com.park61.teacherhelper.module.home.bean.ExpertOpinionBean;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.modular.BannerAndFastGoModule;
import com.park61.teacherhelper.widget.modular.BannerMidOneModule;
import com.park61.teacherhelper.widget.modular.BannerMidTwoModule;
import com.park61.teacherhelper.widget.modular.ExpertCourseModule;
import com.park61.teacherhelper.widget.modular.ExpertOpinionModule;
import com.park61.teacherhelper.widget.modular.FuWuBDContentModule;
import com.park61.teacherhelper.widget.modular.GoldTeacherModule;
import com.park61.teacherhelper.widget.modular.GuessYourLoveModule;
import com.park61.teacherhelper.widget.modular.ImageHorizontalModule;
import com.park61.teacherhelper.widget.modular.ImageVerticalModule;
import com.park61.teacherhelper.widget.modular.MoreContentModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务宝典原生界面
 * Created by shubei on 2018/10/16.
 */

public class FuWuBaoDianNativeActivity extends BaseActivity {


    private LinearLayout ll_content_root;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;

    private String pageId;//页面id
    public ShareInfoBean mShareInfoBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fuwubaodian_native_activity);
    }

    @Override
    public void initView() {
        ll_content_root = (LinearLayout) findViewById(R.id.ll_content_root);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
    }

    @Override
    public void initData() {
        //setPagTitle(getIntent().getStringExtra("PAGE_TITLE"));
        pageId = getIntent().getStringExtra("pageId");
        asyncLoadPageById();

        String url = "http://m.61park.cn/teach/#/servicedict/index/" + pageId;
        String title = "服务宝典";
        mShareInfoBean = new ShareInfoBean(url, AppUrl.SHARE_APP_ICON, title, "更多精彩，尽在61学院！");
    }

    @Override
    public void initListener() {
        area_right.setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_share_red);
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(mShareInfoBean);
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncLoadPageById();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    /**
     * 请求页面模块数据
     */
    private void asyncLoadPageById() {
        String wholeUrl = AppUrl.host + AppUrl.loadPageDataById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageId", pageId);
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
            mPullRefreshScrollView.onRefreshComplete();
            if ("0000040005".equals(errorCode)) {
                dDialog.showDialog("提示", errorMsg, "取消", "确定",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                finish();
                            }
                        });
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshScrollView.onRefreshComplete();
            //加载之前先移除老数据
            ll_content_root.removeAllViews();
            setPagTitle(jsonResult.optString("pageName"));
            mShareInfoBean.setTitle(jsonResult.optString("pageName"));
            JSONArray dataJay = jsonResult.optJSONArray("modules");
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                if ("imageVertical".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> picList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            picList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new ImageVerticalModule(mContext, picList));
                    }
                } else if ("imageCrosswise".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> hpicList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            hpicList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new ImageHorizontalModule(mContext, hpicList));
                    }
                } else if ("cmsContent".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<ContentItem> sList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            sList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                        }
                        ll_content_root.addView(new FuWuBDContentModule(mContext, sList, tempItemJot.optString("moduleName")));
                    }
                }
            }
        }
    };
}
