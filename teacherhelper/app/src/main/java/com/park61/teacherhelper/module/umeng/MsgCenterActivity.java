package com.park61.teacherhelper.module.umeng;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.umeng.adapter.MsgCenterAdapter;
import com.park61.teacherhelper.module.umeng.bean.MsgBean;
import com.park61.teacherhelper.module.workplan.WorkPlanMainActivity;
import com.park61.teacherhelper.module.workplan.adapter.ToDoWorkRvAdapter;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息中心
 * Created by shubei on 2018/6/7.
 */

public class MsgCenterActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 6;
    private int totalPage = 100;

    private View area_nodata;//无数据提示
    private LRecyclerView rv_firsthead;

    private List<MsgBean> sList;
    private MsgCenterAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_msg_center);
    }

    @Override
    public void initView() {
        setPagTitle("消息中心");
        area_nodata = findViewById(R.id.area_nodata);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void initData() {
        sList = new ArrayList<>();
        adapter = new MsgCenterAdapter(mContext, sList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        rv_firsthead.setOnLoadMoreListener(() -> {
            if (PAGE_NUM < totalPage - 1) {
                getNextPage();
            } else {
                rv_firsthead.setNoMore(true);
            }
        });
        rv_firsthead.forceToRefresh();
        asyncUpdateViewStatus();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickedListener(position -> {
            asyncFindByMessageId(sList.get(position).getId());
        });
    }

    /**
     * 刷新列表数据
     */
    public void refreshList() {
        PAGE_NUM = 0;
        //重置可以加载更多
        rv_firsthead.setNoMore(false);
        asyncMsgList();
    }

    /**
     * 获得列表下一页数据
     */
    public void getNextPage() {
        PAGE_NUM++;
        asyncMsgList();
    }

    private void asyncMsgList() {
        String wholeUrl = AppUrl.host + AppUrl.messageList;//"http://10.10.10.18:8380/service/messagePush/messageList";//
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getLsner);
    }

    BaseRequestListener getLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            parseJosnToShow(jsonResult);
        }
    };

    /**
     * 解析列表数据
     */
    private void parseJosnToShow(JSONObject jsonResult) {
        JSONArray jayList = jsonResult.optJSONArray("rows");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 0 && (jayList == null || jayList.length() <= 0)) {
            sList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            area_nodata.setVisibility(View.VISIBLE);
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            sList.clear();
            area_nodata.setVisibility(View.GONE);
        }
        ArrayList<MsgBean> currentPageList = new ArrayList<>();
        totalPage = jsonResult.optInt("pageCount");
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            MsgBean item = gson.fromJson(jot.toString(), MsgBean.class);
            currentPageList.add(item);
        }
        sList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 当前用户消息状态批量更新
     */
    private void asyncUpdateViewStatus() {
        String wholeUrl = AppUrl.host + AppUrl.updateViewStatus;//"http://10.10.10.18:8380/service/messagePush/updateViewStatus";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, new SimpleRequestListener());
    }

    /**
     * 单条消息查看
     */
    private void asyncFindByMessageId(int messageId) {
        String wholeUrl = AppUrl.host + AppUrl.findByMessageId;//"http://10.10.10.18:8380/service/messagePush/findByMessageId";//
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messageId", messageId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }


}
