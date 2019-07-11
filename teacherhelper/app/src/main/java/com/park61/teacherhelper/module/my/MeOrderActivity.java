package com.park61.teacherhelper.module.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.park61.teacherhelper.module.my.adapter.MeTradeOrderListAdapter;
import com.park61.teacherhelper.module.my.bean.MeOrderInfoBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 课程订单列表
 * Created by shubei on 2018/1/11.
 */
public class MeOrderActivity extends BaseActivity {

    private final int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;

    private View area_error_tip;
    private PullToRefreshListView mPullRefreshListView;
    private ArrayList<MeOrderInfoBean> mList;
    private MeTradeOrderListAdapter mAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_myorders);
    }

    @Override
    public void initView() {
        setPagTitle("我的订单");
        registerRefreshReceiver();
        area_error_tip = findViewById(R.id.area_error_tip);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
    }

    @Override
    public void initData() {
        mList = new ArrayList<MeOrderInfoBean>();
        mAdapter = new MeTradeOrderListAdapter(mContext, mList);
        mPullRefreshListView.setAdapter(mAdapter);

        PAGE_NUM = 0;
        asyncGetOrderList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetOrderList();
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, MeOrderDetailActivity.class);
                it.putExtra("orderId", mList.get(position - 1).getId());
                startActivity(it);
            }
        });
    }

    /**
     * 请求订单列表数据
     */
    private void asyncGetOrderList() {
        String wholeUrl = AppUrl.host + AppUrl.findMyCourseSoList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    JsonRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
            if (PAGE_NUM >= 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshListView.onRefreshComplete();
            dismissDialog();
            JSONArray ordersJay = jsonResult.optJSONArray("rows");
            if (PAGE_NUM == 0 && (ordersJay == null || ordersJay.length() <= 0)) {
                area_error_tip.setVisibility(View.VISIBLE);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            if (PAGE_NUM == 0) {
                mList.clear();
                area_error_tip.setVisibility(View.GONE);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            } else {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            for (int i = 0; i < ordersJay.length(); i++) {
                JSONObject orderJot = ordersJay.optJSONObject(i);
                MeOrderInfoBean meOrderInfoBean = gson.fromJson(orderJot.toString(), MeOrderInfoBean.class);
                mList.add(meOrderInfoBean);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_ORDER");
        mContext.registerReceiver(orderChangeReceiver, filter);
    }

    private BroadcastReceiver orderChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            PAGE_NUM = 0;
            asyncGetOrderList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(orderChangeReceiver);
    }
}
