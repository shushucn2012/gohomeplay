package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
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
 * Created by shubei on 2017/9/25.
 */

public class LoginByPhoneActivity extends BaseActivity {

    private EditText edit_user_phone;
    private Button btn_next;
    private View area_back;

    private String userMobile;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login_by_phone);
    }

    @Override
    public void initView() {
        edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        btn_next = (Button) findViewById(R.id.btn_next);
        area_back = findViewById(R.id.area_back);
    }

    @Override
    public void initData() {
        area_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhone()) {
                    return;
                }
                asyncVerifyPhone();
               /* Intent it = new Intent(mContext, LoginByPhoneVCodeActivity.class);
                it.putExtra("userMobile", userMobile);
                startActivity(it);*/
            }
        });
    }

    protected boolean validatePhone() {
        userMobile = edit_user_phone.getText().toString();
        if (TextUtils.isEmpty(userMobile)) {
            showShortToast("登录手机号没有填写！");
            return false;
        }
        if (!RegexValidator.isMobile(userMobile)) {
            showShortToast("手机号输入有误！");
            return false;
        }
        return true;
    }

    @Override
    public void initListener() {
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_phone);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_next);
    }

    /**
     * 验证手机号是否存在
     */
    protected void asyncVerifyPhone() {
        String wholeUrl = AppUrl.host + AppUrl.checkLoginAccountIsExist;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userMobile", userMobile);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, LLsner);
    }

    BaseRequestListener LLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("002".equals(errorCode)) {
                Intent it = new Intent(mContext, LoginByPhoneVCodeActivity.class);
                it.putExtra("userMobile", userMobile);
                startActivity(it);
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Intent it = new Intent(mContext, RegisterV2GetVCodeActivity.class);
            it.putExtra("userMobile", userMobile);
            startActivity(it);
        }
    };
}
