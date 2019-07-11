package com.park61.teacherhelper.module.home;

import android.databinding.DataBindingUtil;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.databinding.ActivityScanSuccessBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenlie on 2018/1/10.
 */

public class ParentalSignActivity extends BaseActivity {

    ActivityScanSuccessBinding binding;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_success);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String json = getIntent().getStringExtra("signData");

        try {
            JSONObject j = new JSONObject(json);
            setData(j);
        } catch (JSONException e) {

        }
    }

    /**
     * "applyDate": "2018-01-02 16:43",
     * "childName": "小张",
     * "className": "提姆幼儿园哈弗神童小班",
     * "coverUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20171016183207850_390.jpg?x-oss-process=style/compress_nologo",
     * "qrCodeUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20180102164500924_237.jpg?x-oss-process=style/compress_nologo",
     * "schoolName": "测试1228",
     * "signDate": "2018-01-03 15:19",
     * "startDate": "2017-12-28 10:21",
     * "status": 1,   （0：报名中，1：进行中，2：已结束）
     * "title": "亲子游活动",
     * "userName": "张三",
     * "userTel": "13911111111"
     */
    private void setData(JSONObject j) {

        int state = j.optInt("status");
        if(state == 1){
            binding.setHeadState("已签到");
        }else if(state == 2){
            binding.setHeadState("活动已结束");
        }

        binding.contactName.setText(j.optString("userName"));
        binding.contactPhone.setText(j.optString("userTel"));
        ImageManager.getInstance().displayImg(binding.activityImg, j.optString("coverUrl"));
        binding.activityTitle.setText(j.optString("title"));
        binding.activitySite.setText(j.optString("schoolName"));
        binding.activityTime.setText(j.optString("startDate"));

        binding.activitySignupTime.setText(j.optString("applyDate"));
        binding.activitySigninTime.setText(j.optString("signDate"));
    }

    @Override
    public void initListener() {
        binding.setGoBack(v-> finish());
    }
}
