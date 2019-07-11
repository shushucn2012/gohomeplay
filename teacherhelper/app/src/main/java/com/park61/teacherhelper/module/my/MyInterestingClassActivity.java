package com.park61.teacherhelper.module.my;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.InterestingClassActsAdapter;
import com.park61.teacherhelper.module.my.bean.InterestingClassActBean;
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
 * 我的兴趣课
 * Created by shubei on 2018/8/15.
 */

public class MyInterestingClassActivity extends BaseActivity {

    private final int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;

    private PullToRefreshListView plv_interesting_class;

    private List<InterestingClassActBean> dataList;
    private InterestingClassActsAdapter adapter;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_interesting_class);
    }

    @Override
    public void initView() {
        plv_interesting_class = (PullToRefreshListView) findViewById(R.id.plv_interesting_class);
        ViewInitTool.initPullToRefresh(plv_interesting_class, mContext);

        dataList = new ArrayList<>();
        adapter = new InterestingClassActsAdapter(mContext, dataList);
        plv_interesting_class.setAdapter(adapter);
        plv_interesting_class.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncDataList();
            }
        });
    }

    @Override
    public void initData() {
        setPagTitle("我的兴趣班");
        plv_interesting_class.setRefreshing(true);
    }

    @Override
    public void initListener() {

    }

    /**
     * 查询列表数据
     */
    private void asyncDataList() {
        String wholeUrl = AppUrl.host + AppUrl.myInterestClassActivityList;
        Map<String, Object> map = new HashMap<String, Object>();
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
            plv_interesting_class.onRefreshComplete();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            plv_interesting_class.onRefreshComplete();
            ArrayList<InterestingClassActBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                dataList.clear();
                adapter.notifyDataSetChanged();
                plv_interesting_class.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                ViewInitTool.setListEmptyView(mContext, plv_interesting_class.getRefreshableView(), "暂无数据", R.mipmap.icon_cotent_empty, null);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                dataList.clear();
                plv_interesting_class.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                plv_interesting_class.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), InterestingClassActBean.class));
            }
            dataList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };
}
