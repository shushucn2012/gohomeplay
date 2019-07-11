package com.park61.teacherhelper.module.workplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.workplan.adapter.TrainCrouseListAdapter;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.R.id.tv_doperson;

/**
 * 添加工作任务
 * Created by shubei on 2018/8/5.
 */

public class AddWorkTaskActivity extends BaseActivity {

    private static final int REQ_CHOOSE_DATES = 1;
    private static final int REQ_CHOOSE_PERSON = 2;
    private static final int REQ_CHOOSE_FILE = 3;

    private WindowManager.LayoutParams params;
    private TaskChooseLevelPopWin mTaskChooseLevelPopWin;
    private View area_starttime, area_endtime, area_chooseman, area_level1, area_add_file;
    private TextView tv_start_time, tv_end_time, tv_start_weekday, tv_end_weekday, tv_doperson, tv_level1_name, tv_start_time_tip, tv_end_time_tip,
            tv_goto_add;
    private EditText edit_task_name, edit_info;
    private ImageView img_writeto_gourpmaster_chk;
    private ListView lv_file;

    private int taskId;//任务id
    private int taskCalendarClassId;//传过来的模板分配班级id
    private int teachGroupId;//幼儿园id
    private String name, intro, level1Name, userNameListStrs, startShowDate, endShowDate;
    private List<TaskLevelBean> taskLevelList = new ArrayList<>();//任务级别列表
    private List<TaskRole> roleList = new ArrayList<>();//执行人列表
    private List<KnowledgeBean> kList = new ArrayList<>();//参考文档列表
    private String userIdListStrs = "";//默认空字符串避免报错
    private String trainerCourseIds = "";//参考文档id
    private int level1;//任务一级分类
    private boolean isWriteToGroupMaster;//是否抄送园长
    private TrainCrouseListAdapter mTrainCrouseListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_add_worktask);
    }

    @Override
    public void initView() {
        area_starttime = findViewById(R.id.area_starttime);
        area_endtime = findViewById(R.id.area_endtime);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_start_weekday = (TextView) findViewById(R.id.tv_start_weekday);
        tv_end_weekday = (TextView) findViewById(R.id.tv_end_weekday);
        area_chooseman = findViewById(R.id.area_chooseman);
        tv_doperson = (TextView) findViewById(R.id.tv_doperson);
        area_level1 = findViewById(R.id.area_level1);
        tv_level1_name = (TextView) findViewById(R.id.tv_level1_name);
        img_writeto_gourpmaster_chk = (ImageView) findViewById(R.id.img_writeto_gourpmaster_chk);
        area_add_file = findViewById(R.id.area_add_file);
        tv_start_time_tip = (TextView) findViewById(R.id.tv_start_time_tip);
        tv_end_time_tip = (TextView) findViewById(R.id.tv_end_time_tip);
        lv_file = (ListView) findViewById(R.id.lv_file);
        tv_goto_add = (TextView) findViewById(R.id.tv_goto_add);
        edit_task_name = (EditText) findViewById(R.id.edit_task_name);
        edit_info = (EditText) findViewById(R.id.edit_info);
    }

    @Override
    public void initData() {
        taskId = getIntent().getIntExtra("taskId", -1);
        taskCalendarClassId = getIntent().getIntExtra("taskCalendarClassId", -1);
        teachGroupId = getIntent().getIntExtra("teachGroupId", -1);
        if (taskId > 0) {
            setPagTitle("编辑任务");
            asyncGetTaskDetailById();
        }
        //参考文档列表
        mTrainCrouseListAdapter = new TrainCrouseListAdapter(mContext, kList, true, 0, taskId);
        lv_file.setAdapter(mTrainCrouseListAdapter);

        asyncLevel1List();
        asyncTeachGroupTaskList();
    }

    @Override
    public void initListener() {
        area_starttime.setOnClickListener(new View.OnClickListener() {
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
        area_endtime.setOnClickListener(new View.OnClickListener() {
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
        area_chooseman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WorkPlanTaskChooseManActivity.class);
                intent.putExtra("roleList", (Serializable) roleList);
                startActivityForResult(intent, REQ_CHOOSE_PERSON);
            }
        });
        area_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
        img_writeto_gourpmaster_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWriteToGroupMaster) {
                    img_writeto_gourpmaster_chk.setImageResource(R.mipmap.icon_writeto_groupmaster_default);
                    isWriteToGroupMaster = false;
                } else {
                    img_writeto_gourpmaster_chk.setImageResource(R.mipmap.icon_writeto_groupmaster_focus);
                    isWriteToGroupMaster = true;
                }
            }
        });
        area_add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WorkTaskChooseFileActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("kList", (Serializable) kList);
                startActivityForResult(intent, REQ_CHOOSE_FILE);
            }
        });
        mTrainCrouseListAdapter.setOnDeleteClickedListener(new TrainCrouseListAdapter.OnDeleteClickedListener() {
            @Override
            public void onClicked(int position) {
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        kList.remove(position);
                        mTrainCrouseListAdapter.notifyDataSetChanged();
                    }
                });
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
                if (!CommonMethod.isListEmpty(kList)) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < kList.size(); j++) {
                        sb.append(kList.get(j).getId());
                        sb.append(",");
                    }
                    trainerCourseIds = sb.substring(0, sb.length() - 1);
                }
                asyncAddTask();
            }
        });
        /*edit_task_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showShortToast("" + v.toString() + " get " + hasFocus);
            }
        });*/
       /* findViewById(R.id.main_view).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    edit_task_name.setCursorVisible(false);// 隐藏光标
                }
                return false;
            }
        });*/

        edit_task_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    edit_task_name.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
        edit_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    edit_info.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus().getId() == edit_task_name.getId()) {
                View v1 = edit_task_name;
                if (isShouldHideInput(v1, ev)) {
                    edit_task_name.setCursorVisible(false);// 隐藏光标
                }
            } else if (getCurrentFocus().getId() == edit_info.getId()) {
                View v2 = edit_info;
                if (isShouldHideInput(v2, ev)) {
                    edit_info.setCursorVisible(false);// 隐藏光标
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_DATES) {
            if (resultCode == RESULT_OK) {
                startShowDate = data.getStringExtra("startDate");
                endShowDate = data.getStringExtra("endDate");

                setStartEndDateAndWeek(startShowDate, endShowDate);
            }
        } else if (requestCode == REQ_CHOOSE_PERSON) {
            if (resultCode == RESULT_OK) {
                userIdListStrs = data.getStringExtra("userIdListStrs");
                userNameListStrs = data.getStringExtra("userNameListStrs");
                tv_doperson.setText(userNameListStrs);
            }
        } else if (requestCode == REQ_CHOOSE_FILE) {
            if (resultCode == RESULT_OK) {
                kList.clear();
                kList.addAll((List<KnowledgeBean>) data.getSerializableExtra("kList"));
                if (!CommonMethod.isListEmpty(kList))
                    tv_goto_add.setVisibility(View.VISIBLE);
                mTrainCrouseListAdapter.notifyDataSetChanged();
            }
        }
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
     * 获取执行人列表
     */
    private void asyncTeachGroupTaskList() {
        String url = AppUrl.host + AppUrl.TeachGroupTaskList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskId > 0)
            map.put("id", taskId);
        if (teachGroupId > 0)
            map.put("teachGroupId", teachGroupId);
        String requestBodyData = ParamBuild.buildParams(map);
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
     * 获取任务信息
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

            if (taskInfoBean.getIsImportTask() == 0) {
                isWriteToGroupMaster = false;
                img_writeto_gourpmaster_chk.setImageResource(R.mipmap.icon_writeto_groupmaster_default);
            } else {
                isWriteToGroupMaster = true;
                img_writeto_gourpmaster_chk.setImageResource(R.mipmap.icon_writeto_groupmaster_focus);
            }

            level1 = taskInfoBean.getLevel1();
            startShowDate = taskInfoBean.getStartShowDate();
            endShowDate = taskInfoBean.getEndShowDate();
            setStartEndDateAndWeek(startShowDate, endShowDate);

            if (!CommonMethod.isListEmpty(taskInfoBean.getTrainerCourseList())) {
                tv_goto_add.setVisibility(View.VISIBLE);
                kList.clear();
                kList.addAll(taskInfoBean.getTrainerCourseList());
                mTrainCrouseListAdapter.notifyDataSetChanged();
            }
        }
    };

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
        map.put("isImportTask", isWriteToGroupMaster ? "1" : "0");//是否重点任务，是否抄送园长
        if (!TextUtils.isEmpty(trainerCourseIds))
            map.put("trainerCourseIds", trainerCourseIds);
        map.put("templateTaskType", 0);//执行任务类型：0任务类，1学习类
        map.put("taskCalendarClassId", taskCalendarClassId);
        if (teachGroupId > 0)
            map.put("teachGroupId", teachGroupId);
        map.put("teachClassId", GlobalParam.teachClassId);
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
     * 根据日期字符串 填充开始与结束位置数据
     *
     * @param startShowDate
     * @param endShowDate
     */
    public void setStartEndDateAndWeek(String startShowDate, String endShowDate) {
        Date startDate = DateTool.parseDateStr2(startShowDate);
        Date endDate = DateTool.parseDateStr2(endShowDate);

        tv_start_time_tip.setVisibility(View.GONE);
        tv_end_time_tip.setVisibility(View.GONE);

        tv_start_time.setVisibility(View.VISIBLE);
        tv_end_time.setVisibility(View.VISIBLE);
        tv_start_weekday.setVisibility(View.VISIBLE);
        tv_end_weekday.setVisibility(View.VISIBLE);

        tv_start_time.setText(DateTool.getTheDateStrP8(startDate));
        tv_end_time.setText(DateTool.getTheDateStrP8(endDate));
        tv_start_weekday.setText(DateTool.getWeekOfDate2(startDate.getTime()));
        tv_end_weekday.setText(DateTool.getWeekOfDate2(endDate.getTime()));
    }
}
