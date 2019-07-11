package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.workplan.adapter.ChooseRolesListAdapter;
import com.park61.teacherhelper.module.workplan.adapter.WorkTaskChooseFileAdapter;
import com.park61.teacherhelper.module.workplan.bean.CourseSeriesBean;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;
import com.park61.teacherhelper.module.workplan.bean.TaskLevelBean;
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加任务选择参考文档
 * Created by shubei on 2018/8/5.
 */

public class WorkTaskChooseFileActivity extends BaseActivity {

    private ListView lv_whole;
    private TextView tv_chosen_num;
    private Button btn_sure;
    private EditText edit_sousuo;
    private ImageView img_del;

    private int taskId;//任务id
    private String keyword;//搜索关键字
    private WorkTaskChooseFileAdapter adapter;
    private List<CourseSeriesBean> sList = new ArrayList<>();//参考文档列表
    private List<KnowledgeBean> kList;//已选课程列表

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_worktask_choose_file);
    }

    @Override
    public void initView() {
        setPagTitle("课程选择");
        lv_whole = (ListView) findViewById(R.id.lv_whole);
        tv_chosen_num = (TextView) findViewById(R.id.tv_chosen_num);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
    }

    @Override
    public void initData() {
        taskId = getIntent().getIntExtra("taskId", -1);
        kList = (List<KnowledgeBean>) getIntent().getSerializableExtra("kList");
        adapter = new WorkTaskChooseFileAdapter(mContext, sList);
        lv_whole.setAdapter(adapter);
        asyncGetTrainerCourseListt();
    }

    @Override
    public void initListener() {
        adapter.setOnCheckedListener(new WorkTaskChooseFileAdapter.OnCheckedListener() {
            @Override
            public void onChecked() {
                updateTotalNum();
            }
        });
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edit_sousuo.getText().toString().trim();
                    asyncGetTrainerCourseListt();
                }
                return false;
            }
        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    img_del.setVisibility(View.GONE);
                } else {
                    img_del.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<KnowledgeBean> backList = new ArrayList<>();
                for (int i = 0; i < sList.size(); i++) {
                    List<KnowledgeBean> courseList = sList.get(i).getTrainerCourseList();
                    if (!CommonMethod.isListEmpty(courseList)) {
                        for (int j = 0; j < courseList.size(); j++) {
                            if (courseList.get(j).isChecked()) {
                                backList.add(courseList.get(j));
                            }
                        }
                    }
                }
                if (CommonMethod.isListEmpty(backList))
                    return;
                Intent returnData = new Intent();
                returnData.putExtra("kList", (Serializable) backList);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    /**
     * 获取课程列表
     */
    private void asyncGetTrainerCourseListt() {
        String url = AppUrl.host + AppUrl.getTrainerCourseList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (taskId > 0)
            map.put("id", taskId);
        if (!TextUtils.isEmpty(keyword))
            map.put("name", keyword);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, monthPointListener);
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
            sList.clear();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    CourseSeriesBean courseSeriesBean = gson.fromJson(arr.optJSONObject(i).toString(), CourseSeriesBean.class);
                    sList.add(courseSeriesBean);
                }
                for (int k = 0; k < sList.size(); k++) {
                    CourseSeriesBean courseSeriesBean = sList.get(k);
                    if (!CommonMethod.isListEmpty(courseSeriesBean.getTrainerCourseList())) {
                        for (int m = 0; m < courseSeriesBean.getTrainerCourseList().size(); m++) {
                            KnowledgeBean knowledgeBean = courseSeriesBean.getTrainerCourseList().get(m);
                            for (KnowledgeBean chosenBean : kList) {
                                if (knowledgeBean.getId() == chosenBean.getId()) {
                                    knowledgeBean.setChecked(true);
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                updateTotalNum();
            } else {
                ViewInitTool.setListEmptyView(mContext, lv_whole, "没有找到相关内容", R.mipmap.icon_search_empty, null);
            }
        }
    };

    /**
     * 更新已选人数
     */
    public void updateTotalNum() {
        int cNum = getChosenNum();
        tv_chosen_num.setText("已选择：" + cNum);
        if (cNum > 0) {
            btn_sure.setBackgroundResource(R.drawable.rec_deepred_stroke_deepred_solid_corner30);
            btn_sure.setTextColor(mContext.getResources().getColor(R.color.gffffff));
        } else {
            btn_sure.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            btn_sure.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
    }

    /**
     * 计算已选人数
     */
    public int getChosenNum() {
        int totalChosenNum = 0;
        for (int i = 0; i < sList.size(); i++) {
            List<KnowledgeBean> knowledgeBeanList = sList.get(i).getTrainerCourseList();
            if (!CommonMethod.isListEmpty(knowledgeBeanList)) {
                for (int j = 0; j < knowledgeBeanList.size(); j++) {
                    if (knowledgeBeanList.get(j).isChecked()) {
                        totalChosenNum++;
                    }
                }
            }
        }
        return totalChosenNum;
    }

}
