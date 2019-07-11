package com.park61.teacherhelper.module.workplan;

import android.widget.ListView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.workplan.adapter.TempTasksAdapter;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.module.workplan.bean.TempTaskBean;
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
 * 模板的所有任务一览
 * Created by shubei on 2018/7/27.
 */

public class TempTasksActivity extends BaseActivity {

    private ListView lv_tasks;
    private int tempId;
    private String tempName;

    private List<TempTaskBean> dataList = new ArrayList<>();
    private TempTasksAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_temp_tasks);
    }

    @Override
    public void initView() {
        lv_tasks = (ListView) findViewById(R.id.lv_tasks);
    }

    @Override
    public void initData() {
        tempId = getIntent().getIntExtra("tempId", -1);
        tempName= getIntent().getStringExtra("tempName");
        setPagTitle(tempName);
        adapter = new TempTasksAdapter(mContext, dataList);
        lv_tasks.setAdapter(adapter);
        asyncGetTemplateTaskList();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取模版任务列表
     */
    public void asyncGetTemplateTaskList() {
        String wholeUrl = AppUrl.host + AppUrl.templateTaskList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", tempId);
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
            dataList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject itemJot = actJay.optJSONObject(i);
                    TempTaskBean g = gson.fromJson(itemJot.toString(), TempTaskBean.class);
                    dataList.add(g);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
}
