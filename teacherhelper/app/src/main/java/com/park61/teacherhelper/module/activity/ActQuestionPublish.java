package com.park61.teacherhelper.module.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/10/21.
 */

public class ActQuestionPublish extends BaseActivity {

    private EditText edit_input_word;
    private TextView tv_words_num;

    private String content;
    private int activityId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_question_publish);
    }

    @Override
    public void initView() {
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        tv_words_num = (TextView) findViewById(R.id.tv_words_num);
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
    }

    @Override
    public void initListener() {
        edit_input_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                logout("s=======================" + s);
                tv_words_num.setText(s.length() + "/40");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showShortToast("请输入内容！");
                    return;
                }
                if (content.length() < 5) {
                    showShortToast("内容不能少于5个字！");
                    return;
                }
                asyncAsk();
            }
        });
    }

    private void asyncAsk() {
        String wholeUrl = AppUrl.host + AppUrl.addQuestion;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", content);
        map.put("teachActivityId", activityId);//activityId=1
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, blistener);
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
            showShortToast("发布成功！");
            finish();
        }
    };
}
