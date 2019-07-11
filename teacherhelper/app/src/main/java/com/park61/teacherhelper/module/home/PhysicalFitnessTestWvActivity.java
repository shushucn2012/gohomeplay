package com.park61.teacherhelper.module.home;

import android.view.View;

import com.park61.teacherhelper.CanBackWebViewActivity;


/**
 * 悦来接入
 *
 * @author shubei
 * @time 2019/3/13 15:26
 */
public class PhysicalFitnessTestWvActivity extends CanBackWebViewActivity {

    public void initListener() {
        super.initListener();
        area_right.setVisibility(View.INVISIBLE);
        area_right.setOnClickListener(null);
    }

    protected void setShareAndNowTitle(String mtitle) {
        logout("====================mtitle====================" + mtitle);
        //DO NOTHING
    }


}
