package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.workplan.adapter.AdminCalendarTaskAdapter;
import com.park61.teacherhelper.module.workplan.bean.CalendarTask;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.net.request.interfa.StringRequestListener;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.DateTool.DATE_TIME_FORMAT_PATTERN_4;

/**
 * 行事历任务管理
 * Created by shubei on 2018/6/30.
 */

public class ManageTaskActivity extends BaseActivity {

    private TimePickerView pvTime;
    private NCalendar myCalendar;
    private LinearLayout area_topdate;
    private TextView tv_date_top;
    private RecyclerView rLv;
    private ImageView img_add_task;
    private View area_tip_cover, bottom_bar;

    private String gotDateStr;//传过来的日期
    private int taskCalendarClassId;//传过来的模板分配班级id
    private boolean isFirstWpmFlag = false;// 是否是首次启动
    private int selectYear, selectMonth, selectDay;//当前所选年月日
    private List<String> select = new ArrayList<>();//有任务的日期列表
    private List<CalendarTask> taskList = new ArrayList<>();//当天任务列表
    private AdminCalendarTaskAdapter adapter;
    private String start, end;
    private Date selectedDate;//选中的如期
    private int teachGroupId;//园中校添加编辑任务时，获取执行人列表时须传入

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_taskmanage);
    }

    @Override
    public void initView() {
        setPagTitle("日历");
        registerRefreshReceiver();
        area_topdate = (LinearLayout) findViewById(R.id.area_topdate);
        tv_date_top = (TextView) findViewById(R.id.tv_date_top);
        myCalendar = (NCalendar) findViewById(R.id.myCalendar);
        rLv = (RecyclerView) findViewById(R.id.rLv);
        img_add_task = (ImageView) findViewById(R.id.img_add_task);
        area_tip_cover = findViewById(R.id.area_tip_cover);
        bottom_bar = findViewById(R.id.bottom_bar);
    }

    @Override
    public void initData() {
        img_add_task.setVisibility(View.VISIBLE);
        bottom_bar.setVisibility(View.VISIBLE);
        //空出底部栏
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) myCalendar.getLayoutParams();
        lp.bottomMargin = DevAttr.dip2px(mContext, 45);
        myCalendar.setLayoutParams(lp);
        // 判断是否是第一次进入管理界面
        SharedPreferences isFirstRunSp = getSharedPreferences("isFirstWpmFlag", Activity.MODE_PRIVATE);
        isFirstWpmFlag = isFirstRunSp.getBoolean("IsFirstWpmRun", true);
        if (isFirstWpmFlag) {
            area_tip_cover.setVisibility(View.VISIBLE);
            area_tip_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = getSharedPreferences("isFirstWpmFlag", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = sp.edit();
                    mEditor.putBoolean("IsFirstWpmRun", false);
                    mEditor.commit();
                    area_tip_cover.setVisibility(View.GONE);
                }
            });
        }
        gotDateStr = getIntent().getStringExtra("gotDateStr");
        taskCalendarClassId = getIntent().getIntExtra("taskCalendarClassId", -1);
        if (taskCalendarClassId < 0)
            taskCalendarClassId = GlobalParam.taskCalendarClassId;
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(gotDateStr)) {//如果有日期传过来就跳到指定日期
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myCalendar.setDate(gotDateStr);
                    myCalendar.toWeek();
                }
            }, 500);
            calendar.setTime(DateTool.parseDateStr(gotDateStr));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myCalendar.toWeek();
                }
            }, 500);
        }
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //改变日历控件
                myCalendar.setDate(DateTool.getTheDateStrP4(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                //nothing to do
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setDate(calendar).build();

        adapter = new AdminCalendarTaskAdapter(taskList, this);
        rLv.setLayoutManager(new LinearLayoutManager(this));
        rLv.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        area_topdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(tv_date_top);
            }
        });
        myCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                selectDay = dateTime.getDayOfMonth();
                if (dateTime.getYear() != selectYear || dateTime.getMonthOfYear() != selectMonth) { //获取本月打点数据
                    selectYear = dateTime.getYear();
                    selectMonth = dateTime.getMonthOfYear();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, selectYear);
                    c.set(Calendar.MONTH, selectMonth - 1);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.add(Calendar.DATE, -7);
                    start = format.format(c.getTime());
                    c.set(Calendar.MONTH, selectMonth);
                    c.set(Calendar.DAY_OF_MONTH, 0);
                    end = format.format(c.getTime());
                    c.add(Calendar.DATE, 7);

                    //前后增加7天让周任务的圆点连续
                    asyncMonthTask(start, end);
                }
                selectedDate = dateTime.toDate();
                tv_date_top.setText(selectYear + "." + (selectMonth < 10 ? "0" + selectMonth : selectMonth));
                asyncTodayTask(); //获取当天任务
            }
        });
        adapter.setEditListener(new AdminCalendarTaskAdapter.EditListener() {
            @Override
            public void deleteTask(int taskId, int position) {
                asyncDeleteTask(taskId);
            }

            @Override
            public void editTask(int taskId, int position) {
                if (taskList.get(position).getTemplateTaskType() == 0) {//任务类型，0工作任务1学习任务
                    Intent it = new Intent(mContext, AddWorkTaskActivity.class);
                    it.putExtra("taskCalendarClassId", taskCalendarClassId);
                    it.putExtra("taskId", taskId);
                    it.putExtra("teachGroupId", teachGroupId);
                    startActivity(it);
                } else {
                    Intent it = new Intent(mContext, AddStudyTaskActivity.class);
                    it.putExtra("taskCalendarClassId", taskCalendarClassId);
                    it.putExtra("taskId", taskId);
                    it.putExtra("teachGroupId", teachGroupId);
                    startActivity(it);
                }
            }
        });
        img_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChooseTaskTypeActivity.class);
                intent.putExtra("taskCalendarClassId", taskCalendarClassId);
                intent.putExtra("teachGroupId", teachGroupId);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取本月打点数据
     *
     * @param start 开始日期 2018/04/01
     * @param end   结束日期 2018/04/30
     */
    private void asyncMonthTask(String start, String end) {
        String url = AppUrl.host + AppUrl.getAdminCalendarList;//"http://10.10.10.18:8380/service/taskCalendar/getAdminCalendarList";//
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
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

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
                myCalendar.setPoint(select);
            }
        }
    };

    /**
     * 获取当天任务列表
     */
    private void asyncTodayTask() {
        String url = AppUrl.host + AppUrl.getAdminTaskListByStartDate;//"http://10.10.10.18:8380/service/taskCalendar/getAdminTaskListByStartDate";//
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", DateTool.getTheDateStrP7(selectedDate));
        if (taskCalendarClassId > 0)
            map.put("taskCalendarClassId", taskCalendarClassId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, todayTaskListener);
    }

    BaseRequestListener todayTaskListener = new StringRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, String result) {
            dismissDialog();
            JSONObject jsonResponse = null;
            String code = "";
            try {
                jsonResponse = new JSONObject(result);
                code = jsonResponse.optString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonResult = jsonResponse.optJSONObject("data");
            if (code.equals("0")) {//请求成功
                taskList.clear();
                teachGroupId = jsonResult.optInt("teachGroupId");
                JSONArray arr = jsonResult.optJSONArray("taskCalendarList");
                //加一个头部---start//
                taskList.add(new CalendarTask(0));
                if (arr != null && arr.length() > 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        CalendarTask t = gson.fromJson(arr.optJSONObject(i).toString(), CalendarTask.class);
                        taskList.add(t);
                    }
                } else {
                    CalendarTask empty = new CalendarTask();
                    empty.setType(2);
                    taskList.add(empty);
                }
                adapter.notifyDataSetChanged();
            } else if (code.equals("0000042010")) {//没有数据
                taskList.clear();
                //加一个头部---start//
                taskList.add(new CalendarTask(0));
                CalendarTask empty = new CalendarTask();
                empty.setType(2);
                taskList.add(empty);
                adapter.notifyDataSetChanged();

                teachGroupId = jsonResult.optInt("teachGroupId");
            } else {//其他错误
                taskList.clear();
                //加一个头部---start//
                taskList.add(new CalendarTask(0));
                CalendarTask empty = new CalendarTask();
                empty.setType(2);
                taskList.add(empty);
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
            taskList.clear();
            //加一个头部---start//
            taskList.add(new CalendarTask(0));
            CalendarTask empty = new CalendarTask();
            empty.setType(2);
            taskList.add(empty);
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 删除行事历任务
     */
    private void asyncDeleteTask(int taskId) {
        String url = AppUrl.host + AppUrl.deleteTaskCalendar;//"http://10.10.10.18:8380/service/taskCalendar/deleteTaskCalendar";//
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, deleteListener);
    }

    BaseRequestListener deleteListener = new JsonRequestListener() {
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
            asyncMonthTask(start, end);
            asyncTodayTask();
        }
    };

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_MANAGE");
        registerReceiver(workChangenReceiver, filter);
    }

    private BroadcastReceiver workChangenReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncMonthTask(start, end);
            asyncTodayTask();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(workChangenReceiver);
    }
}
