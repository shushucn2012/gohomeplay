package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.clazz.adapter.FamilyChoseStuListAdapter;
import com.park61.teacherhelper.module.clazz.bean.StudentBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家庭联系薄-发布评语-选择学生
 *
 * @author shubei
 * @time 2018/11/22 15:24
 */
public class FamilyConBookChoseStuActivity extends BaseActivity {

    private List<StudentBean> sList = new ArrayList<>();
    private String teachClassId;//班级id
    private String userChildIds = "";
    ;

    private ListView lv_whole;
    private FamilyChoseStuListAdapter myListAdapter;
    private TextView tv_chosen_num, tv_area_right;
    private Button btn_sure;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workplan_task_chooseman);
    }

    @Override
    public void initView() {
        setPagTitle("选择学生");
        tv_area_right = (TextView) findViewById(R.id.tv_area_right);
        tv_area_right.setText("全选");
        lv_whole = (ListView) findViewById(R.id.lv_whole);
        tv_chosen_num = (TextView) findViewById(R.id.tv_chosen_num);
        btn_sure = (Button) findViewById(R.id.btn_sure);

        tv_chosen_num.setText("已选择：0位宝宝");
    }

    @Override
    public void initData() {
        teachClassId = getIntent().getStringExtra("teachClassId");
        userChildIds = getIntent().getStringExtra("userChildIds");

        sList = new ArrayList<>();//(List<TaskRole>) getIntent().getSerializableExtra("roleList");
        myListAdapter = new FamilyChoseStuListAdapter(this, sList);
        lv_whole.setAdapter(myListAdapter);
        asyncGetClazzStusList();
    }

    /**
     * 请求数据
     */
    private void asyncGetClazzStusList() {
        String wholeUrl = AppUrl.host + AppUrl.listChild;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gClassId", teachClassId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            JSONArray dataJay = jsonResult.optJSONArray("rows");
            if (dataJay != null && dataJay.length() > 0) {
                sList.clear();
                for (int i = 0; i < dataJay.length(); i++) {
                    StudentBean studentBean = gson.fromJson(dataJay.optJSONObject(i).toString(), StudentBean.class);
                    if (userChildIds != null && userChildIds.contains(studentBean.getUserChildId() + "")) {
                        studentBean.setChecked(true);
                    }
                    sList.add(studentBean);
                }
                updateTotalNum();
                myListAdapter.notifyDataSetChanged();
            } else {
                sList.clear();
                myListAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, lv_whole);
            }
        }
    };

    @Override
    public void initListener() {
        myListAdapter.setOnCheckedListener(new FamilyChoseStuListAdapter.OnCheckedListener() {
            @Override
            public void onChecked() {
                updateTotalNum();
            }
        });
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_area_right.getText().toString().equals("全选")) {
                    for (int i = 0; i < sList.size(); i++) {
                        sList.get(i).setChecked(true);
                    }
                    myListAdapter.notifyDataSetChanged();
                    tv_area_right.setText("全不选");
                } else {
                    for (int i = 0; i < sList.size(); i++) {
                        sList.get(i).setChecked(false);
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
                for (int i = 0; i < sList.size(); i++) {
                    if (sList.get(i).isChecked()) {
                        sb.append(sList.get(i).getUserChildId());
                        sb.append(",");
                    }
                }
                if (!TextUtils.isEmpty(sb)) {
                    userChildIds = sb.substring(0, sb.length() - 1);
                } else {
                    showShortToast("请选择学生！");
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("userChildIds", userChildIds);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    /**
     * 更新已选人数
     */
    public void updateTotalNum() {
        tv_chosen_num.setText("已选择：" + getChosenNum() + "位宝宝");
    }

    /**
     * 计算已选人数
     */
    public int getChosenNum() {
        int totalChosenNum = 0;
        for (int i = 0; i < sList.size(); i++) {
            if (sList.get(i).isChecked()) {
                totalChosenNum++;
            }
        }
        return totalChosenNum;
    }
}
