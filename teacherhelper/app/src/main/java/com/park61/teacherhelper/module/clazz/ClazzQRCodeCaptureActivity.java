package com.park61.teacherhelper.module.clazz;

import android.app.Activity;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;

/**
 * Created by zhangchi on 2017/8/22.
 */

public class ClazzQRCodeCaptureActivity extends BaseActivity {
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_qr_capture);
    }

    @Override
    public void initView() {
        setPagTitle("扫描二维码");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
