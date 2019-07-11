package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.park61.teacherhelper.module.activity.bean.Double11TicketDetailsBean;
import com.park61.teacherhelper.module.pay.DoPayActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 双11入场证详情页面
 * Created by shubei on 2017/11/2.
 */
public class Double11ActTicketDetailsActivity extends BaseActivity {

    private int activityApplyId;//入场证id
    private Double11TicketDetailsBean mDouble11TicketDetailsBean;//入场证详情bean

    private TextView tv_state, browse_name_tv, browse_number_tv, tv_toy_name, tv_addr, tv_real_price, tv_amount, tv_manage_amount, tv_party_amount,
            tv_create_order_time, tv_pay_time, tv_sign_time, tv_refund_time, tv_order_no;
    private View area_right, bottom_area, area_pay_time, area_sign_time, area_refund_time;
    private ImageView img_state, img_toy;
    private Button btn_cancel, btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_double11_act_ticket_details);
    }

    @Override
    public void initView() {
        setPagTitle("入场证");
        tv_state = (TextView) findViewById(R.id.tv_state);
        img_state = (ImageView) findViewById(R.id.img_state);
        browse_name_tv = (TextView) findViewById(R.id.browse_name_tv);
        browse_number_tv = (TextView) findViewById(R.id.browse_number_tv);
        img_toy = (ImageView) findViewById(R.id.img_toy);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_real_price = (TextView) findViewById(R.id.tv_real_price);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_manage_amount = (TextView) findViewById(R.id.tv_manage_amount);
        tv_party_amount = (TextView) findViewById(R.id.tv_party_amount);
        tv_create_order_time = (TextView) findViewById(R.id.tv_create_order_time);
        tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_sign_time = (TextView) findViewById(R.id.tv_sign_time);
        tv_refund_time = (TextView) findViewById(R.id.tv_refund_time);
        area_right = findViewById(R.id.area_right);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        bottom_area = findViewById(R.id.bottom_area);
        area_pay_time = findViewById(R.id.area_pay_time);
        area_sign_time = findViewById(R.id.area_sign_time);
        area_refund_time = findViewById(R.id.area_refund_time);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
    }

    @Override
    public void initData() {
        mDouble11TicketDetailsBean = getIntent().getParcelableExtra("from_scan");
        if (mDouble11TicketDetailsBean == null) {
            activityApplyId = getIntent().getIntExtra("activityApplyId", -1);
            asyncActivityApplyDetail();
        } else {
            fillData();
        }
    }

    @Override
    public void initListener() {
        //拨打电话联系客服退款
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneShow = "15325969066";
                final String phoneReal = "15325969066";
                dDialog.showDialog("提示", "确定拨打" + phoneShow + "客服电话退款吗?", "取消", "确认", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneReal));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dDialog.dismissDialog();
                    }
                });
            }
        });
        //取消订单
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "确定取消订单吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncCancelOrder();
                    }
                });
            }
        });
        //继续付款
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, DoPayActivity.class);
                it.putExtra("orderId", mDouble11TicketDetailsBean.getMySoVo().getId());
                it.putExtra("rphone", mDouble11TicketDetailsBean.getUserMobile());
                startActivity(it);
                finish();
            }
        });
    }

    /**
     * 请求入场证详情数据
     */
    private void asyncActivityApplyDetail() {
        String wholeUrl = AppUrl.host + AppUrl.activityApplyDetail;//"http://192.168.100.13:8080/mockjsdata/6/service/activity/activityApplyDetail";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityApplyId", activityApplyId);
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
            mDouble11TicketDetailsBean = gson.fromJson(jsonResult.toString(), Double11TicketDetailsBean.class);
            //填充数据
            fillData();
        }
    };

    private void fillData() {
        //根据状态展示不同数据
        if (mDouble11TicketDetailsBean.getComboStatus() == 0) { //待付款
            img_state.setImageResource(R.mipmap.icon_details_daifukuan);
            tv_state.setText("待签到（待付款）");

            //不显示下单时间，签到时间，退款时间，和退款联系客服按钮
            tv_pay_time.setVisibility(View.GONE);
            area_pay_time.setVisibility(View.GONE);
            tv_sign_time.setVisibility(View.GONE);
            area_sign_time.setVisibility(View.GONE);
            tv_refund_time.setVisibility(View.GONE);
            area_refund_time.setVisibility(View.GONE);
            area_right.setVisibility(View.GONE);

            //显示取消订单，付款按钮
            bottom_area.setVisibility(View.VISIBLE);
        } else if (mDouble11TicketDetailsBean.getComboStatus() == 1) {//待签到
            img_state.setImageResource(R.mipmap.icon_details_yifukuan);
            tv_state.setText("待签到（已付款）");

            //不显示签到时间，退款时间，显示退款联系客服按钮
            tv_pay_time.setVisibility(View.VISIBLE);
            area_pay_time.setVisibility(View.VISIBLE);
            tv_sign_time.setVisibility(View.GONE);
            area_sign_time.setVisibility(View.GONE);
            tv_refund_time.setVisibility(View.GONE);
            area_refund_time.setVisibility(View.GONE);
            area_right.setVisibility(View.VISIBLE);

            //不显示取消订单，付款按钮
            bottom_area.setVisibility(View.GONE);
        } else if (mDouble11TicketDetailsBean.getComboStatus() == 2) {//已签到
            img_state.setImageResource(R.mipmap.icon_details_yiqiandao);
            tv_state.setText("已签到");

            //不显示退款时间，不显示退款联系客服按钮
            tv_pay_time.setVisibility(View.VISIBLE);
            area_pay_time.setVisibility(View.VISIBLE);
            tv_sign_time.setVisibility(View.VISIBLE);
            area_sign_time.setVisibility(View.VISIBLE);
            tv_refund_time.setVisibility(View.GONE);
            area_refund_time.setVisibility(View.GONE);
            area_right.setVisibility(View.GONE);

            //不显示取消订单，付款按钮
            bottom_area.setVisibility(View.GONE);
        } else if (mDouble11TicketDetailsBean.getComboStatus() == 3) {//未付款已取消
            img_state.setImageResource(R.mipmap.icon_details_yiquxiao);
            tv_state.setText("已取消");

            //只显示下单时间，不显示退款联系客服按钮
            tv_pay_time.setVisibility(View.GONE);
            area_pay_time.setVisibility(View.GONE);
            tv_sign_time.setVisibility(View.GONE);
            area_sign_time.setVisibility(View.GONE);
            tv_refund_time.setVisibility(View.GONE);
            area_refund_time.setVisibility(View.GONE);
            area_right.setVisibility(View.GONE);

            //不显示取消订单，付款按钮
            bottom_area.setVisibility(View.GONE);
        } else if (mDouble11TicketDetailsBean.getComboStatus() == 4) {//已付款已过期
            img_state.setImageResource(R.mipmap.icon_details_yiquxiao);
            tv_state.setText("已过期");

            //显示下单时间,支付时间，显示退款联系客服按钮
            tv_pay_time.setVisibility(View.VISIBLE);
            area_pay_time.setVisibility(View.VISIBLE);
            tv_sign_time.setVisibility(View.GONE);
            area_sign_time.setVisibility(View.GONE);
            tv_refund_time.setVisibility(View.GONE);
            area_refund_time.setVisibility(View.GONE);
            area_right.setVisibility(View.VISIBLE);

            //不显示取消订单，付款按钮
            bottom_area.setVisibility(View.GONE);
        } else if (mDouble11TicketDetailsBean.getComboStatus() == 5) {//已退款成功已取消
            img_state.setImageResource(R.mipmap.jiaoyiguanbi);
            tv_state.setText("已取消(退款成功)");

            //显示下单时间,支付时间，付款时间，不显示退款联系客服按钮
            tv_pay_time.setVisibility(View.VISIBLE);
            area_pay_time.setVisibility(View.VISIBLE);
            tv_sign_time.setVisibility(View.GONE);
            area_sign_time.setVisibility(View.GONE);
            tv_refund_time.setVisibility(View.VISIBLE);
            area_refund_time.setVisibility(View.VISIBLE);
            area_right.setVisibility(View.GONE);

            //不显示取消订单，付款按钮
            bottom_area.setVisibility(View.GONE);
        }

        browse_name_tv.setText("联系人：" + mDouble11TicketDetailsBean.getUserName());//联系人姓名
        browse_number_tv.setText("电话：" + mDouble11TicketDetailsBean.getUserMobile());//联系人电话
        ImageManager.getInstance().displayImg(img_toy, mDouble11TicketDetailsBean.getTeachActivityVO().getCoverImg());//封面
        tv_toy_name.setText(mDouble11TicketDetailsBean.getTeachActivityVO().getName());//活动名称
        tv_addr.setText(mDouble11TicketDetailsBean.getTeachActivityVO().getAddress());//活动地址
        tv_real_price.setText("￥" + FU.strDbFmt(mDouble11TicketDetailsBean.getTeachActivityVO().getPrice()));//活动价格
        tv_amount.setText("￥" + FU.strDbFmt(mDouble11TicketDetailsBean.getMySoVo().getProductAmount()));//订单金额
        tv_manage_amount.setText("-￥" + FU.strDbFmt(mDouble11TicketDetailsBean.getMySoVo().getOrderPaidByCoupon()));//优惠金额
        tv_party_amount.setText("￥" + FU.strDbFmt(mDouble11TicketDetailsBean.getMySoVo().getOrderAmount()));//总计
        tv_order_no.setText(mDouble11TicketDetailsBean.getMySoVo().getId() + "");
        tv_create_order_time.setText(mDouble11TicketDetailsBean.getCreateDate());//下单时间
        tv_pay_time.setText(mDouble11TicketDetailsBean.getPayTime());//支付时间
        tv_sign_time.setText(mDouble11TicketDetailsBean.getSignDate());//签到时间
        tv_refund_time.setText(mDouble11TicketDetailsBean.getMySoVo().getOrderCancelDate());//取消时间
    }

    /**
     * 取消订单
     */
    private void asyncCancelOrder() {
        String wholeUrl = AppUrl.host + AppUrl.cancelOrder;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", mDouble11TicketDetailsBean.getTeachActivityVO().getId());
        map.put("orderId", mDouble11TicketDetailsBean.getMySoVo().getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {
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
            showShortToast("取消成功");
            finish();
        }
    };

}
