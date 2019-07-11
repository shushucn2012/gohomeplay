package com.park61.teacherhelper.module.member;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.member.fragment.FragmentCurMembers;
import com.park61.teacherhelper.module.member.fragment.FragmentUnAuthMembers;
import com.park61.teacherhelper.widget.viewpager.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员成员列表
 *
 * @author shubei
 * @time 2019/1/19 14:35
 */
public class MemberListActivity extends BaseActivity {

    private NoScrollViewPager viewpager;
    private List<BaseFragment> fragmentList;

    private TextView left_tv, right_tv;
    private ImageView img_reddot;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_member_list);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        viewpager = findViewById(R.id.viewpager);
        left_tv = findViewById(R.id.left_tv);
        right_tv = findViewById(R.id.right_tv);
        img_reddot = findViewById(R.id.img_reddot);

        fragmentList = new ArrayList<>();
        FragmentCurMembers fragmentCurMembers = new FragmentCurMembers();
        FragmentUnAuthMembers fragmentUnAuthMembers = new FragmentUnAuthMembers();
        fragmentList.add(fragmentCurMembers);
        fragmentList.add(fragmentUnAuthMembers);

        viewpager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);
    }

    @Override
    public void initData() {
        if (GlobalParam.isGroupManager == false) {//非管理员只能看到成员列表
            findViewById(R.id.area_two_tabs).setVisibility(View.GONE);
            findViewById(R.id.tv_one_title).setVisibility(View.VISIBLE);
            viewpager.setScroll(false);
            findViewById(R.id.area_right).setVisibility(View.GONE);
       }
    }

    @Override
    public void initListener() {
        left_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(0);
            }
        });
        right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
            }
        });
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareInfoBean shareInfoBean = new ShareInfoBean();
                StringBuilder sb = new StringBuilder();
                sb.append("http://m.61park.cn/teach/#/member/invite/");
                sb.append(android.net.Uri.encode(GlobalParam.currentUser.getName()));//为了避免空格变“+”的问题
                sb.append("/");
                sb.append(GlobalParam.currentUser.getGroupId());
                shareInfoBean.setShareUrl(sb.toString());
                shareInfoBean.setPicUrl(AppUrl.SHARE_APP_ICON);
                shareInfoBean.setTitle("61学院会员免费认证，快来加入吧");
                shareInfoBean.setDescription("通过我的邀请，即可快速成为会员哦");
                showShareDialog(shareInfoBean);
            }
        });
    }

    /**
     * 切换标签页
     */
    private void changeTab(int position) {
        if (position == 0) {
            left_tv.setTextColor(Color.parseColor("#ffffff"));
            left_tv.setBackgroundResource(R.drawable.shape_corner_left_y);
            right_tv.setBackgroundResource(R.drawable.shape_corner_right);
            right_tv.setTextColor(Color.parseColor("#ff5a80"));
        } else {
            right_tv.setTextColor(Color.parseColor("#ffffff"));
            right_tv.setBackgroundResource(R.drawable.shape_corner_right_y);
            left_tv.setBackgroundResource(R.drawable.shape_corner_left);
            left_tv.setTextColor(Color.parseColor("#ff5a80"));
        }
    }

    /**
     * viewpager adapter
     */
    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Subscribe
    public void onMessage(MessageEvent event) {
        if ("SHOW_HAS_POINT".equals(event.getMessage())) {
            Log.out("==========onMessage============HIDE_HAS_POINT===============");
            img_reddot.setVisibility(View.VISIBLE);
        } else if ("HIDE_HAS_POINT".equals(event.getMessage())) {
            Log.out("==========onMessage============HIDE_HAS_POINT===============");
            img_reddot.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}