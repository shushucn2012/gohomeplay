package com.park61.teacherhelper.module.my;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.ActivitySignDataBinding;
import com.park61.teacherhelper.module.my.adapter.SignDataAdapter;
import com.park61.teacherhelper.module.my.bean.SignDataBean;
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
 * Created by chenlie on 2017/12/28.
 *
 */

public class SignDataActivity extends BaseActivity {

    ActivitySignDataBinding binding;
    LRecyclerViewAdapter mAdapter;
    List<SignDataBean> list;
    public static final int PAGE_SIZE = 8;
    private int PAGE_NUM = 0;
    private int totalPage = 100;
    private int classId;
    private int activityId;
    private View emptyView;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_data);
        emptyView = binding.emptyView.getRoot();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        classId = getIntent().getIntExtra("classId", -1);
        activityId = getIntent().getIntExtra("id", -1);

        list = new ArrayList<>();
        SignDataAdapter adapter = new SignDataAdapter(list);
        mAdapter = new LRecyclerViewAdapter(adapter);
        binding.dataLv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.dataLv.setAdapter(mAdapter);

        asyncSignData();
    }

    private void asyncSignData(){
        String url = AppUrl.host + AppUrl.activitySignData ;
        Map<String, Object> map = new HashMap<>();
        map.put("classId", classId);
        map.put("id", activityId);
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, signDataListener);
    }

    BaseRequestListener signDataListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) {
            binding.dataLv.refreshComplete(PAGE_SIZE);
            dismissDialog();

            if(PAGE_NUM == 0){
                list.clear();
            }
            //报名人员列表
            JSONObject peoples = j.optJSONObject("partyApplyList");
            if(peoples != null) {
                totalPage = peoples.optInt("pageCount");
                JSONArray arr = peoples.optJSONArray("rows");
                if(arr !=null && arr.length()>0){
                    emptyView.setVisibility(View.GONE);
                    binding.topSignData.setVisibility(View.VISIBLE);
                    //设置签到人数 和 购买装备人数
                    binding.signNum.setText(j.optInt("signNum")+"/"+j.optInt("totalNum"));
                    binding.buyNum.setText(j.optInt("buyNum")+"");
                    setSignList(arr);
                }
            }
            initTopView();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            binding.dataLv.refreshComplete(PAGE_SIZE);
            dismissDialog();
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            if(PAGE_NUM > 0){
                PAGE_NUM --;
            }
            initTopView();
        }
    };

    private void initTopView(){
        if(list.size() == 0){
            binding.topSignData.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            binding.topSignData.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void setSignList(JSONArray arr){
        for(int i=0; i<arr.length(); i++){
            SignDataBean b = gson.fromJson(arr.optJSONObject(i).toString(), SignDataBean.class);
            list.add(b);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        binding.setGoBack(v->finish());

        binding.dataLv.setOnRefreshListener(()->{
            PAGE_NUM = 0;
            binding.dataLv.setNoMore(false);
            asyncSignData();
        });

        binding.dataLv.setOnLoadMoreListener(()->{
            if(PAGE_NUM < totalPage -1){
                PAGE_NUM ++;
                asyncSignData();
            }else{
                binding.dataLv.setNoMore(true);
            }
        });

        emptyView.setOnClickListener(v-> asyncSignData());
    }
}
