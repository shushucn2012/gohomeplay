package com.park61.teacherhelper.module.my.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.FragmentGetRewardBinding;
import com.park61.teacherhelper.module.my.AgentInforActivity;
import com.park61.teacherhelper.module.my.KidGardenActivity;
import com.park61.teacherhelper.module.my.adapter.ComponListAdapter;
import com.park61.teacherhelper.module.my.bean.ComponBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/5/7.
 *
 */

public class FragmentGetReward extends BaseFragment {

    FragmentGetRewardBinding binding;
    List<ComponBean> datas;
    ComponListAdapter adapter;
    boolean needRequest = false;
    int couponActivityId;
    Toast t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_reward, container, false);
        parentView = binding.getRoot();
        needRequest = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if(isVisibleToUser && needRequest){
            needRequest = false;
            asyncComponList();
        }
    }

    @Override
    public void initView() {
        t = new Toast(parentActivity);
        View c = LayoutInflater.from(parentActivity).inflate(R.layout.toast_center, null);
        t.setView(c);
        t.setDuration(Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
    }

    @Override
    public void initData() {
        //获取礼品列表
        datas = new ArrayList<>();
        adapter = new ComponListAdapter(parentActivity, datas);
        binding.rLv.setLayoutManager(new LinearLayoutManager(parentActivity));
        binding.rLv.setAdapter(adapter);
        binding.rLv.setNestedScrollingEnabled(false);
    }

    @Override
    public void initListener() {
        binding.confirmGet.setOnClickListener( v -> {
            //弹出对话框
            if(datas.size() >0){
                t.show();
                getActivity().finish();
            }
        });

    }

    private void asyncComponList(){
        if(getActivity() instanceof KidGardenActivity){
            couponActivityId = ((KidGardenActivity)getActivity()).getCouponActivityId();
        }else if(getActivity() instanceof AgentInforActivity){
            couponActivityId = ((AgentInforActivity)getActivity()).getCouponActivityId();
        }
        String url = AppUrl.host + AppUrl.lookCoupon;
        Map<String, Object> map = new HashMap<>();
        map.put("couponActivityId", couponActivityId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0 , couponListener);
    }

    BaseRequestListener couponListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if(arr != null && arr.length() > 0){
                for(int i=0; i<arr.length(); i++){
                    ComponBean b = gson.fromJson(arr.optJSONObject(i).toString(), ComponBean.class);
                    datas.add(b);
                }

                binding.componNum.setText(""+datas.size());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t != null) {
            t.cancel();
        }

    }
}
