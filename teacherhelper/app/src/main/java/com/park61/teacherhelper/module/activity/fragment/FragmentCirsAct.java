package com.park61.teacherhelper.module.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.activity.ChristmasActListActivity;
import com.park61.teacherhelper.module.activity.adapter.ChristmasActListRvAdapter;
import com.park61.teacherhelper.module.activity.bean.ChristmasActItem;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/8/30.
 */

public class FragmentCirsAct extends BaseFragment {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    private ChristmasActListRvAdapter mRecyclerViewAdapter;
    private List<ChristmasActItem> christmasActItemList = new ArrayList<>();
    ;
    private int sortType = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_cirs_act_newest, container, false);
        sortType = getArguments().getInt("sortType");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) parentView.findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数
//        mPullLoadMoreRecyclerView.setFooterViewText("loading");
        mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.color_text_red_deep);
//        mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.g000000);
        ((StaggeredGridLayoutManager) mPullLoadMoreRecyclerView.getLayoutManager()).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        asyncGetList();
    }*/

    @Override
    public void initData() {

        mRecyclerViewAdapter = new ChristmasActListRvAdapter(parentActivity, christmasActItemList);
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        asyncGetList();
    }

    @Override
    public void initListener() {
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }

            @Override
            public void onLoadMore() {
                nextList();
            }
        });
        mPullLoadMoreRecyclerView.getRecyclerView().addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int[] firstVisibleItem = null;
                firstVisibleItem = ((StaggeredGridLayoutManager) mPullLoadMoreRecyclerView.getLayoutManager()).findFirstVisibleItemPositions(firstVisibleItem);
                if (firstVisibleItem != null && firstVisibleItem[0] < 3) {
                    ((StaggeredGridLayoutManager) mPullLoadMoreRecyclerView.getLayoutManager()).invalidateSpanAssignments();
                }
            }
        });
    }

    public void refreshList() {
        mPullLoadMoreRecyclerView.scrollToTop();
        PAGE_NUM = 0;
        asyncGetList();
    }

    public void nextList() {
        PAGE_NUM++;
        asyncGetList();
    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.christmasContent;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(((ChristmasActListActivity) parentActivity).keyword)) {
            map.put("keyword", ((ChristmasActListActivity) parentActivity).keyword);
        }
        map.put("sortType", sortType);
        map.put("teachActivityId", ((ChristmasActListActivity) parentActivity).activityId);

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
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            List<ChristmasActItem> currentPageList = new ArrayList<>();
            JSONArray jay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (jay == null || jay.length() <= 0)) {
                christmasActItemList.clear();
                mRecyclerViewAdapter.notifyDataSetChanged();
                mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
                parentView.findViewById(R.id.area_empty).setVisibility(View.VISIBLE);
                return;
            }
            if (PAGE_NUM == 0) {
                parentView.findViewById(R.id.area_empty).setVisibility(View.GONE);
                christmasActItemList.clear();
            }
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
            } else {
                mPullLoadMoreRecyclerView.setPushRefreshEnable(true);
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ChristmasActItem itemInfo = gson.fromJson(jot.toString(), ChristmasActItem.class);
                currentPageList.add(itemInfo);
            }
            int startPos = christmasActItemList.size();
            christmasActItemList.addAll(currentPageList);
            if (PAGE_NUM == 0) {
                mRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                mRecyclerViewAdapter.notifyItemInserted(startPos);
            }
        }
    };
}
