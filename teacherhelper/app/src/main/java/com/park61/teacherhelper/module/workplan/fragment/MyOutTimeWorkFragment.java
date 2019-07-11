package com.park61.teacherhelper.module.workplan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.workplan.LeftMsgActivity;
import com.park61.teacherhelper.module.workplan.WorkAdjustActivity;
import com.park61.teacherhelper.module.workplan.WorkPlanMainActivity;
import com.park61.teacherhelper.module.workplan.adapter.OutTimeWorkRvAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskCalendarListBean;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.FastClickUtils.isFastClick;

/**
 * 行事历已完成fragment
 * modify by super 20181034
 */
public class MyOutTimeWorkFragment extends BaseFragment {

    //private PullToRefreshListView mPullRefreshListView;
    private View area_nodata;//无数据提示
    private LRecyclerView rv_firsthead;
    private Button btn_done;

    private List<WorkMonthBean> list;
    //private OutTimeWorkListAdapter adapter;
    private OutTimeWorkRvAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_mywork_rv_done, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        area_nodata = parentView.findViewById(R.id.area_nodata);
       /* mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        list = new ArrayList<>();
        adapter = new OutTimeWorkListAdapter(parentActivity, list);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncWorkList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPullRefreshListView.onRefreshComplete();
            }
        });*/
        rv_firsthead = (LRecyclerView) parentView.findViewById(R.id.rv_firsthead);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(parentActivity));
        btn_done = (Button) parentView.findViewById(R.id.btn_done);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        adapter = new OutTimeWorkRvAdapter(parentActivity, list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncWorkList();
                ((WorkPlanMainActivity) parentActivity).asyncGetTaskCount();//重刷数量
            }
        });
        rv_firsthead.forceToRefresh();
    }

    @Override
    public void initListener() {
        adapter.setOnItemOperateClickListener(new OutTimeWorkRvAdapter.OnItemOperateClickListener() {
            @Override
            public void onLeftMsgClicked(int id) {
                Intent it = new Intent(parentActivity, LeftMsgActivity.class);
                it.putExtra("taskCalendarId", id);
                startActivity(it);
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastClick()) {
                    return;
                }
                String ids = "";
                for (int i = 0; i < list.size(); i++) {
                    WorkMonthBean wmb = list.get(i);
                    for (int j = 0; j < wmb.getTaskCalendarList().size(); j++) {
                        TaskCalendarListBean taskBean = wmb.getTaskCalendarList().get(j);
                        if (taskBean.getIsKeyView() == 0) {
                            ids += taskBean.getId() + ",";
                        } else {
                            if (taskBean.getIsEnableCheck() == 1) {
                                ids += taskBean.getId() + ",";
                            }
                        }
                    }
                }
                if (ids.endsWith(",")) {//有数据
                    ids = ids.substring(0, ids.length() - 1);
                } else {//一条可执行的任务也没有
                    showShortToast("没有可完成的任务");
                    return;
                }
                asyncDoneAll(ids);
            }
        });
    }

    /**
     * 查询列表数据
     */
    private void asyncWorkList() {
        String wholeUrl = AppUrl.host + AppUrl.getUserTaskListByStatus;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 2);//状态：0待办，1已完成
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            rv_firsthead.refreshComplete(50);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_firsthead.refreshComplete(50);
            ArrayList<WorkMonthBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (actJay == null || actJay.length() <= 0) {
                list.clear();
                adapter.notifyDataSetChanged();
                area_nodata.setVisibility(View.VISIBLE);
                btn_done.setVisibility(View.GONE);
                return;
            }
            list.clear();
            btn_done.setVisibility(View.GONE);
            area_nodata.setVisibility(View.GONE);
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject couponJot = actJay.optJSONObject(i);
                WorkMonthBean workMonthBean = gson.fromJson(couponJot.toString(), WorkMonthBean.class);
                currentPageList.add(workMonthBean);
            }
            list.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 完成任务/重启任务
     */
    private void asyncDone(int id) {
        String wholeUrl = AppUrl.host + AppUrl.restartTask;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("status", "1");//状态（1完成0重启）
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            //失败也刷数据，延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
                    ((WorkPlanMainActivity) parentActivity).asyncGetTaskCount();//完成或重启任务重刷数量
                }
            }, 500);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
            //延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((WorkPlanMainActivity) parentActivity).asyncGetTaskCount();//完成或重启任务重刷数量
                }
            }, 500);

        }
    };

    /**
     * 批量完成yu期任务
     */
    private void asyncDoneAll(String ids) {
        String wholeUrl = AppUrl.host + AppUrl.batchFinishDueTask;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarIds", ids);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

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
            parentActivity.sendBroadcast(new Intent("ACTION_REFRESH_WORK"));//切换通知fragment列表刷新
            //延迟一点避免服务器数据还未处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((WorkPlanMainActivity) parentActivity).asyncGetTaskCount();//完成或重启任务重刷数量
                }
            }, 500);
        }
    };

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_WORK");
        parentActivity.registerReceiver(workChangeReceiver, filter);
    }

    private BroadcastReceiver workChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncWorkList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentActivity.unregisterReceiver(workChangeReceiver);
    }

}
