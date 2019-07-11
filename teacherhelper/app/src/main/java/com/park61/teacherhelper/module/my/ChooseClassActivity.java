package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.ActivityChooseClassBinding;
import com.park61.teacherhelper.module.my.adapter.ChooseClassAdapter;
import com.park61.teacherhelper.module.my.bean.ClassBean;
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
 * Created by chenlie on 2018/4/11.
 *
 * 发布活动 选择班级界面
 */

public class ChooseClassActivity extends BaseActivity {

    ActivityChooseClassBinding bind;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;
    private StringBuilder selectClass;
    boolean selectAll = true;
    private long clickTime = 0;
    private String title,notice,signStart,signEnd,activityStart,activityEnd;
    private int totalPage = 100;
    private List<ClassBean> list;
    LRecyclerViewAdapter mAdapter;

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_choose_class);
    }

    @Override
    public void initView() {
        setPagTitle("选择班级");

    }

    @Override
    public void initData() {
        //接收上个页面数据，活动名称，活动须知，报名时间，活动时间
        title = getIntent().getStringExtra("title");
        notice = getIntent().getStringExtra("notice");
        signStart = getIntent().getStringExtra("signStart");
        signEnd = getIntent().getStringExtra("signEnd");
        activityStart = getIntent().getStringExtra("activityStart");
        activityEnd = getIntent().getStringExtra("activityEnd");

        selectClass = new StringBuilder();
        list = new ArrayList<>();
        ChooseClassAdapter adapter = new ChooseClassAdapter(mContext, list);
        mAdapter = new LRecyclerViewAdapter(adapter);
        bind.classList.setLayoutManager(new LinearLayoutManager(mContext));
        bind.classList.setAdapter(mAdapter);
        //获取班级列表
        asyncClassList();
    }

    @Override
    public void initListener() {
        bind.setSelectAll(v -> {
            //全选班级
            if(list.size() > 0){
                showDialog();
                for(int i=0; i<list.size(); i++){
                    list.get(i).setCheck(!selectAll);
                }
                bind.tvAreaRight.setText((selectAll = !selectAll)?"取消全选":"全选");
                mAdapter.notifyDataSetChanged();
                dismissDialog();
            }
        });

        bind.classList.setOnRefreshListener(() -> {
            PAGE_NUM = 0;
            selectAll = true;
            bind.tvAreaRight.setText("取消全选");
            bind.classList.setNoMore(false);
            //清空数据，刷新
            list.clear();
            mAdapter.notifyDataSetChanged();
            asyncClassList();
        });
        bind.classList.setOnLoadMoreListener(() ->{
            if(PAGE_NUM < totalPage -1){
                PAGE_NUM++;
                asyncClassList();
            }else{
                bind.classList.setNoMore(true);
            }
        });

        bind.setSubmit(v -> {
            if(System.currentTimeMillis() - clickTime > 600){
                clickTime = System.currentTimeMillis();
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).isCheck())
                    selectClass.append(list.get(i).getId()).append(",");
                }

                if(TextUtils.isEmpty(selectClass)){
                    showShortToast("请选择班级");
                }else{
                    //提交数据,发布活动
                    publishActivity();
                }
            }
        });
    }

    /**
     * 获取班级列表
     */
    private void asyncClassList(){
        String url = AppUrl.host + AppUrl.myClassList;
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bind.classList.refreshComplete(PAGE_SIZE);
            //json 解析
            totalPage = jsonResult.optInt("pageCount");
            JSONArray rows = jsonResult.optJSONArray("rows");
            if(rows != null && rows.length() > 0){
                for (int i=0; i<rows.length(); i++){
                    ClassBean b = gson.fromJson(rows.optJSONObject(i).toString(), ClassBean.class);
                    list.add(b);
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            bind.classList.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
            if(PAGE_NUM > 0){
                PAGE_NUM --;
            }
        }
    };

    /**
     * 发布活动
     */
    private void publishActivity(){
        String url = AppUrl.host + AppUrl.publishLib;
        Map<String, Object> map = new HashMap<>();
        map.put("partyTemplateId", getIntent().getIntExtra("id", -1));
        map.put("name", title);
        map.put("notice", notice);
        map.put("applyStartDate", signStart);
        map.put("applyEndDate", signEnd);
        map.put("startDate", activityStart);
        map.put("endDate", activityEnd);
        map.put("listTeachGClassId", selectClass.substring(0, selectClass.length()-1));
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, submitListener);
    }

    BaseRequestListener submitListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //跳转到成功页面，关闭上一个页面，刷新列表页面
            startActivity(new Intent(mContext, PublishSuccessActivity.class));
            finish();
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
}
