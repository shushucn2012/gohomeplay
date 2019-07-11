package com.park61.teacherhelper.module.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.broadcast.BCActOrderRefresh;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.bean.MeOrderDetailBean;
import com.park61.teacherhelper.module.pay.DoPayActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品订单详情页面
 * create by super on 20180111
 */
public class MeOrderDetailActivity extends BaseActivity {

    private long orderId;
    private MeOrderDetailBean meOrderDetailBean;

    private ImageView img_course;
    private TextView tv_course_title, tv_goods_price, tv_total_price, tv_coupon_money, tv_final_money, tv_order_number, tv_order_time, tv_total_money;
    private View bottom_bar;
    private Button btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_me_orderdetail);
    }

    @Override
    public void initView() {
        setPagTitle("订单详情");
        registerRefreshReceiver();
        img_course = (ImageView) findViewById(R.id.img_course);
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        bottom_bar = findViewById(R.id.bottom_bar);
        btn_pay = (Button) findViewById(R.id.btn_pay);
    }

    @Override
    public void initData() {
        orderId = getIntent().getLongExtra("orderId", -1);
        asyncGetOrderDetail();
    }

    @Override
    public void initListener() {
        btn_pay.setOnClickListener(v -> {
            Intent it = new Intent(mContext, DoPayActivity.class);
            it.putExtra("orderId", orderId);
            startActivity(it);
        });
    }

    /**
     * 请求订单详情
     */
    private void asyncGetOrderDetail() {
        String wholeUrl = AppUrl.host + AppUrl.courseSoDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            dDialog.showDialog("提示", "网络请求失败", "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        asyncGetOrderDetail();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            meOrderDetailBean = gson.fromJson(jsonResult.toString(), MeOrderDetailBean.class);
            setDataToView();
        }
    };

    public void setDataToView() {
        ImageManager.getInstance().displayImg(img_course, meOrderDetailBean.getCoverUrl());
        tv_course_title.setText(meOrderDetailBean.getTitle());
        tv_goods_price.setText("￥" + FU.strDbFmt(meOrderDetailBean.getProductAmount()));//价格
        tv_total_price.setText("￥" + FU.strDbFmt(meOrderDetailBean.getProductAmount()));//订单金额
        tv_coupon_money.setText("-￥" + FU.strDbFmt(meOrderDetailBean.getCouponAmount()));//优惠券金额
        tv_final_money.setText("￥" + FU.strDbFmt(meOrderDetailBean.getAmount()));//总计
        tv_total_money.setText("￥" + FU.strDbFmt(meOrderDetailBean.getAmount()));//总计
        tv_order_number.setText("订单编号：" + meOrderDetailBean.getId());
        tv_order_time.setText("下单时间：" + meOrderDetailBean.getCreateDate());

        if (meOrderDetailBean.getStatus() == 0) {//未支付
            bottom_bar.setVisibility(View.VISIBLE);
        } else {
            bottom_bar.setVisibility(View.GONE);
        }
    }

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_ORDER");
        mContext.registerReceiver(orderChangeReceiver, filter);
    }

    private BroadcastReceiver orderChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncGetOrderDetail();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(orderChangeReceiver);
    }

}
