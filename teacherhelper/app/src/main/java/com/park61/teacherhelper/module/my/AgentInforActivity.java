package com.park61.teacherhelper.module.my;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityKidGardenBinding;
import com.park61.teacherhelper.module.my.fragment.FragmentChooseArea;
import com.park61.teacherhelper.module.my.fragment.FragmentGetReward;
import com.park61.teacherhelper.module.my.fragment.FragmentNickName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/5/7.
 * <p>
 * 完善信息(代理商)  填写昵称 > 选择区域 > 领取奖励
 */

public class AgentInforActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ActivityKidGardenBinding binding;
    int currentIndex = 0;
    List<BaseFragment> fragments;
    int colorRed, colorGray, colorBlack, userDuty = 3;
    int couponActivityId;
    String nickName;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kid_garden);
    }

    @Override
    public void initView() {
        setPagTitle("完善资料");
        binding.pageName.setText("填写昵称");
        binding.setTitleOne("填写昵称");
        binding.setTitleTwo("选择区域");
        colorRed = Color.parseColor("#fb4364");
        colorGray = Color.parseColor("#aaaaaa");
        colorBlack = Color.parseColor("#333333");
        binding.firstTitle.setTextColor(colorRed);
    }

    @Override
    public void initData() {

        userDuty = getIntent().getIntExtra("userDuty", 3);
        fragments = new ArrayList<>();
        fragments.add(new FragmentNickName());
        fragments.add(new FragmentChooseArea());
        fragments.add(new FragmentGetReward());

        binding.pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        binding.pager.setOffscreenPageLimit(2);
    }

    @Override
    public void initListener() {
        binding.pager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        switch (position) {
            case 0:
                binding.pageName.setText("填写昵称");
                binding.firstTitle.setTextColor(colorRed);
                binding.firstGap.setTextColor(colorGray);
                binding.secondTitle.setTextColor(colorGray);
                binding.secondGap.setTextColor(colorGray);
                binding.thirdTitle.setTextColor(colorGray);
                break;
            case 1:
                binding.pageName.setText("选择区域");
                binding.firstTitle.setTextColor(colorBlack);
                binding.firstGap.setTextColor(colorBlack);
                binding.secondTitle.setTextColor(colorRed);
                binding.secondGap.setTextColor(colorGray);
                binding.thirdTitle.setTextColor(colorGray);
                break;
            case 2:
                binding.pageName.setText("领取奖励");
                binding.firstTitle.setTextColor(colorBlack);
                binding.firstGap.setTextColor(colorBlack);
                binding.secondTitle.setTextColor(colorBlack);
                binding.secondGap.setTextColor(colorBlack);
                binding.thirdTitle.setTextColor(colorRed);
                findViewById(R.id.top_bar).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void goNext(){
        binding.pager.setCurrentItem(currentIndex+1);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserDuty(){
        return userDuty;
    }

    public int getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(int couponActivityId) {
        this.couponActivityId = couponActivityId;
    }
}
