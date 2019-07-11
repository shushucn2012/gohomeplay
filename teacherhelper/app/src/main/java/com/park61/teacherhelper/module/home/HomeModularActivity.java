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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.common.tool.ListDataSave;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.park61.teacherhelper.module.course.CourseSearchActivity;
import com.park61.teacherhelper.module.home.bean.AdvertsieBean;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.login.bean.UserBean;
import com.park61.teacherhelper.module.my.CompleteInfoActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.modular.BannerMidOneModule;
import com.park61.teacherhelper.widget.modular.BannerMidTwoModule;
import com.park61.teacherhelper.widget.modular.BannerModule;
import com.park61.teacherhelper.widget.modular.CourseRecommendModule;
import com.park61.teacherhelper.widget.modular.FastGoGridModule;
import com.park61.teacherhelper.widget.modular.GoldTeacherModule;
import com.park61.teacherhelper.widget.modular.GuessYourLoveModule;
import com.park61.teacherhelper.widget.modular.ImageVerticalModule;
import com.park61.teacherhelper.widget.modular.MoreContentModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化
 * Created by shubei on 2018/4/16.
 */

public class HomeModularActivity extends BaseActivity {

    private static final int SHOW_TOP = 0;//隐藏top
    private static final int HIDE_TOP = 1;//显示top

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;//权限组件
    public static final String SP_FILENAME = "park61.teacher";
    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private LinearLayout ll_content_root;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;

    private int goldTeacherPageIndex = 0;//培训师换一换页码
    private int guessPageIndex = 0;//猜你喜欢换一换页码
    private GoldTeacherModule curGoldTeacherModule;//当前点击换一换的培训师组件
    private GuessYourLoveModule curGuessYourLoveModule;//当前点击换一换的猜你喜欢组件
    private boolean goOthers = false;//蒙层判断
    private ListDataSave mListDataSave;//广告数据

    //处理下拉刷新加载时隐藏topbar
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HIDE_TOP) {
                findViewById(R.id.top_bar).setVisibility(View.GONE);
            } else if (msg.what == SHOW_TOP) {
                findViewById(R.id.top_bar).setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_home_modular);
    }

    @Override
    public void initView() {
        ll_content_root = (LinearLayout) findViewById(R.id.ll_content_root);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
    }

    @Override
    public void initData() {
        AddStatistics(mContext, -1, "home");
        goOthers = getIntent().getBooleanExtra("goOthers", false);
        showDialog();
        asyncGetTeachHomePage();
        asyncGetAndroidStartPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*//首页加载完后，判断是否显示蒙层
        if (!goOthers && getSharedPreferences(SP_FILENAME, MODE_PRIVATE).getBoolean("needShow", true)) {
            startActivity(new Intent(mContext, ServerRightTipActivity.class));
        } else {
            goOthers = false;
        }*/
        logout("=========================GlobalParam.currentUser====================" + GlobalParam.currentUser);
        logout("=========================GlobalParam.userToken====================" + GlobalParam.userToken);
        if (GlobalParam.currentUser != null || GlobalParam.userToken != null) {
            asyncGetMyInfor();
        }
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(HomeModularActivity.this.getParent());
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
                    }
                }
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetTeachHomePage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        //下拉刷新加载时隐藏topbar
        mPullRefreshScrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state == PullToRefreshBase.State.PULL_TO_REFRESH && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    handler.sendEmptyMessage(HIDE_TOP);
                } else if (state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    handler.sendEmptyMessage(SHOW_TOP);
                }
            }
        });
        findViewById(R.id.edit_sousuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStatistics(mContext, -1, "search");
                startActivity(new Intent(mContext, CourseSearchActivity.class));
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
                ((ImageView) findViewById(R.id.img_scan)).setImageResource(R.mipmap.icon_scan_red);
                findViewById(R.id.serach_area).setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            } else if (scrollY > DevAttr.dip2px(mContext, 145)) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
            } else if (scrollY >= -10) {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
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

    /**
     * 请求主页模块数据
     */
    private void asyncGetTeachHomePage() {
        String wholeUrl = AppUrl.host + AppUrl.getTeachHomePage;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            mPullRefreshScrollView.onRefreshComplete();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshScrollView.onRefreshComplete();
            //加载之前先移除老数据
            ll_content_root.removeAllViews();
            JSONArray dataJay = jsonResult.optJSONArray("modules");
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                if ("cmsTopBanner".equals(tempItemJot.optString("templeteCode"))) { //顶部banner
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        List<CmsItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            bannerlList.add(gson.fromJson(bannerJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new BannerModule(mContext, bannerlList));
                    }
                } else if ("cmsFastGoTo".equals(tempItemJot.optString("templeteCode"))) {//快捷入口
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> quickList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            quickList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new FastGoGridModule(mContext, quickList));
                    }
                } else if ("cmsMiddleBannerOne".equals(tempItemJot.optString("templeteCode"))) {//中部banner1
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        List<CmsItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            bannerlList.add(gson.fromJson(bannerJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new BannerMidOneModule(mContext, bannerlList));
                    }
                } else if ("cmsCourseSeriesRecommend".equals(tempItemJot.optString("templeteCode"))) {//今日推荐
                    JSONArray todayRec = tempItemJot.optJSONArray("templeteData");
                    if (todayRec != null && todayRec.length() > 0) {
                        List<TeacherCourseBean> todayList = new ArrayList<>();
                        for (int j = 0; j < todayRec.length(); j++) {
                            todayList.add(gson.fromJson(todayRec.optJSONObject(j).toString(), TeacherCourseBean.class));
                        }
                        ll_content_root.addView(new CourseRecommendModule(mContext, todayList, tempItemJot.optString("moduleName")));
                    }
                } else if ("cmsGoldTeacher".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONObject("templeteData") != null) {
                        JSONArray dateJay = tempItemJot.optJSONObject("templeteData").optJSONArray("rows");
                        if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                            List<GoldTeacher> gList = new ArrayList<>();
                            for (int j = 0; j < dateJay.length(); j++) {
                                gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                            }
                            GoldTeacherModule mGoldTeacherModule = new GoldTeacherModule(mContext, gList, tempItemJot.optString("moduleName"));
                            mGoldTeacherModule.setOnChangeClickCallBack(new GoldTeacherModule.OnChangeClickCallBack() {
                                @Override
                                public void onChange() {
                                    curGoldTeacherModule = mGoldTeacherModule;
                                    asyncGetGoldTeacherList();
                                }
                            });
                            ll_content_root.addView(mGoldTeacherModule);
                        }
                    }
                } else if ("cmsMiddleBannerTwo".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray cmsMiddleBannerTwoJay = tempItemJot.optJSONArray("templeteData");
                    if (cmsMiddleBannerTwoJay != null && cmsMiddleBannerTwoJay.length() > 0) {
                        List<CmsItem> midBanner2List = new ArrayList<>();
                        for (int j = 0; j < cmsMiddleBannerTwoJay.length(); j++) {
                            midBanner2List.add(gson.fromJson(cmsMiddleBannerTwoJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new BannerMidTwoModule(mContext, midBanner2List));
                    }
                } else if ("cmsGuessYourLove".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONObject("templeteData") != null) {
                        JSONArray dateJay = tempItemJot.optJSONObject("templeteData").optJSONArray("rows");
                        if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                            List<ContentItem> dList = new ArrayList<>();
                            for (int j = 0; j < dateJay.length(); j++) {
                                dList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                            }
                            GuessYourLoveModule mGuessYourLoveModule = new GuessYourLoveModule(mContext, dList, tempItemJot.optString("moduleName"));
                            mGuessYourLoveModule.setOnChangeClickCallBack(new GoldTeacherModule.OnChangeClickCallBack() {
                                @Override
                                public void onChange() {
                                    curGuessYourLoveModule = mGuessYourLoveModule;
                                    asyncGetGuessContentList();
                                }
                            });
                            ll_content_root.addView(mGuessYourLoveModule);
                        }
                    }
                } else if ("cmsMoreContent".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<ContentItem> sList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            sList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                        }
                        ll_content_root.addView(new MoreContentModule(mContext, sList, tempItemJot.optString("moduleName")));
                    }
                } else if ("imageVertical".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> picList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            picList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new ImageVerticalModule(mContext, picList));
                    }
                }
            }
        }
    };

    /**
     * 培训师换一换
     */
    private void asyncGetGoldTeacherList() {
        String wholeUrl = AppUrl.host + AppUrl.getGoldTeacherList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", goldTeacherPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gtlistener);
    }

    BaseRequestListener gtlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            goldTeacherPageIndex = jsonResult.optInt("pageIndex");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                List<GoldTeacher> gList = new ArrayList<>();
                for (int j = 0; j < dateJay.length(); j++) {
                    gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                }
                if (curGoldTeacherModule != null) {
                    curGoldTeacherModule.changeData(gList);
                }
            }
        }
    };

    /**
     * 猜你喜欢换一换
     */
    private void asyncGetGuessContentList() {
        String wholeUrl = AppUrl.host + AppUrl.getGuessContentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", guessPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gglistener);
    }

    BaseRequestListener gglistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            guessPageIndex = jsonResult.optInt("pageIndex");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                List<ContentItem> dList = new ArrayList<>();
                for (int j = 0; j < dateJay.length(); j++) {
                    dList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                }
                if (curGuessYourLoveModule != null) {
                    curGuessYourLoveModule.changeData(dList);
                }
            }
        }
    };

    /**
     * 请求启动广告页
     */
    private void asyncGetAndroidStartPage() {
        String wholeUrl = AppUrl.host + AppUrl.getAndroidStartPage;
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

    /**
     * 获取我的信息,判断是否去完善信息
     */
    protected void asyncGetMyInfor() {
        String wholeUrl = AppUrl.host + AppUrl.myInfor;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            if ("0000000003".equals(errorCode)) {
                startActivity(new Intent(mContext, CompleteInfoActivity.class));
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            UserBean userBean = new Gson().fromJson(jsonResult.toString(), UserBean.class);
            GlobalParam.currentUser = userBean;
            if (TextUtils.isEmpty(userBean.getUserDuty())) {//userDuty为空
                startActivity(new Intent(mContext, CompleteInfoActivity.class));
            }
        }
    };
}
