package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.activity.adapter.ActivityCityAdapter;
import com.park61.teacherhelper.module.activity.adapter.ActivityTopImageAdapter;
import com.park61.teacherhelper.module.activity.adapter.ActivityTopPagerAdapter;
import com.park61.teacherhelper.module.activity.adapter.Adapteractivitylist;
import com.park61.teacherhelper.module.activity.bean.ActivityCityMoudleBean;
import com.park61.teacherhelper.module.activity.bean.ActivityDataBean;
import com.park61.teacherhelper.module.activity.bean.ActivityListBean;
import com.park61.teacherhelper.module.activity.bean.TopBannerBean;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ObservableScrollView;
import com.park61.teacherhelper.widget.pw.SharePopWin;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nieyu on 2017/10/31.
 */

public class ActivityList extends BaseActivity implements View.OnClickListener {
    RecyclerView id_recyclerview;
    RecyclerView id_recyclerviewcity;
    ViewPager top_viewpager;
    public ListView lv_activitylist;
    private List<LinearLayout> mListViews;
    ActivityTopPagerAdapter activityTopPagerAdapter;
    int selectPage = -1;

    ImageView iv_redshre;
    ImageView iv_back;
    TextView tv_titlename;
    ObservableScrollView sv;
    LinearLayout top_vp_dot;
    //private CmsBanner mp0, mp1;
    LinearLayout ll_topback;

    // 底部小店图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;

    ImageView iv_imagenodata;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_activitylist);
    }

    @Override
    public void initView() {
//        iv_imagenodata=(ImageView)findViewById(R.id.iv_imagenodata);
        ll_topback = (LinearLayout) findViewById(R.id.ll_topback);
        top_vp_dot = (LinearLayout) findViewById(R.id.top_vp_dot);
        tv_titlename = (TextView) findViewById(R.id.tv_titlename);
        id_recyclerview = (RecyclerView) findViewById(R.id.id_recyclerview);
        top_viewpager = (ViewPager) findViewById(R.id.top_viewpagers);
        id_recyclerviewcity = (RecyclerView) findViewById(R.id.id_recyclerviewcity);
        lv_activitylist = (ListView) findViewById(R.id.lv_activitylist);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        id_recyclerview.setLayoutManager(manager);
        LinearLayoutManager managers = new LinearLayoutManager(mContext);
        managers.setOrientation(LinearLayout.HORIZONTAL);
        id_recyclerviewcity.setLayoutManager(managers);
        mListViews = new ArrayList<LinearLayout>();

        iv_redshre = (ImageView) findViewById(R.id.iv_redshre);
        iv_redshre.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        sv = (ObservableScrollView) findViewById(R.id.sv);
    }

    @Override
    public void initData() {
        asynGetTopBanner();
    }

    TopBannerBean topBannerBean;

    public void asynGetTopBanner() {
        String wholeUrl = AppUrl.host + AppUrl.addActivityCity;
//    String wholeUrl = "http://192.168.100.13:8080/mockjsdata/6/service/cms/getTeachDoubleElevenPag";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageId", getIntent().getIntExtra("pageId", -1));
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, bannerlisener);
    }

    BaseRequestListener bannerlisener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            topBannerBean = gson.fromJson(jsonResult.toString(), TopBannerBean.class);

            filldata(topBannerBean);
        }
    };

    @Override
    public void initListener() {
        sv.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int scrollY, int oldx, int oldy) {
                // 控制TOPBAR的渐变效果
                if (scrollY > DevAttr.dip2px(mContext, 185)) {
                    findViewById(R.id.ll_title).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                    ((ImageView) findViewById(R.id.iv_back)).setImageResource(R.mipmap.icon_red_backimg);
                    ((ImageView) findViewById(R.id.iv_redshre)).setImageResource(R.mipmap.red_contentshare);
                    tv_titlename.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                } else if (scrollY > DevAttr.dip2px(mContext, 145)) {
                    findViewById(R.id.ll_title).setBackgroundColor(Color.parseColor("#66ffffff"));
                } else {
                    findViewById(R.id.ll_title).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                    ((ImageView) findViewById(R.id.iv_back)).setImageResource(R.mipmap.icon_content_backimg);
                    ((ImageView) findViewById(R.id.iv_redshre)).setImageResource(R.mipmap.icon_shadow_share);
                    tv_titlename.setTextColor(mContext.getResources().getColor(R.color.gffffff));
                }
            }
        });
    }

    ActivityTopImageAdapter activityTopImageAdapter;

    public void filldata(TopBannerBean topdata) {
        if (topdata == null || CommonMethod.isListEmpty(topdata.getModules()) || CommonMethod.isListEmpty(topdata.getModules().get(0).getTempleteData())) {
            return;
        }
        activityTopImageAdapter = new ActivityTopImageAdapter(this, topdata.getModules().get(0).getTempleteData());
        id_recyclerview.setAdapter(activityTopImageAdapter);
        for (int i = 0; i < topdata.getModules().get(0).getTempleteData().size(); i++) {
            if (topdata.getModules().get(0).getTempleteData().get(i).getBannerAdLink() != null) {
                ImageView iv = new ImageView(this);
                LinearLayout phone_LayoutAccount = new LinearLayout(mContext);
                LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams ivlm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                phone_LayoutAccount.setGravity(Gravity.CENTER);
                ivlm.gravity = Gravity.CENTER_VERTICAL;
                ivlm.leftMargin = 30;
                ivlm.rightMargin = 30;
                ivlm.bottomMargin = 40;
                iv.setLayoutParams(ivlm);
                phone_LayoutAccount.setLayoutParams(lm);
                phone_LayoutAccount.addView(iv);
                mListViews.add(phone_LayoutAccount);
                if (topdata.getModules().get(0).getTempleteData().get(i).getBannerAdLink().getSelected() == 1) {
                    selectPage = i;
                    ImageManager.getInstance().displayImg(iv, topdata.getModules().get(0).getTempleteData().get(i).getBannerAdLink().getLinkPic());
                } else {
                    ImageManager.getInstance().displayImg(iv, topdata.getModules().get(0).getTempleteData().get(i).getBannerAdLink().getLinkPic());
                }
            }
        }
        if (selectPage < 0) {
            return;
        }

        activityTopPagerAdapter = new ActivityTopPagerAdapter(mListViews);
        top_viewpager.setAdapter(activityTopPagerAdapter);
        top_viewpager.setCurrentItem(selectPage);
        //if (mp0 != null) {
                            /*mp0.clear();
                            mp0.init(bannerlList);*/
        //} else {
        //  mp0 = new CmsBanner(mContext, top_viewpager, top_vp_dot, "top");
//            mp0.init(bannerlList);
        //}
        currentIndex = selectPage;
        initDots();

        top_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                asygetCityData();
//                asygetActivityList(cityBean.getModules().get(0).getTempleteData().get(position).getModuleId());
                activityTopImageAdapter.currentselect(position);
                id_recyclerview.smoothScrollToPosition(position);
                activityTopImageAdapter.notifyDataSetChanged();
                setCurDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        activityTopImageAdapter.setOnItemClickListener(new ContentRecomendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                top_viewpager.setCurrentItem(position);
                activityTopImageAdapter.currentselect(position);
                asygetCityData();
//                asygetActivityList(cityBean.getModules().get(0).getTempleteData().get(position).getModuleId());
            }
        });
        tv_titlename.setText(topdata.getPageName());

        ImageLoader.getInstance().loadImage(topdata.getPageProperties().getBackgroundUrl(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(loadedImage);
                ll_topback.setBackground(drawable);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        asygetCityData();
    }

    ActivityCityMoudleBean cityBean;

    public void asygetCityData() {
        String linkdata = topBannerBean.getModules().get(0).getTempleteData().get(top_viewpager.getCurrentItem()).getIndicatorAdLink().getLinkData();
        String wholeUrl = AppUrl.host + AppUrl.addActivityCity;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/6/service/cms/loadPageDataById";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageId", linkdata);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cityLisener);
    }

    BaseRequestListener cityLisener = new JsonRequestListener() {
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
            cityBean = gson.fromJson(jsonResult.toString(), ActivityCityMoudleBean.class);
            filldata(cityBean);
        }
    };

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.right = space;
        }

    }

    public void filldata(final ActivityCityMoudleBean cityBean) {
        final ActivityCityAdapter activityCityAdapter = new ActivityCityAdapter(this, cityBean.getModules().get(0).getTempleteData());
//        id_recyclerviewcity.addItemDecoration(new ActivityList.SpaceItemDecoration());

        id_recyclerviewcity.setAdapter(activityCityAdapter);
//        if (cityBean.getModules().get(0).getTempleteData().get(0).getBackgroundColor() != null) {
//            id_recyclerviewcity.setBackgroundColor(Color.parseColor("#" + topBannerBean.getPageProperties().getBackgroundColor()));
//        }
        activityCityAdapter.setOnItemClickListener(new ActivityCityAdapter.OnItemClickListener() {
            @Override
            public void onItemCityClick(View view, int position) {
                asygetActivityList(cityBean.getModules().get(0).getTempleteData().get(position).getModuleId());
                activityCityAdapter.notifyDataSetChanged();
            }
        });
        for (int i = 0; i < cityBean.getModules().get(0).getTempleteData().size(); i++) {
            if (cityBean.getModules().get(0).getTempleteData().get(i).getHasActive() != null) {
                if (cityBean.getModules().get(0).getTempleteData().get(i).getHasActive()) {
                    asygetActivityList(cityBean.getModules().get(0).getTempleteData().get(i).getModuleId());
                }
            }
        }

    }

    public void asygetActivityList(int modleid) {
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/6/service/cms/loadModuleDataById";
        String wholeUrl = AppUrl.host + AppUrl.addActivitylist;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("moduleId", modleid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, modulLisenter);
    }

    BaseRequestListener modulLisenter = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            String ddd = errorCode;
//            iv_imagenodata.setVisibility(View.VISIBLE);
            List<ActivityListBean> listBeen = new ArrayList<>();
            Adapteractivitylist adapteractivitylist = new Adapteractivitylist(ActivityList.this, listBeen, "FFFFFF");
            lv_activitylist.setAdapter(adapteractivitylist);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();

            ActivityDataBean activityDataBean = gson.fromJson(jsonResult.toString(), ActivityDataBean.class);
            filldata(activityDataBean);
        }
    };

    public void filldata(final ActivityDataBean activityDataBean) {
        Adapteractivitylist adapteractivitylist = new Adapteractivitylist(ActivityList.this, activityDataBean.getTempleteData(), cityBean.getPageProperties().getBackgroundColor());
        id_recyclerviewcity.setBackgroundColor(Color.parseColor("#" + cityBean.getPageProperties().getBackgroundColor()));
        lv_activitylist.setAdapter(adapteractivitylist);
//        if(activityDataBean.getTempleteData()==null||activityDataBean.getTempleteData().size()==0){
//            iv_imagenodata.setVisibility(View.VISIBLE);
//        }else {
//            iv_imagenodata.setVisibility(View.GONE);
//        }
        lv_activitylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MobclickAgent.onEvent(ActivityList.this, "TrainActivity");
                Intent intent = new Intent(ActivityList.this, Double11ActDetailsActivity.class);
                intent.putExtra("activityId", activityDataBean.getTempleteData().get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (viewid == R.id.iv_redshre) {
            toShare();
        } else if (viewid == R.id.iv_back) {
            finish();
        }
    }

    public void toShare() {
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = "";
        shareUrl = "http://m.61park.cn/t/html-sui/doubleOne/activity-index.html";
        showShareDialog(shareUrl, AppUrl.SHARE_APP_ICON, topBannerBean.getPageName(), "");
    }

    /**
     * 初始化引导点
     */
    private void initDots() {
        dots = new ImageView[mListViews.size()];
        // 循环取得小点图片
        for (int i = 0; i < mListViews.size(); i++) {
            dots[i] = (ImageView) top_vp_dot.getChildAt(i);
            dots[i].setVisibility(View.VISIBLE);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
      /*  for (int j = 0; j < top_vp_dot.getChildCount(); j++) {
            top_vp_dot.getChildAt(j).setVisibility(View.GONE);
        }*/
        //currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }
}
