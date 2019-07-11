package com.park61.teacherhelper.module.workplan.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.park61.teacherhelper.module.workplan.fragment.FragmentCalendarPlan;
import com.park61.teacherhelper.module.workplan.fragment.FragmentCalendarPlan2;

import java.util.Arrays;
import java.util.List;

/**
 * 个人主页日历计划页面viewpager adapter
 * Created by shubei on 2018/8/3.
 */

public class CalendarPlanPagerAdapter extends FragmentPagerAdapter {

    private List<String> monthList = Arrays.asList("十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", "一月");
    private int taskCalendarClassId;

    public CalendarPlanPagerAdapter(FragmentManager fm, int taskCalendarClassId) {
        super(fm);
        this.taskCalendarClassId = taskCalendarClassId;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return monthList.get(position);
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        FragmentCalendarPlan2 fragmentCalendarPlan = new FragmentCalendarPlan2();
        Bundle data = new Bundle();
        data.putInt("index", position > 0 ? position - 1 : position);
        data.putInt("taskCalendarClassId", taskCalendarClassId);
        fragmentCalendarPlan.setArguments(data);
        return fragmentCalendarPlan;
    }
}
