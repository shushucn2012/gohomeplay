package com.park61.teacherhelper.module.my.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.my.adapter.MyCourseListAdapter;
import com.park61.teacherhelper.module.my.adapter.RecentlyCourseListAdapter;
import com.park61.teacherhelper.module.my.bean.RecentlyItemBean;
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
 * 我的课程-最近学习
 */
public class FragmentRecentlyCourse extends BaseFragment {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;

    private List<RecentlyItemBean> mList;
    private RecentlyCourseListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_courselist, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetList();
            }
        });
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        adapter = new RecentlyCourseListAdapter(parentActivity, mList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetList();
    }

    @Override
    public void initListener() {

    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.myCourseList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            List<RecentlyItemBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                mList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(parentActivity, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                mList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), RecentlyItemBean.class));
            }
            mList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };
}
