package com.park61.teacherhelper.module.member.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.member.adapter.CurMemberListAdapter;
import com.park61.teacherhelper.module.member.adapter.UnAuthMemberListAdapter;
import com.park61.teacherhelper.module.member.bean.VipGroupMemberBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待审核会员列表
 */
public class FragmentUnAuthMembers extends BaseFragment {

    private PullToRefreshListView mPullRefreshListView;

    private List<VipGroupMemberBean> mList;
    private UnAuthMemberListAdapter adapter;
    private int curOperateId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_cur_memberlist, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshListView = parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //asyncGetList();
            }
        });
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        adapter = new UnAuthMemberListAdapter(parentActivity, mList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetList();
    }

    @Override
    public void initListener() {
        adapter.setOnOperateListener(new UnAuthMemberListAdapter.OnOperateListener() {
            @Override
            public void onPassClicked(int pos) {
                curOperateId = mList.get(pos).getId();
                asyncAcceptMemberApply();
            }

            @Override
            public void onRejectClicked(int pos) {
                curOperateId = mList.get(pos).getId();
                asyncRejectMemberApply();
            }
        });
    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.memberListInvite;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            //showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            JSONArray actJay = jsonResult.optJSONArray("list");
            mList.clear();
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    mList.add(gson.fromJson(actJay.optJSONObject(i).toString(), VipGroupMemberBean.class));
                }
                /*VipGroupMemberBean testBean = new VipGroupMemberBean();
                testBean.setUserId(123);
                testBean.setUserName("haha");
                testBean.setDutyName("配班老师");
                testBean.setUserMobile("18865122233");
                testBean.setStatus(2);
                mList.add(testBean);*/
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent("SHOW_HAS_POINT"));
                Log.out("==========send============SHOW_HAS_POINT===============");
            } else {
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(parentActivity, mPullRefreshListView.getRefreshableView());
                EventBus.getDefault().post(new MessageEvent("HIDE_HAS_POINT"));
                Log.out("==========send============HIDE_HAS_POINT===============");
            }
        }
    };

    /**
     * 请求通过
     */
    private void asyncAcceptMemberApply() {
        String wholeUrl = AppUrl.host + AppUrl.acceptMemberApply;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curOperateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, aListener);
    }

    BaseRequestListener aListener = new JsonRequestListener() {
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
            showShortToast("已通过！");
            asyncGetList();
        }
    };

    /**
     * 请求拒绝
     */
    private void asyncRejectMemberApply() {
        String wholeUrl = AppUrl.host + AppUrl.rejectMemberApply;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curOperateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, rListener);
    }

    BaseRequestListener rListener = new JsonRequestListener() {
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
            showShortToast("已拒绝！");
            asyncGetList();
        }
    };
}
