package com.park61.teacherhelper;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.Double11ActDetailsActivity;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.HomeModularListActivity;
import com.park61.teacherhelper.module.home.ServiceApplyActivity;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.my.MyMainActivity;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;
import com.park61.teacherhelper.module.workplan.WorkPlanMainWholeActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 * create by super
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements OnCheckedChangeListener {
    private Context mContext;
    private TabHost tabHost;
    private RadioButton radio_button_main;
    //    private RadioButton radio_button_home;
    private RadioButton radio_button_grow;
    private RadioButton radio_button_me;

    private Intent mainIntent; // 首页
    //    private Intent homeIntent; // 首页
    private Intent growIntent;// 成长
    private Intent meIntent;// 我的
    private long _firstTime = 0;
    private boolean isHomeNeedRefresh = false; // 首页是否处于需要刷新的状态

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tab_layout);
       /* // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        //从广告页进入
        boolean isAdClickIn = getIntent().getBooleanExtra("isAdClickIn", false);
        boolean isH5ClickIn = getIntent().getBooleanExtra("isH5ClickIn", false);
        boolean isPushIn = getIntent().getBooleanExtra("isPushIn", false);
        intView();
        setupIntent();
        if (isAdClickIn) {
            CmsItem bi = (CmsItem) getIntent().getSerializableExtra("CmsItem");
            ViewInitTool.judgeGoWhere(bi, mContext);
        }
        //从h5打开app
        if (isH5ClickIn) {
            String host = getIntent().getStringExtra("host");
            if ("open61teach".equals(host)) {
                String coursePlanId = getIntent().getStringExtra("id");
                if (TextUtils.isEmpty(coursePlanId) || "null".equals(coursePlanId))
                    return;
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                intent.putExtra("coursePlanId", FU.paseInt(coursePlanId));
                startActivity(intent);
            } else if ("openCourse".equals(host)) {
                String courseId = getIntent().getStringExtra("id");
                if (TextUtils.isEmpty(courseId) || "null".equals(courseId))
                    return;
                Intent intent = new Intent(mContext, ExpertCourseListActivity.class);
                intent.putExtra("courseId", FU.paseInt(courseId));
                startActivity(intent);
            }
        }
        if (isPushIn) {//从推送点入
            String webUrl = getIntent().getStringExtra("WEB_VIEW_URL");
            Uri mUri = Uri.parse(webUrl);
            if ("http".equals(mUri.getScheme()) || "https".equals(mUri.getScheme())) {
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", webUrl);
                startActivity(intent);
            } else if ("teach61".equals(mUri.getScheme())) {
                if ("serviceRights".equals(mUri.getHost())) {//服务权益
                    startActivity(new Intent(mContext, ServiceApplyActivity.class));
                } else if ("Double11ActDetail".equals(mUri.getHost())) {//双11活动详情
                    Intent it = new Intent(mContext, Double11ActDetailsActivity.class);
                    it.putExtra("activityId", FU.paseInt(mUri.getQueryParameter("activityId")));
                    startActivity(it);
                } else if ("open61teach".equals(mUri.getHost())) {
                    String coursePlanId = mUri.getQueryParameter("id");
                    Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                    intent.putExtra("coursePlanId", FU.paseInt(coursePlanId));
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销-其他页面要改变当前tab页的广播
        mContext.unregisterReceiver(tabChangeReceiver);
    }

    private void intView() {
        mContext = this;
        // 注册-其他页面要改变当前tab页的广播
        registerTabChangeReceiver();
        radio_button_main = (RadioButton) findViewById(R.id.radio_button_main);
//        radio_button_home = (RadioButton) findViewById(R.id.radio_button_home);
        radio_button_grow = (RadioButton) findViewById(R.id.radio_button_grow);
        radio_button_me = (RadioButton) findViewById(R.id.radio_button_me);

        mainIntent = new Intent(this, HomeModularListActivity.class);
//        homeIntent = new Intent(this, FuWuBaoDianActivity.class);
//        growIntent = new Intent(this, WorkManageMainActivity.class);
        growIntent = new Intent(this, WorkPlanMainWholeActivity.class);
        meIntent = new Intent(this, MyMainActivity.class);
        radio_button_main.setOnCheckedChangeListener(this);
//        radio_button_home.setOnCheckedChangeListener(this);
        radio_button_grow.setOnCheckedChangeListener(this);
        radio_button_me.setOnCheckedChangeListener(this);

        findViewById(R.id.button_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent it = new Intent(mContext, FuWuBaoDianActivity.class);
                it.putExtra("PAGE_TITLE", "服务宝典");
                it.putExtra("url", "http://m.61park.cn/teach/#/servicedict/index/309");
                startActivity(it);*/
                Intent it = new Intent(mContext, FuWuBaoDianNativeActivity.class);
                it.putExtra("PAGE_TITLE", "服务宝典");
                it.putExtra("pageId", "309");
                startActivity(it);
            }
        });
        //刷新首页
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent("ACTION_REFRESH_HOME_DATA"));
                //状态置为不需要刷新
                isHomeNeedRefresh = false;
                //隐藏刷新按钮
                findViewById(R.id.bottom_bar).setVisibility(View.GONE);
            }
        });
    }

    private void setupIntent() {
        this.tabHost = getTabHost();
        setupSettingTab("首页", "tab_main", mainIntent);
//        setupSettingTab("课程", "tab_class", homeIntent);
        setupSettingTab("发现", "tab_find", growIntent);
        setupSettingTab("我的", "tab_me", meIntent);
    }

    private void setupSettingTab(String str, String tag, Intent intent) {
        tabHost.addTab(tabHost.newTabSpec(tag).setIndicator(getView(str)).setContent(intent));
    }

    private View getView(String str) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null);
        TextView tab_item_name = (TextView) view.findViewById(R.id.tab_item_name);
        tab_item_name.setText(str);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_button_main:
                    tabHost.setCurrentTabByTag("tab_main");
                    if (isHomeNeedRefresh) {
                        //显示刷新按钮
                        findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
                    }
                    break;
               /* case R.id.radio_button_home:
                    tabHost.setCurrentTabByTag("tab_class");
                    break;*/
                case R.id.radio_button_grow:
                    tabHost.setCurrentTabByTag("tab_find");
                    //隐藏刷新按钮
                    findViewById(R.id.bottom_bar).setVisibility(View.GONE);
                    break;
                case R.id.radio_button_me:
                    if (GlobalParam.userToken == null) {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        Intent changeIt = new Intent();
                        changeIt.setAction("ACTION_TAB_CHANGE");
                        changeIt.putExtra("TAB_NAME", "tab_main");
                        sendBroadcast(changeIt);
                    } else {
                        tabHost.setCurrentTabByTag("tab_me");
                        //隐藏刷新按钮
                        findViewById(R.id.bottom_bar).setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 其他页面要改变当前tab页的广播
     */
    private void registerTabChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_TAB_CHANGE");
        mContext.registerReceiver(tabChangeReceiver, filter);
    }

    private BroadcastReceiver tabChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收到广播，改变当前tab页
            String tab_name = intent.getStringExtra("TAB_NAME");
            if (tab_name != null && !tab_name.equals("")) {
                if ("tab_home".equals(tab_name)) {
//                    radio_button_home.setChecked(true);
                } else if ("tab_grow".equals(tab_name)) {
                    radio_button_grow.setChecked(true);
                } else if ("tab_me".equals(tab_name)) {
                    radio_button_me.setChecked(true);
                } else if ("tab_main".equals(tab_name)) {
                    radio_button_main.setChecked(true);
                } else if ("tab_refresh".equals(tab_name)) {
                    //状态置为需要刷新
                    isHomeNeedRefresh = true;
                    //显示刷新按钮
                    findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
                }
            }
        }
    };

    /**
     * 两次退出程序
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - _firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(mContext, "再按一次退出程序...", Toast.LENGTH_SHORT).show();
                _firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                MobclickAgent.onKillProcess(mContext);
                ExitAppUtils.getInstance().exit();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
