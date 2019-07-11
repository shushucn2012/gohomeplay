package com.park61.teacherhelper.module.course;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.adapter.ExpertCourseAdapter;
import com.park61.teacherhelper.module.home.bean.ExpertCourseBean;
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
 * 幼教讲堂
 * Created by shubei on 2018/5/21.
 */

public class ExpertCourseMainActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;

    private ExpertCourseAdapter mAdapter;
    private List<ExpertCourseBean> dataList;
    private int couponId;//优惠券id，过滤可用课程

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_expert_course_main);
    }

    @Override
    public void initView() {
        setPagTitle("幼教讲堂");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
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
    }

    @Override
    public void initData() {
        couponId = getIntent().getIntExtra("couponId", -1);
        if (couponId > 0) {
            setPagTitle("可用课程");
        }
        dataList = new ArrayList<ExpertCourseBean>();
        mAdapter = new ExpertCourseAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
        mPullRefreshListView.setRefreshing(true);
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, ExpertCourseListActivity.class);
                it.putExtra("courseId", dataList.get(position - 1).getId());
                startActivity(it);
            }
        });
    }

    /**
     * 请求数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.trainerCourseList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        if (couponId > 0) {
            map.put("couponId", couponId);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshListView.onRefreshComplete();
            ArrayList<ExpertCourseBean> currentPageList = new ArrayList<ExpertCourseBean>();
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
                ExpertCourseBean c = gson.fromJson(actJot.toString(), ExpertCourseBean.class);
                currentPageList.add(c);
            }
            dataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
}
