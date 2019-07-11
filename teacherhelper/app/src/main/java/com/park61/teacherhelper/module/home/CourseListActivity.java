package com.park61.teacherhelper.module.home;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.adapter.CourseCombineListAdapter;
import com.park61.teacherhelper.module.home.bean.CourseCombine;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/8/16.
 */

public class CourseListActivity extends BaseActivity {

    private int PAGE_NUM = 0;// 页码
    private final int PAGE_SIZE = 10;// 每页条数
    private int type;//type  1为教案;3为师训

    private List<CourseSectionBean> courseBeanList;
    private List<CourseCombine> courseCombinesList;
    private PullToRefreshListView mPullRefreshListView;
    private CourseCombineListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_courselist);
    }

    @Override
    public void initView() {
        setPagTitle("课程");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetCommendShixun();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetCommendShixun();
            }
        });
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            setPagTitle("教案");
        } else {
            setPagTitle("师训");
        }
        courseBeanList = new ArrayList<>();
        courseCombinesList = new ArrayList<>();
        setGoodsToCombList();
        adapter = new CourseCombineListAdapter(mContext, courseCombinesList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetCommendShixun();
    }

    @Override
    public void initListener() {
        area_right.setVisibility(View.VISIBLE);
    }

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

    private void asyncGetCommendShixun() {
        String wholeUrl = AppUrl.host + AppUrl.teachPromPostion_more;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);//type  1为教案;3为师训
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
        }
    };
}
