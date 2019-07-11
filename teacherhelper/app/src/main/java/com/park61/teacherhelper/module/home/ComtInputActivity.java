package com.park61.teacherhelper.module.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/8/30.
 */

public class ComtInputActivity extends BaseActivity {

    private ImageView face1, face2, face3, face4, face5;
    private EditText edit_input_word;

    private int index = -1;
    private String content;
    private int teachCourseId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_comt_input);
    }

    @Override
    public void initView() {
        face1 = (ImageView) findViewById(R.id.face1);
        face2 = (ImageView) findViewById(R.id.face2);
        face3 = (ImageView) findViewById(R.id.face3);
        face4 = (ImageView) findViewById(R.id.face4);
        face5 = (ImageView) findViewById(R.id.face5);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
    }

    @Override
    public void initData() {
        teachCourseId = getIntent().getIntExtra("teachCourseId", -1);
    }

    @Override
    public void initListener() {
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < 0) {
                    showShortToast("请选择你的表情！");
                    return;
                }
                content = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showShortToast("请输入反馈内容！");
                    return;
                }
                if(content.length() < 5){
                    showShortToast("内容至少5个字哦！");
                    return;
                }
                asyncAddEvaluate();
            }
        });
        face1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                face1.setImageResource(R.mipmap.icon_comt1_focus);
                face2.setImageResource(R.mipmap.icon_comt2_default);
                face3.setImageResource(R.mipmap.icon_comt3_default);
                face4.setImageResource(R.mipmap.icon_comt4_default);
                face5.setImageResource(R.mipmap.icon_comt5_default);
            }
        });
        face2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                face1.setImageResource(R.mipmap.icon_comt1_default);
                face2.setImageResource(R.mipmap.icon_comt2_focus);
                face3.setImageResource(R.mipmap.icon_comt3_default);
                face4.setImageResource(R.mipmap.icon_comt4_default);
                face5.setImageResource(R.mipmap.icon_comt5_default);
            }
        });
        face3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 2;
                face1.setImageResource(R.mipmap.icon_comt1_default);
                face2.setImageResource(R.mipmap.icon_comt2_default);
                face3.setImageResource(R.mipmap.icon_comt3_focus);
                face4.setImageResource(R.mipmap.icon_comt4_default);
                face5.setImageResource(R.mipmap.icon_comt5_default);
            }
        });
        face4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 3;
                face1.setImageResource(R.mipmap.icon_comt1_default);
                face2.setImageResource(R.mipmap.icon_comt2_default);
                face3.setImageResource(R.mipmap.icon_comt3_default);
                face4.setImageResource(R.mipmap.icon_comt4_focus);
                face5.setImageResource(R.mipmap.icon_comt5_default);
            }
        });
        face5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 4;
                face1.setImageResource(R.mipmap.icon_comt1_default);
                face2.setImageResource(R.mipmap.icon_comt2_default);
                face3.setImageResource(R.mipmap.icon_comt3_default);
                face4.setImageResource(R.mipmap.icon_comt4_default);
                face5.setImageResource(R.mipmap.icon_comt5_focus);
            }
        });
    }

    private void asyncAddEvaluate() {
        String wholeUrl = AppUrl.host + AppUrl.teachCourseEvaluate_add;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teachCourseId", teachCourseId);
        map.put("score", index + 1);
        map.put("content", content);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
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
            showShortToast("评价成功！");
            setResult(RESULT_OK);
            finish();
        }
    };
}
