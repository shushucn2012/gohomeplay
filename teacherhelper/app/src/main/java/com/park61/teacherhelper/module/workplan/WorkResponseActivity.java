package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.pay.MyCouponActiity;
import com.park61.teacherhelper.module.pay.fragment.MyCouponPaperFragment;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.park61.teacherhelper.module.workplan.fragment.FragmentKnowledge;
import com.park61.teacherhelper.module.workplan.fragment.WorkRespFragment;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2018/4/28.
 */

public class WorkResponseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private View choose_line;
    private ViewPager pager;

    private int cur_index = 0;//当前所选项
    private final int[] BUTION_IDS = {R.id.rb_resp, R.id.rb_kg};
    private int taskCalendarId;
    public String isDetailFrom;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_work_resp);
    }

    @Override
    public void initView() {
        setPagTitle("工作反馈");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        choose_line = findViewById(R.id.choose_line);
        choose_line.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels / BUTION_IDS.length, (int) (4 * dm.density)));
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public void initData() {
        isDetailFrom = getIntent().getStringExtra("isDetailFrom");
        taskCalendarId = getIntent().getIntExtra("taskCalendarId", -1);
        pager.setCurrentItem(cur_index, false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.rb_resp)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.rb_kg)).setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(this);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return BUTION_IDS.length;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment;
            if (position == 0) {
                fragment = new WorkRespFragment();
                Bundle data = new Bundle();
                data.putInt("taskCalendarId", taskCalendarId);
                fragment.setArguments(data);
            } else {
                fragment = new FragmentKnowledge();
                Bundle data = new Bundle();
                data.putString("taskId", taskCalendarId + "");
                fragment.setArguments(data);
            }
            return fragment;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_resp:
                    changeTabList(0);
                    break;
                case R.id.rb_kg:
                    changeTabList(1);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
        }
    }

    /**
     * 这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 这个方法会在屏幕滚动过程中不断被调用 positionOffset : 当前页面滑动比例 positionOffsetPixels :
     * 当前页面滑动像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        imageLineChanged(position, positionOffsetPixels / BUTION_IDS.length);
    }

    /**
     * 这个方法有一个参数position，代表哪个页面被选中
     */
    @Override
    public void onPageSelected(int position) {
        RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[position]);
        radioButton.performClick();
    }

    public void imageLineChanged(int paper, int offset) {
        RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[paper]);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) choose_line.getLayoutParams();
        lp.leftMargin = (int) (radioButton.getLeft() + offset);
        choose_line.setLayoutParams(lp);
    }

}
