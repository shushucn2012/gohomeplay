package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.workplan.adapter.GourpTTaskDoneInfoAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskDoneInfoBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 幼儿园用户-幼儿园模板任务完成情况总览页
 * Created by shubei on 2018/8/4.
 */

public class GroupTempInfoMainActivity extends BaseActivity {

    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;
    private ListViewForScrollView lv_task_done_info;
    private TextView tv_group_name, tv_undone_num, tv_finish_num, tv_delayed_num;
    private ImageView img_group_header;
    private View area_all_task_status;

    private String groupName;
    private List<TaskDoneInfoBean> dataList = new ArrayList<>();
    private GourpTTaskDoneInfoAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_group_tempinfo_main);
    }

    @Override
    public void initView() {
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        lv_task_done_info = (ListViewForScrollView) findViewById(R.id.lv_task_done_info);
        lv_task_done_info.setFocusable(false);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        tv_undone_num = (TextView) findViewById(R.id.tv_undone_num);
        tv_finish_num = (TextView) findViewById(R.id.tv_finish_num);
        tv_delayed_num = (TextView) findViewById(R.id.tv_delayed_num);
        img_group_header = (ImageView) findViewById(R.id.img_group_header);
        area_all_task_status = findViewById(R.id.area_all_task_status);
    }

    @Override
    public void initData() {
        adapter = new GourpTTaskDoneInfoAdapter(mContext, dataList);
        lv_task_done_info.setAdapter(adapter);
        asyncGetUserAllTaskCount();
        asyncAssignedTaskCalendarList();
    }

    @Override
    public void initListener() {
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                asyncGetUserAllTaskCount();
                asyncAssignedTaskCalendarList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });

        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        scrollView.setCallbacks(mCallbacks);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mCallbacks.onScrollChanged(scrollView.getScrollY());
            }
        });
        adapter.setOnNoticeClickedListener(new GourpTTaskDoneInfoAdapter.OnNoticeClickedListener() {
            @Override
            public void onClicked(int position) {

            }

            @Override
            public void onTitle(int position) {
                if (!GlobalParam.isGroupManager)//普通幼儿园用户不能进入,避免混乱
                    return;
                GlobalParam.teachClassId = dataList.get(position).getTeachClassId();
                Intent it = new Intent(mContext, ManageTaskActivity.class);
                GlobalParam.taskCalendarClassId = dataList.get(position).getId();
                it.putExtra("taskCalendarClassId", dataList.get(position).getId());
                startActivity(it);
            }
        });
       /* area_all_task_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WorkPlanMainActivity.class));
            }
        });*/
        findViewById(R.id.area_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, WorkPlanMainActivity.class);
                it.putExtra("taskStatus", 0);
                startActivity(it);
            }
        });
        findViewById(R.id.area_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, WorkPlanMainActivity.class);
                it.putExtra("taskStatus", 1);
                startActivity(it);
            }
        });
        findViewById(R.id.area_delay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, WorkPlanMainActivity.class);
                it.putExtra("taskStatus", 2);
                startActivity(it);
            }
        });
    }

    /**
     * 滑动黏贴view的监听事件
     */
    private ObservableScrollView.Callbacks mCallbacks = new ObservableScrollView.Callbacks() {

        @Override
        public void onUpOrCancelMotionEvent() {
        }

        @Override
        public void onScrollChanged(int scrollY) {
            // 控制TOPBAR的渐变效果
            if (scrollY > DevAttr.dip2px(mContext, 185)) {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                ((ImageView) findViewById(R.id.img_right_back)).setImageResource(R.mipmap.icon_red_backimg);
                setPagTitle(TextUtils.isEmpty(groupName) ? "" : groupName);
            } else if (scrollY > DevAttr.dip2px(mContext, 145)) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
            } else if (scrollY >= -10) {
                findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                ((ImageView) findViewById(R.id.img_right_back)).setImageResource(R.mipmap.icon_content_backimg);
                setPagTitle("");
            } else if (scrollY < -10) {
                findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#00000000"));
            }
        }

        @Override
        public void onDownMotionEvent() {
        }
    };

    /**
     * 个人任务数量
     */
    public void asyncGetUserAllTaskCount() {
        String wholeUrl = AppUrl.host + AppUrl.getUserAllTaskCount;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshScrollView.onRefreshComplete();
            groupName = jsonResult.optString("teachGroupName");
            tv_group_name.setText(groupName);
            tv_undone_num.setText(jsonResult.optString("unfinished"));
            tv_finish_num.setText(jsonResult.optString("finished"));
            tv_delayed_num.setText(jsonResult.optString("delayed"));
            ImageManager.getInstance().displayImg(img_group_header, jsonResult.optString("teachGroupUrl"));
        }
    };

    /**
     * 已分配模板列表
     */
    public void asyncAssignedTaskCalendarList() {
        String wholeUrl = AppUrl.host + AppUrl.assignedTaskCalendarList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject itemJot = actJay.optJSONObject(i);
                    TaskDoneInfoBean g = gson.fromJson(itemJot.toString(), TaskDoneInfoBean.class);
                    dataList.add(g);
                }
                adapter.notifyDataSetChanged();
                findViewById(R.id.area_empty).setVisibility(View.GONE);
            } else {
                findViewById(R.id.area_empty).setVisibility(View.VISIBLE);
            }
        }
    };
}
