package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.park61.teacherhelper.module.clazz.adapter.ClazzChildListAdapter;
import com.park61.teacherhelper.module.clazz.bean.TeachGClassChild;
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
 * 宝宝列表页
 * Created by zhangchi on 2017/8/21.
 */
public class ClazzChildListActivity extends BaseActivity {

    private int PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private TextView tv_clazz_name_and_num;

    private ClazzChildListAdapter mAdapter;
    private List<TeachGClassChild> dataList; //TeachGClassTeacher  TeachMember

    /**
     * 班级ID，在初始化时带入
     */
    private String clazzId;
    private String clazzName = "";

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_child_list);
    }

    @Override
    public void initView() {
        setPagTitle("班级宝宝");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);

        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.img_add_66);
        findViewById(R.id.area_right).setVisibility(View.VISIBLE);
        tv_clazz_name_and_num = findViewById(R.id.tv_clazz_name_and_num);

        //init clazzId
        clazzId = getIntent().getStringExtra("clazzId");
        clazzName = getIntent().getStringExtra("clazzName");
    }

    @Override
    public void initData() {
        dataList = new ArrayList<>();
        mAdapter = new ClazzChildListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
        asyncGetDataList();
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
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AddStudentToClazzActivity.class);
                it.putExtra("gClassId", clazzId);
                startActivity(it);
            }
        });
        mAdapter.setEditListener(new ClazzChildListAdapter.EditListener() {
            @Override
            public void deleteTask(String taskId, int position) {
                dDialog.showDialog("提示", "确定将" + dataList.get(position).getName() + "从班级删除吗？", "取消", "确定",
                        null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                asyncDelData(taskId);
                            }
                        });
            }
        });
    }

    /**
     * 请求数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CLAZZ_CHILD_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PageParam.PAGE_NUM, PAGE_NUM);
        map.put(PageParam.PAGE_SIZE, PAGE_SIZE);
        //获取当前用户信息
        map.put("gClassId", clazzId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            List<TeachGClassChild> currentPageList = new ArrayList<TeachGClassChild>();

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
            if (PAGE_NUM >= jsonResult.optInt(PageParam.PAGE_COUNT) - 1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            tv_clazz_name_and_num.setText(clazzName + jsonResult.optInt("total") + "名");
            // 指定Gson的日期格式
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

            for (int i = 0, len = dataJay.length(); i < len; i++) {
                JSONObject actJot = dataJay.optJSONObject(i);
                TeachGClassChild c = gson.fromJson(actJot.toString(), TeachGClassChild.class);
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

    /**
     * 请求删除数据
     *
     * @param userChildId
     */
    private void asyncDelData(String userChildId) {
        String wholeUrl = AppUrl.host + AppUrl.delClassChild;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gClassId", clazzId);
        map.put("userChildId", userChildId);
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
            asyncGetDataList();
        }
    };
}
