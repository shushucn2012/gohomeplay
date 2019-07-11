package com.park61.teacherhelper.module.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.module.course.bean.BigCate;
import com.park61.teacherhelper.module.course.fragment.FragmentCourseListByCate;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.viewpager.CourseListPagerSlidingTabStrip;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章分类列表页面
 * Created by shubei on 2018/11/2.
 */

public class CourseListByCateActivity extends BaseActivity {

    private CourseListPagerSlidingTabStrip tabs;// PagerSlidingTabStrip的实例
    private ViewPager pager;
    private CardView cardview_big_class, cardview_mid_class, cardview_small_class;
    private TextView tv_big_class, tv_mid_class, tv_small_class;

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private List<BigCate> cates = new ArrayList<>();
    private int id;//传过来的二级类目id
    private int level;//传过来的类目级别
    private int curSelectItemIndex = 0;//当前选中项
    public int classValue = 0;//小班1，中班2，大班3，全部1，2，3

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_courselist_by_cate);
    }

    @Override
    public void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (CourseListPagerSlidingTabStrip) findViewById(R.id.tabs);
        cardview_big_class = findViewById(R.id.cardview_big_class);
        cardview_mid_class = findViewById(R.id.cardview_mid_class);
        cardview_small_class = findViewById(R.id.cardview_small_class);

        tv_big_class = findViewById(R.id.tv_big_class);
        tv_mid_class = findViewById(R.id.tv_mid_class);
        tv_small_class = findViewById(R.id.tv_small_class);
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra("id", -1);
        level = getIntent().getIntExtra("level", -1);
        if (level != 2) {
            id = 0;
        }
        asyncGetMyLabel();
    }

    @Override
    public void initListener() {
        cardview_big_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classValue != 3) {
                    tv_big_class.setTextColor(getResources().getColor(R.color.com_orange));
                    tv_mid_class.setTextColor(getResources().getColor(R.color.g333333));
                    tv_small_class.setTextColor(getResources().getColor(R.color.g333333));
                    classValue = 3;
                } else {
                    tv_big_class.setTextColor(getResources().getColor(R.color.g333333));
                    classValue = 0;
                }
                EventBus.getDefault().post(new MessageEvent("REFRESH_LIST_BY_CATE", pager.getCurrentItem()));
            }
        });
        cardview_mid_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classValue != 2) {
                    tv_big_class.setTextColor(getResources().getColor(R.color.g333333));
                    tv_mid_class.setTextColor(getResources().getColor(R.color.com_orange));
                    tv_small_class.setTextColor(getResources().getColor(R.color.g333333));
                    classValue = 2;
                } else {
                    tv_mid_class.setTextColor(getResources().getColor(R.color.g333333));
                    classValue = 0;
                }
                EventBus.getDefault().post(new MessageEvent("REFRESH_LIST_BY_CATE", pager.getCurrentItem()));
            }
        });
        cardview_small_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classValue != 1) {
                    tv_big_class.setTextColor(getResources().getColor(R.color.g333333));
                    tv_mid_class.setTextColor(getResources().getColor(R.color.g333333));
                    tv_small_class.setTextColor(getResources().getColor(R.color.com_orange));
                    classValue = 1;
                } else {
                    tv_small_class.setTextColor(getResources().getColor(R.color.g333333));
                    classValue = 0;
                }
                EventBus.getDefault().post(new MessageEvent("REFRESH_LIST_BY_CATE", pager.getCurrentItem()));
            }
        });
        findViewById(R.id.edit_sousuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CourseSearchActivity.class));
            }
        });
    }

    private void asyncGetMyLabel() {
        String wholeUrl = AppUrl.host + AppUrl.level2list;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, rlistener);
    }

    BaseRequestListener rlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                fragmentList.clear();
                cates.clear();
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject cateJot = actJay.optJSONObject(i);
                    BigCate bigCate = gson.fromJson(cateJot.toString(), BigCate.class);
                    cates.add(bigCate);

                    if (id == bigCate.getId()) {
                        curSelectItemIndex = i;
                    }

                    FragmentCourseListByCate fragmentItem = new FragmentCourseListByCate();
                    Bundle data = new Bundle();
                    data.putString("level2CateId", bigCate.getId() + "");
                    data.putInt("indexInViewPager", i);
                    fragmentItem.setArguments(data);
                    fragmentList.add(fragmentItem);
                }
            }

            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(pager);
            tabs.setTextSize(DevAttr.dip2px(mContext, 14));
            tabs.setTextColor(getResources().getColor(R.color.g333333));
            tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
            pager.setCurrentItem(curSelectItemIndex);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return cates.get(position).getName();
        }

        @Override
        public int getCount() {
            return cates.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
