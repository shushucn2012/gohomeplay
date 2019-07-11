package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.workplan.adapter.CalendarPlanPagerAdapter;
import com.park61.teacherhelper.module.workplan.adapter.MCPageChangeListener;
import com.park61.teacherhelper.widget.pw.CalandarMainTipPopWin;
import com.park61.teacherhelper.widget.pw.TaskChooseLevelPopWin;
import com.park61.teacherhelper.widget.viewpager.MyPagerSlidingTabStrip;

import java.util.Date;

/**
 * 日历计划总览页面
 * Created by shubei on 2018/7/31.
 */

public class CalendarPlanMainActivity extends BaseActivity {

    private MyPagerSlidingTabStrip tabs;
    private ViewPager pager;
    private View click_tv;
    private TimePickerView pvTime;
    private TextView tv_date_top;

    private int taskCalendarClassId;//班级模板id
    /*private int mCurrentPagePosition;
    private boolean mIsChanged;
    private final static int POINT_LENGTH = 12;
    private final static int FIRST_ITEM_INDEX = 1;*/

    public String curYear = "2018"; //当前所选时间年份，默认系统时间年份

    public void setCurYearAndTextView(String year) {
        curYear = year;
        tv_date_top.setText(curYear + "年");
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_calendar_plan_main);
    }

    @Override
    public void initView() {
        tabs = (MyPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        click_tv = findViewById(R.id.click_tv);
        tv_date_top = (TextView) findViewById(R.id.tv_date_top);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent("ACTION_PARENT_ACTIVITY_RESUME"));//发送广播，通知当前显示的fragment刷新数据
    }

    @Override
    public void initData() {
        curYear = DateTool.getSysYear();//默认系统时间年份
        tv_date_top.setText(curYear + "年");

        area_right.setVisibility(View.INVISIBLE);
        area_left.setVisibility(View.VISIBLE);
        taskCalendarClassId = getIntent().getIntExtra("taskCalendarClassId", -1);
        pager.setAdapter(new CalendarPlanPagerAdapter(getSupportFragmentManager(), taskCalendarClassId));
        tabs.setViewPager(pager);
        tabs.setParentActivity(CalendarPlanMainActivity.this);
        tabs.setTextSize(DevAttr.dip2px(mContext, 16));
        tabs.setTextColor(getResources().getColor(R.color.gc9bec2));
        tabs.setSelectedTextColor(getResources().getColor(R.color.g333333));
        tabs.setSelectedTextSize(DevAttr.dip2px(mContext, 17));
        pager.setOffscreenPageLimit(1);
        pager.setCurrentItem(new Date().getMonth() + 1, false);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //改变日历控件
                curYear = String.valueOf(date.getYear() + 1900);
                tv_date_top.setText(curYear + "年");
                if (date.getMonth() + 1 == pager.getCurrentItem()) {//如果相等，代表显示的page不会切换不会重新initData去取数据，只能发广播让他重拿
                    sendBroadcast(new Intent("ACTION_PARENT_ACTIVITY_RESUME"));//发送广播，通知当前显示的fragment刷新数据
                } else {//如果不等，可以切换数据
                    pager.setCurrentItem(date.getMonth() + 1, false);
                }
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
    }

    @Override
    public void initListener() {
        click_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出日期选择
                pvTime.show(click_tv);
            }
        });
        pager.addOnPageChangeListener(new MCPageChangeListener(pager, CalendarPlanMainActivity.this));
    }

    public void showTips(){
        // 幼儿园用户首页，判断是否是第一次进入，给出引导提示
        SharedPreferences isFirstRunSp = getSharedPreferences("isFirstWmmFlag", Activity.MODE_PRIVATE);
        boolean isFirstRun = isFirstRunSp.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            CalandarMainTipPopWin mCalandarMainTipPopWin = new CalandarMainTipPopWin(mContext);
            mCalandarMainTipPopWin.showAsDropDown(findViewById(R.id.top_line));
            SharedPreferences sp = getSharedPreferences("isFirstWmmFlag", Activity.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sp.edit();
            mEditor.putBoolean("isFirstRun", false);
            mEditor.commit();
        }
    }

}
