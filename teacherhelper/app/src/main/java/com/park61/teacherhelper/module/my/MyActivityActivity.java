package com.park61.teacherhelper.module.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityMyActivityBinding;
import com.park61.teacherhelper.module.my.fragment.FragmentMyActivity;
import com.park61.teacherhelper.module.my.fragment.FragmentMyLib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class MyActivityActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ActivityMyActivityBinding binding;
    List<BaseFragment> fragments;
    FragmentMyActivity fragmentMyActivity;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_activity);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        fragments.add(fragmentMyActivity = new FragmentMyActivity());
        fragments.add(new FragmentMyLib());
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        binding.fragmentPage.setAdapter(adapter);
        binding.fragmentPage.addOnPageChangeListener(this);

        IntentFilter it = new IntentFilter();
        it.addAction(PublishSuccessActivity.PUBLISH_SUCCESS);
        registerReceiver(receiver, it);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(PublishSuccessActivity.PUBLISH_SUCCESS.equalsIgnoreCase(intent.getAction())){
                binding.fragmentPage.setCurrentItem(0);
                fragmentMyActivity.refresh();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 更改tab
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            binding.leftTitle.setTextColor(Color.parseColor("#ffffff"));
            binding.leftTitle.setBackgroundResource(R.drawable.shape_corner_left_y);
            binding.rightTitle.setBackgroundResource(R.drawable.shape_corner_right);
            binding.rightTitle.setTextColor(Color.parseColor("#ff5a80"));
        }else{
            binding.rightTitle.setTextColor(Color.parseColor("#ffffff"));
            binding.rightTitle.setBackgroundResource(R.drawable.shape_corner_right_y);
            binding.leftTitle.setBackgroundResource(R.drawable.shape_corner_left);
            binding.leftTitle.setTextColor(Color.parseColor("#ff5a80"));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyAdapter extends FragmentPagerAdapter{

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

    @Override
    public void initListener() {
        binding.setGoBack(v -> finish());

        binding.leftTitle.setOnClickListener(v -> binding.fragmentPage.setCurrentItem(0));
        binding.rightTitle.setOnClickListener(v -> binding.fragmentPage.setCurrentItem(1));
    }

}
