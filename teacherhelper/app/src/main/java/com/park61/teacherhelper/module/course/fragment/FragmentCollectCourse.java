package com.park61.teacherhelper.module.course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.broadcast.BCSkListRefresh;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.adapter.MyCollectCourseListAdapter;
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
 * Created by shubei on 2017/8/29.
 */

public class FragmentCollectCourse extends BaseFragment {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private BCSkListRefresh mBCSkListRefresh;

    private PullToRefreshListView mPullRefreshListView;

    private MyCollectCourseListAdapter mAdapter;
    private List<CourseSectionBean> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_my_collect_course, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
    }

    @Override
    public void initData() {
        dataList = new ArrayList<CourseSectionBean>();
        mAdapter = new MyCollectCourseListAdapter(parentActivity, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(parentActivity, mPullRefreshListView.getRefreshableView());
        asyncGetDataList();
    }

    @Override
    public void initListener() {
        mBCSkListRefresh = new BCSkListRefresh(new BCSkListRefresh.OnReceiveDoneLsner() {
            @Override
            public void onGot(Intent intent) {
                PAGE_NUM = 0;
                asyncGetDataList();
            }
        });
        mBCSkListRefresh.registerReceiver(parentActivity);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetDataList();
            }
        });
        mAdapter.setOnOperateClickedLsner(new MyCollectCourseListAdapter.OnOperateClickedLsner() {
            @Override
            public void onCancel(int pos) {
                asyncCancelCollect(dataList.get(pos).getTeachCourseId());
            }

            @Override
            public void onTop(int pos) {
                asyncSetTop(dataList.get(pos).getId());
            }
        });
    }

    /**
     * 请求数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.listTeachMyCourse;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 1);//type:1教案，3课程
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            ArrayList<CourseSectionBean> currentPageList = new ArrayList<CourseSectionBean>();
            JSONArray dataJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (dataJay == null || dataJay.length() <= 0)) {
                dataList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                dataList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject actJot = dataJay.optJSONObject(i);
                CourseSectionBean c = gson.fromJson(actJot.toString(), CourseSectionBean.class);
                currentPageList.add(c);
            }
            dataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    private void asyncSetTop(int id) {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_stick;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

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
            PAGE_NUM = 0;
            asyncGetDataList();
        }
    };

    private void asyncCancelCollect(int teachCourseId) {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_cancel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teachCourseId", teachCourseId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

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
            PAGE_NUM = 0;
            asyncGetDataList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBCSkListRefresh.unregisterReceiver(parentActivity);
    }
}
