package com.park61.teacherhelper.module.home;

import android.content.Intent;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.clazz.ClazzMainActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/9/4.
 */

public class ScanTeacherToClassActivity extends BaseActivity {

    private long classId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_scan_teacher_to_class);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        classId = getIntent().getLongExtra("classId", -1l);
        asyncAddClassTeacher();
    }

    @Override
    public void initListener() {

    }

    private void asyncAddClassTeacher() {
        String wholeUrl = AppUrl.host + AppUrl.addClassTeacher;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gClassId", classId);
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
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("加入班级成功！");
            Intent it = new Intent(mContext, ClazzMainActivity.class);
            startActivity(it);
            finish();
        }
    };
}
