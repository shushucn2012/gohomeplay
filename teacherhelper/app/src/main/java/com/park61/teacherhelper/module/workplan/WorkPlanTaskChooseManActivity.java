package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.adapter.ChooseRolesListAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.park61.teacherhelper.module.workplan.bean.TaskRole;

import java.util.ArrayList;
import java.util.List;

/**
 * 行事历-任务管理-选择执行人
 * Created by shubei on 2018/7/2.
 */

public class WorkPlanTaskChooseManActivity extends BaseActivity {

    private List<TaskRole> roleList = new ArrayList<>();

    private ListView lv_whole;
    private ChooseRolesListAdapter myListAdapter;
    private TextView tv_chosen_num, tv_area_right;
    private Button btn_sure;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workplan_task_chooseman);
    }

    @Override
    public void initView() {
        setPagTitle("选择执行人");
        tv_area_right = (TextView) findViewById(R.id.tv_area_right);
        tv_area_right.setText("全选");
        lv_whole = (ListView) findViewById(R.id.lv_whole);
        tv_chosen_num = (TextView) findViewById(R.id.tv_chosen_num);
        btn_sure = (Button) findViewById(R.id.btn_sure);
    }

    @Override
    public void initData() {
        roleList = (List<TaskRole>) getIntent().getSerializableExtra("roleList");
        myListAdapter = new ChooseRolesListAdapter(this, roleList);
        lv_whole.setAdapter(myListAdapter);
    }

    @Override
    public void initListener() {
        myListAdapter.setOnCheckedListener(new ChooseRolesListAdapter.OnCheckedListener() {
            @Override
            public void onChecked() {
                updateTotalNum();
            }
        });
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_area_right.getText().toString().equals("全选")) {
                    for (int i = 0; i < roleList.size(); i++) {
                        List<TaskPerson> persons = roleList.get(i).getPersons();
                        for (int j = 0; j < persons.size(); j++) {
                            persons.get(j).setChecked(true);
                        }
                    }
                    myListAdapter.notifyDataSetChanged();
                    tv_area_right.setText("全不选");
                } else {
                    for (int i = 0; i < roleList.size(); i++) {
                        List<TaskPerson> persons = roleList.get(i).getPersons();
                        for (int j = 0; j < persons.size(); j++) {
                            persons.get(j).setChecked(false);
                        }
                    }
                    myListAdapter.notifyDataSetChanged();
                    tv_area_right.setText("全选");
                }
                updateTotalNum();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for (int i = 0; i < roleList.size(); i++) {
                    List<TaskPerson> persons = roleList.get(i).getPersons();
                    for (int j = 0; j < persons.size(); j++) {
                        if (persons.get(j).isChecked()) {
                            sb.append(persons.get(j).getUserId());
                            sb.append(",");
                            sb2.append(TextUtils.isEmpty(persons.get(j).getName()) ? persons.get(j).getMobile() : persons.get(j).getName());
                            sb2.append(",");
                        }
                    }
                }
                String userIdListStrs = "";
                if (!TextUtils.isEmpty(sb)) {
                    userIdListStrs = sb.substring(0, sb.length() - 1);
                } else {
                    showShortToast("未选择执行人员！");
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("userIdListStrs", userIdListStrs);

                String userNameListStrs = "";
                String[] userNameListArr = sb2.toString().split(",");
                if (userNameListArr.length == 1) {
                    userNameListStrs = userNameListArr[0];
                } else if (userNameListArr.length == 2) {
                    userNameListStrs = userNameListArr[0] + "," + userNameListArr[1];
                } else {
                    userNameListStrs = userNameListArr[0] + "," + userNameListArr[1] + "等" + userNameListArr.length + "人";
                }
                returnData.putExtra("userNameListStrs", userNameListStrs);

                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    /**
     * 更新已选人数
     */
    public void updateTotalNum() {
        tv_chosen_num.setText("已选择：" + getChosenNum() + "位老师");
    }

    /**
     * 计算已选人数
     */
    public int getChosenNum() {
        int totalChosenNum = 0;
        for (int i = 0; i < roleList.size(); i++) {
            List<TaskPerson> persons = roleList.get(i).getPersons();
            for (int j = 0; j < persons.size(); j++) {
                if (persons.get(j).isChecked()) {
                    totalChosenNum++;
                }
            }
        }
        return totalChosenNum;
    }
}
