package com.park61.teacherhelper.module.clazz;

import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.clazz.adapter.ClazzTeacherListAdapter;
import com.park61.teacherhelper.module.clazz.bean.TeachGClassTeacher;
import com.park61.teacherhelper.module.dict.PageParam;
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
 *
 * Created by zhangchi on 2017/8/18.
 */
public class ClazzTeacherListActivity extends BaseActivity {

    private int PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;

    private ClazzTeacherListAdapter mAdapter;
    private List<TeachGClassTeacher> dataList; //TeachGClassTeacher  TeachMember

    /**
     * 班级ID，在初始化时带入
     */
    private String clazzId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_teacher_list);
    }

    @Override
    public void initView() {
        setPagTitle("班级老师");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);

        //init clazzId
        clazzId = getIntent().getStringExtra("clazzId");
    }

    @Override
    public void initData() {
        dataList = new ArrayList<>();
        mAdapter = new ClazzTeacherListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
        asyncGetDataList();
    }

    /**
     * 请求数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CLAZZ_TEACHER_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PageParam.PAGE_NUM, PAGE_NUM);
        map.put(PageParam.PAGE_SIZE, PAGE_SIZE);
        //获取当前用户信息
        map.put("gClassId", clazzId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    @Override
    public void initListener() {
        //初始化下拉刷新Listener
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
                asyncGetDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetDataList();
            }
        });
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            List<TeachGClassTeacher> currentPageList = new ArrayList<TeachGClassTeacher>();

            JSONArray dataJay = jsonResult.optJSONArray(PageParam.RESULT_LIST_LABEL);
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == PageParam.PAGE_NUM_ORIGIN && (dataJay == null || dataJay.length() <= 0)) {
                dataList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == PageParam.PAGE_NUM_ORIGIN) {
                dataList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt(PageParam.PAGE_COUNT)) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            // 指定Gson的日期格式
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

            for (int i = 0, len = dataJay.length(); i < len; i++) {
                JSONObject actJot = dataJay.optJSONObject(i);
                TeachGClassTeacher c = gson.fromJson(actJot.toString(), TeachGClassTeacher.class);
                currentPageList.add(c);
            }

            dataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
        }
    };
}
