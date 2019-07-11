package com.park61.teacherhelper.module.my;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.InterestingCrouseListAdapter;
import com.park61.teacherhelper.module.my.bean.InterestingClassActBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 兴趣班活动详情
 * Created by shubei on 2018/8/15.
 */

public class InterestingClassActDetailsActivity extends BaseActivity {

    private static final int IMAGE_HEIGHT = 250;

    private ImageView img_act;
    private TextView tv_title, tv_summum;
    private ListViewForScrollView lv_classes;
    private ShowImageWebView wv_content;
    private ObservableScrollView scrollview;

    private int activityId;
    private InterestingClassActBean mInterestingClassActBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_interestingclass_details);
    }

    @Override
    public void initView() {
        img_act = (ImageView) findViewById(R.id.img_act);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_summum = (TextView) findViewById(R.id.tv_summum);
        lv_classes = (ListViewForScrollView) findViewById(R.id.lv_classes);
        lv_classes.setFocusable(false);
        wv_content = (ShowImageWebView) findViewById(R.id.wv_content);
        scrollview = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollview.setCallbacks(mCallbacks);
        scrollview.getViewTreeObserver().addOnGlobalLayoutListener(() -> mCallbacks.onScrollChanged(scrollview.getScrollY()));
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("id", -1);
        asyncGetDetailById();
    }

    @Override
    public void initListener() {
        area_right.setOnClickListener(v -> showShareDialog("http://m.61park.cn/#/hobby/detail/" + mInterestingClassActBean.getId(),
                mInterestingClassActBean.getDetailUrl(),
                mInterestingClassActBean.getTitle(),
                mInterestingClassActBean.getSummary()));
    }

    /**
     * 滑动黏贴view的监听事件
     */
    private ObservableScrollView.Callbacks mCallbacks = new ObservableScrollView.Callbacks() {

        @Override
        public void onUpOrCancelMotionEvent() {
        }

        @Override
        public void onScrollChanged(int scrollY) {
            // 控制TOPBAR的渐变效果
            if (scrollY > DevAttr.dip2px(mContext, IMAGE_HEIGHT)) {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                ((ImageView) findViewById(R.id.img_back)).setImageResource(R.mipmap.icon_red_backimg);
                ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_share_red);
            } else if (scrollY > DevAttr.dip2px(mContext, IMAGE_HEIGHT / 2)) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
            } else {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                ((ImageView) findViewById(R.id.img_back)).setImageResource(R.mipmap.icon_content_backimg);
                ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_shadow_share);
            }
        }

        @Override
        public void onDownMotionEvent() {
        }
    };

    /**
     * 获取详情
     */
    private void asyncGetDetailById() {
        String url = AppUrl.host + AppUrl.interestClassDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, taskListener);
    }

    BaseRequestListener taskListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            findViewById(R.id.area_empty).setVisibility(View.GONE);
            mInterestingClassActBean = gson.fromJson(jsonResult.toString(), InterestingClassActBean.class);
            if (mInterestingClassActBean != null)
                fillData();
        }
    };

    private void fillData() {
        ImageManager.getInstance().displayImg(img_act, mInterestingClassActBean.getCoverUrl());
        tv_title.setText(mInterestingClassActBean.getTitle());
        tv_summum.setText(mInterestingClassActBean.getSummary());
        ViewInitTool.initShowimgWebview(wv_content);
        ViewInitTool.setWebData(wv_content, mInterestingClassActBean.getContent());
        InterestingCrouseListAdapter adapter = new InterestingCrouseListAdapter(mContext,
                mInterestingClassActBean.getInterestClassList(), InterestingCrouseListAdapter.FROM_PAGE.ACTDETAILS_PAGE);
        lv_classes.setAdapter(adapter);
    }
}
