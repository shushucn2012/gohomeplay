package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.databinding.ActivityExpertlistBinding;
import com.park61.teacherhelper.module.home.adapter.ExpertListAdapter;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;
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
 * Created by chenlie on 2018/3/19.
 * 所有专家列表界面
 */

public class ExpertListActivity extends BaseActivity {

    ActivityExpertlistBinding bind;
    ExpertListAdapter adapter;
    List<GoldTeacher> datas;
    int pageNum = 0;
    int pageSize = 10;
    int pageCount = 100;

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_expertlist);
    }

    @Override
    public void initView() {
        bind.expertLv.setMode(PullToRefreshBase.Mode.BOTH);
        ViewInitTool.initPullToRefresh(bind.expertLv, this);
    }

    @Override
    public void initData(){
        setPagTitle("专家");
        datas = new ArrayList<>();
        adapter = new ExpertListAdapter(mContext, datas);
        bind.expertLv.setAdapter(adapter);
        asyncGetGoldTeacherList();
    }

    @Override
    public void initListener() {
        bind.expertLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNum = 0;
                datas.clear();
                adapter.notifyDataSetChanged();
                asyncGetGoldTeacherList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncGetGoldTeacherList();
            }
        });
        bind.expertLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(mContext, TeachHouseActivity.class);
                intent.putExtra("teachId", datas.get(i-1).getId());
                startActivity(intent);
            }
        });
    }

    private void asyncGetGoldTeacherList() {
        String wholeUrl = AppUrl.host + AppUrl.goldTeacherListNew;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", pageNum);
        map.put("pageSize", pageSize);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gtlistener);
    }

    BaseRequestListener gtlistener = new JsonRequestListener() {

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bind.expertLv.onRefreshComplete();
            bind.emptyView.setVisibility(View.GONE);
            pageNum ++;
            pageCount = jsonResult.optInt("pageCount");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {
                //如果数组不为空
                for (int j = 0; j < dateJay.length(); j++) {
                    datas.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                }
                adapter.notifyDataSetChanged();
            }else{
                ViewInitTool.setPullToRefreshViewEnd(bind.expertLv);
            }
            if(pageNum >= pageCount){
                ViewInitTool.setPullToRefreshViewEnd(bind.expertLv);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            bind.expertLv.onRefreshComplete();
            if(pageNum == 0){
                bind.emptyView.setVisibility(View.VISIBLE);
            }
        }
    };
}
