package com.park61.teacherhelper.module.my;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.my.fragment.FragmentPayCourse;
import com.park61.teacherhelper.module.my.fragment.FragmentRecentlyCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的课程
 * Created by shubei on 2018/3/28.
 */

public class MyCourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private int[] BUTION_IDS = {R.id.rb_first, R.id.rb_second};
    private View[] stickArray = new View[2];
    private ViewPager pager;
    private int cur_index = 0;//当前分类

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_course);
    }

    @Override
    public void initView() {
        setPagTitle("我的课程");

        FragmentRecentlyCourse fragmentRecentlyCourse = new FragmentRecentlyCourse();
        fragmentList.add(fragmentRecentlyCourse);

        FragmentPayCourse fragmentPayCourse = new FragmentPayCourse();
        fragmentList.add(fragmentPayCourse);

        BUTION_IDS = new int[]{R.id.rb_first, R.id.rb_second};
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        pager.setCurrentItem(cur_index, false);
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
        ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(MyCourseActivity.this);
        ((RadioButton) findViewById(R.id.rb_second)).setOnCheckedChangeListener(MyCourseActivity.this);
        pager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_first:
                    changeTabList(0);
                    break;
                case R.id.rb_second:
                    changeTabList(1);
                    break;
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
            showStick(tabindex);
        }
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
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
            return fragmentList.get(position);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[position]);
            radioButton.performClick();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
