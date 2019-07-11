package com.park61.teacherhelper.module.workplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.databinding.ActivityCalendarBinding;
import com.park61.teacherhelper.module.workplan.adapter.CalendarAllTaskAdapter;
import com.park61.teacherhelper.module.workplan.adapter.CalendarTaskAdapter;
import com.park61.teacherhelper.module.workplan.bean.CalendarTask;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/4/24.
 * <p>
 * 行事历 日历界面
 */

public class CalendarAllTaskActivity extends BaseActivity implements OnCalendarChangedListener, CalendarTaskAdapter.OperateListener {

    private ActivityCalendarBinding bind;
    private List<CalendarTask> datas;
    private TimePickerView pvTime;
    private CalendarAllTaskAdapter adapter;
    private List<String> select;
    private int selectYear, selectMonth, selectDay;
    private int clickItemPosition = -1;
    private String gotDateStr;//传过来的日期
    private int taskCalendarClassId;//传过来的模板分配班级id

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
    }

    @Override
    public void initView() {
        setPagTitle("日历");
        registerRefreshReceiver();
        bind.title.areaRight.setVisibility(View.GONE);

        gotDateStr = getIntent().getStringExtra("gotDateStr");

        taskCalendarClassId = getIntent().getIntExtra("taskCalendarClassId", -1);
        if (taskCalendarClassId < 0)
            taskCalendarClassId = GlobalParam.taskCalendarClassId;

        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(gotDateStr)) {//如果有日期传过来就跳到指定日期
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bind.myCalendar.setDate(gotDateStr);
                    bind.myCalendar.toWeek();
                }
            }, 500);
            calendar.setTime(DateTool.parseDateStr(gotDateStr));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bind.myCalendar.toWeek();
                }
            }, 500);
        }
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //改变日历控件
                bind.myCalendar.setDate(getTime(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setDate(calendar).build();
    }

    @Override
    public void initData() {
        datas = new ArrayList<>();
        adapter = new CalendarAllTaskAdapter(datas, this);
        //adapter.setOperateListener(this);
        bind.rLv.setLayoutManager(new LinearLayoutManager(this));
        bind.rLv.setAdapter(adapter);
        select = new ArrayList<>();
    }

    @Override
    public void initListener() {
        bind.myCalendar.setOnCalendarChangedListener(this);
        bind.setDateClick(v -> {
            //弹出日期选择
            pvTime.show(bind.clickTv);
        });
    }


    @Override
    public void onCalendarChanged(DateTime dateTime) {
        selectDay = dateTime.getDayOfMonth();
        if (dateTime.getYear() != selectYear || dateTime.getMonthOfYear() != selectMonth) {
            //获取本月打点数据
            selectYear = dateTime.getYear();
            selectMonth = dateTime.getMonthOfYear();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, selectYear);
            c.set(Calendar.MONTH, selectMonth - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -7);
            String start = format.format(c.getTime());
            c.set(Calendar.MONTH, selectMonth);
            c.set(Calendar.DAY_OF_MONTH, 0);
            c.add(Calendar.DATE, 7);
            String end = format.format(c.getTime());
            asyncMonthTask(start, end);
        }
        bind.clickTv.setText(selectYear + "." + (selectMonth < 10 ? "0" + selectMonth : selectMonth));
        //获取当天任务
        asyncTodayTask();
    }

    private void asyncTodayTask() {
        String url = AppUrl.host + AppUrl.getAdminTaskListByStartDate;
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", selectYear + "/" + selectMonth + "/" + selectDay);
        if (taskCalendarClassId > 0)
            map.put("taskCalendarClassId", taskCalendarClassId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, todayTaskListener);
    }

    BaseRequestListener todayTaskListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            datas.clear();
            JSONArray arr = jsonResult.optJSONArray("taskCalendarList");
            datas.add(new CalendarTask(0));
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    CalendarTask t = gson.fromJson(arr.optJSONObject(i).toString(), CalendarTask.class);
                    datas.add(t);
                }
            } else {
                CalendarTask empty = new CalendarTask();
                empty.setType(2);
                datas.add(empty);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            datas.clear();
            datas.add(new CalendarTask(0));
            CalendarTask empty = new CalendarTask();
            empty.setType(2);
            datas.add(empty);
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * @param start 开始日期 2018/04/01
     * @param end   结束日期 2018/04/30
     */
    private void asyncMonthTask(String start, String end) {
        String url = AppUrl.host + AppUrl.getAdminCalendarList;
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", start);
        map.put("endDate", end);
        if (taskCalendarClassId > 0)
            map.put("taskCalendarClassId", taskCalendarClassId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, monthPointListener);
    }

    BaseRequestListener monthPointListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            select.clear();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < arr.length(); i++) {
                    long mili = arr.optJSONObject(i).optLong("startDate");
                    select.add(format.format(new Date(mili)));
                }
                bind.myCalendar.setPoint(select);
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

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void leaveMsg(int taskId, int position) {
        Intent it = new Intent(this, LeftMsgActivity.class);
        it.putExtra("taskCalendarId", taskId);
        startActivity(it);
    }

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_WORK");
        registerReceiver(workChangenNumReceiver, filter);
    }

    private BroadcastReceiver workChangenNumReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncTodayTask();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(workChangenNumReceiver);
    }
}
