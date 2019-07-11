package com.park61.teacherhelper.module.course;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.bean.AgeCate;
import com.park61.teacherhelper.module.course.bean.BigCate;
import com.park61.teacherhelper.module.home.adapter.ContentCombineListAdapter;
import com.park61.teacherhelper.module.home.bean.CourseCombine;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.SearchAgePopWin;
import com.park61.teacherhelper.widget.pw.SearchOrderPopWin;
import com.park61.teacherhelper.widget.pw.SearchTypePopWin;
import com.park61.teacherhelper.widget.pw.SearchTypePopWin.OnBigCateSelectLsner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/10/12.
 */

public class CourseSearchResultActivity extends BaseActivity {

    private int PAGE_NUM = 0;// 页码
    private final int PAGE_SIZE = 10;// 每页条数
    private int type;//type  1为教案;3为师训
    private String keyword;
    private long level1CateId = -1;
    private long level2CateId = -1;
    private String tagIds, adapterAges, sortType;
    private boolean isTypeFromCms = false;//是否是cms传来额cate

    private View area_type, area_age, area_order;
    private SearchTypePopWin mSearchTypePopWin;
    private SearchAgePopWin mSearchAgePopWin;
    private SearchOrderPopWin mSearchOrderPopWin;
    private TextView tv_type, tv_age, tv_order, tv_sousuo;
    private ImageView img_type_xiala, img_age_xiala, img_order_xiala;
    private TextView edit_sousuo;

    private List<BigCate> bList = new ArrayList<BigCate>();
    private List<AgeCate> aList = new ArrayList<AgeCate>();
    private List<AgeCate> oList = new ArrayList<AgeCate>();
    private List<CourseSectionBean> courseBeanList;
    private List<CourseCombine> courseCombinesList;
    private PullToRefreshListView mPullRefreshListView;
    private ContentCombineListAdapter adapter;
    private WindowManager.LayoutParams params;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_search_result);
    }

    @Override
    public void initView() {
        area_type = findViewById(R.id.area_type);
        area_age = findViewById(R.id.area_age);
        area_order = findViewById(R.id.area_order);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_order = (TextView) findViewById(R.id.tv_order);
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        edit_sousuo = (TextView) findViewById(R.id.edit_sousuo);
        img_type_xiala = (ImageView) findViewById(R.id.img_type_xiala);
        img_age_xiala = (ImageView) findViewById(R.id.img_age_xiala);
        img_order_xiala = (ImageView) findViewById(R.id.img_order_xiala);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetSearch();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetSearch();
            }
        });
    }

    @Override
    public void initData() {
//        logout("getIntent().getIntExtra(level)===========" + getIntent().getIntExtra("level", -1));
//        logout("getIntent().getIntExtra(id)===========" + getIntent().getIntExtra("id", -1));
        int level = getIntent().getIntExtra("level", -1);
        if (level > -1) {//如果大于-1，代表是从cms进来的，isTypeFromCms置为true
            isTypeFromCms = true;
            if (level == 1) {
                level1CateId = getIntent().getIntExtra("id", -1);
            } else if (level == 2) {
                level2CateId = getIntent().getIntExtra("id", -1);
            }
        }

        keyword = getIntent().getStringExtra("keyword");
        edit_sousuo.setText(keyword);
        courseBeanList = new ArrayList<>();
        courseCombinesList = new ArrayList<>();
        setGoodsToCombList();
        adapter = new ContentCombineListAdapter(mContext, courseCombinesList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetSearch();
    }

    @Override
    public void initListener() {
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(edit_sousuo.getText().toString().trim())) {
                    keyword = edit_sousuo.getText().toString().trim();
                } else {
                    keyword = "";
                }
                PAGE_NUM = 0;
                asyncGetSearch();
            }
        });
        area_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchTypePopWin == null) {
                    asyncGetBigType();
                } else {
                    //mSearchTypePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 0);
                    showSearchTypePW();
                }
            }
        });
        area_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchAgePopWin == null) {
                    asyncGetAgeType();
                } else {
                    //mSearchAgePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
                    showSearchAgePW();
                }
            }
        });
        area_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchOrderPopWin == null) {
                    asyncGetOrderType();
                } else {
                    //mSearchOrderPopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
                    showSearchOrderPW();
                }
            }
        });
        edit_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseSearchActivity.class);
                it.putExtra("keyword", keyword);
                startActivity(it);
                finish();
            }
        });
    }

    private void asyncGetBigType() {
        String wholeUrl = AppUrl.host + AppUrl.contentCategory_list;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, trlistener);
    }

    BaseRequestListener trlistener = new JsonRequestListener() {

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
            bList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                bList.add(gson.fromJson(actJay.optJSONObject(i).toString(), BigCate.class));
            }
            mSearchTypePopWin = new SearchTypePopWin(mContext, bList, 0, new OnBigCateSelectLsner() {
                @Override
                public void onSelect(int pos) {
                    mSearchTypePopWin.initSmallCateList(bList.get(pos).getListContentTag(), 0);
                }
            });

            mSearchTypePopWin.initSmallCateList(bList.get(0).getListContentTag(), 0);

            mSearchTypePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 0);
            findViewById(R.id.cover).setVisibility(View.VISIBLE);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_type_xiala.setImageResource(R.mipmap.icon_xiangshang);

            mSearchTypePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_type.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_type_xiala.setImageResource(R.mipmap.xiala);
                }
            });
            mSearchTypePopWin.getBtnConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchTypePopWin.dismiss();
                    if (isTypeFromCms) {//只要点了确认，从cms带来的参数都清空，isTypeFromCms置为false
                        level1CateId = -1;
                        level2CateId = -1;
                        isTypeFromCms = false;
                    }
                    level1CateId = mSearchTypePopWin.getSelectedBigCate();
                    tagIds = mSearchTypePopWin.getSelectedTags();
                    PAGE_NUM = 0;
                    asyncGetSearch();
                }
            });
        }
    };

    public void showSearchTypePW() {
        mSearchTypePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 0);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_type.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_type_xiala.setImageResource(R.mipmap.icon_xiangshang);
    }

    public void showSearchAgePW() {
        mSearchAgePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_age.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_age_xiala.setImageResource(R.mipmap.icon_xiangshang);
    }

    public void showSearchOrderPW() {
        mSearchOrderPopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_order.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_order_xiala.setImageResource(R.mipmap.icon_xiangshang);
    }

    private void asyncGetAgeType() {
        String wholeUrl = AppUrl.host + AppUrl.dict_list;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "teach_g_class_type");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

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
            aList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                aList.add(gson.fromJson(actJay.optJSONObject(i).toString(), AgeCate.class));
            }
            mSearchAgePopWin = new SearchAgePopWin(mContext, aList, new SearchAgePopWin.OnBigCateSelectLsner() {
                @Override
                public void onSelect(int pos) {
                    mSearchAgePopWin.dismiss();
                    adapterAges = mSearchAgePopWin.getSelectedTags();
                    PAGE_NUM = 0;
                    asyncGetSearch();
                }
            });

            mSearchAgePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);

            findViewById(R.id.cover).setVisibility(View.VISIBLE);
            tv_age.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_age_xiala.setImageResource(R.mipmap.icon_xiangshang);

            mSearchAgePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_age.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_age_xiala.setImageResource(R.mipmap.xiala);
                }
            });
        }
    };

    private void asyncGetOrderType() {
        String wholeUrl = AppUrl.host + AppUrl.dict_list;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "teach_seach_type");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, olistener);
    }

    BaseRequestListener olistener = new JsonRequestListener() {

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
            oList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                oList.add(gson.fromJson(actJay.optJSONObject(i).toString(), AgeCate.class));
            }
            mSearchOrderPopWin = new SearchOrderPopWin(mContext, oList, 0, new SearchOrderPopWin.OnBigCateSelectLsner() {
                @Override
                public void onSelect(int pos) {
                    mSearchOrderPopWin.dismiss();
                    sortType = oList.get(pos).getValue();
                    tv_order.setText(oList.get(pos).getLabel());
                    PAGE_NUM = 0;
                    asyncGetSearch();
                }
            });

            mSearchOrderPopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);

            findViewById(R.id.cover).setVisibility(View.VISIBLE);
            tv_order.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_order_xiala.setImageResource(R.mipmap.icon_xiangshang);
            mSearchOrderPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_order.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_order_xiala.setImageResource(R.mipmap.xiala);
                }
            });
        }
    };

    /**
     * 把商品列表的数据填充到商品联合bean列表
     */
    public void setGoodsToCombList() {
        courseCombinesList.clear();
        for (int i = 0; i < courseBeanList.size(); i = i + 2) {
            CourseCombine comb = new CourseCombine();
            if (courseBeanList.get(i) != null)
                comb.setCourseBeanLeft(courseBeanList.get(i));
            if (i + 1 < courseBeanList.size() && courseBeanList.get(i + 1) != null)
                comb.setCourseBeanRight(courseBeanList.get(i + 1));
            courseCombinesList.add(comb);
        }
    }

    private void asyncGetSearch() {
        String wholeUrl = AppUrl.host + AppUrl.search;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        if (level1CateId != -1) {
            map.put("level1CateId", level1CateId);
        }
        if (level2CateId != -1) {
            map.put("level2CateId", level2CateId);
        }
        if (!TextUtils.isEmpty(tagIds)) {
            map.put("tagIds", tagIds);
        }
        if (!TextUtils.isEmpty(adapterAges)) {
            map.put("adapterAge", adapterAges);
        }
        if (!TextUtils.isEmpty(sortType)) {
            map.put("sortType", sortType);
        }
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, sxlistener);
    }

    BaseRequestListener sxlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<CourseSectionBean> currentPageList = new ArrayList<CourseSectionBean>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                courseBeanList.clear();
                courseCombinesList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                courseBeanList.clear();
                courseCombinesList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                CourseSectionBean pl = gson.fromJson(actJot.toString(), CourseSectionBean.class);
                currentPageList.add(pl);
            }
            courseBeanList.addAll(currentPageList);
            setGoodsToCombList();
            adapter.notifyDataSetChanged();

            SharedPreferences searchListSp = getSharedPreferences("SEARCH_LIST", Activity.MODE_PRIVATE);
            String his = searchListSp.getString("SEARCH_LIST", "");
            if (TextUtils.isEmpty(his)) {
                his += keyword;
            } else {
                his += "," + keyword;
            }
            searchListSp.edit().putString("SEARCH_LIST", his).commit();
        }
    };
}
