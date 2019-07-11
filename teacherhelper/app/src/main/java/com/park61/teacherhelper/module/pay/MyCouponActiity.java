package com.park61.teacherhelper.module.pay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.pay.fragment.MyCouponPaperFragment;

public class MyCouponActiity extends BaseActivity implements OnCheckedChangeListener, OnPageChangeListener {

    private View choose_line;
    private ViewPager pager;

    private int cur_index = 0;//当前所选项
    private final int[] BUTION_IDS = {R.id.use_tv, R.id.used_tv, R.id.past_tv};

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mycoupon);
    }

    @Override
    public void initView() {
        setPagTitle("优惠券");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        choose_line = findViewById(R.id.choose_line);
        choose_line.setLayoutParams(new LayoutParams(dm.widthPixels / BUTION_IDS.length, (int) (4 * dm.density)));
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
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
            MyCouponPaperFragment fragment = new MyCouponPaperFragment();
            Bundle data = new Bundle();
            data.putInt("useType", position);
            fragment.setArguments(data);
            return fragment;
        }
    }

    @Override
    public void initData() {
        pager.setCurrentItem(cur_index, false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.use_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.used_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.past_tv)).setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.use_tv:
                    changeTabList(0);
                    break;
                case R.id.used_tv:
                    changeTabList(1);
                    break;
                case R.id.past_tv:
                    changeTabList(2);
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
        LayoutParams lp = (LayoutParams) choose_line.getLayoutParams();
        lp.leftMargin = (int) (radioButton.getLeft() + offset);
        choose_line.setLayoutParams(lp);
    }

    public int getCurPaperIndex() {
        return this.cur_index;
    }
}
