package com.park61.teacherhelper.module.pay;

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

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.pay.fragment.CouponChooseFragment;


public class CouponChooseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private View choose_line;
    private ViewPager pager;

    private int cur_index = 0;//当前所选项
    private final int[] BUTION_IDS = {R.id.use_tv, R.id.unuse_tv};
    private int courseSeriesId;
    private int chosenCouponId;
    private int orderType;//订单类型
    private double price;//订单金额

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_coupon_choose);
    }

    @Override
    public void initView() {
        setPagTitle("使用优惠券");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        choose_line = findViewById(R.id.choose_line);
        choose_line.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels / BUTION_IDS.length, (int) (4 * dm.density)));
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
            CouponChooseFragment fragment = new CouponChooseFragment();
            Bundle data = new Bundle();
            data.putInt("useType", position);
            data.putInt("chosenCouponId", chosenCouponId);
            data.putInt("courseId", courseSeriesId);
            data.putInt("orderType", orderType);
            data.putDouble("price", price);
            fragment.setArguments(data);
            return fragment;
        }
    }

    @Override
    public void initData() {
        chosenCouponId = getIntent().getIntExtra("chosenCouponId", -1);
        courseSeriesId = getIntent().getIntExtra("courseId", -1);
        orderType = getIntent().getIntExtra("orderType", -1);
        price = getIntent().getDoubleExtra("price", -1);
        pager.setCurrentItem(cur_index, false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.use_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.unuse_tv)).setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.use_tv:
                    changeTabList(0);
                    break;
                case R.id.unuse_tv:
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

    public void setTopRadionText(int pos, int num) {
        RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[pos]);
        if (pos == 0) {
            radioButton.setText("可用券(" + num + ")");
        } else {
            radioButton.setText("不可用(" + num + ")");
        }
    }
}
