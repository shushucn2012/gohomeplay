package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/9/25.
 */

public class LoginByPwdActivity extends BaseActivity {

    private Button btn_login;
    private EditText edit_user_phone, edit_user_pwd;
    private View area_back;
    private TextView tv_forget_pwd;

    private String username, userpwd;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login_by_pwd);
    }

    @Override
    public void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        edit_user_pwd = (EditText) findViewById(R.id.edit_user_pwd);
        area_back = findViewById(R.id.area_back);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        area_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate())
                    return;
                asyncLogin();
            }
        });
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgetPwdInputPhoneActivity.class));
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_phone);
        etList.add(edit_user_pwd);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_login);
    }

    protected boolean validate() {
        username = edit_user_phone.getText().toString();
        userpwd = edit_user_pwd.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showShortToast("登录手机号未填写！");
            return false;
        }
        if (TextUtils.isEmpty(userpwd)) {
            showShortToast("登录密码未填写！");
            return false;
        }
        return true;
    }

    /**
     * 登录
     */
    private void asyncLogin() {
        String wholeUrl = AppUrl.host + AppUrl.LOGIN;
        String requestBodyData = ParamBuild.login(username, userpwd);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, loginLsner);
    }

    BaseRequestListener loginLsner = new JsonRequestListener() {

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
            GlobalParam.userToken = jsonResult.optString("userToken");
            showShortToast("登录成功！");
            CommonMethod.saveCurrentUserName(mContext);
            startActivity(new Intent(mContext, MainTabActivity.class));
            finish();
        }
    };

}
