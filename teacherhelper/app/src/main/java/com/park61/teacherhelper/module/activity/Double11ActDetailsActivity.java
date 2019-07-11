package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.bean.Double11ActDetailsBean;
import com.park61.teacherhelper.module.pay.PayConfirmActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.textview.Double11ActDetailsDTTv;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 双11活动详情数据
 * Created by shubei on 2017/11/1.
 */
public class Double11ActDetailsActivity extends BaseActivity {

    private TextView tv_title, tv_price, tv_old_price, tv_sale_percent, tv_sale_max, tv_start_time;
    private ImageView img_act;
    private Double11ActDetailsDTTv tv_downtime;
    private LinearLayout area_percent;
    private ShowImageWebView webview;
    private Button btn_pay, btn_before, btn_finish, btn_noleft;
    private View area_before, area_going, area_sales_percent, area_sale_max;
    private PullToRefreshScrollView mPullRefreshScrollView;

    private int activityId;//活动id
    private Double11ActDetailsBean mDouble11ActDetailsBean;//活动详情bean

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_double11_actdetails);
    }

    @Override
    public void initView() {
        setPagTitle("武汉站");
        img_act = (ImageView) findViewById(R.id.img_act);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_downtime = (Double11ActDetailsDTTv) findViewById(R.id.tv_downtime);
        area_percent = (LinearLayout) findViewById(R.id.area_percent);
        webview = (ShowImageWebView) findViewById(R.id.webview);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_old_price = (TextView) findViewById(R.id.tv_old_price);
        ViewInitTool.lineText(tv_old_price);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_sale_percent = (TextView) findViewById(R.id.tv_sale_percent);
        area_before = findViewById(R.id.area_before);
        area_going = findViewById(R.id.area_going);
        tv_sale_max = (TextView) findViewById(R.id.tv_sale_max);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        btn_before = (Button) findViewById(R.id.btn_before);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_noleft = (Button) findViewById(R.id.btn_noleft);
        area_sales_percent = findViewById(R.id.area_sales_percent);
        area_sale_max = findViewById(R.id.area_sale_max);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
        asyncGetEventDetail();
    }

    @Override
    public void initListener() {
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId == -1) {//没有详情不能支付
                    return;
                }
                GlobalParam.ORDER_TYPE = "double11";
                Intent it = new Intent(mContext, PayConfirmActivity.class);
                it.putExtra("activityId", activityId);
                startActivity(it);
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetEventDetail();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
    }

    /**
     * 请求活动详情数据
     */
    private void asyncGetEventDetail() {
        String wholeUrl = AppUrl.host + AppUrl.eventDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, elistener);
    }

    BaseRequestListener elistener = new JsonRequestListener() {
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
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshScrollView.onRefreshComplete();
            mDouble11ActDetailsBean = gson.fromJson(jsonResult.toString(), Double11ActDetailsBean.class);
            //填充数据
            fillData();
        }
    };

    private void fillData() {
        setPagTitle(mDouble11ActDetailsBean.getAddress());//地址
        ImageManager.getInstance().displayImg(img_act, mDouble11ActDetailsBean.getCoverImg());//封面
        tv_title.setText(mDouble11ActDetailsBean.getName());//名称

        //进度条
        ViewGroup.LayoutParams params = area_percent.getLayoutParams();
        params.width = DevAttr.dip2px(mContext, (float) (mDouble11ActDetailsBean.getApplyPer() * 150));
        area_percent.setLayoutParams(params);
        tv_sale_percent.setText((int) (mDouble11ActDetailsBean.getApplyPer() * 100) + "%");

        //倒计时
        startMyTimer();

        tv_sale_max.setText("仅限" + mDouble11ActDetailsBean.getMaxApplyNum() + "名");//最大报名数
        tv_start_time.setText(mDouble11ActDetailsBean.getApplyStartDate() + "\n开抢");//开抢时间

        //活动详情html片段
        ViewInitTool.initShowimgWebview(webview);
        ViewInitTool.setWebData(webview, mDouble11ActDetailsBean.getContent());

        tv_price.setText("￥" + FU.strDbFmt(mDouble11ActDetailsBean.getPrice()));//现价
        if (TextUtils.isEmpty(mDouble11ActDetailsBean.getOriginalPrice())) {
            tv_old_price.setText("");
        } else {
            tv_old_price.setText("￥" + FU.strDbFmt(mDouble11ActDetailsBean.getOriginalPrice()));//原价
        }

        if (!mDouble11ActDetailsBean.isLimit()) {//如果没有上限，隐藏进度调
            area_sales_percent.setVisibility(View.INVISIBLE);
            area_sale_max.setVisibility(View.INVISIBLE);
        }

        //根据活动状态，展示不同内容，与按钮状态
        if (mDouble11ActDetailsBean.getStatus() == 0) {//未开始
            area_going.setVisibility(View.GONE);
            area_before.setVisibility(View.VISIBLE);
            btn_before.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.GONE);
            btn_finish.setVisibility(View.GONE);
            btn_noleft.setVisibility(View.GONE);
        } else if (mDouble11ActDetailsBean.getStatus() == 1) {//报名中
            area_going.setVisibility(View.VISIBLE);
            area_before.setVisibility(View.GONE);
            btn_before.setVisibility(View.GONE);
            btn_pay.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);
            btn_noleft.setVisibility(View.GONE);
        } else if (mDouble11ActDetailsBean.getStatus() == 2) {//完成
            area_going.setVisibility(View.GONE);
            area_before.setVisibility(View.GONE);
            btn_before.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_finish.setVisibility(View.VISIBLE);
            btn_noleft.setVisibility(View.GONE);
        }

        if (mDouble11ActDetailsBean.getApplyPer() == 1.00 && mDouble11ActDetailsBean.getApplyNum() == mDouble11ActDetailsBean.getMaxApplyNum()) {
            btn_before.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_finish.setVisibility(View.GONE);
            btn_noleft.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开始计时
     */
    public void startMyTimer() {
        if (!tv_downtime.isRun()) {
            tv_downtime.setTimes(DateTool.formatDuring(mDouble11ActDetailsBean.getLeftSecond() * 1000));
            tv_downtime.setOnTimeEndLsner(new Double11ActDetailsDTTv.OnTimeEnd() {
                @Override
                public void onEnd() {
                    asyncGetEventDetail();
                }
            });
            tv_downtime.run();
        }
    }


}
