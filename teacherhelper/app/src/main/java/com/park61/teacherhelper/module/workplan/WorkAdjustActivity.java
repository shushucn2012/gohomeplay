package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2018/4/28.
 */

public class WorkAdjustActivity extends BaseActivity {

    private TextView tv_name, tv_status, tv_intro, tv_target, tv_start_time, tv_end_time;
    private View area_starttime, area_endtime, bottom_bar;
    private TimePickerView pvSTime, pvETime;

    private int taskCalendarId;
    private TaskInfoBean mTaskInfoBean;
    private String endDateStr, startDateStr;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_work_adjust);
    }

    @Override
    public void initView() {
        setPagTitle("计划调整");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        bottom_bar = findViewById(R.id.bottom_bar);

        area_starttime = findViewById(R.id.area_starttime);
        area_endtime = findViewById(R.id.area_endtime);
        pvSTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_start_time.setText(DateTool.L2SEndDay(date.getTime() + ""));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvETime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_end_time.setText(DateTool.L2SEndDay(date.getTime() + ""));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
    }

    @Override
    public void initData() {
        taskCalendarId = getIntent().getIntExtra("taskCalendarId", -1);
        asyncWorkInfo();
    }

    @Override
    public void initListener() {
        area_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvSTime.show();
            }
        });
        area_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvETime.show();
            }
        });
        bottom_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDateStr = tv_start_time.getText().toString().trim().replace("-", "/");
                endDateStr = tv_end_time.getText().toString().trim().replace("-", "/");
                asyncSave();
            }
        });
    }

    /**
     * 获取任务信息
     */
    private void asyncWorkInfo() {
        String wholeUrl = AppUrl.host + AppUrl.getTaskDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskCalendarId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            //showShortToast(errorMsg);
            dDialog.showDialog("提示", errorMsg, "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        asyncWorkInfo();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mTaskInfoBean = gson.fromJson(jsonResult.toString(), TaskInfoBean.class);

            if (mTaskInfoBean != null)
                fillData();
        }
    };

    private void fillData() {
        tv_name.setText(mTaskInfoBean.getName());
        tv_status.setText(mTaskInfoBean.getStatusName());
        tv_intro.setText(mTaskInfoBean.getIntro());
        if (!TextUtils.isEmpty(mTaskInfoBean.getTargetNum())) {//没有目标则不显示
            tv_target.setText("目标：" + mTaskInfoBean.getTargetNum());
        } else {
            tv_target.setVisibility(View.GONE);
        }
        tv_start_time.setText(mTaskInfoBean.getStartShowDate().replace("/", "-"));
        tv_end_time.setText(mTaskInfoBean.getEndShowDate().replace("/", "-"));
    }

    /**
     * 保存任务信息
     */
    private void asyncSave() {
        String wholeUrl = AppUrl.host + AppUrl.adjustTask;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskCalendarId);
        map.put("startDate", startDateStr);
        map.put("endDate", endDateStr);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

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
            showShortToast("调整成功！");
            sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
            //延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent("ACTION_REFRESH_WORK_NUM"));//通知刷新数量
                }
            }, 500);
            finish();
        }
    };

}
