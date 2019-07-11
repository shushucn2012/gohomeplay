package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.adapter.MyTrainListAdapter;
import com.park61.teacherhelper.module.activity.bean.TrainBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 培训记录列表
 *
 * @author shubei
 * @time 2019/3/8 15:37
 */
public class MyTrainListActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;
    private boolean isFirstIn = true;//是否第一次进入

    private PullToRefreshListView pull_refresh_list;

    private List<TrainBean> dataList;
    private MyTrainListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_train_list);
    }

    @Override
    public void initView() {
        setPagTitle("培训记录");
        pull_refresh_list = findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(pull_refresh_list, mContext);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        dataList = new ArrayList<>();
        adapter = new MyTrainListAdapter(mContext, dataList);
        pull_refresh_list.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        adapter.setOnContactTrainerClickedListener(new MyTrainListAdapter.OnContactTrainerClickedListener() {
            @Override
            public void onClicked(int pos) {
                dDialog.showDialog("提示", "确认拨打" + dataList.get(pos).getTrainerMobile() + "？", "取消", "确认",
                        null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataList.get(pos).getTrainerMobile()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dDialog.dismissDialog();
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncDataList();
    }

    /**
     * 获取培训列表
     */
    private void asyncDataList() {
        String wholeUrl = AppUrl.host + AppUrl.myTrainingRecord;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0 && isFirstIn) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(pull_refresh_list);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            isFirstIn = false;
            ViewInitTool.listStopLoadView(pull_refresh_list);
            List<TrainBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                dataList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, pull_refresh_list.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(pull_refresh_list);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                dataList.clear();
                ViewInitTool.setPullToRefreshViewBoth(pull_refresh_list);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(pull_refresh_list);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), TrainBean.class));
            }
            dataList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };
}
