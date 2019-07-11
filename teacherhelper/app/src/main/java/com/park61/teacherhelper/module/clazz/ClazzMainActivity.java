package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
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
import com.park61.teacherhelper.module.clazz.adapter.ClazzMgrListAdapter;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.dict.PageParam;
import com.park61.teacherhelper.module.dict.RequestCode;
import com.park61.teacherhelper.module.dict.ResultCode;
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
 * 班级管理首页
 * Created by zhangchi on 2017/8/16.
 */
public class ClazzMainActivity extends BaseActivity {

    private int PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
    private final int PAGE_SIZE = 10;

    private ClazzMgrListAdapter mAdapter;
    private List<TeachGClass> dataList;
    /**
     * 右上角更多按钮，点击弹出菜单
     */
//    private ImageView mImageMore;
    /**
     * 临时按钮，日后加入弹出菜单
     */
    private ImageView mImageAddClazz;
    /**
     * 临时按钮，日后加入弹出菜单
     */
//    private ImageView mImageMergeClazz;
    /**
     * 临时按钮，日后加入弹出菜单
     */
//    private ImageView mImageScanQR;

    private PullToRefreshListView mPullRefreshListView;

    private int operateAdd = 0;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_mgr);
    }

    @Override
    public void initView() {
        setPagTitle("班级管理");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);

//        mImageMore          = (ImageView) findViewById(R.id.img_more);
        mImageAddClazz = (ImageView) findViewById(R.id.img_add_clazz);
//        mImageMergeClazz    = (ImageView) findViewById(R.id.img_merge_clazz);
//        mImageScanQR        = (ImageView) findViewById(R.id.img_scan_qrcode);
    }

    @Override
    public void initData() {
        operateAdd = getIntent().getIntExtra("operateAdd", 0);
        if (operateAdd == 1) {
            Intent intent = new Intent(ClazzMainActivity.this, ClazzAddActivity.class);
            startActivityForResult(intent, RequestCode.REQUET_CODE_ADD_CLAZZ.getCode());
        }
        dataList = new ArrayList<>();
        mAdapter = new ClazzMgrListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, mPullRefreshListView.getRefreshableView());
        asyncGetDataList();
    }

    /**
     * 请求数据
     */
    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CLAZZ_LIST;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PageParam.PAGE_NUM, PAGE_NUM);
        map.put(PageParam.PAGE_SIZE, PAGE_SIZE);

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
        //初始化More点击动作
//        mImageMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //弹出下拉菜单
//            }
//        });
        //初始化新增班级按钮点击动作
        mImageAddClazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClazzMainActivity.this, ClazzAddActivity.class);
                startActivityForResult(intent, RequestCode.REQUET_CODE_ADD_CLAZZ.getCode());
            }
        });
        //初始化合并班级按钮点击动作
//        mImageMergeClazz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        //初始化扫码按钮点击动作
//        mImageScanQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ClazzQRCodeCaptureActivity.class);
//                startActivityForResult(intent, RequestCode.REQUET_CODE_QR_CAPTURE.getCode());
//            }
//        });
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            List<TeachGClass> currentPageList = new ArrayList<TeachGClass>();

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
            // 指定Gson的日期格式
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

            for (int i = 0, len = dataJay.length(); i < len; i++) {
                JSONObject actJot = dataJay.optJSONObject(i);
                TeachGClass c = gson.fromJson(actJot.toString(), TeachGClass.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCode.RESULT_CODE_SUCCESS.getCode()) {
            showShortToast(ResultCode.RESULT_CODE_SUCCESS.getMsg());
        } else if (resultCode == ResultCode.RESULT_CODE_FAIL.getCode()) {
            showShortToast(ResultCode.RESULT_CODE_FAIL.getMsg());
            return;
        }

        PAGE_NUM = PageParam.PAGE_NUM_ORIGIN;
        asyncGetDataList();

//        //如果是编辑班级的请求返回了
//        if (requestCode == RequestCode.REQUET_CODE_EDIT_CLAZZ.getCode()) {
//            if (data != null) {
//                int delete_location = data.getIntExtra("delete_location", -1);
//
//                //如果执行了退出班级的操作
//                if(delete_location != -1) {
//                    dataList.remove(delete_location);
//                    mAdapter.notifyDataSetChanged();
//                    showShortToast("列表刷新成功");
//                }
//            }
//        }
//        //如果是新增班级的请求返回了
//        if (requestCode == RequestCode.REQUET_CODE_ADD_CLAZZ.getCode()) {
//            PAGE_NUM = 0;
//            asyncGetDataList();
//        }


    }
}
