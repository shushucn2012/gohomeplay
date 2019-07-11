package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.TimesSquare.CalendarPickerView;
import com.park61.teacherhelper.widget.TimesSquare.MonthView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 分配任务到班级-选择开始时间
 */
public class TempToClassSetTimeActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {

    private CalendarPickerView calendar;
    private TextView tv_area_right;

    private int taskCalendarTemplateId, teachGroupId;
    private String startDate;
    private String teachClassIds;
    private String taskCalendarTemplateClassId;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_times_square);
    }

    @Override
    public void initView() {
        tv_area_right = (TextView) findViewById(R.id.tv_area_right);

        setPagTitle("开始时间选择");
        MonthView.tag[0] = "";

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.setOnDateSelectedListener(this);

        calendar.init(new Date(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE);
    }

    @Override
    public void initData() {
        tv_area_right.setText("确认");

        taskCalendarTemplateId = getIntent().getIntExtra("taskCalendarTemplateId", -1);
        teachGroupId = getIntent().getIntExtra("teachGroupId", -1);
        teachClassIds = getIntent().getStringExtra("teachClassIds");
    }

    @Override
    public void initListener() {
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(startDate)) {
                    showShortToast("请选择日期");
                    return;
                }
                asyncAssignTaskCalenda();
            }
        });
    }

    @Override
    public void onDateSelected(Date date) {
        startDate = DateTool.getTheDateStrP7(date);
    }


    @Override
    public void onDateUnselected(Date date) {

    }

    /**
     * 模板分配任务
     */
    public void asyncAssignTaskCalenda() {
        String wholeUrl = AppUrl.host + AppUrl.assignClassTaskCalendar;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startDate", startDate);
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        map.put("teachClassIds", teachClassIds);
        map.put("teachGroupId", teachGroupId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, aslistener);
    }

    BaseRequestListener aslistener = new JsonRequestListener() {

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
            taskCalendarTemplateClassId = jsonResult.optString("data");
            showShortToast("分配成功！");
            dDialog.showDialog("提示", "行事历已分配成功，是否立即发送短信通知园长和老师来使用呢？", "暂时不用", "发送短信",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dDialog.dismissDialog();
                            for (int i = 0; i < ExitAppUtils.getInstance().getActList().size(); i++) {
                                Activity activity = ExitAppUtils.getInstance().getActList().get(i);
                                if (activity instanceof ChooseClassAllocateActivity
                                        || activity instanceof ChooseGroupAllocateActivity
                                        || activity instanceof TempMainActivity) {
                                    activity.finish();
                                }
                            }
                            finish();
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dDialog.dismissDialog();
                            asyncSendTaskCalendarMessage();
                        }
                    });
        }
    };

    /**
     * 行事历任务分配后短信提醒
     */
    public void asyncSendTaskCalendarMessage() {
        String wholeUrl = AppUrl.host + AppUrl.sendTaskCalendarMessage;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateClassId", taskCalendarTemplateClassId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

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
            showShortToast("短信发送成功！");
            for (int i = 0; i < ExitAppUtils.getInstance().getActList().size(); i++) {
                Activity activity = ExitAppUtils.getInstance().getActList().get(i);
                if (activity instanceof ChooseClassAllocateActivity
                        || activity instanceof ChooseGroupAllocateActivity
                        || activity instanceof TempMainActivity) {
                    activity.finish();
                }
            }
            finish();
        }
    };


}
