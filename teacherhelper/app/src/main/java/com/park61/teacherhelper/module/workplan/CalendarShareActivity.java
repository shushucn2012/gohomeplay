package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.ActivityChooseTeacherBinding;
import com.park61.teacherhelper.module.workplan.adapter.ChooseTeacherAdapter;
import com.park61.teacherhelper.module.workplan.bean.TeacherBean;
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
 * Created by chenlie on 2018/4/27.
 * 任务共享页面
 */

public class CalendarShareActivity extends BaseActivity {

    private int PAGE_SIZE = 100;
    private int totalPage = 100;
    private int PAGE_NUM = 0;
    ActivityChooseTeacherBinding bind;
    private StringBuilder selectTeacher;
    private int taskId,taskOwnerDuty,teachGroupId;
    private List<TeacherBean> list;
    LRecyclerViewAdapter mAdapter;

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_choose_teacher);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        taskId = getIntent().getIntExtra("id", -1);
        taskOwnerDuty = getIntent().getIntExtra("taskOwnerDuty", -1);
        teachGroupId = getIntent().getIntExtra("teachGroupId", -1);

        selectTeacher = new StringBuilder();
        list = new ArrayList<>();
        ChooseTeacherAdapter adapter = new ChooseTeacherAdapter(mContext, list);
        mAdapter = new LRecyclerViewAdapter(adapter);
        bind.teacherList.setLayoutManager(new LinearLayoutManager(mContext));
        bind.teacherList.setAdapter(mAdapter);
        //获取班级列表
        asyncTeacherList();
    }

    @Override
    public void initListener() {
        bind.setShareTask(v -> {
            //共享任务给老师
            for(int i=0;i<list.size();i++){
                if(list.get(i).isSelect()) {
                    selectTeacher.append(list.get(i).getId()).append(",");
                }
            }
            if(TextUtils.isEmpty(selectTeacher)){
                showShortToast("请选择老师");
            }else{
                shareMyTask();
            }
        });

        bind.teacherList.setOnRefreshListener(() -> {
            PAGE_NUM = 0;
            bind.teacherList.setNoMore(false);
            //清空数据，刷新
            list.clear();
            mAdapter.notifyDataSetChanged();
            asyncTeacherList();
        });
        bind.teacherList.setOnLoadMoreListener(() ->{
            if(PAGE_NUM < totalPage -1){
                PAGE_NUM++;
                asyncTeacherList();
            }else{
                bind.teacherList.setNoMore(true);
            }
        });

    }

    /**
     * 获取老师列表
     */
    private void asyncTeacherList(){
        String url = AppUrl.host + AppUrl.shareTeacherList;
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        map.put("taskOwnerDuty", taskOwnerDuty);
        map.put("teachGroupId", teachGroupId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bind.teacherList.refreshComplete(PAGE_SIZE);
            //json 解析
            totalPage = jsonResult.optInt("pageCount");
            JSONArray rows = jsonResult.optJSONArray("list");
            if(rows != null && rows.length() > 0){
                for (int i=0; i<rows.length(); i++){
                    TeacherBean b = gson.fromJson(rows.optJSONObject(i).toString(), TeacherBean.class);
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
            bind.teacherList.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
            if(PAGE_NUM > 0){
                PAGE_NUM --;
            }
        }
    };

    /**
     * 共享任务
     */
    private void shareMyTask(){
        String url = AppUrl.host + AppUrl.shareCalendarTask;
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        map.put("teachIds", selectTeacher.substring(0, selectTeacher.length()-1));
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, submitListener);
    }

    BaseRequestListener submitListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //切换通知fragment列表刷新
            sendBroadcast(new Intent("ACTION_REFRESH_WORK"));
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
