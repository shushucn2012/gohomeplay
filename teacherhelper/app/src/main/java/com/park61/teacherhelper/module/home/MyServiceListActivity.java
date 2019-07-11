package com.park61.teacherhelper.module.home;

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
import com.park61.teacherhelper.module.home.adapter.MyServiceListAdapter;
import com.park61.teacherhelper.module.home.bean.MServiceItem;
import com.park61.teacherhelper.module.my.adapter.PublishListAdapter;
import com.park61.teacherhelper.module.my.bean.PublishItem;
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
 * 我的服务列表页面
 * Created by shubei on 2018/3/12.
 */
public class MyServiceListActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;
    private List<MServiceItem> mList;

    private PullToRefreshListView mPullRefreshListView;
    private MyServiceListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_myservice_list);
    }

    @Override
    public void initView() {
        setPagTitle("我的服务");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetServiceList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetServiceList();
            }
        });
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        adapter = new MyServiceListAdapter(mContext, mList);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        adapter.setOnConfirmClickedLsner(new MyServiceListAdapter.OnConfirmClickedLsner() {
            @Override
            public void onCliked(int index) {
                dDialog.showDialog("提示", "请确认服务已经完成", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        anyscConfirmFinish(mList.get(index).getId());
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetServiceList();
    }

    /**
     * 请求获取服务列表
     */
    private void asyncGetServiceList() {
        String wholeUrl = AppUrl.host + AppUrl.getMyServiceList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
            ArrayList<MServiceItem> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                mList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
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
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), MServiceItem.class));
            }
            mList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 请求确认完成
     */
    public void anyscConfirmFinish(int applyId) {
        String wholeUrl = AppUrl.host + AppUrl.changeStatusToFinish;
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, clisenter);
    }

    BaseRequestListener clisenter = new JsonRequestListener() {

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
            showShortToast("确认成功");
            PAGE_NUM = 0;
            asyncGetServiceList();
        }
    };
}
