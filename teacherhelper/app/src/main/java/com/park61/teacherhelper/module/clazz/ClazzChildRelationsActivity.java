package com.park61.teacherhelper.module.clazz;

import android.view.View;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.clazz.adapter.ClazzParentList2Adapter;
import com.park61.teacherhelper.module.clazz.bean.TeachGClassChild;
import com.park61.teacherhelper.module.clazz.bean.UserChildKinshipRelation;
import com.park61.teacherhelper.module.dict.PageParam;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 宝宝关注人列表
 *
 * @author shubei
 * @time 2018/11/27 16:50
 */
public class ClazzChildRelationsActivity extends BaseActivity {

    private PullToRefreshListView mPullRefreshListView;

    private Long userChildId;//宝贝ID，在初始化时带入
    private ClazzParentList2Adapter mAdapter;
    private List<UserChildKinshipRelation> dataList = new ArrayList<>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_child_parent_list);
    }

    @Override
    public void initView() {
        setPagTitle("关注家长");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);

        userChildId = getIntent().getLongExtra("userChildId", 0);
    }

    @Override
    public void initData() {
        mAdapter = new ClazzParentList2Adapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        asyncGetDataList();
    }


    @Override
    public void initListener() {
        mAdapter.setEditListener(new ClazzParentList2Adapter.EditListener() {
            @Override
            public void deleteTask(String taskId, int position) {
                dDialog.showDialog("提示", "确定将" + dataList.get(position).getName() + "删除吗？", "取消", "确定",
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
        String wholeUrl = AppUrl.host + AppUrl.GET_CLAZZ_CHILD_DETAIL;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PageParam.PAGE_NUM, 0);
        map.put(PageParam.PAGE_SIZE, 100);
        //获取当前用户信息
        map.put("userChildId", userChildId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            dataList.clear();
            //抽取家属列表数据
            JSONArray dataJay = jsonResult.optJSONArray("listRelation");
            if (dataJay != null && dataJay.length() > 0) {
                // 指定Gson的日期格式
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                for (int i = 0, len = dataJay.length(); i < len; i++) {
                    JSONObject actJot = dataJay.optJSONObject(i);
                    UserChildKinshipRelation c = gson.fromJson(actJot.toString(), UserChildKinshipRelation.class);
                    dataList.add(c);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 请求删除数据
     */
    private void asyncDelData(String taskId) {
        String wholeUrl = AppUrl.host + AppUrl.delChildKinshipRelation;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("kinship", taskId);
        map.put("childId", userChildId);
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
            showShortToast("删除成功!");
            asyncGetDataList();
        }
    };


}
