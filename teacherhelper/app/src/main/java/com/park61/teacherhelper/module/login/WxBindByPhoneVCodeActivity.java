package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.textview.AccoutSafeTimeTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/9/25.
 */

public class WxBindByPhoneVCodeActivity extends BaseActivity {
    private TextView tv_pwd_1, tv_pwd_2, tv_pwd_3, tv_pwd_4, tv_pwd_5, tv_pwd_6, tv_send_vcode, tv_phone;
    private EditText edit_vcode;
    private AccoutSafeTimeTextView ttv_time;
    private Button btn_get_vcode;
    private View area_back;

    private String userMobile;
    private String mopenId, nickname, sex, headimgurl, unionQQorWx;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_loginbyphone_vcode);
    }

    @Override
    public void initView() {
        area_back = findViewById(R.id.area_back);
        edit_vcode = (EditText) findViewById(R.id.edit_vcode);
        tv_pwd_1 = (TextView) findViewById(R.id.tv_pwd_1);
        tv_pwd_2 = (TextView) findViewById(R.id.tv_pwd_2);
        tv_pwd_3 = (TextView) findViewById(R.id.tv_pwd_3);
        tv_pwd_4 = (TextView) findViewById(R.id.tv_pwd_4);
        tv_pwd_5 = (TextView) findViewById(R.id.tv_pwd_5);
        tv_pwd_6 = (TextView) findViewById(R.id.tv_pwd_6);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        ttv_time = (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
        btn_get_vcode = (Button) findViewById(R.id.btn_get_vcode);
    }

    @Override
    public void initData() {
        mopenId = getIntent().getStringExtra("mopenId");
        nickname = getIntent().getStringExtra("nickname");
        sex = getIntent().getStringExtra("sex");
        headimgurl = getIntent().getStringExtra("headimgurl");
        unionQQorWx = getIntent().getStringExtra("unionQQorWx");

        userMobile = getIntent().getStringExtra("userMobile");
        tv_phone.setText(userMobile);
        asyncGetRegVcode();
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
        btn_get_vcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                asyncGetRegVcode();
            }
        });
        edit_vcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    tv_pwd_1.setText("");
                    tv_pwd_2.setText("");
                    tv_pwd_3.setText("");
                    tv_pwd_4.setText("");
                    tv_pwd_5.setText("");
                    tv_pwd_6.setText("");
                } else if (s.length() == 1) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText("");
                    tv_pwd_3.setText("");
                    tv_pwd_4.setText("");
                    tv_pwd_5.setText("");
                    tv_pwd_6.setText("");
                } else if (s.length() == 2) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText(s.charAt(1) + "");
                    tv_pwd_3.setText("");
                    tv_pwd_4.setText("");
                    tv_pwd_5.setText("");
                    tv_pwd_6.setText("");
                } else if (s.length() == 3) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText(s.charAt(1) + "");
                    tv_pwd_3.setText(s.charAt(2) + "");
                    tv_pwd_4.setText("");
                    tv_pwd_5.setText("");
                    tv_pwd_6.setText("");
                } else if (s.length() == 4) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText(s.charAt(1) + "");
                    tv_pwd_3.setText(s.charAt(2) + "");
                    tv_pwd_4.setText(s.charAt(3) + "");
                    tv_pwd_5.setText("");
                    tv_pwd_6.setText("");
                } else if (s.length() == 5) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText(s.charAt(1) + "");
                    tv_pwd_3.setText(s.charAt(2) + "");
                    tv_pwd_4.setText(s.charAt(3) + "");
                    tv_pwd_5.setText(s.charAt(4) + "");
                    tv_pwd_6.setText("");
                } else if (s.length() == 6) {
                    tv_pwd_1.setText(s.charAt(0) + "");
                    tv_pwd_2.setText(s.charAt(1) + "");
                    tv_pwd_3.setText(s.charAt(2) + "");
                    tv_pwd_4.setText(s.charAt(3) + "");
                    tv_pwd_5.setText(s.charAt(4) + "");
                    tv_pwd_6.setText(s.charAt(5) + "");
                    asyncUnionLogin();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 开始计时
     */
    public void reStartMyTimer() {
        if (!ttv_time.isRun()) {
            btn_get_vcode.setVisibility(View.GONE);
            ttv_time.setVisibility(View.VISIBLE);
            ttv_time.setTimes(new long[]{0, 0, 0, 61});
            ttv_time.run();
            ttv_time.setOnTimeEndLsner(new AccoutSafeTimeTextView.OnTimeEnd() {
                @Override
                public void onEnd() {
                    btn_get_vcode.setVisibility(View.VISIBLE);
                    ttv_time.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 发送验证码
     */
    protected void asyncGetRegVcode() {
        String wholeUrl = AppUrl.host + AppUrl.sendUnionLoginBindMobile;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userMobile", userMobile);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getRegVcodeLsner);
    }

    BaseRequestListener getRegVcodeLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("005".equals(errorCode) || "null".equals(errorMsg)) {
                showShortToast("验证码发送过于频繁，请稍后再试！");
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("验证码已发送！");
            reStartMyTimer();
        }
    };

    /**
     * 联合登录
     */
    private void asyncUnionLogin() {
        String wholeUrl = AppUrl.host + AppUrl.UNION_LOGIN;
        String requestBodyData = "";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", userMobile);
        map.put("verifyCode", edit_vcode.getText().toString().trim());
        map.put("loginName", mopenId);
        map.put("unionLoginType", unionQQorWx);
        map.put("name", "");
        map.put("email", "");
        map.put("petName", nickname);
        map.put("sex", sex);
        map.put("pictureUrl", headimgurl);
        requestBodyData = ParamBuild.buildParams(map);
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
