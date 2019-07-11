package com.park61.teacherhelper.module.clazz;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mixiaoxiao.smoothcompoundbutton.SmoothRadioButton;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.module.dict.ClazzType;
import com.park61.teacherhelper.module.dict.ResultCode;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新建班级页
 * Created by zhangchi on 2017/8/17.
 */
public class ClazzAddActivity extends BaseActivity {

    /**
     * 班级名称
     */
    private EditText etClazzName;
    /**
     * 班级类型单选框
     */
    private LinearLayout radioGroupClazzTypes;
    /**
     * 保存按钮
     */
    private TextView tvSave;
    /**
     * 将班级类型保存为散列表<Label:Value>
     */
    private Map<String, String> clazzTypeCache;

    @Override
    protected void onStart() {
        super.onStart();
        showDialog();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_add);
    }

    @Override
    public void initView() {
        etClazzName = (EditText) findViewById(R.id.et_clazz_name);
        radioGroupClazzTypes = (LinearLayout) findViewById(R.id.radio_group_clazz_types);
        tvSave = (TextView) findViewById(R.id.tv_save);
    }

    @Override
    public void initData() {
        //访问接口获取班级列表
        getClazzTypeList();
        if (null == clazzTypeCache) {
            clazzTypeCache = new HashMap<>();
        }
    }

    @Override
    public void initListener() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收集表单信息
                String clazzName = etClazzName.getText().toString();
                int childCount = radioGroupClazzTypes.getChildCount();
                SmoothRadioButton radioButton = null;
                for (int i = 0; i < childCount; i++) {
                    radioButton = (SmoothRadioButton) radioGroupClazzTypes.getChildAt(i);
                    if (radioButton.isChecked()) {
                        break;
                    }
                }
                String clazzTypeName = radioButton.getText().toString();
                asyncData(clazzName, clazzTypeCache.get(clazzTypeName));
            }
        });
    }

    /**
     * 获取班级类型列表的请求
     */
    public void getClazzTypeList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CLAZZ_TYPE;
        Map<String, Object> map = new HashMap<>();
        map.put("type", "teach_g_class_type");
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {

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
            JSONArray array = jsonResult.optJSONArray("list");
            List<ClazzType> types = new ArrayList<>();
            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    ClazzType clazzType = gson.fromJson(array.optJSONObject(i).toString(), ClazzType.class);
                    types.add(clazzType);
                }
            }
            for (int i = 0, size = types.size(); i < size; i++) {
                ClazzType type = types.get(i);
                //caching
                clazzTypeCache.put(type.getLabel(), type.getValue());
                //动态加载radio button
                View view = View.inflate(mContext, R.layout.smooth_radio_btn, null);
                SmoothRadioButton radioButton = (SmoothRadioButton) view.findViewById(R.id.radio_btn);
                radioButton.setText(type.getLabel());
                if (i > 0) {
                    //只有第一个才默认选中
                    radioButton.setChecked(false);
                }
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View btnCurr) {
                        ViewGroup group = (ViewGroup) btnCurr.getParent();
                        for (int i = 0, len = group.getChildCount(); i < len; i++) {
                            View btnI = group.getChildAt(i);
                            if (btnCurr.equals(btnI)) {
                                continue;
                            }
                            if (btnI instanceof SmoothRadioButton) {
                                SmoothRadioButton s1 = (SmoothRadioButton) btnI;
                                if (s1.isChecked()) {
                                    s1.setChecked(false);
                                }
                            }
                        }
                    }
                });
                radioGroupClazzTypes.addView(view);
            }
        }
    };

    /**
     * 请求保存
     */
    private void asyncData(String clazzName, String typeValue) {
        String wholeUrl = AppUrl.host + AppUrl.SAVE_NEW_CLAZZ;
        Map<String, Object> map = new HashMap<>();
        map.put("name", clazzName);
        map.put("typeValue", typeValue);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, sListener);
    }

    BaseRequestListener sListener = new JsonRequestListener() {

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
            showShortToast("添加成功");
            setResult(ResultCode.RESULT_CODE_SUCCESS.getCode());
            EventBus.getDefault().post(new MessageEvent("REFRESH_FAMILYCONBOOK_MAIN_ClASS"));
            finish();
        }
    };


}
