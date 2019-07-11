package com.park61.teacherhelper.module.pay;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.CourseOrderBean;
import com.park61.teacherhelper.module.home.bean.JiXunOrderBean;
import com.park61.teacherhelper.module.pay.CouponChooseActivity;
import com.park61.teacherhelper.module.pay.DoPayActivity;
import com.park61.teacherhelper.module.pay.PayAllSuccessActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.ComSelectDialogBottom;
import com.park61.teacherhelper.widget.pw.ComBottomPopWin;
import com.park61.teacherhelper.widget.pw.TaskChooseLevelPopWin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.park61.teacherhelper.R.id.tv_level1_name;
import static com.park61.teacherhelper.common.tool.FU.fmtSmallFrontUnit;
import static com.park61.teacherhelper.common.tool.FU.strDbFmt;

/**
 * 集训活动订单确认页面
 * Created by shuebi on 2018/9/13
 */

public class JiXunActPayConfirmActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQ_CHOOSE_COUPON = 0;
    private JiXunOrderBean orderBean;//结算对象
    private int intensifiedTrainingActivityId;
    private int chosenCouponId;//选择优惠券的id
    private boolean useCoupon = true;
    private int joinNum;

    private ImageView img_course;
    private TextView tv_course_title, tv_goods_price, tv_total_price, tv_final_money, tv_haspaper, tv_total_money, tv_coupon_money, tv_canuse_coupon_count,
            tv_rname, tv_rphone, tv_raddr, tv_join_person;
    private View area_coupon;
    private Button btn_pay;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_jixun_act_pay_confirm);
    }

    @Override
    public void initView() {
        setPagTitle("确认订单");
        img_course = (ImageView) findViewById(R.id.img_course);
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        tv_haspaper = (TextView) findViewById(R.id.tv_haspaper);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        tv_canuse_coupon_count = (TextView) findViewById(R.id.tv_canuse_coupon_count);
        area_coupon = findViewById(R.id.area_coupon);
        tv_rname = (TextView) findViewById(R.id.tv_rname);
        tv_rphone = (TextView) findViewById(R.id.tv_rphone);
        tv_raddr = (TextView) findViewById(R.id.tv_raddr);
        tv_join_person = (TextView) findViewById(R.id.tv_join_person);
    }

    @Override
    public void initData() {
        intensifiedTrainingActivityId = getIntent().getIntExtra("intensifiedTrainingActivityId", -1);
        ansyGetTrandOrder();
    }

    @Override
    public void initListener() {
        btn_pay.setOnClickListener(this);
        area_coupon.setOnClickListener(this);

        findViewById(R.id.area_join_person_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
    }

    public void showPopFormBottom() {
        List<String> items = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            items.add(i + "");
        }
        ComBottomPopWin mComBottomPopWin = new ComBottomPopWin(mContext, items, "参与人数选择");
        // 设置Popupwindow显示位置（从底部弹出）
        mComBottomPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mComBottomPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        mComBottomPopWin.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_join_person.setText(items.get(position) + "人");
                joinNum = FU.paseInt(items.get(position));
                mComBottomPopWin.dismiss();
            }
        });
    }

    /**
     * couponUseId 用户优惠券id
     * courseSeriesId 课程系列id
     * useCoupon 是否使用优惠券
     * userToken 用户登录标志
     */
    public void ansyGetTrandOrder() {
        String wholeUrl = AppUrl.host + AppUrl.intensifiedTraining_confirm;
        Map<String, Object> map = new HashMap<String, Object>();
        if (chosenCouponId > 0) {
            map.put("couponUseId", chosenCouponId);
        }
        map.put("useCoupon", useCoupon);
        map.put("intensifiedTrainingActivityId", intensifiedTrainingActivityId);
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
            orderBean = gson.fromJson(jsonResult.toString(), JiXunOrderBean.class);
            if (orderBean != null)
                fillData();
        }
    };

    private void fillData() {
        tv_rname.setText("联系人：" + orderBean.getUserName());
        tv_rphone.setText(orderBean.getUserMobile());
        tv_raddr.setText(orderBean.getTeachGroupName());

        chosenCouponId = orderBean.getCouponUseId();
        ImageManager.getInstance().displayImg(img_course, orderBean.getImgUrl());
        tv_course_title.setText(orderBean.getTitle());
        tv_goods_price.setText("¥" + FU.strDbFmt(orderBean.getOriginalPrice()));

        if (orderBean.getCanUserCouponNum() > 0) {//有可用优惠券
            if (0 == orderBean.getCouponAmount()) {//未使用
                tv_canuse_coupon_count.setText("(" + orderBean.getCanUserCouponNum() + "张可用)");
                tv_haspaper.setText("未使用");
            } else {//已使用
                tv_canuse_coupon_count.setText("(已选1张)");
                tv_haspaper.setText("-¥" + FU.strDbFmt(orderBean.getCouponAmount()));
            }
        } else {//无可用优惠券
            tv_canuse_coupon_count.setText("");
            tv_haspaper.setText("无可用");
        }

        tv_total_price.setText("¥" + FU.strDbFmt(orderBean.getOriginalPrice()));
        tv_coupon_money.setText("-¥" + FU.strDbFmt(orderBean.getCouponAmount()));
        tv_final_money.setText("¥" + FU.strDbFmt(orderBean.getSalePrice()));
        //tv_total_money.setText("¥" + FU.strDbFmt(orderBean.getSalePrice()));
        tv_total_money.setText(fmtSmallFrontUnit(strDbFmt(orderBean.getSalePrice()), 12, false));
    }

    @Override
    public void onClick(View view) {
        if (R.id.btn_pay == view.getId()) {
            if (orderBean == null)
                return;
            if (joinNum == 0) {
                showShortToast("请选择参与人数");
                return;
            }
            ansyGetTranSubmit();
        } else if (R.id.area_coupon == view.getId()) {
            Intent cIt = new Intent(mContext, CouponChooseActivity.class);
            cIt.putExtra("orderType", orderBean.getOrderType());
            cIt.putExtra("price", orderBean.getOriginalPrice());
            cIt.putExtra("chosenCouponId", chosenCouponId);
            startActivityForResult(cIt, REQ_CHOOSE_COUPON);
        }
    }

    public void ansyGetTranSubmit() {
        String wholeUrl = AppUrl.host + AppUrl.intensifiedTraining_submit;
        Map<String, Object> map = new HashMap<String, Object>();
        if (chosenCouponId > 0) {
            map.put("couponUseId", chosenCouponId);
        }
        map.put("intensifiedTrainingActivityId", intensifiedTrainingActivityId);
        map.put("joinNum", joinNum);
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
            if (FU.isNumEmpty(orderBean.getSalePrice() + "")) {//0元订单，直接跳成功
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