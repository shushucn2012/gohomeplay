package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.adapter.ServiceApplyListAdapter;
import com.park61.teacherhelper.module.activity.bean.ServiceApplyBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的培训提报列表
 *
 * @author shubei
 * @time 2019/3/8 10:22
 */
public class MyTrainApplyListActivity extends BaseActivity {

    private PullToRefreshListView pull_refresh_list;

    private List<ServiceApplyBean> dataList;
    private ServiceApplyListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_apply_list);
    }

    @Override
    public void initView() {
        setPagTitle("提报记录");
        pull_refresh_list = findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(pull_refresh_list, mContext);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    @Override
    public void initData() {
        dataList = new ArrayList<>();
        adapter = new ServiceApplyListAdapter(mContext, dataList);
        pull_refresh_list.setAdapter(adapter);
        asyncDataList();
    }

    @Override
    public void initListener() {
        pull_refresh_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, MyTrainApplyActivity.class);
                intent.putExtra("intensifiedTrainingId", dataList.get(position - 1).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 请求列表数据
     */
    private void asyncDataList() {
        String wholeUrl = AppUrl.host + AppUrl.myIntensifiedTraining;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(pull_refresh_list);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(pull_refresh_list);
            JSONArray actJay = jsonResult.optJSONArray("list");
            dataList.clear();
            if (actJay == null || actJay.length() <= 0) {
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, pull_refresh_list.getRefreshableView());
                return;
            }
            for (int i = 0; i < actJay.length(); i++) {
                dataList.add(gson.fromJson(actJay.optJSONObject(i).toString(), ServiceApplyBean.class));
            }
            adapter.notifyDataSetChanged();
        }
    };
}
