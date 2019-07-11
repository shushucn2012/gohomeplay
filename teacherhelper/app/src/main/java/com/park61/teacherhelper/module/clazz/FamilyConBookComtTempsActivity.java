package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.module.clazz.adapter.FamilyConBookTempsAdapter;
import com.park61.teacherhelper.module.clazz.bean.FamilyConBookComtTempBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 评语模板列表
 *
 * @author shubei
 * @time 2018/11/26 15:34
 */
public class FamilyConBookComtTempsActivity extends BaseActivity {

    private ListView lv_whole;

    private List<FamilyConBookComtTempBean> sList = new ArrayList<>();
    private FamilyConBookTempsAdapter myListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_familyconbook_comttemps);
    }

    @Override
    public void initView() {
        setPagTitle("选择模板");
        ((TextView) findViewById(R.id.tv_area_right)).setText("确定");
        lv_whole = (ListView) findViewById(R.id.lv_whole);
    }

    @Override
    public void initData() {
        myListAdapter = new FamilyConBookTempsAdapter(this, sList);
        lv_whole.setAdapter(myListAdapter);
        asyncGetTemplateList();
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sList.size(); i++) {
                    List<FamilyConBookComtTempBean.TeachCommentTemplateDetailListBean> detailListBeans = sList.get(i).getTeachCommentTemplateDetailList();
                    for (int j = 0; j < detailListBeans.size(); j++) {
                        if (detailListBeans.get(j).isChecked()) {
                            if (!"个人保存".equals(sList.get(i).getTitle())) {
                                sb.append("#" + sList.get(i).getTitle() + "#：");
                                //sb.append("\n");
                            }
                            sb.append(detailListBeans.get(j).getContent());
                            sb.append("\n");
                        }
                    }
                }
                Intent returnData = new Intent();
                returnData.putExtra("backStr", sb.toString());
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    private void asyncGetTemplateList() {
        String wholeUrl = AppUrl.host + AppUrl.teachCommentTemplateList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
            JSONArray jsonArray = jsonResult.optJSONArray("list");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    FamilyConBookComtTempBean familyConBookComtTempBean = gson.fromJson(jsonArray.optJSONObject(i).toString(), FamilyConBookComtTempBean.class);
                    sList.add(familyConBookComtTempBean);
                }
                myListAdapter.notifyDataSetChanged();
            }
        }
    };
}
