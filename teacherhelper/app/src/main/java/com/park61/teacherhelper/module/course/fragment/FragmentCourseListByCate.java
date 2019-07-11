package com.park61.teacherhelper.module.course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.broadcast.BCSkListRefresh;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.CourseListByCateActivity;
import com.park61.teacherhelper.module.course.adapter.MyCollectCourseListAdapter;
import com.park61.teacherhelper.module.home.adapter.ShowRvAdapter;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/8/29.
 */

public class FragmentCourseListByCate extends BaseFragment {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;
    private int totalPage = 100;

    private LRecyclerView rv_firsthead;
    private LinearLayoutManager linearLayoutManager;

    private List<ContentItem> sList;
    private ShowRvAdapter mShowRvAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String level2CateId;
    private int indexInViewPager;//序号

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_courselist_by_cate, container, false);
        level2CateId = getArguments().getString("level2CateId");
        indexInViewPager = getArguments().getInt("indexInViewPager");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        rv_firsthead = (LRecyclerView) parentView.findViewById(R.id.rv_firsthead);
        linearLayoutManager = new LinearLayoutManager(parentActivity);
        rv_firsthead.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void initData() {
        sList = new ArrayList<>();
        mShowRvAdapter = new ShowRvAdapter(parentActivity, sList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mShowRvAdapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        asyncGetMoreData();
    }

    @Override
    public void initListener() {
        rv_firsthead.setOnRefreshListener(() -> {
            refreshList();
        });
        rv_firsthead.setPullRefreshEnabled(true);
        rv_firsthead.setOnLoadMoreListener(() -> {
            if (PAGE_NUM < totalPage - 1) {
                getNextPage();
            } else {
                rv_firsthead.setNoMore(true);
            }
        });
    }

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
    }

    /**
     * 获得更多内容列表数据
     */
    private void asyncGetMoreData() {
        String wholeUrl = AppUrl.host + AppUrl.searchContentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);

        String classValueStr = "";
        if (((CourseListByCateActivity) parentActivity).classValue == 0)
            classValueStr = "1,2,3";
        else
            classValueStr = ((CourseListByCateActivity) parentActivity).classValue + "";
        map.put("adaptAge", classValueStr);

        if (FU.paseInt(level2CateId) > 0)
            map.put("level2CateId", level2CateId);
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
            parentView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            return;
        } else {
            parentView.findViewById(R.id.empty_view).setVisibility(View.GONE);
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            sList.clear();
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

    @Subscribe
    public void onMessage(MessageEvent event) {
        if ("REFRESH_LIST_BY_CATE".equals(event.getMessage()) &&
                (indexInViewPager == event.getArg() || indexInViewPager == event.getArg() + 1 || indexInViewPager == event.getArg() - 1)) {
            refreshList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
