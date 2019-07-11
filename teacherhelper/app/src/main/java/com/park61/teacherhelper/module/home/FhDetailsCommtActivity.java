package com.park61.teacherhelper.module.home;

import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.home.bean.CommentItemBean;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.adapter.CommentAllAdapter;
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
 * Created by shubei on 2017/6/12.
 */

public class FhDetailsCommtActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;

    private int itemId;
    private CommentAllAdapter mAdapter;
    private List<CommentItemBean> mList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fhdetails_commt);
    }

    @Override
    public void initView() {
        setPagTitle("评论");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        itemId = getIntent().getIntExtra("itemId", -1);
        mList = new ArrayList<>();
        mAdapter = new CommentAllAdapter(this, mList);
        actualListView.setAdapter(mAdapter);
        asyncCommtData();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncCommtData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncCommtData();

            }
        });
    }

    /**
     * 请求评论数据
     */
    private void asyncCommtData() {
        String wholeUrl = AppUrl.host + AppUrl.addContentComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("sort", "create_date");
        map.put("order", "desc");
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            Toast.makeText(FhDetailsCommtActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshListView.onRefreshComplete();
            ArrayList<CommentItemBean> currentPageList = new ArrayList<CommentItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                mList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            if (cmtJay != null) {
                for (int i = 0; i < cmtJay.length(); i++) {
                    JSONObject actJot = cmtJay.optJSONObject(i);
                    CommentItemBean p = gson.fromJson(actJot.toString(), CommentItemBean.class);
                    currentPageList.add(p);
                }
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

}
