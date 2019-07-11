package com.park61.teacherhelper.module.workplan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.workplan.WorkResponseActivity;
import com.park61.teacherhelper.module.workplan.adapter.TaskLogAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.park61.teacherhelper.module.workplan.bean.TaskLogBean;
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

import static com.park61.teacherhelper.common.tool.FastClickUtils.isFastClick;

/**
 * 行事历工作反馈fragment
 * modify by super 20181034
 */
public class WorkRespFragment extends BaseFragment {

    private ImageView check_task;
    private TextView tv_name, tv_status, tv_intro, tv_target, tv_start_time, tv_end_time, tv_doperson;
    private ListViewForScrollView lv_log;
    private View area_task_log;

    private int taskCalendarId;
    private TaskInfoBean mTaskInfoBean;
    private List<TaskLogBean> taskLogBeanList = new ArrayList<>();
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_mywork_resp, container, false);
        taskCalendarId = getArguments().getInt("taskCalendarId");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        check_task = (ImageView) parentView.findViewById(R.id.check_task);
        tv_name = (TextView) parentView.findViewById(R.id.tv_name);
        tv_status = (TextView) parentView.findViewById(R.id.tv_status);
        tv_intro = (TextView) parentView.findViewById(R.id.tv_intro);
        tv_target = (TextView) parentView.findViewById(R.id.tv_target);
        tv_start_time = (TextView) parentView.findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) parentView.findViewById(R.id.tv_end_time);
        lv_log = (ListViewForScrollView) parentView.findViewById(R.id.lv_log);
        lv_log.setFocusable(false);
        area_task_log = parentView.findViewById(R.id.area_task_log);
        tv_doperson = (TextView) parentView.findViewById(R.id.tv_doperson);

        if ("1".equals(((WorkResponseActivity) parentActivity).isDetailFrom)) {
            check_task.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        asyncWorkInfo();
    }

    @Override
    public void initListener() {
    }

    /**
     * 获取任务信息
     */
    private void asyncWorkInfo() {
        String wholeUrl = AppUrl.host + AppUrl.getTaskDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskCalendarId);
        if ("1".equals(((WorkResponseActivity) parentActivity).isDetailFrom)) {
            map.put("isDetailFrom", "1");
        }
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
            showShortToast(errorMsg);
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

        if (!CommonMethod.isListEmpty(mTaskInfoBean.getTaskExecutors())) {//执行人不为空
            String userNames = "";
            for (int i = 0; i < mTaskInfoBean.getTaskExecutors().size(); i++) {
                userNames += mTaskInfoBean.getTaskExecutors().get(i);
                if (i != mTaskInfoBean.getTaskExecutors().size() - 1)
                    userNames += "、";
            }
            tv_doperson.setText(userNames);
        }

        if (!TextUtils.isEmpty(mTaskInfoBean.getTargetNum())) {//没有目标则不显示
            tv_target.setText("目标：" + mTaskInfoBean.getTargetNum());
        } else {
            tv_target.setVisibility(View.GONE);
        }
        tv_start_time.setText(mTaskInfoBean.getStartShowDate().replace("/", "-"));
        tv_end_time.setText(mTaskInfoBean.getEndShowDate().replace("/", "-"));

        if (mTaskInfoBean.getIsKeyView() == 0) {//不是关键任务；是否显示关键目标0不显示1显示
            dealCanDo();
        } else {//是关键任务
            if (mTaskInfoBean.getIsEnableCheck() == 0) {//不可勾选；关键任务是否可勾选 0不可 1可勾选
                check_task.setImageResource(R.mipmap.icon_forbid);
                check_task.setOnClickListener(null);
            } else {//可勾选
                dealCanDo();
            }
        }

        asyncWorkLog();
    }

    /**
     * 可选时的状态与处理
     */
    private void dealCanDo() {
        if (mTaskInfoBean.getStatus() == 0) {
            check_task.setImageResource(R.drawable.shape_corner_22c7);
        } else {
            check_task.setImageResource(R.mipmap.task_gou);
        }
        check_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastClick()) {
                    return;
                }
                asyncDone(taskCalendarId);
            }
        });
    }

    /**
     * 任务动态列表
     */
    private void asyncWorkLog() {
        String wholeUrl = AppUrl.host + AppUrl.getTaskLogList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarId", taskCalendarId);
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
            TaskLogAdapter adapter = new TaskLogAdapter(parentActivity, taskLogBeanList);
            lv_log.setAdapter(adapter);
        }
    };

    /**
     * 完成任务/重启任务
     */
    private void asyncDone(int id) {
        String wholeUrl = AppUrl.host + AppUrl.restartTask;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("status", mTaskInfoBean.getStatus() == 0 ? "1" : "0");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dolistener);
    }

    BaseRequestListener dolistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            //失败也刷数据，延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
                    parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK_NUM"));//通知刷新数量
                }
            }, 500);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
            //延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK_NUM"));//通知刷新数量
                }
            }, 500);
            asyncWorkInfo();
        }
    };


}
