package com.park61.teacherhelper.module.my.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.FragmentMyActivityBinding;
import com.park61.teacherhelper.module.my.ParentalDetailActivity;
import com.park61.teacherhelper.module.my.adapter.MyActivityAdapter;
import com.park61.teacherhelper.module.my.bean.ActivityBean;
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
 * Created by chenlie on 2018/4/8.
 *
 * 我的活动fragment
 */

public class FragmentMyActivity extends BaseFragment {

    FragmentMyActivityBinding binding;
    MyActivityAdapter dataAdapter;
    private int PAGE_NUM = 0;
    private int totalPage = 100;
    private static final int PAGE_SIZE = 6;
    List<ActivityBean> list;
    LRecyclerViewAdapter adapter;
    View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_activity, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

        emptyView = binding.emptyView.getRoot();
        binding.emptyView.emptyTv.setText("暂无活动，快去发布吧!");
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        dataAdapter = new MyActivityAdapter(parentActivity, list);
        binding.activityLv.setLayoutManager(new LinearLayoutManager(parentActivity));
        adapter = new LRecyclerViewAdapter(dataAdapter);
        adapter.setOnItemClickListener((view, position)->{
            Intent it = new Intent(parentActivity, ParentalDetailActivity.class);
            ActivityBean b = list.get(position);
            it.putExtra("id", b.getId());
            it.putExtra("classId", b.getClassId());
            startActivity(it);
        });
        binding.activityLv.setAdapter(adapter);
        asyncGetMyActivity();
    }

    private void asyncGetMyActivity(){
        //我的活动列表
        String url = AppUrl.host + AppUrl.myActivity;
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            binding.activityLv.refreshComplete(PAGE_SIZE);
            dismissDialog();
            totalPage = jsonResult.optInt("pageCount");
            JSONArray rows = jsonResult.optJSONArray("rows");
            if(PAGE_NUM == 0 ){
                list.clear();
            }
            if(rows != null && rows.length() >0){
                for(int i=0; i<rows.length(); i++){
                    ActivityBean bean = gson.fromJson(rows.optJSONObject(i).toString(), ActivityBean.class);
                    list.add(bean);
                }
            }
            initEmptyView();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            binding.activityLv.refreshComplete(PAGE_SIZE);
            dismissDialog();
            if (PAGE_NUM > 0) {
                // 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }else{
                list.clear();
            }

            initEmptyView();
            adapter.notifyDataSetChanged();
        }
    };

    private void initEmptyView(){
        if(list.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        binding.activityLv.setOnRefreshListener(()->{
            PAGE_NUM = 0;
            binding.activityLv.setNoMore(false);
            asyncGetMyActivity();
        });

        binding.activityLv.setOnLoadMoreListener(()->{
            if(PAGE_NUM < totalPage -1){
                PAGE_NUM++;
                asyncGetMyActivity();
            }else{
                binding.activityLv.setNoMore(true);
            }
        });

        emptyView.setOnClickListener(v -> asyncGetMyActivity());
    }

    public void refresh(){
        PAGE_NUM = 0;
        binding.activityLv.setNoMore(false);
        asyncGetMyActivity();
    }

}
