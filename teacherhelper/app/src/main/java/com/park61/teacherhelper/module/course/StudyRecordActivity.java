package com.park61.teacherhelper.module.course;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.course.adapter.StudyRecordListAdapter;
import com.park61.teacherhelper.module.course.bean.StudyRecordBean;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/8/23.
 */

public class StudyRecordActivity extends BaseActivity {

    private List<StudyRecordBean> tlist;
    private List<StudyRecordBean> wlist;
    private List<StudyRecordBean> elist;
    private StudyRecordListAdapter tStudyRecordListAdapter;
    private StudyRecordListAdapter wStudyRecordListAdapter;
    private StudyRecordListAdapter eStudyRecordListAdapter;

    private ListViewForScrollView lv_record_today, lv_record_week, lv_record_early;
    private TextView tv_today_time, tv_total_time, tv_percent_man, tv_early, tv_week;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_study_record);
    }

    @Override
    public void initView() {
        setPagTitle("学习记录");
        lv_record_today = (ListViewForScrollView) findViewById(R.id.lv_record_today);
        lv_record_week = (ListViewForScrollView) findViewById(R.id.lv_record_week);
        lv_record_early = (ListViewForScrollView) findViewById(R.id.lv_record_early);
        tv_today_time = (TextView) findViewById(R.id.tv_today_time);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);
        tv_percent_man = (TextView) findViewById(R.id.tv_percent_man);
        tv_early = (TextView) findViewById(R.id.tv_early);
        tv_week = (TextView) findViewById(R.id.tv_week);
    }

    @Override
    public void initData() {
        tlist = new ArrayList<>();
        tStudyRecordListAdapter = new StudyRecordListAdapter(mContext, tlist);
        lv_record_today.setAdapter(tStudyRecordListAdapter);

        wlist = new ArrayList<>();
        wStudyRecordListAdapter = new StudyRecordListAdapter(mContext, wlist);
        lv_record_week.setAdapter(wStudyRecordListAdapter);

        elist = new ArrayList<>();
        eStudyRecordListAdapter = new StudyRecordListAdapter(mContext, elist);
        lv_record_early.setAdapter(eStudyRecordListAdapter);

        asyncFindRecodeTime();
        asyncTodayRecordList();
    }

    @Override
    public void initListener() {
        area_right.setVisibility(View.VISIBLE);
        lv_record_today.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncDelRecord(tlist.get(position).getId(), 0);
                        dDialog.dismissDialog();
                    }
                });
                return true;
            }
        });
        lv_record_week.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncDelRecord(wlist.get(position).getId(), 1);
                        dDialog.dismissDialog();
                    }
                });
                return true;
            }
        });
        lv_record_early.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncDelRecord(elist.get(position).getId(), 2);
                        dDialog.dismissDialog();
                    }
                });
                return true;
            }
        });
        lv_record_today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", tlist.get(position).getCourseId());
                startActivity(it);
            }
        });
        lv_record_week.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", wlist.get(position).getCourseId());
                startActivity(it);
            }
        });
        lv_record_early.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", elist.get(position).getCourseId());
                startActivity(it);
            }
        });
    }

    /**
     * 记录学习时间
     */
    private void asyncFindRecodeTime() {
        String wholeUrl = AppUrl.host + AppUrl.findRecodeTime;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, flistener);
    }

    BaseRequestListener flistener = new JsonRequestListener() {

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
            tv_today_time.setText(jsonResult.optString("today"));
            tv_total_time.setText(jsonResult.optString("total"));
            tv_percent_man.setText(jsonResult.optString("result") + "%");
        }
    };

    /**
     * 删除
     */
    private void asyncDelRecord(int recordId, int type) {
        String wholeUrl = AppUrl.host + AppUrl.delRecord;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("recordId", recordId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, type, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            showShortToast("删除成功！");
            switch (requestId) {
                case 0:
                    asyncTodayRecordList();
                    break;
                case 1:
                    asyncWeekRecordList();
                    break;
                case 2:
                    asyncEarlyRecordList();
                    break;
            }
        }
    };

    private void asyncTodayRecordList() {
        String wholeUrl = AppUrl.host + AppUrl.recordList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, trlistener);
    }

    BaseRequestListener trlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("rows");
            tlist.clear();
            for (int i = 0; i < actJay.length(); i++) {
                tlist.add(gson.fromJson(actJay.optJSONObject(i).toString(), StudyRecordBean.class));
            }
            tStudyRecordListAdapter.notifyDataSetChanged();

            asyncWeekRecordList();
        }
    };

    private void asyncWeekRecordList() {
        String wholeUrl = AppUrl.host + AppUrl.recordList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 2);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, wrlistener);
    }

    BaseRequestListener wrlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("rows");
            wlist.clear();
            for (int i = 0; i < actJay.length(); i++) {
                wlist.add(gson.fromJson(actJay.optJSONObject(i).toString(), StudyRecordBean.class));
            }
            wStudyRecordListAdapter.notifyDataSetChanged();

            asyncEarlyRecordList();
        }
    };

    private void asyncEarlyRecordList() {
        String wholeUrl = AppUrl.host + AppUrl.recordList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 3);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, erlistener);
    }

    BaseRequestListener erlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("rows");
            elist.clear();
            for (int i = 0; i < actJay.length(); i++) {
                elist.add(gson.fromJson(actJay.optJSONObject(i).toString(), StudyRecordBean.class));
            }
            eStudyRecordListAdapter.notifyDataSetChanged();

            if (CommonMethod.isListEmpty(elist)) {
                tv_early.setVisibility(View.GONE);
                lv_record_early.setVisibility(View.GONE);

                if (CommonMethod.isListEmpty(wlist)) {
                    tv_week.setVisibility(View.GONE);
                    lv_record_week.setVisibility(View.GONE);
                }
            }
        }
    };
}
