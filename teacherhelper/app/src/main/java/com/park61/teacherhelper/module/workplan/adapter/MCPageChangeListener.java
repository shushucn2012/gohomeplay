package com.park61.teacherhelper.module.workplan.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.module.workplan.CalendarPlanMainActivity;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;

/**
 * Created by shubei on 2018/8/10.
 */

public class MCPageChangeListener implements ViewPager.OnPageChangeListener {

    private int mCurrentPagePosition;
    private boolean mIsChanged;
    private final static int POINT_LENGTH = 12;
    private final static int FIRST_ITEM_INDEX = 1;
    private ViewPager pager;
    private Activity parentActivity;

    public MCPageChangeListener(ViewPager pager, Activity parentActivity) {
        this.pager = pager;
        this.parentActivity = parentActivity;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.out("================position===================" + position);
        if (position == 13) {
            if (parentActivity instanceof CalendarPlanMainActivity)
                ((CalendarPlanMainActivity) parentActivity).setCurYearAndTextView(Integer.parseInt(((CalendarPlanMainActivity) parentActivity).curYear) + 1 + "");
            else if (parentActivity instanceof WorkManageMainActivity)
                ((WorkManageMainActivity) parentActivity).setCurYearAndTextView(Integer.parseInt(((WorkManageMainActivity) parentActivity).curYear) + 1 + "");
            pager.setCurrentItem(1, false);
        } else if (position == 0) {
            if (parentActivity instanceof CalendarPlanMainActivity)
                ((CalendarPlanMainActivity) parentActivity).setCurYearAndTextView(Integer.parseInt(((CalendarPlanMainActivity) parentActivity).curYear) - 1 + "");
            else if (parentActivity instanceof WorkManageMainActivity)
                ((WorkManageMainActivity) parentActivity).setCurYearAndTextView(Integer.parseInt(((WorkManageMainActivity) parentActivity).curYear) - 1 + "");
            pager.setCurrentItem(12, false);
        } else {
            pager.setCurrentItem(position, false);
        }

       /* mIsChanged = true;
        Log.out("================position===================" + position);
        Log.out("================mCurrentPagePosition===================1" + mCurrentPagePosition);
        if (position > POINT_LENGTH) {// 末位之后，跳转到首位（1）
            mCurrentPagePosition = FIRST_ITEM_INDEX;
            *//*if (position == 13)
                mCurrentPagePosition = 2;*//*
        } else if (position < FIRST_ITEM_INDEX) {// 首位之前，跳转到末尾（N）
            mCurrentPagePosition = POINT_LENGTH;
            *//*if (position == 0)
                mCurrentPagePosition = 11;*//*
        } else {
            mCurrentPagePosition = position;
        }
        Log.out("================mCurrentPagePosition===================2" + mCurrentPagePosition);*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {
       /* if (ViewPager.SCROLL_STATE_IDLE == state) {
            if (mIsChanged) {
                mIsChanged = false;
                pager.setCurrentItem(mCurrentPagePosition, false);
            }
        }*/
    }
}
