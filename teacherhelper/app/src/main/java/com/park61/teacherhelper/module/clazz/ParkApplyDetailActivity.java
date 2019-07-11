package com.park61.teacherhelper.module.clazz;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.clazz.adapter.BaseGartenApplyDetailListAdapter;
import com.park61.teacherhelper.module.clazz.bean.ApplyGartenDetail;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 基地园申请详情页
 * Created by shubei on 2018/5/29.
 */

public class ParkApplyDetailActivity extends BaseActivity {

    private TextView tv_state, tv_tip, tv_group_type, tv_group_name, tv_group_addr, tv_groupp_detail_addr, tv_contactor, tv_contact_phone;
    private ListViewForScrollView lv_zz;
    private Button bottom_bar;

    private int applyId;//申请id
    private ApplyGartenDetail mApplyGartenDetail;//详情bean
    private BaseGartenApplyDetailListAdapter detailListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_park_apply_detail);
    }

    @Override
    public void initView() {
        setPagTitle("申请详情");
        lv_zz = (ListViewForScrollView) findViewById(R.id.lv_zz);
        lv_zz.setFocusable(false);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_group_type = (TextView) findViewById(R.id.tv_group_type);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        tv_group_addr = (TextView) findViewById(R.id.tv_group_addr);
        tv_groupp_detail_addr = (TextView) findViewById(R.id.tv_groupp_detail_addr);
        tv_contactor = (TextView) findViewById(R.id.tv_contactor);
        tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);
        bottom_bar = (Button) findViewById(R.id.bottom_bar);
    }

    @Override
    public void initData() {
        applyId = getIntent().getIntExtra("applyId", -1);
        asyncApplyDetail();
    }

    @Override
    public void initListener() {
        bottom_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncResubmitApply();
            }
        });
    }

    /**
     * 请求申请详情
     */
    private void asyncApplyDetail() {
        String url = AppUrl.host + AppUrl.getApplyDetail;
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {

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
            mApplyGartenDetail = gson.fromJson(jsonResult.toString(), ApplyGartenDetail.class);
            fillData();
        }
    };

    private void fillData() {
        tv_state.setText(mApplyGartenDetail.getStatusName());
        if (mApplyGartenDetail.getStatus() == 3) {//1：待审核，2：审核通过，3：审核失败
            bottom_bar.setVisibility(View.VISIBLE);
        } else {
            bottom_bar.setVisibility(View.GONE);
        }
        tv_tip.setText(mApplyGartenDetail.getRejectReason());
        tv_group_type.setText(mApplyGartenDetail.getTypeName());
        tv_group_name.setText(mApplyGartenDetail.getSchoolName());
        tv_group_addr.setText(mApplyGartenDetail.getProvinceName() + " " + mApplyGartenDetail.getCityName() + " " + mApplyGartenDetail.getCountyName());
        tv_groupp_detail_addr.setText(mApplyGartenDetail.getAddressDetail());
        tv_contactor.setText(mApplyGartenDetail.getApplyName());
        tv_contact_phone.setText(mApplyGartenDetail.getApplyMobile());
        detailListAdapter = new BaseGartenApplyDetailListAdapter(mContext, mApplyGartenDetail.getItemList());
        lv_zz.setAdapter(detailListAdapter);
    }

    /**
     * 重新提交改变状态
     */
    private void asyncResubmitApply() {
        String url = AppUrl.host + AppUrl.resubmitApply;
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, rListener);
    }

    BaseRequestListener rListener = new JsonRequestListener() {

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
            showShortToast("提交成功");
            finish();
        }
    };
}
