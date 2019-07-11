package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.RegexValidator;
import com.park61.teacherhelper.common.tool.ViewInitTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/9/25.
 */

public class UnionWxBindActivity extends BaseActivity {

    private EditText edit_user_phone;
    private Button btn_next;
    private View area_back;

    private String userMobile;
    private String mopenId, nickname, sex, headimgurl, unionQQorWx;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login_by_phone);
    }

    @Override
    public void initView() {
        edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        btn_next = (Button) findViewById(R.id.btn_next);
        area_back = findViewById(R.id.area_back);
        ((TextView) findViewById(R.id.tv_big_tip)).setText("绑定手机号码");
        ((TextView) findViewById(R.id.tv_small_tip)).setText("为了账户安全，请绑定您的手机号");
    }

    @Override
    public void initData() {
        mopenId = getIntent().getStringExtra("mopenId");
        nickname = getIntent().getStringExtra("nickname");
        sex = getIntent().getStringExtra("sex");
        headimgurl = getIntent().getStringExtra("headimgurl");
        unionQQorWx = getIntent().getStringExtra("unionQQorWx");
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhone()) {
                    return;
                }
                Intent it = new Intent(mContext, WxBindByPhoneVCodeActivity.class);
                it.putExtra("mopenId", mopenId);
                it.putExtra("unionQQorWx", unionQQorWx);
                it.putExtra("nickname", nickname);
                it.putExtra("sex", sex);
                it.putExtra("headimgurl", headimgurl);
                it.putExtra("userMobile", userMobile);
                startActivity(it);
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
        area_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_phone);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_next);
    }
}
