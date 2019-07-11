package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.CourseOrderBean;
import com.park61.teacherhelper.module.pay.CouponChooseActivity;
import com.park61.teacherhelper.module.pay.DoPayActivity;
import com.park61.teacherhelper.module.pay.PayAllSuccessActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nieyu on 2017/12/11.
 */

public class ExpertCoursePaysActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQ_CHOOSE_COUPON = 0;
    private CourseOrderBean tob;//结算对象
    private int courseSeriesId;
    private int chosenCouponId;//选择优惠券的id
    private boolean useCoupon = true;

    private ImageView iv_righticon;
    private TextView tv_titles, tv_agearount, tv_realprice, tv_allprices, tv_orignalprices, tv_haspaper, tv_real_price, tv_coupon_money, tv_canuse_coupon_count;
    private View area_coupon;
    private Button btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_order);
    }

    @Override
    public void initView() {
        setPagTitle("确认订单");
        iv_righticon = (ImageView) findViewById(R.id.iv_righticon);
        tv_titles = (TextView) findViewById(R.id.tv_titles);
        tv_agearount = (TextView) findViewById(R.id.tv_agearount);
        tv_realprice = (TextView) findViewById(R.id.tv_realprice);
        tv_allprices = (TextView) findViewById(R.id.tv_allprices);
        tv_orignalprices = (TextView) findViewById(R.id.tv_orignalprices);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_haspaper = (TextView) findViewById(R.id.tv_haspaper);
        tv_real_price = (TextView) findViewById(R.id.tv_real_price);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_canuse_coupon_count = (TextView) findViewById(R.id.tv_canuse_coupon_count);
        area_coupon = findViewById(R.id.area_coupon);
    }

    @Override
    public void initData() {
        courseSeriesId = getIntent().getIntExtra("courseSeriesId", -1);
        ansyGetTrandOrder();
    }

    @Override
    public void initListener() {
        btn_pay.setOnClickListener(this);
        area_coupon.setOnClickListener(this);
    }

    /**
     * couponUseId 用户优惠券id
     * courseSeriesId 课程系列id
     * useCoupon 是否使用优惠券
     * userToken 用户登录标志
     */
    public void ansyGetTrandOrder() {
        String wholeUrl = AppUrl.host + AppUrl.getCourseOrderInfo;
        Map<String, Object> map = new HashMap<String, Object>();
        if (chosenCouponId > 0) {
            map.put("couponUseId", chosenCouponId);
        }
        map.put("useCoupon", useCoupon);
        map.put("courseSeriesId", courseSeriesId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, trandorderlisenter);
    }

    BaseRequestListener trandorderlisenter = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            //增加提示
            dDialog.showDialog("提示", errorMsg, "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        ansyGetTrandOrder();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            tob = gson.fromJson(jsonResult.toString(), CourseOrderBean.class);
            chosenCouponId = tob.getCouponId();
            ImageManager.getInstance().displayImg(iv_righticon, tob.getImgUrl());
            tv_titles.setText(tob.getTitle());
            tv_realprice.setText("¥" + FU.strDbFmt(tob.getOriginalPrice()));

            if (tob.getCanUserCouponNum() > 0) {//有可用优惠券
                if (0 == tob.getCouponAmount()) {//未使用
                    tv_canuse_coupon_count.setText("(" + tob.getCanUserCouponNum() + "张可用)");
                    tv_haspaper.setText("未使用");
                } else {//已使用
                    tv_canuse_coupon_count.setText("(已选1张)");
                    tv_haspaper.setText("-¥" + FU.strDbFmt(tob.getCouponAmount()));
                }
            } else {//无可用优惠券
                tv_canuse_coupon_count.setText("");
                tv_haspaper.setText("无可用");
            }

            tv_allprices.setText("¥" + FU.strDbFmt(tob.getSalePrice()));
            tv_orignalprices.setText("¥" + FU.strDbFmt(tob.getOriginalPrice()));
            tv_coupon_money.setText("-¥" + FU.strDbFmt(tob.getCouponAmount()));
            tv_real_price.setText("¥" + FU.strDbFmt(tob.getSalePrice()));
        }
    };

    @Override
    public void onClick(View view) {
        if (R.id.btn_pay == view.getId()) {
            if (tob == null)
                return;
            ansyGetTranSubmit();
        } else if (R.id.area_coupon == view.getId()) {
            Intent cIt = new Intent(mContext, CouponChooseActivity.class);
            cIt.putExtra("courseId", courseSeriesId);
            cIt.putExtra("chosenCouponId", chosenCouponId);
            startActivityForResult(cIt, REQ_CHOOSE_COUPON);
        }
    }

    public void ansyGetTranSubmit() {
        String wholeUrl = AppUrl.host + AppUrl.submitCourseOrder;
        Map<String, Object> map = new HashMap<String, Object>();
        if (chosenCouponId > 0) {
            map.put("couponUseId", chosenCouponId);
        }
        map.put("courseSeriesId", courseSeriesId);
        map.put("uuid", UUID.randomUUID());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, trandsubmitlisenter);
    }

    BaseRequestListener trandsubmitlisenter = new JsonRequestListener() {

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
            if (FU.isNumEmpty(tob.getSalePrice() + "")) {//0元订单，直接跳成功
                Intent paySuccessIt = new Intent(mContext, PayAllSuccessActivity.class);
                startActivity(paySuccessIt);
            } else {//有金额，去支付
                Intent it = new Intent(mContext, DoPayActivity.class);
                it.putExtra("finishToPage", "home");
                it.putExtra("orderId", FU.paseLong(jsonResult.optString("data")));
                startActivity(it);
            }
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_COUPON) {
            if (resultCode == RESULT_OK) {
                chosenCouponId = data.getIntExtra("chosenCouponId", -1);
                if (chosenCouponId == -1) {
                    useCoupon = false;
                } else {
                    useCoupon = true;
                }
                ansyGetTrandOrder();
            }
        }
    }
}