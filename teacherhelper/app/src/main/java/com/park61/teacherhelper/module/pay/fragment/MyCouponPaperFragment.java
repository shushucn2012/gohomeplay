package com.park61.teacherhelper.module.pay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.pay.adapter.MyCouponListAdapter;
import com.park61.teacherhelper.module.pay.bean.CouponBean;
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
 * 我的优惠券fragment
 * modify by super 20181034
 */
public class MyCouponPaperFragment extends BaseFragment {

    private final int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;

    private PullToRefreshListView mPullRefreshListView;
    private View area_nodata;//无数据提示
    private View footView;

    private List<CouponBean> list;
    private MyCouponListAdapter adapter;
    private int useType;//优惠券使用状态（0：未使用 1 已使用 2 已过期）
    private String endTip;//list到底提示

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_mycoupon, container, false);
        useType = getArguments().getInt("useType");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        area_nodata = parentView.findViewById(R.id.area_nodata);
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        list = new ArrayList<>();
        adapter = new MyCouponListAdapter(parentActivity, list, 1, useType);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncCouponList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncCouponList();
            }
        });
        if (useType == 0) {
            endTip = "没有更多了";
        } else if (useType == 1) {
            endTip = "以上是近期已使用的优惠券";
        } else {
            endTip = "以上是近期已过期的优惠券";
        }

        footView = LayoutInflater.from(parentActivity).inflate(R.layout.listview_foot, null);
        ((TextView) footView.findViewById(R.id.tv_end_tip)).setText(endTip);
    }

    @Override
    public void initData() {
        mPullRefreshListView.setRefreshing(true);
    }

    @Override
    public void initListener() {
    }

    /**
     * 查询优惠券列表数据
     */
    private void asyncCouponList() {
        String wholeUrl = AppUrl.host + AppUrl.getUserCouponList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("useType", useType);
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
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
            ArrayList<CouponBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                list.clear();
                adapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
                area_nodata.setVisibility(View.VISIBLE);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                list.clear();
                mPullRefreshListView.setMode(Mode.BOTH);
                area_nodata.setVisibility(View.GONE);
                if (mPullRefreshListView.getRefreshableView().getFooterViewsCount() > 1) {//移除到底提示
                    mPullRefreshListView.getRefreshableView().removeFooterView(footView);
                }
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
                if (mPullRefreshListView.getRefreshableView().getFooterViewsCount() == 1) {//添加到底提示
                    mPullRefreshListView.getRefreshableView().addFooterView(footView);
                }
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject couponJot = actJay.optJSONObject(i);
                CouponBean g = gson.fromJson(couponJot.toString(), CouponBean.class);
                currentPageList.add(g);
            }
            list.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

}
