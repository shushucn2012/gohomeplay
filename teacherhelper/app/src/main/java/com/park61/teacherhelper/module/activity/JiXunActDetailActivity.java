package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.bean.JiXunActBean;
import com.park61.teacherhelper.module.pay.JiXunActPayConfirmActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.textview.JiXunActDetailsDTTv;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 集训活动详情页
 * Created by shubei on 2018/9/4.
 */

public class JiXunActDetailActivity extends BaseActivity {

    private PullToRefreshScrollView pull_refresh_scrollview;
    private ImageView img_act;
    private TextView tv_title, tv_sale_percent, tv_sale_max, tv_start_time, tv_addr, tv_price, tv_old_price;
    private JiXunActDetailsDTTv tv_downtime;
    private View area_percent;
    private ShowImageWebView webview;
    private Button btn_pay, btn_before, btn_finish, btn_noleft;

    private int intensifiedTrainingActivityId;//活动id，前一个页面传过来
    private JiXunActBean mJiXunActBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_jixun_act_detail);
    }

    @Override
    public void initView() {
        pull_refresh_scrollview = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(pull_refresh_scrollview, mContext);

        img_act = (ImageView) findViewById(R.id.img_act);
        tv_title = (TextView) findViewById(R.id.tv_title);
        area_percent = findViewById(R.id.area_percent);
        tv_sale_percent = (TextView) findViewById(R.id.tv_sale_percent);
        tv_sale_max = (TextView) findViewById(R.id.tv_sale_max);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        webview = (ShowImageWebView) findViewById(R.id.webview);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_old_price = (TextView) findViewById(R.id.tv_old_price);
        btn_before = (Button) findViewById(R.id.btn_before);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_noleft = (Button) findViewById(R.id.btn_noleft);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_downtime = (JiXunActDetailsDTTv) findViewById(R.id.tv_downtime);
    }

    @Override
    public void initData() {
        setPagTitle("集训活动");

        area_right.setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_share_red);

        intensifiedTrainingActivityId = getIntent().getIntExtra("intensifiedTrainingActivityId", -1);
        //intensifiedTrainingActivityId = 1;//测试数据
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetEventDetail();
    }

    @Override
    public void initListener() {
        pull_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetEventDetail();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, JiXunActPayConfirmActivity.class);
                it.putExtra("intensifiedTrainingActivityId", intensifiedTrainingActivityId);
                startActivity(it);
            }
        });
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mJiXunActBean == null)
                    return;
                showShareDialog("http://m.61park.cn/teach/#/training/detail/" + intensifiedTrainingActivityId,
                        mJiXunActBean.getCoverImg(),
                        mJiXunActBean.getName(),
                        "我们期待您的参与，快来看看吧！");
            }
        });
    }

    /**
     * 请求活动详情数据
     */
    private void asyncGetEventDetail() {
        String wholeUrl = AppUrl.host + AppUrl.intensifiedTrainingActivityDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("intensifiedTrainingActivityId", intensifiedTrainingActivityId);
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
            pull_refresh_scrollview.onRefreshComplete();
            if ("0000042003".equals(errorCode)) {
                dDialog.showDialog("提示", errorMsg, "返回", "确认",
                        v -> {
                            dDialog.dismissDialog();
                            finish();
                        }, v -> {
                            dDialog.dismissDialog();
                            finish();
                        });
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            pull_refresh_scrollview.onRefreshComplete();
            findViewById(R.id.area_empty).setVisibility(View.GONE);
            mJiXunActBean = gson.fromJson(jsonResult.toString(), JiXunActBean.class);
            //mJiXunActBean.setBtnStatus(1);
            //mJiXunActBean.setLimit(false);
            //填充数据
            fillData();
        }
    };

    private void fillData() {
        //setPagTitle(mJiXunActBean.getAddress());//地址
        ImageManager.getInstance().displayImg(img_act, mJiXunActBean.getCoverImg());//封面
        tv_title.setText(mJiXunActBean.getName());//名称
        tv_addr.setText("地址: " + mJiXunActBean.getFullAddress());

        //进度条
        ViewGroup.LayoutParams params = area_percent.getLayoutParams();
        params.width = DevAttr.dip2px(mContext, (float) (mJiXunActBean.getApplyPer() * 150));
        area_percent.setLayoutParams(params);
        tv_sale_percent.setText((int) (mJiXunActBean.getApplyPer() * 100) + "%");

        //倒计时
        startMyTimer();

        tv_sale_max.setText("仅限" + mJiXunActBean.getGroupNum() + "名");//最大报名数

        String startTime = DateTool.getTheDateSt(DateTool.parseDateStr(mJiXunActBean.getApplyStartTime(), "yyyy-MM-dd HH:mm:ss"), "MM月dd日HH时mm分");
        SpannableString sStr = new SpannableString(startTime);
        changeTimeUnitStyle(startTime, "月", sStr);
        changeTimeUnitStyle(startTime, "日", sStr);
        changeTimeUnitStyle(startTime, "时", sStr);
        changeTimeUnitStyle(startTime, "分", sStr);
        tv_start_time.setText(sStr);

        //活动详情html片段
        ViewInitTool.initShowimgWebview(webview);
        ViewInitTool.setWebData(webview, mJiXunActBean.getContent());

        tv_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(mJiXunActBean.getSalePrice()));//现价
        if (TextUtils.isEmpty(mJiXunActBean.getOriginalPrice())) {
            tv_old_price.setText("");
        } else {
            tv_old_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(mJiXunActBean.getOriginalPrice()));//原价
            ViewInitTool.lineText(tv_old_price);
        }

        if (!mJiXunActBean.isLimit()) {//如果没有上限，隐藏进度调
            findViewById(R.id.area_sales_percent).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_sale_max).setVisibility(View.INVISIBLE);
        }

        //根据活动状态，展示不同内容，与按钮状态
        if (mJiXunActBean.getBtnStatus() == 0) {//未开始
            findViewById(R.id.area_going).setVisibility(View.GONE);
            findViewById(R.id.area_before).setVisibility(View.VISIBLE);
            btn_before.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.GONE);
            btn_finish.setVisibility(View.GONE);
            btn_noleft.setVisibility(View.GONE);
        } else if (mJiXunActBean.getBtnStatus() == 1) {//报名中
            findViewById(R.id.area_going).setVisibility(View.VISIBLE);
            findViewById(R.id.area_before).setVisibility(View.GONE);
            btn_before.setVisibility(View.GONE);
            btn_pay.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);
            btn_noleft.setVisibility(View.GONE);
        } else if (mJiXunActBean.getBtnStatus() == 2) {//完成
            findViewById(R.id.area_going).setVisibility(View.GONE);
            findViewById(R.id.area_before).setVisibility(View.GONE);
            btn_before.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_finish.setVisibility(View.VISIBLE);
            btn_noleft.setVisibility(View.GONE);
        } else if (mJiXunActBean.getBtnStatus() == 3) {//已抢光
            findViewById(R.id.area_going).setVisibility(View.VISIBLE);
            findViewById(R.id.area_before).setVisibility(View.GONE);
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
            tv_downtime.setTimes(DateTool.formatDuring(mJiXunActBean.getLeftSecond() * 1000));
            tv_downtime.setOnTimeEndLsner(new JiXunActDetailsDTTv.OnTimeEnd() {
                @Override
                public void onEnd() {
                    asyncGetEventDetail();
                }
            });
            tv_downtime.run();
        }
    }

    private void changeTimeUnitStyle(String strTime, String unit, SpannableString sStr) {
        int indexStart = strTime.indexOf(unit);
        int indexEnd = indexStart + 1;
        sStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.gc9bec2)), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new AbsoluteSizeSpan(12, true), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


}
