package com.park61.teacherhelper.module.activity;

import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.activity.adapter.TrainEvaQuestionListAdapter;
import com.park61.teacherhelper.module.activity.bean.TrainQuestionSectionBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrainFeedBackSucActivity extends BaseActivity {

    private int trainingId;

    private TextView tv_shownum;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_train_feedback_suc);
    }

    @Override
    public void initView() {
        setPagTitle("评价反馈");
        tv_shownum = findViewById(R.id.tv_shownum);
    }

    @Override
    public void initData() {
        trainingId = getIntent().getIntExtra("trainingId", -1);
        asyncGetData();
    }

    @Override
    public void initListener() {

    }

    /**
     * 请求列表数据
     */
    private void asyncGetData() {
        String wholeUrl = AppUrl.host + AppUrl.trainingEvaluateCisPosition;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainingId", trainingId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

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
            tv_shownum.setText("您是第" + jsonResult.optString("data") + "位老师，感谢您的反馈！");
        }
    };
}
