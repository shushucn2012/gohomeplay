package com.park61.teacherhelper.module.my;

import android.view.View;

import com.park61.teacherhelper.CanBackWebViewActivity;

/**
 * 服务提报记录h5
 * Created by shubei on 2018/9/4.
 */
public class MyTiBaoActivity extends CanBackWebViewActivity {

    /**
     * 重写父类方法
     */
    public void initListener() {
        super.initListener();
        area_close.setVisibility(View.GONE);
        area_right.setVisibility(View.INVISIBLE);
        area_right.setOnClickListener(null);
    }
}
