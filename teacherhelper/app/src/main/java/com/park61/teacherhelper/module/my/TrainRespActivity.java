package com.park61.teacherhelper.module.my;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.TrainRespListAdapter;
import com.park61.teacherhelper.module.my.bean.TrainRespBean;
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
 * 反馈问卷
 * Created by shubei on 2018/3/30.
 */

public class TrainRespActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private TrainRespListAdapter adapter;
    private List<TrainRespBean> eList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_trainresp);
    }

    @Override
    public void initView() {
        setPagTitle("反馈问卷");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetList();
            }
        });
    }

    @Override
    public void initData() {
        eList = new ArrayList<>();
        adapter = new TrainRespListAdapter(mContext, eList);
        mPullRefreshListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetList();
    }

    @Override
    public void initListener() {
        adapter.setOnFocusClickedLsner(position -> {
            //分享地址 提取公共host
            String shareUrl = "http://m.61park.cn/teach/#/feedback/questionnaire/" + eList.get(position).getId();
            String sharePic = "";
            String shareTitle = eList.get(position).getName();
           /* String subStartTime = eList.get(position).getStartTime();
            String subEndTime = eList.get(position).getEndTime();
            String timeWhole = subStartTime.substring(0, subStartTime.length() - 3).replace("-", ".")
                    + " - "
                    + subEndTime.substring(5, subEndTime.length() - 3).replace("-", ".");*/
            String shareDescription = "时间：" +  eList.get(position).getStartEndTime();
            showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
        });
    }

    /**
     * 获取问卷列表
     */
    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.listMyTraining;
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
            List<TrainRespBean> currentPageList = new ArrayList<>();
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
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), TrainRespBean.class));
            }
            eList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };
}
