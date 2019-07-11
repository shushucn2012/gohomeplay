package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.MServiceItem;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的服务详情
 * Created by shubei on 2018/3/12.
 */

public class MyServiceDetailsActivity extends BaseActivity {

    private TextView tv_status_name, tv_status_tip, tv_contactor, tv_contact_phone, tv_school_name, tv_addr;
    private ImageView img_rights;
    private View bottom_bar;

    private int applyId;//服务申请id
    private MServiceItem mServiceItem;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_myservice_details);
    }

    @Override
    public void initView() {
        setPagTitle("服务申请");
        tv_status_name = (TextView) findViewById(R.id.tv_status_name);
        tv_status_tip = (TextView) findViewById(R.id.tv_status_tip);
        tv_contactor = (TextView) findViewById(R.id.tv_contactor);
        tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        img_rights = (ImageView) findViewById(R.id.img_rights);
        bottom_bar = findViewById(R.id.bottom_bar);
        makeImageFitHeight();
    }

    /**
     * 是图片高度自适应
     */
    private void makeImageFitHeight(){
        int screenWidth = DevAttr.getScreenWidth(mContext) - DevAttr.dip2px(mContext, 30);
        ViewGroup.LayoutParams lp = img_rights.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        img_rights.setLayoutParams(lp);
        img_rights.setMaxWidth(screenWidth);
        img_rights.setMaxHeight(screenWidth * 5); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
    }

    @Override
    public void initData() {
        applyId = getIntent().getIntExtra("applyId", -1);
        anyscGetServiceDetails();
    }

    @Override
    public void initListener() {
        bottom_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "请确认服务已经完成", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        anyscConfirmFinish();
                    }
                });
            }
        });
    }

    /**
     * 请求服务申请详情数据
     */
    public void anyscGetServiceDetails() {
        String wholeUrl = AppUrl.host + AppUrl.getMyServiceDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vidodatalisenter);
    }

    BaseRequestListener vidodatalisenter = new JsonRequestListener() {

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
                        anyscGetServiceDetails();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mServiceItem = gson.fromJson(jsonResult.toString(), MServiceItem.class);
            fillData();
        }
    };

    public void fillData() {
        tv_status_name.setText(mServiceItem.getStatusName());
        tv_status_tip.setText(mServiceItem.getDescription());
        tv_contactor.setText(mServiceItem.getApplyName());
        tv_contact_phone.setText(mServiceItem.getApplyMobile());
        tv_school_name.setText(mServiceItem.getSchoolName());
        tv_addr.setText(mServiceItem.getAddressName());
        ImageManager.getInstance().displayImg(img_rights, mServiceItem.getRightsUrl());
        if (mServiceItem.getStatus() == 1) {//已受理
            bottom_bar.setVisibility(View.VISIBLE);
        } else {
            bottom_bar.setVisibility(View.GONE);
        }
    }

    /**
     * 请求确认完成
     */
    public void anyscConfirmFinish() {
        String wholeUrl = AppUrl.host + AppUrl.changeStatusToFinish;
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, clisenter);
    }

    BaseRequestListener clisenter = new JsonRequestListener() {

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
            showShortToast("确认成功");
            finish();
        }
    };
}
