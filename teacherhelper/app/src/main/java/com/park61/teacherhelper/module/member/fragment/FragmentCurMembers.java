package com.park61.teacherhelper.module.member.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.member.adapter.CurMemberListAdapter;
import com.park61.teacherhelper.module.member.bean.VipGroupMemberBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前会员列表
 */
public class FragmentCurMembers extends BaseFragment {

    private PullToRefreshListView mPullRefreshListView;
    private TextView tv_group_name;
    private Button btn_manage;

    private List<VipGroupMemberBean> mList;
    private CurMemberListAdapter adapter;
    private int memberUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_cur_memberlist, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        tv_group_name = parentView.findViewById(R.id.tv_group_name);
        btn_manage = parentView.findViewById(R.id.btn_manage);
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
        if (GlobalParam.isGroupManager == false) {
            btn_manage.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        adapter = new CurMemberListAdapter(parentActivity, mList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetList();
    }

    @Override
    public void initListener() {
        btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isDeleteBtnShow()) {
                    btn_manage.setText("管理");
                    adapter.showDeleteBtn(false);
                } else {
                    btn_manage.setText("取消");
                    adapter.showDeleteBtn(true);
                }
            }
        });
        adapter.setOnOperateClickedListener(new CurMemberListAdapter.OnOperateClickedListener() {
            @Override
            public void onDelete(int pos) {
                dDialog.showDialog("提示", "您确定将该成员从园所移除吗？", "取消", "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                memberUserId = mList.get(pos).getUserId();
                                asyncDeleteMember();
                            }
                        });
            }
        });
    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.host + AppUrl.teachGroupMemberList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            JSONArray actJay = jsonResult.optJSONArray("teachGroupMemberList");
            mList.clear();
            if (actJay != null && actJay.length() > 0) {
                parentView.findViewById(R.id.area_group_name).setVisibility(View.VISIBLE);
                tv_group_name.setText(jsonResult.optString("teachGroupName") + "  " + jsonResult.optInt("memberNum") + "名");
                for (int i = 0; i < actJay.length(); i++) {
                    mList.add(gson.fromJson(actJay.optJSONObject(i).toString(), VipGroupMemberBean.class));
                }
                adapter.notifyDataSetChanged();
            } else {
                parentView.findViewById(R.id.area_group_name).setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(parentActivity, mPullRefreshListView.getRefreshableView());
            }
        }
    };

    private void asyncDeleteMember() {
        String wholeUrl = AppUrl.host + AppUrl.deleteTeachGroupMember;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberUserId", memberUserId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dListener);
    }

    BaseRequestListener dListener = new JsonRequestListener() {
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
            showShortToast("删除成功！");
            asyncGetList();
        }
    };
}
