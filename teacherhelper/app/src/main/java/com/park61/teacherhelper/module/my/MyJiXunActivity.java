package com.park61.teacherhelper.module.my;

import android.view.View;

import com.park61.teacherhelper.CanBackWebViewActivity;

/**
 * 集训记录h5
 * Created by shubei on 2018/9/3.
 */
public class MyJiXunActivity extends CanBackWebViewActivity {

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
