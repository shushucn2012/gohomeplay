package com.park61.teacherhelper.module.activity;

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
import com.park61.teacherhelper.module.activity.adapter.Double11TicketListAdapter;
import com.park61.teacherhelper.module.activity.bean.Double11TicketBean;
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
 * 双11入场证列表页面
 * Created by shubei on 2017/11/2.
 */
public class Double11ActTicketActivity extends BaseActivity {

    private int PAGE_NUM = 0; //当前页码
    private final int PAGE_SIZE = 10;//每页数量
    private List<Double11TicketBean> dList;//列表数据

    private PullToRefreshListView mPullRefreshListView;//拉下上拉列表控件
    private Double11TicketListAdapter adapter;//列表数据适配器

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_double11_act_ticket);
    }

    @Override
    public void initView() {
        setPagTitle("我的入场证");

        //初始化拉下上拉列表控件
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                PAGE_NUM = 0;
                asyncMyActivityApplyList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                PAGE_NUM++;
                asyncMyActivityApplyList();
            }
        });
    }

    @Override
    public void initData() {
        //初始化列表数据
        dList = new ArrayList<>();
        adapter = new Double11TicketListAdapter(mContext, dList);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncMyActivityApplyList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //列表item点击到详情
                Intent it = new Intent(mContext, Double11ActTicketDetailsActivity.class);
                it.putExtra("activityApplyId", dList.get(position - 1).getId());
                startActivity(it);
            }
        });
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMe();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMe();
    }

    /**
     * 请求列表数据
     */
    private void asyncMyActivityApplyList() {
        String wholeUrl = AppUrl.host + AppUrl.myActivityApplyList;//"http://192.168.100.13:8080/mockjsdata/6/service/activity/myActivityApplyList";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

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
            ArrayList<Double11TicketBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                dList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                findViewById(R.id.root).setBackgroundColor(mContext.getResources().getColor(R.color.bg_color));
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                dList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), Double11TicketBean.class));
            }
            dList.addAll(currentPageList);
            //数据加载完后刷新列表
            adapter.notifyDataSetChanged();
        }
    };
}
