package com.park61.teacherhelper.module.home;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CRequest;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.common.tool.ListDataSave;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.park61.teacherhelper.common.tool.PreferencesUtils;
import com.park61.teacherhelper.module.course.CourseSearchActivity;
import com.park61.teacherhelper.module.home.adapter.ShowRvAdapter;
import com.park61.teacherhelper.module.home.bean.AdvertsieBean;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.ExpertCourseBean;
import com.park61.teacherhelper.module.home.bean.ExpertOpinionBean;
import com.park61.teacherhelper.module.login.bean.UserBean;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.module.my.CompleteInfoActivity;
import com.park61.teacherhelper.module.umeng.MsgCenterActivity;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.VipOpenTipDialog;
import com.park61.teacherhelper.widget.modular.BannerAndFastGoModule;
import com.park61.teacherhelper.widget.modular.BannerMidOneModule;
import com.park61.teacherhelper.widget.modular.BannerMidTwoModule;
import com.park61.teacherhelper.widget.modular.CategoryModule;
import com.park61.teacherhelper.widget.modular.ExpertCourseModule;
import com.park61.teacherhelper.widget.modular.ExpertOpinionModule;
import com.park61.teacherhelper.widget.modular.GoldTeacherModule;
import com.park61.teacherhelper.widget.modular.GuessYourLoveModule;
import com.park61.teacherhelper.widget.modular.ImageHorizontalModule;
import com.park61.teacherhelper.widget.modular.ImageVerticalModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.PreferencesUtils.IS_EXPMEMBER_GET_TIP_SHOW;
import static com.park61.teacherhelper.common.tool.PreferencesUtils.IS_MEMBER_OPEN_SUCCESS_TIP_SHOW;
import static com.park61.teacherhelper.common.tool.PreferencesUtils.IS_MEMBER_OUTDATE_1_TIP_SHOW;
import static com.park61.teacherhelper.common.tool.PreferencesUtils.IS_MEMBER_OUTDATE_2_TIP_SHOW;
import static com.park61.teacherhelper.common.tool.PreferencesUtils.IS_MEMBER_OUTDATE_3_TIP_SHOW;
import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化列表
 * Created by shubei on 2018/5/18.
 */
public class HomeModularListActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;//权限组件

    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 6;
    private int totalPage = 100;

    private View headview, area_right_msg;
    private LRecyclerView rv_firsthead;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout ll_content_root;
    private TextView tv_msg_num;
    private VipOpenTipDialog vipOpenTipDialog;

    private ListDataSave mListDataSave;//广告数据
    private List<ContentItem> sList;
    private ShowRvAdapter mShowRvAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private List<CmsItem> topBannerList = new ArrayList<>();
    private List<CmsItem> topQuickList = new ArrayList<>();
    private GuessYourLoveModule curGuessYourLoveModule;//当前点击换一换的猜你喜欢组件
    private int guessPageIndex = 0;//猜你喜欢换一换页码

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_home_modular_list);
    }

    @Override
    public void initView() {
        headview = LayoutInflater.from(mContext).inflate(R.layout.main_headview, null);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        linearLayoutManager = new LinearLayoutManager(mContext);
        rv_firsthead.setLayoutManager(linearLayoutManager);

        ll_content_root = (LinearLayout) headview.findViewById(R.id.ll_content_root);
        tv_msg_num = (TextView) findViewById(R.id.tv_msg_num);
        area_right_msg = findViewById(R.id.area_right_msg);
        vipOpenTipDialog = new VipOpenTipDialog(mContext, netRequest);
    }

    @Override
    public void initData() {
        AddStatistics(mContext, -1, "home");
        sList = new ArrayList<>();
        mShowRvAdapter = new ShowRvAdapter(mContext, sList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mShowRvAdapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);

        showDialog();
        asyncGetTeachHomePage();
        asyncGetAndroidStartPage();

        //上传device_token
        asyncSaveAppDeviceToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logout("=========================GlobalParam.currentUser====================" + GlobalParam.currentUser);
        logout("=========================GlobalParam.userToken====================" + GlobalParam.userToken);
        if (GlobalParam.currentUser != null || GlobalParam.userToken != null) {
            asyncGetMyInfor();
            asyncMessageNum();
        }
    }

    @Override
    public void initListener() {
        registerBtnRefreshReceiver();
        permissionHelper = new PermissionHelper(HomeModularListActivity.this.getParent());
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
        findViewById(R.id.edit_sousuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStatistics(mContext, -1, "search");
                startActivity(new Intent(mContext, CourseSearchActivity.class));
            }
        });
        area_right_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MsgCenterActivity.class));
                //startActivity(new Intent(mContext, WorkManageMainActivity.class));
            }
        });
        rv_firsthead.setOnRefreshListener(() -> asyncGetTeachHomePage());
        rv_firsthead.setPullRefreshEnabled(true);
        rv_firsthead.setOnLoadMoreListener(() -> {
            if (PAGE_NUM < totalPage - 1) {
                getNextPage();
            } else {
                rv_firsthead.setNoMore(true);
            }
        });
        /*rv_firsthead.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                View viewFirst = linearLayoutManager.findViewByPosition(1);
                if (viewFirst != null) {// 控制TOPBAR的渐变效果
                    if ((0 - viewFirst.getTop()) > DevAttr.dip2px(mContext, 185)) {
                        findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                        ((ImageView) findViewById(R.id.img_scan)).setImageResource(R.mipmap.icon_scan_red);
                        findViewById(R.id.serach_area).setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
                    } else if ((0 - viewFirst.getTop()) > DevAttr.dip2px(mContext, 145)) {
                        findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
                    } else {
                        findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                        ((ImageView) findViewById(R.id.img_scan)).setImageResource(R.mipmap.icon_scan_white);
                        findViewById(R.id.serach_area).setBackgroundResource(R.drawable.rec_white_stroke_white_solid_corne30);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(int state) {
            }
        });*/
    }

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
            logout("=================str=============" + str);
            if (TextUtils.isEmpty(str))
                return;
            if (str.contains("park61_scanLogin_uuid") && str.indexOf("=") > -1) {
                asyncScanQRCodeLogin(str.split("=")[1]);
            } else {
                CommonMethod.dealWithScanBack(str, mContext);
            }
        }
    }

    /**
     * app扫描二维码授权登录
     */
    private void asyncScanQRCodeLogin(String uuid) {
        String wholeUrl = AppUrl.host + AppUrl.scanQRCodeLogin;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uuid", uuid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, uLsner);
    }

    BaseRequestListener uLsner = new JsonRequestListener() {

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
            showShortToast("扫码成功！");
        }
    };


    /**
     * 请求主页模块数据
     */
    private void asyncGetTeachHomePage() {
        String wholeUrl = AppUrl.host + AppUrl.getTeachHomePage;//"http://10.10.10.18:8380/service/cms/getTeachHomePage";
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
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
                        if (CommonMethod.isListEmpty(topBannerList)) {
                            topBannerList.addAll(bannerlList);
                        } else {//已经有过，之后的就直接添加
                            //ll_content_root.addView(new BannerModule(mContext, bannerlList));
                        }
                    }
                } else if ("cmsFastGoTo".equals(tempItemJot.optString("templeteCode"))) {//快捷入口
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> quickList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            quickList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        if (CommonMethod.isListEmpty(topQuickList)) {
                            topQuickList.addAll(quickList);
                        } else {//已经有过，之后的就直接添加
                            //ll_content_root.addView(new FastGoGridModule(mContext, quickList));
                        }
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
                }
                /*else if ("cmsCourseSeriesRecommend".equals(tempItemJot.optString("templeteCode"))) {//今日推荐
                    JSONArray todayRec = tempItemJot.optJSONArray("templeteData");
                    if (todayRec != null && todayRec.length() > 0) {
                        List<TeacherCourseBean> todayList = new ArrayList<>();
                        for (int j = 0; j < todayRec.length(); j++) {
                            todayList.add(gson.fromJson(todayRec.optJSONObject(j).toString(), TeacherCourseBean.class));
                        }
                        ll_content_root.addView(new CourseRecommendModule(mContext, todayList, tempItemJot.optString("moduleName")));
                    }
                }
                else if ("cmsGoldTeacher".equals(tempItemJot.optString("templeteCode"))) {
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
                                    //curGoldTeacherModule = mGoldTeacherModule;
                                    //asyncGetGoldTeacherList();
                                }
                            });
                            ll_content_root.addView(mGoldTeacherModule);
                        }
                    }
                } */
                else if ("cmsExpertOpinion".equals(tempItemJot.optString("templeteCode"))) {//专家观点
                    JSONArray expertOpinionArray = tempItemJot.optJSONArray("templeteData");
                    if (expertOpinionArray != null && expertOpinionArray.length() > 0) {
                        List<ExpertOpinionBean> expertOpinionBeanList = new ArrayList<>();
                        for (int j = 0; j < expertOpinionArray.length(); j++) {
                            expertOpinionBeanList.add(gson.fromJson(expertOpinionArray.optJSONObject(j).toString(), ExpertOpinionBean.class));
                        }
                        ll_content_root.addView(new ExpertOpinionModule(mContext, expertOpinionBeanList, tempItemJot.optString("moduleName")));
                    }
                } else if ("cmsExpertCourse".equals(tempItemJot.optString("templeteCode"))) {//专家讲堂
                    JSONArray expertCourseArray = tempItemJot.optJSONArray("templeteData");
                    if (expertCourseArray != null && expertCourseArray.length() > 0) {
                        List<ExpertCourseBean> expertCourseBeanList = new ArrayList<>();
                        for (int j = 0; j < expertCourseArray.length(); j++) {
                            expertCourseBeanList.add(gson.fromJson(expertCourseArray.optJSONObject(j).toString(), ExpertCourseBean.class));
                        }
                        ll_content_root.addView(new ExpertCourseModule(mContext, expertCourseBeanList, tempItemJot.optString("moduleName")));
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
                }
               /* else if ("cmsMoreContent".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<ContentItem> sList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            sList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), ContentItem.class));
                        }
                        ll_content_root.addView(new MoreContentModule(mContext, sList, tempItemJot.optString("moduleName")));
                    }
                } */
                else if ("imageVertical".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> picList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            picList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new ImageVerticalModule(mContext, picList));
                    }
                } else if ("imageCrosswise".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> hpicList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            hpicList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ll_content_root.addView(new ImageHorizontalModule(mContext, hpicList));
                    }
                } else if ("cmsCategory".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                    if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                        List<CmsItem> cateList = new ArrayList<>();
                        for (int j = 0; j < dateJay.length(); j++) {
                            cateList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        if (cateList.size() == 3 || cateList.size() == 4 || cateList.size() == 5) {
                            ll_content_root.addView(new CategoryModule(mContext, cateList, tempItemJot.optString("moduleName")));
                        }
                    }
                }
            }

            if (!CommonMethod.isListEmpty(topBannerList) && !CommonMethod.isListEmpty(topQuickList)) {
                ll_content_root.addView(new BannerAndFastGoModule(mContext, topBannerList, topQuickList), 0);
            }

            if (mLRecyclerViewAdapter.getHeaderViewsCount() == 0) {//第一次加载添加，之后只改变不添加
                mLRecyclerViewAdapter.addHeaderView(headview);
            }

            refreshList();
        }
    };

    /**
     * 刷新列表数据
     */
    public void refreshList() {
        PAGE_NUM = 0;
        //重置可以加载更多
        rv_firsthead.setNoMore(false);
        asyncGetMoreData();
    }

    /**
     * 获得列表下一页数据
     */
    public void getNextPage() {
        PAGE_NUM++;
        asyncGetMoreData();
        if (PAGE_NUM == 4) {//第4页的时候显示刷新按钮
            Intent changeIt = new Intent();
            changeIt.setAction("ACTION_TAB_CHANGE");
            changeIt.putExtra("TAB_NAME", "tab_refresh");
            sendBroadcast(changeIt);
        }
    }

    /**
     * 获得更多内容列表数据
     */
    private void asyncGetMoreData() {
        String wholeUrl = AppUrl.host + AppUrl.getNewRecommendContent;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getLsner);
    }

    BaseRequestListener getLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            } else {//第一页报错时，清空数据
                sList.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            parseJosnToShow(jsonResult);
        }
    };

    /**
     * 解析列表数据
     */
    private void parseJosnToShow(JSONObject jsonResult) {
        JSONArray jayList = jsonResult.optJSONArray("rows");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 0 && (jayList == null || jayList.length() <= 0)) {
            sList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            headview.findViewById(R.id.tv_more_content_show).setVisibility(View.GONE);
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            sList.clear();
            headview.findViewById(R.id.tv_more_content_show).setVisibility(View.VISIBLE);
        }
        ArrayList<ContentItem> currentPageList = new ArrayList<>();
        totalPage = jsonResult.optInt("pageCount");
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            ContentItem contentItem = gson.fromJson(jot.toString(), ContentItem.class);
            currentPageList.add(contentItem);
        }
        sList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

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
            if ("0000000003".equals(errorCode)) {//个人信息获取失败
                startActivity(new Intent(mContext, CompleteInfoActivity.class));
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            UserBean userBean = new Gson().fromJson(jsonResult.toString(), UserBean.class);
            GlobalParam.currentUser = userBean;
            if (TextUtils.isEmpty(userBean.getUserDuty())) {//userDuty为空,去完善个人信息
                startActivity(new Intent(mContext, CompleteInfoActivity.class));
            } else {//已经完善信息的用户开始会员状态提示
                if (userBean.getIsMember() == 1 && !PreferencesUtils.getBoolean(mContext, IS_MEMBER_OPEN_SUCCESS_TIP_SHOW)) {//1正式会员
                    //是正式会员，并且没显示过开通成功提示,显示提示框
                    vipOpenTipDialog.showDialog("会员开通成功", null);
                    //标记记录已提示过
                    PreferencesUtils.putBoolean(mContext, IS_MEMBER_OPEN_SUCCESS_TIP_SHOW, true);
                } else if (userBean.getIsMember() == 0 && !PreferencesUtils.getBoolean(mContext, IS_EXPMEMBER_GET_TIP_SHOW)) {//0体验会员
                    //是体验会员，并且没显示过赠送提示,显示提示框
                    vipOpenTipDialog.showDialog("免费会员限时体验3天", null);
                    //标记记录已提示过
                    PreferencesUtils.putBoolean(mContext, IS_EXPMEMBER_GET_TIP_SHOW, true);
                } else if (userBean.getIsMember() > -1 && userBean.getMemberRemainDays() <= 3 && userBean.getMemberRemainDays() > 0) {
                    //是体验会员或者黄金会员，并且甚于天数小于等于3天
                    if (userBean.getMemberRemainDays() == 3 && !PreferencesUtils.getBoolean(mContext, IS_MEMBER_OUTDATE_3_TIP_SHOW)) {
                        showTipDailog();
                        PreferencesUtils.putBoolean(mContext, IS_MEMBER_OUTDATE_3_TIP_SHOW, true);
                    } else if (userBean.getMemberRemainDays() == 2 && !PreferencesUtils.getBoolean(mContext, IS_MEMBER_OUTDATE_2_TIP_SHOW)) {
                        showTipDailog();
                        PreferencesUtils.putBoolean(mContext, IS_MEMBER_OUTDATE_2_TIP_SHOW, true);
                    } else if (userBean.getMemberRemainDays() == 1 && !PreferencesUtils.getBoolean(mContext, IS_MEMBER_OUTDATE_1_TIP_SHOW)) {
                        showTipDailog();
                        PreferencesUtils.putBoolean(mContext, IS_MEMBER_OUTDATE_1_TIP_SHOW, true);
                    }
                }
            }
        }
    };

    private void showTipDailog() {
        dDialog.showDialog("提示",
                "您的会员还有" + GlobalParam.currentUser.getMemberRemainDays() + "天过期，快去申请继续使用吧", "取消", "去开通",
                null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        startActivity(new Intent(mContext, MemberMainActivity.class));
                    }
                });
    }

    /**
     * 刷新广播
     */
    private void registerBtnRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_HOME_DATA");
        mContext.registerReceiver(tabRefreshReceiver, filter);
    }

    private BroadcastReceiver tabRefreshReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            rv_firsthead.forceToRefresh();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(tabRefreshReceiver);
    }

    /**
     * 将友盟token上传到服务器
     */
    private void asyncSaveAppDeviceToken() {
        String wholeUrl = AppUrl.host + AppUrl.SAVE_APP_DEVICE_TOKEN;//"http://10.10.10.169/service/appDevice/saveAppDeviceToken";//

        SharedPreferences youmengRunSp = getSharedPreferences("YOUMENG_DEVICE_TOKEN", Activity.MODE_PRIVATE);
        GlobalParam.YOUMENG_DEVICE_TOKEN = youmengRunSp.getString("deviceToken", "");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceToken", GlobalParam.YOUMENG_DEVICE_TOKEN);
        map.put("bundleId", GlobalParam.BUNDLE_ID);

        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 获取未查看消息数量
     */
    protected void asyncMessageNum() {
        String wholeUrl = AppUrl.host + AppUrl.messageNum;//"http://10.10.10.18:8380/service/messagePush/messageNum";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, msgLsner);
    }

    BaseRequestListener msgLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            int msgNum = jsonResult.optInt("data");
            if (msgNum <= 0) {
                tv_msg_num.setVisibility(View.GONE);
            } else {
                tv_msg_num.setVisibility(View.VISIBLE);
                String showMsgNum = "";
                if (msgNum > 99)
                    showMsgNum = "99+";
                else
                    showMsgNum = msgNum + "";
                tv_msg_num.setText(showMsgNum);
            }
        }
    };
}
