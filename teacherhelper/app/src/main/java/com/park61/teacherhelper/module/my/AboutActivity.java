package com.park61.teacherhelper.module.my;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;

public class AboutActivity extends BaseActivity {

    private TextView tv_version;
    private TextView tv_about;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_about = (TextView) findViewById(R.id.tv_about);
        Button btn_request = (Button) findViewById(R.id.btn_request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 退用户押金
     *//*
    private void asyncBackUserDeposit() {
        String wholeUrl = "http://101.200.173.227:9090/rest/auth/login?user=ayyey&passwd=MTIzNDU2&_=1497342288801";//AppUrl.host + AppUrl.EXTRACTDEPOSITAMOUNT;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

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
            showShortToast("押金退还申请成功，预计2小时内到账");
        }
    };*/

    @Override
    public void initData() {
        tv_version.setText("版本号：V" + GlobalParam.versionName);
    }

    @Override
    public void initListener() {

    }

}
