package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.park61.teacherhelper.module.workplan.bean.TaskLevelBean;
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.park61.teacherhelper.module.workplan.bean.TaskRole;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.TaskChooseLevelPopWin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 添加任务
 * Created by shubei on 2018/6/30.
 */

public class AddWorkPlanTaskActivity extends BaseActivity {

    private static final int REQ_CHOOSE_DATES = 1;
    private static final int REQ_CHOOSE_PERSON = 2;
    private WindowManager.LayoutParams params;
    private TaskChooseLevelPopWin mTaskChooseLevelPopWin;
    private TextView tv_date_chosen, tv_level1_name, tv_doperson;
    private EditText edit_task_name, edit_info;
    private View area_isimportant_true, area_isimportant_false;
    private ImageView img_ckb1, img_ckb2;

    private int taskId;
    private List<TaskLevelBean> taskLevelList = new ArrayList<>();
    private String name, intro, level1Name, userNameListStrs, startShowDate, endShowDate;
    private String userIdListStrs = "";//默认空字符串避免报错
    private int level1;
    private int isImportTask;//是否重点任务：0否，1是；
    private List<TaskRole> roleList = new ArrayList<>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_add_workplan_task);
    }

    @Override
    public void initView() {
        setPagTitle("添加任务");
        ((TextView) findViewById(R.id.tv_area_right)).setText("确定");
        tv_date_chosen = (TextView) findViewById(R.id.tv_date_chosen);
        edit_task_name = (EditText) findViewById(R.id.edit_task_name);
        edit_info = (EditText) findViewById(R.id.edit_info);
        tv_level1_name = (TextView) findViewById(R.id.tv_level1_name);
        tv_doperson = (TextView) findViewById(R.id.tv_doperson);
        area_isimportant_true = findViewById(R.id.area_isimportant_true);
        area_isimportant_false = findViewById(R.id.area_isimportant_false);
        img_ckb1 = (ImageView) findViewById(R.id.img_ckb1);
        img_ckb2 = (ImageView) findViewById(R.id.img_ckb2);
    }

    @Override
    public void initData() {
        taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId > 0) {
            setPagTitle("编辑任务");
            asyncGetTaskDetailById();
        }
        asyncLevel1List();
        asyncTeachGroupTaskList();
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_level1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
        findViewById(R.id.area_chooseman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String[] chosenManIDs = null;
                if (!TextUtils.isEmpty(userIdListStrs)) {
                    chosenManIDs = userIdListStrs.split(",");
                }

                //将已选中的人勾上
                for (int i = 0; i < roleList.size(); i++) {
                    List<TaskPerson> persons = roleList.get(i).getPersons();
                    for (int j = 0; j < persons.size(); j++) {
                        if (isPersonInChosens(persons.get(j).getUserId(), chosenManIDs)) {
                            persons.get(j).setChecked(true);
                        }
                    }
                }*/

                Intent intent = new Intent(mContext, WorkPlanTaskChooseManActivity.class);
                intent.putExtra("roleList", (Serializable) roleList);
                startActivityForResult(intent, REQ_CHOOSE_PERSON);
            }
        });
        findViewById(R.id.area_choosetime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TimesSquareActivity.class);
                if (!TextUtils.isEmpty(startShowDate))
                    intent.putExtra("startDate", startShowDate);
                if (!TextUtils.isEmpty(endShowDate))
                    intent.putExtra("endDate", endShowDate);
                startActivityForResult(intent, REQ_CHOOSE_DATES);
            }
        });
        area_isimportant_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ckb1.setImageResource(R.mipmap.download_check);
                img_ckb2.setImageResource(R.mipmap.download_uncheck);
                isImportTask = 1;
            }
        });
        area_isimportant_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ckb1.setImageResource(R.mipmap.download_uncheck);
                img_ckb2.setImageResource(R.mipmap.download_check);
                isImportTask = 0;
            }
        });
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edit_task_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showShortToast("事项名称未填写！");
                    return;
                }
                intro = edit_info.getText().toString().trim();
                if (TextUtils.isEmpty(intro)) {
                    showShortToast("事项描述未填写！");
                    return;
                }
                level1Name = tv_level1_name.getText().toString().trim();
                if (TextUtils.isEmpty(level1Name)) {
                    showShortToast("一级分类未选择！");
                    return;
                }
                if (TextUtils.isEmpty(userIdListStrs)) {
                    showShortToast("执行人未选择！");
                    return;
                }
                if (TextUtils.isEmpty(startShowDate) || TextUtils.isEmpty(endShowDate)) {
                    showShortToast("执行时间未选择！");
                    return;
                }
                asyncAddTask();
            }
        });
    }

    public void showPopFormBottom() {
        mTaskChooseLevelPopWin = new TaskChooseLevelPopWin(mContext, taskLevelList);
        // 设置Popupwindow显示位置（从底部弹出）
        mTaskChooseLevelPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mTaskChooseLevelPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        mTaskChooseLevelPopWin.getLvTeachers().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_level1_name.setText(taskLevelList.get(position).getName());
                level1 = taskLevelList.get(position).getId();
                mTaskChooseLevelPopWin.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_DATES) {
            if (resultCode == RESULT_OK) {
                startShowDate = data.getStringExtra("startDate");
                endShowDate = data.getStringExtra("endDate");
                tv_date_chosen.setText(data.getStringExtra("startDate") + "-" + data.getStringExtra("endDate"));
            }
        } else if (requestCode == REQ_CHOOSE_PERSON) {
            if (resultCode == RESULT_OK) {
                userIdListStrs = data.getStringExtra("userIdListStrs");
                userNameListStrs = data.getStringExtra("userNameListStrs");
                tv_doperson.setText(userNameListStrs);
            }
        }
    }

    /**
     * 添加任务
     */
    public void asyncAddTask() {
        String wholeUrl = AppUrl.host + AppUrl.addTask;//"http://10.10.10.18:8380/service/taskCalendar/addTask";//
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskId > 0) {//有id的时候是编辑
            map.put("id", taskId);//任务id
        }
        map.put("name", name);//任务标题
        map.put("intro", intro);//简介
        map.put("level1", level1);//一级分类id
        map.put("level1Name", level1Name);//一级分类名称
        map.put("userIdList", userIdListStrs);//执行人id
        map.put("startShowDate", startShowDate);//开始时间"yyyy/MM/dd"
        map.put("endShowDate", endShowDate);//结束时间"yyyy/MM/dd"
        map.put("isImportTask", isImportTask);//结束时间
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

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
            if (taskId > 0) {//有id的时候是编辑
                showShortToast("编辑成功！");
            } else {
                showShortToast("添加成功！");
            }
            sendBroadcast(new Intent("ACTION_REFRESH_MANAGE"));//刷新管理主页
            finish();
        }
    };

    /**
     * 获取执行人列表
     */
    private void asyncTeachGroupTaskList() {
        String url = AppUrl.host + AppUrl.TeachGroupTaskList;//"http://10.10.10.18:8380/service/taskCalendar/TeachGroupTaskList";//
        String requestBodyData = "";
        if (taskId > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", taskId);
            requestBodyData = ParamBuild.buildParams(map);
        }
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, tgListener);
    }

    BaseRequestListener tgListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            //showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            Iterator iterator = jsonResult.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                logout("=======================key=======================" + key);
                TaskRole taskRole = new TaskRole();
                taskRole.setTitle(key);

                List<TaskPerson> persons = new ArrayList<>();
                JSONArray arr = jsonResult.optJSONArray(key);
                if (arr != null && arr.length() > 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        TaskPerson taskPerson = gson.fromJson(arr.optJSONObject(i).toString(), TaskPerson.class);
                        persons.add(taskPerson);
                    }
                }

                taskRole.setPersons(persons);
                roleList.add(taskRole);
            }
        }
    };

    /**
     * 获取一级分类数据
     */
    private void asyncLevel1List() {
        String url = AppUrl.host + AppUrl.level1List;//"http://10.10.10.18:8380/service/taskCalendar/level1List";//
        netRequest.startRequest(url, Request.Method.POST, "", 0, monthPointListener);
    }

    BaseRequestListener monthPointListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            //showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            taskLevelList.clear();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    TaskLevelBean taskLevelBean = gson.fromJson(arr.optJSONObject(i).toString(), TaskLevelBean.class);
                    taskLevelList.add(taskLevelBean);
                }
            }
        }
    };

    /**
     * 获取任务信息
     */
    private void asyncGetTaskDetailById() {
        String url = AppUrl.host + AppUrl.getTaskDetailById;//"http://10.10.10.18:8380/service/taskCalendar/getTaskDetailById";//
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
            TaskInfoBean taskInfoBean = gson.fromJson(jsonResult.toString(), TaskInfoBean.class);
            edit_task_name.setText(taskInfoBean.getName());
            edit_info.setText(taskInfoBean.getIntro());
            tv_level1_name.setText(taskInfoBean.getLevel1Name());
            if (!CommonMethod.isListEmpty(taskInfoBean.getTaskExecutors())) {
                if (taskInfoBean.getTaskExecutors().size() == 1) {
                    userNameListStrs = taskInfoBean.getTaskExecutors().get(0);
                } else if (taskInfoBean.getTaskExecutors().size() == 2) {
                    userNameListStrs = taskInfoBean.getTaskExecutors().get(0) + "," + taskInfoBean.getTaskExecutors().get(1);
                } else {
                    userNameListStrs = taskInfoBean.getTaskExecutors().get(0) + "," + taskInfoBean.getTaskExecutors().get(1) + "等" + taskInfoBean.getTaskExecutors().size() + "人";
                }
            }
            tv_doperson.setText(userNameListStrs);

            if (!CommonMethod.isListEmpty(taskInfoBean.getTaskExecutorUserIds())) {
                for (int i = 0; i < taskInfoBean.getTaskExecutorUserIds().size(); i++) {
                    userIdListStrs += taskInfoBean.getTaskExecutorUserIds().get(i);
                    if (i != taskInfoBean.getTaskExecutorUserIds().size() - 1) {
                        userIdListStrs += ",";
                    }
                }
            }

            tv_date_chosen.setText(taskInfoBean.getStartShowDate() + "-" + taskInfoBean.getEndShowDate());
            if (taskInfoBean.getIsImportTask() == 0) {
                isImportTask = 0;
                img_ckb1.setImageResource(R.mipmap.download_uncheck);
                img_ckb2.setImageResource(R.mipmap.download_check);
            } else {
                isImportTask = 1;
                img_ckb1.setImageResource(R.mipmap.download_check);
                img_ckb2.setImageResource(R.mipmap.download_uncheck);
            }

            level1 = taskInfoBean.getLevel1();
            startShowDate = taskInfoBean.getStartShowDate();
            endShowDate = taskInfoBean.getEndShowDate();
        }
    };

    /**
     * 判断用户id是否已选中
     *
     * @param personId
     * @param userIds
     * @return
     */
    public boolean isPersonInChosens(int personId, String[] userIds) {
        if (userIds == null)
            return false;
        for (int i = 0; i < userIds.length; i++) {
            if (personId == FU.paseInt(userIds[i])) {
                return true;
            }
        }
        return false;
    }
}
