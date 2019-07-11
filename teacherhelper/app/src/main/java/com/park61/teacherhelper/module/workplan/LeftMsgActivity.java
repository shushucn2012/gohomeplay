package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 留言
 * Created by shubei on 2018/4/28.
 */

public class LeftMsgActivity extends BaseActivity {

    private TextView tv_area_right;
    private EditText edit_input_word;

    private String intro;
    private int taskCalendarId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_leftmsg);
    }

    @Override
    public void initView() {
        setPagTitle("留言");
        tv_area_right = (TextView) findViewById(R.id.tv_area_right);
        tv_area_right.setText("确定");
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
    }

    @Override
    public void initData() {
        taskCalendarId = getIntent().getIntExtra("taskCalendarId", -1);
    }

    @Override
    public void initListener() {
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intro = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(intro)) {
                    showShortToast("请输入留言");
                    return;
                }
                asyncLeftMsg();
            }
        });
    }

    /**
     * 任务留言
     */
    private void asyncLeftMsg() {
        String wholeUrl = AppUrl.host + AppUrl.addMessage;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarId", taskCalendarId);
        map.put("intro", intro);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            showShortToast("留言成功！");
            sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
            finish();
        }
    };

}
