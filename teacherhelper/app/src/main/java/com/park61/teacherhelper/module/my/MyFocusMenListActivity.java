package com.park61.teacherhelper.module.my;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.MyFocusExpertListAdapter;
import com.park61.teacherhelper.module.my.bean.MyfocusExpertBean;
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
 * 我的关注的专家
 * Created by shubei on 2017/7/5.
 */

public class MyFocusMenListActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private MyFocusExpertListAdapter adapter;
    private List<MyfocusExpertBean> eList;
    private int curClickedExpertIndex;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_myfuns_list);
    }

    @Override
    public void initView() {
        setPagTitle("我关注的专家");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetFansList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetFansList();
            }
        });
    }

    @Override
    public void initData() {
        eList = new ArrayList<>();
        adapter = new MyFocusExpertListAdapter(mContext, eList);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetFansList();
    }

    @Override
    public void initListener() {
        adapter.setOnFocusClickedLsner(new MyFocusExpertListAdapter.OnFocusClickedLsner() {
            @Override
            public void onFocus(int position) {
                curClickedExpertIndex = position;
                dDialog.showDialog("提示", "确定不再关注此人？", "取消", "确认", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncUnCollectData();
                        dDialog.dismissDialog();
                    }
                });
            }
        });
    }

    private void asyncGetFansList() {
        String wholeUrl = AppUrl.host + AppUrl.myFocusTrainer;
        Map<String, Object> map = new HashMap<String, Object>();
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
            List<MyfocusExpertBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                eList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                eList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("total") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), MyfocusExpertBean.class));
            }
            eList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    private void asyncUnCollectData() {
        String wholeUrl = AppUrl.host + AppUrl.unfollowTeacher;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerId", eList.get(curClickedExpertIndex).getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cLsner);
    }

    BaseRequestListener cLsner = new JsonRequestListener() {

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
            if (PAGE_NUM > 0) { //大于第一页的时候直接更新不请求数据
                eList.remove(curClickedExpertIndex);
                adapter.notifyDataSetChanged();
            } else { //刷请求
                asyncGetFansList();
            }
        }
    };
}
