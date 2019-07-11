package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.workplan.adapter.TaskLogAdapter;
import com.park61.teacherhelper.module.workplan.adapter.TrainCrouseListAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.park61.teacherhelper.module.workplan.bean.TaskLogBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务详情页面
 * Created by shubei on 2018/8/3.
 */

public class WorkDetailActivity extends BaseActivity {

    private ImageView img_task_state;
    private TextView tv_title, tv_start_time, tv_start_weekday, tv_end_time, tv_end_weekday, tv_doperson, tv_task_desc;
    private ListViewForScrollView lv_file, lv_log;
    private View area_task_file, area_task_log, tv_file_label;
    private Button btn_finish;

    private int taskId;//任务id
    private int taskType;////任务类型，0工作任务1学习任务
    private int isOwnTask;//是否是自己的任务
    private TaskInfoBean mTaskInfoBean;
    private TrainCrouseListAdapter mTrainCrouseListAdapter;
    private List<TaskLogBean> taskLogBeanList = new ArrayList<>();
    private int lastTaskStatus = 1;//之前一次的任务状态，默认为1，代表第一次不判断

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workdetail);
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_start_weekday = (TextView) findViewById(R.id.tv_start_weekday);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_end_weekday = (TextView) findViewById(R.id.tv_end_weekday);
        tv_doperson = (TextView) findViewById(R.id.tv_doperson);
        tv_task_desc = (TextView) findViewById(R.id.tv_task_desc);
        img_task_state = (ImageView) findViewById(R.id.img_task_state);
        lv_file = (ListViewForScrollView) findViewById(R.id.lv_file);
        lv_log = (ListViewForScrollView) findViewById(R.id.lv_log);
        area_task_file = findViewById(R.id.area_task_file);
        area_task_log = findViewById(R.id.area_task_log);
        tv_file_label = findViewById(R.id.tv_file_label);
        btn_finish = (Button) findViewById(R.id.btn_finish);
    }

    @Override
    public void initData() {
        taskId = getIntent().getIntExtra("taskCalendarId", -1);
        taskType = getIntent().getIntExtra("taskType", -1);
        isOwnTask = getIntent().getIntExtra("isOwnTask", -1);
        setPagTitle(taskType == 0 ? "工作任务" : "课程学习");
        if (taskType != 0) {//学习任务隐藏不干区域
            tv_file_label.setVisibility(View.GONE);
            tv_task_desc.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {//学习完了延迟刷新
            @Override
            public void run() {
                asyncGetTaskDetailById();
            }
        }, 600);
    }

    @Override
    public void initListener() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncFinishTaskCalendarShare();
            }
        });
    }

    /**
     * 获取任务详情
     */
    private void asyncGetTaskDetailById() {
        String url = AppUrl.host + AppUrl.getTaskDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, taskListener);
    }

    BaseRequestListener taskListener = new JsonRequestListener() {
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
            findViewById(R.id.area_empty).setVisibility(View.GONE);
            mTaskInfoBean = gson.fromJson(jsonResult.toString(), TaskInfoBean.class);
            if (mTaskInfoBean != null)
                fillData();
        }
    };

    private void fillData() {
        tv_title.setText(mTaskInfoBean.getName());
        if (mTaskInfoBean.getStatus() == 0) {//未完成
            img_task_state.setImageResource(R.mipmap.icon_taskstate_undone);
            if (taskType == 0) {//工作任务才判断按钮
                if (GlobalParam.isTempManager) {//园中校管理员
                    btn_finish.setVisibility(View.GONE);
                } else {
                    //不是关键任务；是否显示关键目标0不显示1显示, 不可勾选；关键任务是否可勾选 0不可 1可勾选
                    if ((mTaskInfoBean.getIsKeyView() == 0 || mTaskInfoBean.getIsEnableCheck() == 1) && isUserOwnTask()) {
                        btn_finish.setVisibility(View.VISIBLE);
                    } else {//是关键任务
                        btn_finish.setVisibility(View.GONE);
                    }
                }
            }
        } else if (mTaskInfoBean.getStatus() == 1) {//1已完成
            img_task_state.setImageResource(R.mipmap.icon_taskstate_done);
            btn_finish.setVisibility(View.GONE);
            if (lastTaskStatus != 1 && taskType == 1) {//之前没有完成，并且是学习任务，完成之后刷新列表
                new Handler().postDelayed(new Runnable() {//学习完了延迟刷新主页
                    @Override
                    public void run() {
                        sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
                        sendBroadcast(new Intent("ACTION_REFRESH_WORK_NUM"));//通知刷新数量
                        sendBroadcast(new Intent("ACTION_REFRESH_MANAGE"));//刷新管理主页
                    }
                }, 800);
            }
        } else {//逾期
            img_task_state.setVisibility(View.INVISIBLE);
            if (taskType == 0) {//工作任务才判断按钮
                if (GlobalParam.isTempManager) {//园中校管理员
                    btn_finish.setVisibility(View.GONE);
                } else {
                    //不是关键任务；是否显示关键目标0不显示1显示, 不可勾选；关键任务是否可勾选 0不可 1可勾选
                    if ((mTaskInfoBean.getIsKeyView() == 0 || mTaskInfoBean.getIsEnableCheck() == 1) && isUserOwnTask()) {
                        btn_finish.setVisibility(View.VISIBLE);
                    } else {//是关键任务
                        btn_finish.setVisibility(View.GONE);
                    }
                }
            }
        }
        tv_task_desc.setText(mTaskInfoBean.getIntro());

        if (!CommonMethod.isListEmpty(mTaskInfoBean.getTaskExecutors())) {//执行人不为空
            String userNames = "";
            for (int i = 0; i < mTaskInfoBean.getTaskExecutors().size(); i++) {
                userNames += mTaskInfoBean.getTaskExecutors().get(i);
                if (i != mTaskInfoBean.getTaskExecutors().size() - 1)
                    userNames += "、";
            }
            tv_doperson.setText(userNames);
        }

        tv_start_time.setText(new SimpleDateFormat("MM.dd").format(new Date(mTaskInfoBean.getStartDate())));
        tv_end_time.setText(new SimpleDateFormat("MM.dd").format(new Date(mTaskInfoBean.getEndDate())));
        tv_start_weekday.setText(DateTool.getWeekOfDate2(mTaskInfoBean.getStartDate()));
        tv_end_weekday.setText(DateTool.getWeekOfDate2(mTaskInfoBean.getEndDate()));

        if (CommonMethod.isListEmpty(mTaskInfoBean.getTrainerCourseList())) {//没有课程数据则隐藏该区域
            area_task_file.setVisibility(View.GONE);
        } else {
            area_task_file.setVisibility(View.VISIBLE);
            mTrainCrouseListAdapter = new TrainCrouseListAdapter(mContext, mTaskInfoBean.getTrainerCourseList(), false, taskType, taskId);
            lv_file.setAdapter(mTrainCrouseListAdapter);
        }


        /*if (mTaskInfoBean.getIsKeyView() == 0) {//不是关键任务；是否显示关键目标0不显示1显示
            dealCanDo();
        } else {//是关键任务
            if (mTaskInfoBean.getIsEnableCheck() == 0) {//不可勾选；关键任务是否可勾选 0不可 1可勾选
                check_task.setImageResource(R.mipmap.icon_forbid);
                check_task.setOnClickListener(null);
            } else {//可勾选
                dealCanDo();
            }
        }*/
        lastTaskStatus = mTaskInfoBean.getStatus();
        asyncWorkLog();
    }

    /**
     * 任务动态列表
     */
    private void asyncWorkLog() {
        String wholeUrl = AppUrl.host + AppUrl.getTaskLogList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarId", taskId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, wlistener);
    }

    BaseRequestListener wlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0) {
                area_task_log.setVisibility(View.GONE);
                return;
            }
            area_task_log.setVisibility(View.VISIBLE);
            taskLogBeanList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                TaskLogBean taskLogBean = gson.fromJson(actJay.optJSONObject(i).toString(), TaskLogBean.class);
                taskLogBeanList.add(taskLogBean);
            }
            TaskLogAdapter adapter = new TaskLogAdapter(mContext, taskLogBeanList);
            lv_log.setAdapter(adapter);
        }
    };

    /**
     * 任务完成
     */
    private void asyncFinishTaskCalendarShare() {
        String wholeUrl = AppUrl.host + AppUrl.finishTaskCalendarShare;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, flistener);
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
                    sendBroadcast(new Intent("ACTION_REFRESH_WORK_NUM"));//通知刷新数量
                }
            }, 500);
            sendBroadcast(new Intent("ACTION_REFRESH_MANAGE"));//刷新管理主页

            //更改状态，隐藏按钮
            img_task_state.setVisibility(View.VISIBLE);
            img_task_state.setImageResource(R.mipmap.icon_taskstate_done);
            btn_finish.setVisibility(View.GONE);
        }
    };

    /**
     * 判断用户是否在执行人中
     *
     * @return
     */
    public boolean isUserOwnTask() {
        if (mTaskInfoBean == null || CommonMethod.isListEmpty(mTaskInfoBean.getTaskExecutorUserIds()))
            return false;
        for (int i = 0; i < mTaskInfoBean.getTaskExecutorUserIds().size(); i++) {
            if (mTaskInfoBean.getTaskExecutorUserIds().get(i).equals(GlobalParam.currentUser.getUserId() + ""))
                return true;
        }
        return false;
    }
}
