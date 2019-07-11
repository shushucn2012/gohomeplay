package com.park61.teacherhelper.module.my.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.FragmentChooseJobBinding;
import com.park61.teacherhelper.module.my.AgentInforActivity;
import com.park61.teacherhelper.module.my.KidGardenActivity;
import com.park61.teacherhelper.module.my.bean.DutyBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.ComSelectDialogBottom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/5/7.
 */

public class FragmentChooseJob extends BaseFragment {

    FragmentChooseJobBinding binding;
    List<String> jobs;
    List<DutyBean> duties;
    int jobId;
    private int couponActivityId;//优惠券id
    private int retryCountMax = 3;//最大领取重试次数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_job, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        jobs = new ArrayList<>();
        duties = new ArrayList<>();
    }

    @Override
    public void initListener() {
        binding.jobArea.setOnClickListener(v -> {
            //获取职务列表
            asyncDutyList();
        });

        binding.jobName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100f4);
                } else {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100c9);
                }
            }
        });

        binding.goNext.setOnClickListener(v -> {
            if (binding.jobName.getText().toString().length() > 0) {
                // 提交信息后去下一页
                submitGardenInfo();
            }
        });
    }

    private void asyncDutyList() {
        String url = AppUrl.host + AppUrl.getMemberDuties;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, dutyListener);
    }

    BaseRequestListener dutyListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray list = jsonResult.optJSONArray("list");
            if (list != null && list.length() > 0) {
                jobs.clear();
                duties.clear();
                for (int i = 0; i < list.length(); i++) {
                    DutyBean b = gson.fromJson(list.optJSONObject(i).toString(), DutyBean.class);
                    jobs.add(b.getLabel());
                    duties.add(b);
                }

                //职务列表选择
                ComSelectDialogBottom mComSelectDialog = new ComSelectDialogBottom(parentActivity, jobs);
                mComSelectDialog.setOnItemSelectLsner(new ComSelectDialogBottom.OnItemSelect() {
                    @Override
                    public void onSelect(int position) {
                        jobId = Integer.parseInt(duties.get(position).getValue());
                        binding.jobName.setText(jobs.get(position));
                    }
                });
                mComSelectDialog.showDialog();
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    /**
     * 提交园长和教师信息
     */
    private void submitGardenInfo() {
        String url = AppUrl.host + AppUrl.submitTeach;
        Map<String, Object> map = new HashMap<>();
        map.put("groupId", ((KidGardenActivity) getActivity()).getGroupId());
        map.put("duties", jobId);
        map.put("userDuty", ((KidGardenActivity) getActivity()).getUserDuty());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, submitListener);
    }

    BaseRequestListener submitListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //返回优惠券活动id
            couponActivityId = jsonResult.optInt("couponActivityId");
            int status = jsonResult.optInt("status");
            //关闭引导页面
            getActivity().sendBroadcast(new Intent("RECEIVE_SUCCESS"));

            if (status == 0) {
                //无优惠券活动直接关闭
                getActivity().finish();
            } else {
                ((KidGardenActivity) getActivity()).setCouponActivityId(couponActivityId);
                //领取优惠券
                receiveCoupon(couponActivityId);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void receiveCoupon(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("couponActivityId", id);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(AppUrl.receiveCoupon, Request.Method.POST, requestData, 0, receiceListener);
    }

    BaseRequestListener receiceListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ((KidGardenActivity) getActivity()).goNext();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000042007".equals(errorCode)) {//优惠券已领，成功
                ((KidGardenActivity) getActivity()).goNext();
            } else if ("0000042010".equals(errorCode)) {//已领完，或者活动失败
                AlertDialog.Builder d = new AlertDialog.Builder(parentActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                d.setMessage("活动已结束，优惠券领完啦~");
                d.setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().finish();
                });
                d.setCancelable(false);
                d.create();
                d.show();
            } else {
                if (retryCountMax > 0) {
                    AlertDialog.Builder d = new AlertDialog.Builder(parentActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    d.setMessage("奖励领取失败，请重试");
                    d.setNegativeButton("放弃领取", (dialog, which) -> {
                        dialog.dismiss();
                        getActivity().finish();
                    });
                    d.setPositiveButton("重试", (dialog, which) -> {
                        dialog.dismiss();
                        receiveCoupon(couponActivityId);
                    });
                    d.setCancelable(false);
                    d.create();
                    d.show();
                    retryCountMax--;
                } else {
                    showShortToast("奖励领取失败");
                    getActivity().finish();
                }
            }
        }
    };
}
