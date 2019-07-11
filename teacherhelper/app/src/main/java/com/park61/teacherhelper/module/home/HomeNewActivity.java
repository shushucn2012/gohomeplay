package com.park61.teacherhelper.module.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ListDataSave;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.CourseSearchActivity;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.adapter.DateGvAdapter;
import com.park61.teacherhelper.module.home.adapter.ExpertListAdapter;
import com.park61.teacherhelper.module.home.adapter.ShowListAdapter;
import com.park61.teacherhelper.module.home.adapter.TodayRecommentAdapter;
import com.park61.teacherhelper.module.home.bean.AdvertsieBean;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;
import com.park61.teacherhelper.widget.listview.HorizontalListViewV2;
import com.park61.teacherhelper.widget.viewpager.CmsBanner;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * cms首页
 * Created by shubei on 2017/8/14.
 */

public class HomeNewActivity extends BaseActivity {
    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;
    public static final String SP_FILENAME = "park61.teacher";
    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private PullToRefreshScrollView mPullRefreshScrollView;
    private ListView lv_teacher, lv_show;
    private LinearLayout areaSeeMore;
    private CmsBanner mp0, mp1;
    private GridViewForScrollView gv_date;
    private ObservableScrollView scrollView;

    private ShowListAdapter mShowListAdapter;
    private ExpertListAdapter mExpertListAdapter;
    private DateGvAdapter mDateGvAdapter;
    private List<ContentItem> dList;
    private List<ContentItem> sList;
    private List<GoldTeacher> gList;
    private int goldTeacherPageIndex = 0;
    private int guessPageIndex = 0;
    private ListDataSave mListDataSave;
    private HorizontalListViewV2 todayHoriztLv;
    private TodayRecommentAdapter tAdapter;
    List<TeacherCourseBean> todayList = new ArrayList<>();
    private boolean goOthers = false;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_home_new);
    }

    @Override
    public void initView() {
        CommonMethod.getDeviceInfo(this);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        lv_teacher = (ListView) findViewById(R.id.lv_teacher);
        lv_teacher.setFocusable(false);
        gv_date = (GridViewForScrollView) findViewById(R.id.gv_date);
        gv_date.setFocusable(false);
        lv_show = (ListView) findViewById(R.id.lv_show);
        lv_show.setFocusable(false);
        todayHoriztLv = (HorizontalListViewV2) findViewById(R.id.horilistview);
        areaSeeMore = (LinearLayout) findViewById(R.id.area_see_more);
    }

    @Override
    public void initData() {
        AddStatistics(-1, "home");
        goOthers = getIntent().getBooleanExtra("goOthers", false);
        tAdapter = new TodayRecommentAdapter(this, todayList);
        todayHoriztLv.setAdapter(tAdapter);
        asyncGetTeachHomePage();
        asyncGetAndroidStartPage();
    }

    private void asyncGetTeachHomePage() {
        String wholeUrl = AppUrl.host + AppUrl.getTeachHomePage;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, blistener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //首页加载完后，判断是否显示蒙层
        if(!goOthers && getSharedPreferences(SP_FILENAME, MODE_PRIVATE).getBoolean("needShow", true)){
            startActivity(new Intent(mContext, ServerRightTipActivity.class));
        }else{
            goOthers = false;
        }
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.judgeIsShow(HomeNewActivity.this, jsonResult);
            JSONArray dataJay = jsonResult.optJSONArray("modules");
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                if ("cmsTopBanner".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        findViewById(R.id.banner_area).setVisibility(View.VISIBLE);
                        List<CmsItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            bannerlList.add(gson.fromJson(bannerJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ViewPager top_viewpager = (ViewPager) findViewById(R.id.top_viewpager);
                        LinearLayout top_vp_dot = (LinearLayout) findViewById(R.id.top_vp_dot);
                        if (mp0 != null) {
                            /*mp0.clear();
                            mp0.init(bannerlList);*/
                        } else {
                            mp0 = new CmsBanner(mContext, top_viewpager, top_vp_dot, "top");
                            mp0.init(bannerlList);
                        }
                    } else {
                        findViewById(R.id.banner_area).setVisibility(View.GONE);
                    }
                }
                if ("cmsFastGoTo".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONArray("templeteData") != null && tempItemJot.optJSONArray("templeteData").length() > 0) {
                        findViewById(R.id.area_banner2).setVisibility(View.VISIBLE);
                        JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                        final List<CmsItem> quickList = new ArrayList<>();
                        if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                            for (int j = 0; j < dateJay.length(); j++) {
                                quickList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                            }
                        }
                        initFastGo(quickList);
                    } else {
                        findViewById(R.id.area_banner2).setVisibility(View.GONE);
                    }
                }
                if ("cmsMiddleBannerOne".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        findViewById(R.id.area_banner3_out).setVisibility(View.VISIBLE);
                        List<CmsItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            bannerlList.add(gson.fromJson(bannerJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ViewPager top_viewpager = (ViewPager) findViewById(R.id.top_viewpager2);
                        LinearLayout top_vp_dot = (LinearLayout) findViewById(R.id.top_vp_dot2);
                        if (mp1 != null) {
                           /* mp1.clear();
                            mp1.init(bannerlList);*/
                        } else {
                            mp1 = new CmsBanner(mContext, top_viewpager, top_vp_dot, "mid1");
                            mp1.init(bannerlList);
                        }
                    } else {
                        findViewById(R.id.area_banner3_out).setVisibility(View.GONE);
                    }
                }
                if("cmsCourseSeriesRecommend".equals(tempItemJot.optString("templeteCode"))){
                    //今日推荐
                    JSONArray todayRec = tempItemJot.optJSONArray("templeteData");
                    if (todayRec != null && todayRec.length() > 0) {
                        findViewById(R.id.area_today_rc).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.tv_today_rc)).setText(tempItemJot.optString("moduleName"));
                        todayList.clear();
                        for (int j = 0; j < todayRec.length(); j++) {
                            todayList.add(gson.fromJson(todayRec.optJSONObject(j).toString(), TeacherCourseBean.class));
                        }
                        todayHoriztLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                ViewInitTool.judgeGoWhere(todayList.get(position), mContext);
                                Intent it = new Intent(mContext, ExpertCourseListActivity.class);
                                it.putExtra("courseId", todayList.get(position).getId());
                                startActivity(it);
                            }
                        });
                        tAdapter.notifyDataSetChanged();
                    } else {
                        findViewById(R.id.area_today_rc).setVisibility(View.GONE);
                    }
                }
                if ("cmsGoldTeacher".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONObject("templeteData").optJSONArray("rows");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        findViewById(R.id.area_gold_teacher).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.tv_gold_teacher)).setText(tempItemJot.optString("moduleName"));
                        gList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                        }
                        mExpertListAdapter = new ExpertListAdapter(mContext, gList);
                        lv_teacher.setAdapter(mExpertListAdapter);
                    } else {
                        findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
                    }
                }
                if ("cmsMiddleBannerTwo".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray cmsMiddleBannerTwoJay = tempItemJot.optJSONArray("templeteData");
                    if (cmsMiddleBannerTwoJay != null && cmsMiddleBannerTwoJay.length() > 0) {
                        findViewById(R.id.area_mid_banner2).setVisibility(View.VISIBLE);
                        final List<CmsItem> midBanner2List = new ArrayList<>();
                        for (int j = 0; j < cmsMiddleBannerTwoJay.length(); j++) {
                            midBanner2List.add(gson.fromJson(cmsMiddleBannerTwoJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        initMiddleBanner2(midBanner2List);
                    } else {
                        findViewById(R.id.area_mid_banner2).setVisibility(View.GONE);
                    }
                }
                if ("cmsGuessYourLove".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONObject("templeteData").optJSONArray("rows");
                    ((TextView) findViewById(R.id.tv_guess_title)).setText(tempItemJot.optString("moduleName"));
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        findViewById(R.id.area_date_show).setVisibility(View.VISIBLE);
                        dList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            dList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                        }
                        mDateGvAdapter = new DateGvAdapter(mContext, dList);
                        gv_date.setAdapter(mDateGvAdapter);
                    } else {
                        findViewById(R.id.area_date_show).setVisibility(View.GONE);
                    }
                }
                if ("cmsMoreContent".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    ((TextView) findViewById(R.id.tv_teacher_show)).setText(tempItemJot.optString("moduleName"));
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        findViewById(R.id.area_show).setVisibility(View.VISIBLE);
                        sList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            sList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                        }
                        mShowListAdapter = new ShowListAdapter(mContext, sList);
                        lv_show.setAdapter(mShowListAdapter);
                    } else {
                        findViewById(R.id.area_show).setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                findViewById(R.id.top_bar).setVisibility(View.GONE); //setBackgroundColor(Color.parseColor("#000000"));
            } else if (msg.what == 1) {
                //findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#ffffff"));
                findViewById(R.id.top_bar).setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(HomeNewActivity.this.getParent());
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
                asyncGetTeachHomePage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
        mPullRefreshScrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state == PullToRefreshBase.State.PULL_TO_REFRESH && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    //findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#000000"));
                    handler.sendEmptyMessage(0);
                } else if (state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    //findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#ffffff"));
                    handler.sendEmptyMessage(1);
                }
                logout("state=====================" + state.name());
                logout("direction=====================" + direction.name());
            }
        });
        lv_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddStatistics(gList.get(i).getId(), "goldTeach");
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("target","GT_"+gList.get(i).getId()+"");
                MobclickAgent.onEvent(mContext, "ClickGoldTrainer",map);

                Intent intent = new Intent(HomeNewActivity.this, TeachHouseActivity.class);
                intent.putExtra("teachId", gList.get(i).getId());
                startActivity(intent);
            }
        });
        areaSeeMore.setOnClickListener(v -> {
            startActivity(new Intent(mContext, ExpertListActivity.class));
        });
        gv_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddStatistics(dList.get(position).getId(), "guess");
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", dList.get(position).getId());
                startActivity(it);
            }
        });
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddStatistics(sList.get(position).getId(), "more");
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", sList.get(position).getId());
                startActivity(it);
            }
        });
        findViewById(R.id.area_right_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToScaner();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToScaner();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToScaner();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                        //showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
//                startActivity(new Intent(mContext, ChristmasActMainActivity.class));
            }
        });
        findViewById(R.id.edit_sousuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStatistics(-1, "search");
                startActivity(new Intent(mContext, CourseSearchActivity.class));
                //startActivity(new Intent(mContext, ChristmasActListActivity.class));
                //startActivity(new Intent(mContext, Double11ActDetailsActivity.class));
            }
        });
        findViewById(R.id.area_change_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGetGoldTeacherList();
            }
        });
        findViewById(R.id.area_change_guess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGetGuessContentList();
            }
        });

        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        scrollView.setCallbacks(mCallbacks);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mCallbacks.onScrollChanged(scrollView.getScrollY());
            }
        });
    }

    /**
     * 滑动黏贴view的监听事件
     */
    private ObservableScrollView.Callbacks mCallbacks = new ObservableScrollView.Callbacks() {

        @Override
        public void onUpOrCancelMotionEvent() {
        }

        @Override
        public void onScrollChanged(int scrollY) {
            // 控制TOPBAR的渐变效果
            if (scrollY > DevAttr.dip2px(mContext, 185)) {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                //top_space_area.setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                ((ImageView) findViewById(R.id.img_scan)).setImageResource(R.mipmap.icon_scan_red);

                findViewById(R.id.serach_area).setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            } else if (scrollY > DevAttr.dip2px(mContext, 145)) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
                //top_space_area.setBackgroundColor(Color.parseColor("#66ffffff"));
            } else if (scrollY >= -10) {
                //findViewById(R.id.top_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                //top_space_area.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                ((ImageView) findViewById(R.id.img_scan)).setImageResource(R.mipmap.icon_scan_white);
                findViewById(R.id.serach_area).setBackgroundResource(R.drawable.rec_white_stroke_white_solid_corne30);
            } else if (scrollY < -10) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#00000000"));
            }
        }

        @Override
        public void onDownMotionEvent() {
        }
    };

    private void asyncGetGoldTeacherList() {
        String wholeUrl = AppUrl.host + AppUrl.getGoldTeacherList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", goldTeacherPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gtlistener);
    }

    BaseRequestListener gtlistener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            goldTeacherPageIndex = jsonResult.optInt("pageIndex");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                findViewById(R.id.area_gold_teacher).setVisibility(View.VISIBLE);
                gList = new ArrayList<>();
                for (int j = 0; j < dateJay.length(); j++) {
                    gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                }
                mExpertListAdapter = new ExpertListAdapter(mContext, gList);
                lv_teacher.setAdapter(mExpertListAdapter);
            } else {
                findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);

        }
    };

    private void asyncGetGuessContentList() {
        String wholeUrl = AppUrl.host + AppUrl.getGuessContentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", guessPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gglistener);
    }

    BaseRequestListener gglistener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            guessPageIndex = jsonResult.optInt("pageIndex");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                findViewById(R.id.area_date_show).setVisibility(View.VISIBLE);
                dList = new ArrayList<>();
                for (int j = 0; j < dateJay.length(); j++) {
                    dList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                }
                mDateGvAdapter = new DateGvAdapter(mContext, dList);
                gv_date.setAdapter(mDateGvAdapter);
            } else {
                findViewById(R.id.area_date_show).setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    /**
     * 去扫码
     */
    public void goToScaner() {
        Intent intent = new Intent();
        intent.setClass(mContext, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        }
    }

    public void initFastGo(final List<CmsItem> quickList) {
        if (quickList.size() == 1) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());//
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(null);
            findViewById(R.id.area_quick2).setOnClickListener(null);
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 2) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(null);
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 3) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(2), false);
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() >= 4) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
//            ((ImageView) findViewById(R.id.img_quick0)).setImageResource(R.mipmap.server_apply);
//            ((TextView) findViewById(R.id.tv_quick0)).setText("服务申请");
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick3), quickList.get(3).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick3)).setText(quickList.get(3).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
//                    startActivity(new Intent(mContext, ServiceApplyActivity.class));
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(2), false);
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            findViewById(R.id.area_quick3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(quickList.get(3), false);
                    ViewInitTool.judgeGoWhere(quickList.get(3), mContext);
                }
            });
        }
    }

    public void initMiddleBanner2(final List<CmsItem> midBanner2List) {
        if (midBanner2List.size() == 1) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(null);
            findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 2) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(1), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 3) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid2), midBanner2List.get(2).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(1), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            findViewById(R.id.img_mid2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(midBanner2List.get(2), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(2), mContext);
                }
            });
        }
    }

    public void AddStatistics(CmsItem bi, boolean isBanner) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (1 == bi.getLinkType() || 2 == bi.getLinkType()) {
            map.put("target", "TB_" + bi.getLinkUrl());
        } else if (3 == bi.getLinkType()) {//分类
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                map.put("target", "TB_CLA_" + jsonObject.optInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (4 == bi.getLinkType()) {//活动
            map.put("target", "TB_ACT_" + bi.getLinkData());
        } else {//内容
            map.put("target", "TB_CNT_" + bi.getLinkData());
        }
        if (isBanner) {
            MobclickAgent.onEvent(mContext, "ClickMiddleBanner2", map);
        } else {
            MobclickAgent.onEvent(mContext, "clickentrance", map);
        }
    }

    public void AddStatistics(int id, String type) {
        if ("guess".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "LV_" + id);
            MobclickAgent.onEvent(mContext, "ClickLovely", map);
            MobclickAgent.onEvent(mContext, "clicklovely", map);
        } else if ("more".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "TB_" + "TM_" + id);
            MobclickAgent.onEvent(mContext, "ClickTheMore", map);
        } else if ("home".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            //MobclickAgent.onEvent(this, "Home", map);
//            MobclickAgent.onEvent(mContext, "Home");
            MobclickAgent.onEvent(mContext, "ClickOnHome");
        } else if ("search".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            MobclickAgent.onEvent(mContext, "ClickSearch", map);
        } else if ("goldTeach".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "GT_" + id);
            MobclickAgent.onEvent(mContext, "clickgoldtrainer", map);
        }
    }

    /**
     * 请求启动广告页
     */
    private void asyncGetAndroidStartPage() {
        String wholeUrl = AppUrl.host + AppUrl.getAndroidStartPage;
        /*Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", guessPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);*/
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, gclistener);
    }

    BaseRequestListener gclistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mListDataSave = new ListDataSave(mContext);
            final List<AdvertsieBean> advertsieBeanList = new ArrayList<>();
            final List<AdvertsieBean> saveList = new ArrayList<>();
            if (jsonResult == null) {
                mListDataSave.setAdvertiseNoShow();
                return;
            }
            JSONArray jsonArray = jsonResult.optJSONArray("list");
            if (jsonArray == null || jsonArray.length() == 0) {
                mListDataSave.setAdvertiseNoShow();
                return;
            }
            mListDataSave.setAdvertiseShow();
            for (int i = 0; i < jsonArray.length(); i++) {
                AdvertsieBean advertsieBean = gson.fromJson(jsonArray.optJSONObject(i).toString(), AdvertsieBean.class);
                advertsieBeanList.add(advertsieBean);
            }
            String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/firstpagefloder";
            File file = new File(downloadPath);
            //文件夹不存在，则创建它
            if (!file.exists()) {
                file.mkdir();
            } else {
                FilesManager.DeleteFileInFloder(file);//删除原有文件
            }
            logout("=========================create_floder==================");
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            for (int j = 0; j < advertsieBeanList.size(); j++) {
                final AdvertsieBean advertsieBean = advertsieBeanList.get(j);
                final String picFileName = j + ".jpg";
                logout("=========================start_loading_pic==================");
                http.download(advertsieBeanList.get(j).getPicUrl(), downloadPath + "/" + picFileName, true, false, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        advertsieBean.setPicLocalPath(picFileName);
                        logout("=========================save_pic_" + picFileName + "==================");
                        saveList.add(advertsieBean);
                        mListDataSave.setDataList(saveList);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        logout("=========================fail_pic_" + picFileName + "==================");
                    }
                });
            }
        }
    };

}




