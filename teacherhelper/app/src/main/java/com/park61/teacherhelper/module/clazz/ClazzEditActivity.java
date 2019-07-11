package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mixiaoxiao.smoothcompoundbutton.SmoothRadioButton;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.StringUtils;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.dict.ClazzType;
import com.park61.teacherhelper.module.dict.ResultCode;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑班级页
 * Created by zhangchi on 2017/8/17.
 */
public class ClazzEditActivity extends BaseActivity {

    private EditText etClazzName;

    private LinearLayout radioGroupClazzTypes;

//    private EditText etNewClazzType;

    private TeachGClass clazz;

    private TextView tvSave;

    private LinearLayout layExitClazz;

    private int listLocation; //在列表中的位置

    private ImageView qrCodeView;

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
        setContentView(R.layout.activity_clazz_edit);
    }

    @Override
    public void initView() {
        etClazzName = (EditText) findViewById(R.id.et_clazz_name);
        radioGroupClazzTypes = (LinearLayout) findViewById(R.id.radio_group_clazz_types);
        tvSave = (TextView) findViewById(R.id.tv_save);
        layExitClazz = (LinearLayout) findViewById(R.id.lay_exit_clazz);
        qrCodeView = (ImageView) findViewById(R.id.img_scan_qrcode);
    }

    @Override
    public void initData() {
        //访问接口获取班级类型列表
        getClazzTypeList();

        if (clazz == null) {
            clazz = new TeachGClass();
        }

        // int clazzTypeCache
        if (null == clazzTypeCache) {
            clazzTypeCache = new HashMap<>();
        }

        Intent intent = getIntent();
        clazz = (TeachGClass) intent.getExtras().getSerializable("clazz");
        etClazzName.setText(clazz.getName());
        listLocation = intent.getIntExtra("list_location", -1);
    }

    @Override
    public void initListener() {
        //保存回调
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
                asyncUpdateData(clazzName, clazzTypeCache.get(clazzTypeName));
            }
        });

        //退出班级回调
        layExitClazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncExitClass();
            }
        });

        //跳转到班级二维码回调
        qrCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClazzQRCodeNewActivity.class);
                intent.putExtra("teachClassId", clazz.getId() + "");

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("clazz", clazz);
//                intent.putExtras(bundle);

                startActivity(intent);
                finish();

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
                if (clazz.getTypeValue().equals(type.getValue())) {
                    radioButton.setChecked(true);
                } else {
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
    private void asyncUpdateData(String clazzName, String typeValue) {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_CLAZZ_INFO;
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(clazz.getId()));
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
            setResult(ResultCode.RESULT_CODE_FAIL.getCode());
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("更新成功");
            //成功
            Intent intent = new Intent();
            intent.putExtra("edit_location", listLocation);
            intent.putExtra("clazz", clazz);
            setResult(ResultCode.RESULT_CODE_SUCCESS.getCode(), intent);
            finish();
        }
    };

    /**
     * 请求退出
     */
    private void asyncExitClass() {
        String wholeUrl = AppUrl.host + AppUrl.EXIT_CLAZZ;
        Map<String, Object> map = new HashMap<>();
        map.put("gClassId", String.valueOf(clazz.getId()));
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, eListeener);
    }

    BaseRequestListener eListeener = new JsonRequestListener() {

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
            Toast.makeText(mContext, "退出成功", Toast.LENGTH_LONG).show();
            //回到班级管理首页，并刷新页面
            Intent intent = new Intent();
            intent.putExtra("delete_location", listLocation);
            setResult(ResultCode.RESULT_CODE_SUCCESS.getCode(), intent);
            finish();
        }
    };

}
