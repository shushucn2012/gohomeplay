package com.park61.teacherhelper.module.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.ActivityPublishLibBinding;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlie on 2018/4/9.
 *
 * 发布活动库的活动
 */

public class PublishLibActivity extends BaseActivity {

    ActivityPublishLibBinding binding;
    TimePickerView pvTime;
    Date dSignStart,dSignEnd,dActivityStart,dActivityEnd;
    boolean goNext = false;
    String notice;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publish_lib);
    }

    @Override
    public void initView() {
        binding.setGoNext(goNext);
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                switch (v.getId()){
                    case R.id.sign_start:
                        //报名开始时间需要小于结束时间
                        if(dSignEnd != null && dSignEnd.before(date)){
                            showShortToast("报名开始时间需要小于结束时间");
                        }else{
                            dSignStart = date;
                            binding.signStart.setText(getTime(date));
                        }
                        break;
                    case R.id.sign_end:
                        if(dSignStart != null && dSignStart.after(date)){
                            showShortToast("报名结束时间需要大于开始时间");
                        }else{
                            dSignEnd = date;
                            binding.signEnd.setText(getTime(date));
                        }
                        break;
                    case R.id.activity_start:
                        //活动开始时间大于报名开始时间
                        if(dSignStart == null){
                            showShortToast("请先选择报名开始时间");
                        }else{
                            //活动开始时间大于报名开始时间，小于活动结束时间
                            if(date.before(dSignStart)){
                                showShortToast("活动开始时间需要大于活动报名时间");
                            }else if(dActivityEnd != null && dActivityEnd.before(date)){
                                showShortToast("活动开始时间需要小于结束时间");
                            }else{
                                dActivityStart = date;
                                binding.activityStart.setText(getTime(date));
                            }
                        }
                        break;
                    case R.id.activity_end:
                        if(dSignStart == null){
                            showShortToast("请先选择报名开始时间");
                        }else{
                            //活动开始时间大于报名开始时间，小于活动结束时间
                            if(date.before(dSignStart)){
                                showShortToast("活动结束时间需要大于活动报名时间");
                            }else if(dActivityStart != null && dActivityStart.after(date)){
                                showShortToast("活动结束时间需要大于开始时间");
                            }else{
                                dActivityEnd = date;
                                binding.activityEnd.setText(getTime(date));
                            }
                        }
                        break;
                }
                checkNextState();
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

            }
        }).setType(new boolean[]{true, true, true, true, true, false}).build();
    }

    @Override
    public void initData() {
        setPagTitle("发布活动");
        binding.head.areaRight.setVisibility(View.GONE);

        IntentFilter it = new IntentFilter();
        it.addAction(PublishSuccessActivity.PUBLISH_SUCCESS);
        registerReceiver(receiver, it);

        asyncLibDetail();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(PublishSuccessActivity.PUBLISH_SUCCESS.equalsIgnoreCase(intent.getAction())){
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void asyncLibDetail(){
        String url = AppUrl.host + AppUrl.myLibDetail;
        Map<String,Object> map = new HashMap<>();
        map.put("partyTemplateId", getIntent().getIntExtra("id", -1));
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String name = jsonResult.optString("name");
            notice = jsonResult.optString("notice");
            if(!TextUtils.isEmpty(name)){
                if(name.length()>30) name = name.substring(0,30);
                binding.title.setText(name);
                binding.title.setSelection(name.length());
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    @Override
    public void initListener() {
        binding.signStart.setOnClickListener(v -> pvTime.show(binding.signStart));
        binding.signEnd.setOnClickListener(v -> pvTime.show(binding.signEnd));
        binding.activityStart.setOnClickListener(v -> pvTime.show(binding.activityStart));
        binding.activityEnd.setOnClickListener(v -> pvTime.show(binding.activityEnd));

        binding.setNextClick(v -> {
            if(goNext){
                //去选择班级界面
                Intent it = new Intent(mContext, ChooseClassActivity.class);
                //活动库id
                it.putExtra("id", getIntent().getIntExtra("id", -1));
                it.putExtra("title", binding.title.getText().toString());
                it.putExtra("notice", notice);
                it.putExtra("signStart", binding.signStart.getText());
                it.putExtra("signEnd", binding.signEnd.getText());
                it.putExtra("activityStart", binding.activityStart.getText());
                it.putExtra("activityEnd", binding.activityEnd.getText());
                startActivity(it);
            }
        });

        binding.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.titleNum.setText(s.toString().length() +"/30");
                checkNextState();
            }
        });

    }

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(date);
    }

    private void checkNextState(){
        if(TextUtils.isEmpty(binding.title.getText().toString()) ||TextUtils.isEmpty(binding.activityStart.getText().toString())
                || TextUtils.isEmpty(binding.activityEnd.getText().toString()) ||TextUtils.isEmpty(binding.signEnd.getText().toString())
                ||TextUtils.isEmpty(binding.signStart.getText().toString())){
            goNext = false;
        }else{
            goNext = true;

        }
        binding.setGoNext(goNext);
    }
}
