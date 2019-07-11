package com.park61.teacherhelper.module.pay;

import android.content.Intent;
import android.text.TextUtils;
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
import com.android.volley.Request;
import com.park61.teacherhelper.module.pay.bean.ApplyBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shubei on 2017/7/22.
 */

public class PayConfirmActivity extends BaseActivity {

    private String rname, rphone;//联系人姓名，电话
    private ApplyBean curApply;//报名信息
    private int activityId;//活动id

    private ImageView img_course;
    private TextView tv_course_title, tv_goods_price, tv_total_price,
            tv_coupon_money, tv_final_money, tv_total_money, tv_rname, tv_rphone, tv_dikou_money;
    private Button btn_pay;
    private View area_dikou;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pay_confirm);
    }

    @Override
    public void initView() {
        img_course = (ImageView) findViewById(R.id.img_course);
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_dikou_money = (TextView) findViewById(R.id.tv_dikou_money);
        tv_rname = (TextView) findViewById(R.id.tv_rname);
        tv_rphone = (TextView) findViewById(R.id.tv_rphone);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        area_dikou = findViewById(R.id.area_dikou);
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
        asyncGetApplyInfo();
    }

    @Override
    public void initListener() {
        setPagTitle("确认订单");
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSubmitCourseOrder();
            }
        });
    }

    /**
     * 请求活动信息
     */
    private void asyncGetApplyInfo() {
        String wholeUrl = AppUrl.host + AppUrl.activity_confirm;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            dDialog.showDialog("提示", "数据请求失败", "取消", "重试",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asyncGetApplyInfo();
                        }
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            curApply = gson.fromJson(jsonResult.toString(), ApplyBean.class);
            //填充数据
            fillData();
        }
    };

    /**
     * 提交订单
     */
    private void asyncSubmitCourseOrder() {
        String wholeUrl = AppUrl.host + AppUrl.activity_submit;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        map.put("uuid", UUID.randomUUID().toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, sLsner);
    }

    BaseRequestListener sLsner = new JsonRequestListener() {

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
            if (curApply.getOrderAmount() > 0) {
                showShortToast("订单提交成功，请继续支付！");
                Intent it = new Intent(mContext, DoPayActivity.class);
                Long orderId = FU.paseLong(jsonResult.optString("data"));
                it.putExtra("orderId", orderId);
                it.putExtra("rphone", rphone);
                startActivity(it);
                finish();
            } else {
                showShortToast("订单提交成功!");
                Intent paySuccessIt = new Intent(mContext, PaySuccessActivity.class);
                startActivity(paySuccessIt);
                finish();

                //发送刷新订单列表广播
                Intent changeIt = new Intent();
                changeIt.setAction("ACTION_REFRESH_ORDER");
                sendBroadcast(changeIt);
            }
        }
    };

    /**
     * 填充数据
     */
    public void fillData() {
        rname = TextUtils.isEmpty(curApply.getUserName()) ? "" : curApply.getUserName();
        rphone = curApply.getUserMobile();
        tv_rname.setText("联系人：" + rname);//姓名
        tv_rphone.setText(rphone);//电话

        ImageManager.getInstance().displayImg(img_course, curApply.getCoverImg());//封面
        tv_course_title.setText(curApply.getName());//活动名称
        tv_goods_price.setText("￥" + FU.strDbFmt(curApply.getPrice()));//价格
        tv_total_price.setText("￥" + FU.strDbFmt(curApply.getOrderAmount()));//订单金额
        tv_coupon_money.setText("-￥" + FU.strDbFmt(curApply.getCouponAmount()));//优惠券金额
        tv_final_money.setText("￥" + FU.strDbFmt(curApply.getOrderAmount()));//总计
        tv_total_money.setText("￥" + FU.strDbFmt(curApply.getOrderAmount()));//合计
        tv_dikou_money.setText("-￥" + FU.strDbFmt(curApply.getTeachDeductAmount()));//抵扣

        if (curApply.getTeachDeductAmount() == 0.00) {
            area_dikou.setVisibility(View.GONE);
        }
    }

}
