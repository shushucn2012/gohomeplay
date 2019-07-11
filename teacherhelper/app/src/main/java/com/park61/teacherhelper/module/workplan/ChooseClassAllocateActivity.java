package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.workplan.adapter.TempClassesAdapter;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.module.workplan.bean.TempClassBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务模板选择班级
 * Created by shubei on 2018/7/30.
 */

public class ChooseClassAllocateActivity extends BaseActivity {

    private static final int REQ_CHOOSE_DATES = 1;
    private ListView lv_classes;
    private TextView tv_area_right, tv_group_name;

    private int taskCalendarTemplateId;//模板id
    private int teachGroupId;//幼儿园id
    private String groupName;//幼儿园名称
    private List<TempClassBean> tempClassList = new ArrayList<>();
    private TempClassesAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_choose_class_allocate);
    }

    @Override
    public void initView() {
        tv_area_right = (TextView) findViewById(R.id.tv_area_right);
        lv_classes = (ListView) findViewById(R.id.lv_classes);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
    }

    @Override
    public void initData() {
        groupName = getIntent().getStringExtra("groupName");
        taskCalendarTemplateId = getIntent().getIntExtra("taskCalendarTemplateId", -1);
        teachGroupId = getIntent().getIntExtra("teachGroupId", -1);
        setPagTitle("选择班级");
        tv_area_right.setText("确定");
        tv_group_name.setText(groupName);
        adapter = new TempClassesAdapter(mContext, tempClassList);
        lv_classes.setAdapter(adapter);
        asyncGetClassList();
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int teachClassId = -1;
                for (int i = 0; i < tempClassList.size(); i++) {
                    if (tempClassList.get(i).isChecked()) {
                        teachClassId = tempClassList.get(i).getId();
                        break;
                    }
                }
                if (teachClassId < 0) {
                    showShortToast("请选择班级!");
                    return;
                }*/
                String teachClassIds = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < tempClassList.size(); i++) {
                    if (tempClassList.get(i).isChecked()) {
                        sb.append(tempClassList.get(i).getId() + "");
                        sb.append(",");
                    }
                }
                if (TextUtils.isEmpty(sb)) {
                    showShortToast("请选择班级!");
                    return;
                }
                teachClassIds = sb.substring(0, sb.length() - 1).toString();
                Intent intent = new Intent(mContext, TempToClassSetTimeActivity.class);
                intent.putExtra("taskCalendarTemplateId", taskCalendarTemplateId);
                intent.putExtra("teachClassIds", teachClassIds);
                intent.putExtra("teachGroupId", teachGroupId);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取班级列表
     */
    public void asyncGetClassList() {
        String wholeUrl = AppUrl.host + AppUrl.getAssignTaskClassList;
        Map<String, Object> map = new HashMap<>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        map.put("teachGroupId", teachGroupId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, alistener);
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
            tempClassList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject itemJot = actJay.optJSONObject(i);
                    TempClassBean g = gson.fromJson(itemJot.toString(), TempClassBean.class);
                    tempClassList.add(g);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
}
