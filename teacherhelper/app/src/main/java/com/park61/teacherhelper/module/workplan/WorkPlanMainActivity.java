package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.park61.teacherhelper.module.workplan.fragment.MyDoneWorkFragment;
import com.park61.teacherhelper.module.workplan.fragment.MyOutTimeWorkFragment;
import com.park61.teacherhelper.module.workplan.fragment.MyToDoWorkFragment;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行事历主页
 * Created by shubei on 2018/4/26.
 */

public class WorkPlanMainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    public static class WorkState {
        public static final int TODO = 0;//待办111
        public static final int DONE = 1;//已完成
    }

    private View choose_line, red_dot;
    private ViewPager pager;
    private RadioGroup rg;

    private int cur_index = 0;//当前所选项
    private final int[] BUTION_IDS = {R.id.use_tv, R.id.used_tv, R.id.past_tv};
    private List<BaseFragment> fragmentList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workplan_main2);
    }

    public void initView() {
        setPagTitle("行事历");
        registerRefreshReceiver();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        choose_line = findViewById(R.id.choose_line);
        choose_line.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels / BUTION_IDS.length, (int) (4 * dm.density)));
        pager = (ViewPager) findViewById(R.id.viewPager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new MyToDoWorkFragment());
        fragmentList.add(new MyDoneWorkFragment());
        fragmentList.add(new MyOutTimeWorkFragment());
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        rg = (RadioGroup) findViewById(R.id.rg);
        red_dot = findViewById(R.id.red_dot);
        //pager.setCurrentItem(0, false);
    }

    @Override
    public void initData() {
        cur_index = getIntent().getIntExtra("taskStatus", 0);//接收传过来的任务状态，默认显示第一个tab--待办
        pager.setCurrentItem(cur_index, false);
        switch (cur_index) {
            case 0:
                ((RadioButton) this.findViewById(R.id.use_tv)).setChecked(true);
                break;
            case 1:
                ((RadioButton) this.findViewById(R.id.used_tv)).setChecked(true);
                break;
            case 2:
                ((RadioButton) this.findViewById(R.id.past_tv)).setChecked(true);
                break;
        }
        asyncGetTaskCount();
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.use_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.used_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.past_tv)).setOnCheckedChangeListener(this);
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
            return fragmentList.get(position);
        }
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
                case R.id.past_tv://点击逾期关闭红点
                    red_dot.setVisibility(View.GONE);
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) choose_line.getLayoutParams();
        lp.leftMargin = (int) (radioButton.getLeft() + offset);
        choose_line.setLayoutParams(lp);
    }


    /**
     * 获取任务数量
     */
    public void asyncGetTaskCount() {
        String wholeUrl = AppUrl.host + AppUrl.getUserAllTaskCount;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ((RadioButton) rg.getChildAt(0)).setText("待办 " + (jsonResult.optInt("unfinished") == 0 ? "" : "" + jsonResult.optInt("unfinished")));
            ((RadioButton) rg.getChildAt(1)).setText("已完成 " + (jsonResult.optInt("finished") == 0 ? "" : "" + jsonResult.optInt("finished")));
            ((RadioButton) rg.getChildAt(2)).setText("逾期 " + (jsonResult.optInt("delayed") == 0 ? "" : "" + jsonResult.optInt("delayed")));

            if (jsonResult.optInt("delayed") > 0) {//逾期红点
                red_dot.setVisibility(View.GONE);
            } else {
                red_dot.setVisibility(View.GONE);
            }
        }
    };

   /* *//**
     * 用户是否幼儿园管理员
     *//*
    public void asyncUserIsKindergartenAdmin() {
        String wholeUrl = AppUrl.host + AppUrl.userIsKindergartenAdmin;//"http://10.10.10.18:8380/service/teachMember/userIsKindergartenAdmin";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, ulistener);
    }

    BaseRequestListener ulistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            if ("0".equals(jsonResult.optString("data"))) {//是否管理员：0不是，1是
                area_right.setVisibility(View.INVISIBLE);
                area_right.setOnClickListener(null);
            } else {
                area_right.setVisibility(View.VISIBLE);
                area_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, ManageTaskActivity.class));
                    }
                });
            }
        }
    };*/

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_WORK_NUM");
        registerReceiver(workChangenNumReceiver, filter);
    }

    private BroadcastReceiver workChangenNumReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncGetTaskCount();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(workChangenNumReceiver);
    }
}
