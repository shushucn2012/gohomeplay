package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.RegexValidator;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/9/26.
 */

public class RegisterV2SetPwdActivity extends BaseActivity {

    private Button btn_commit;
    private EditText edit_user_pwd;
    private View area_back;
    private ImageView img_del, img_eye;

    private String userMobile, pwd, verifyCode;
    private boolean isShowPwd = false;//是否显示密码，初始为false

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_register_v2_setpwd);
    }

    @Override
    public void initView() {
        area_back = findViewById(R.id.area_back);
        edit_user_pwd = (EditText) findViewById(R.id.edit_user_pwd);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        img_del = (ImageView) findViewById(R.id.img_del);
        img_eye = (ImageView) findViewById(R.id.img_eye);
    }

    @Override
    public void initData() {
        userMobile = getIntent().getStringExtra("userMobile");
        verifyCode = getIntent().getStringExtra("verifyCode");
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
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = edit_user_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    showShortToast("请设置登录密码");
                    return;
                }
                if (!RegexValidator.isPassword(pwd)) {
                    showShortToast("密码不符合规则！请设置6到20位，由数字和字母组成密码！");
                    return;
                }
                asyncRegister();
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_pwd);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_commit);
        edit_user_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    img_del.setVisibility(View.VISIBLE);
                    img_eye.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.GONE);
                    img_eye.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_user_pwd.setText("");
            }
        });
        img_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPwd) {//已显示，点击则隐藏
                    img_eye.setImageResource(R.mipmap.icon_eye_closed);
                    edit_user_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_user_pwd.setSelection(edit_user_pwd.getText().length());
                    isShowPwd = false;
                } else {//已隐藏，点击则显示
                    img_eye.setImageResource(R.mipmap.icon_eye_open);
                    edit_user_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_user_pwd.setSelection(edit_user_pwd.getText().length());
                    isShowPwd = true;
                }
            }
        });
    }

    /**
     * 注册
     */
    private void asyncRegister() {
        String wholeUrl = AppUrl.host + AppUrl.register;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userMobile", userMobile);
        map.put("verifyCode", verifyCode);
        map.put("password", pwd);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, regLsner);
    }

    BaseRequestListener regLsner = new JsonRequestListener() {

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
            showShortToast("注册并登录成功！");
            CommonMethod.saveCurrentUserName(mContext);
            startActivity(new Intent(mContext, MainTabActivity.class));
            finish();
        }
    };

}
