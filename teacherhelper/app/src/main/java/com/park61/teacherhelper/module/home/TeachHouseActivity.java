package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.ActivityWebViewActivity;
import com.park61.teacherhelper.module.clazz.adapter.TeacherItemAdapter;
import com.park61.teacherhelper.module.clazz.bean.TeacherItemBean;
import com.park61.teacherhelper.module.home.adapter.TeachCourseAdapter;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.home.bean.TeacherTitleBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;
import com.park61.teacherhelper.widget.listview.RecyclerViewForScrollView;
import com.park61.teacherhelper.widget.pw.SharePopWin;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训师主页
 * Created by nieyu on 2017/10/18.
 * 新增课程列表 cl 2018/03/22
 */

public class TeachHouseActivity extends BaseActivity implements View.OnClickListener {

    private int PAGE_NUM = 1;
    private static final int PAGE_SIZE = 10;

    private RelativeLayout relativeLayout;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private RecyclerViewForScrollView course_lv;
    private ImageView iv_redshre, iv_teachpic, iv_back, iv_teachback, iv_hadershare;
    private TextView tv_teachername, tv_desc, tv_renqi, tv_follow, tv_content;
    private View courseArea,descArea,contentArea;

    private ObservableScrollView scrollView;
    private ListViewForScrollView contentLv;
    private List<TeacherItemBean> mList = new ArrayList<>();
    private List<TeacherCourseBean> mCourse = new ArrayList<>();
    private TeachCourseAdapter tAdapter;
    private TeacherItemAdapter mAdapter;
    private TeacherTitleBean contentRecommendBean;
    private int teachId;
    private String clickedContentId, clickedActivityId;
    private boolean status;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_teachhouse);
    }

    @Override
    public void initView() {
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.push_listll_refre);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        relativeLayout = (RelativeLayout) findViewById(R.id.ll_title);
        iv_redshre = (ImageView) findViewById(R.id.iv_redshre);
        iv_back = (ImageView) findViewById(R.id.iv_back);


        iv_teachpic = (ImageView) findViewById(R.id.iv_teachpic);
        tv_renqi = (TextView) findViewById(R.id.tv_renqi);
        tv_teachername = (TextView) findViewById(R.id.tv_teachername);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        iv_hadershare = (ImageView) findViewById(R.id.iv_hadershare);
        iv_teachback = (ImageView) findViewById(R.id.iv_teachback);
        tv_follow = (TextView) findViewById(R.id.follow_tv);
        tv_content = (TextView) findViewById(R.id.content_title);
        tv_follow.setEnabled(false);
        course_lv = (RecyclerViewForScrollView) findViewById(R.id.courses_lv);
        courseArea = findViewById(R.id.course_area);
        descArea = findViewById(R.id.desc_area);
        contentArea = findViewById(R.id.content_area);
        contentLv = (ListViewForScrollView) findViewById(R.id.article_content);

    }

    @Override
    public void initData() {
        teachId = getIntent().getIntExtra("teachId", -1);
        tAdapter = new TeachCourseAdapter(mContext, mCourse);
        course_lv.setLayoutManager(new LinearLayoutManager(mContext));
        course_lv.setAdapter(tAdapter);
        mAdapter = new TeacherItemAdapter(mContext, mList);
        contentLv.setAdapter(mAdapter);
        asynGetTeacherDetail();
    }

    @Override
    public void initListener() {
        iv_redshre.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_teachback.setOnClickListener(this);
        iv_hadershare.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        descArea.setOnClickListener(this);

        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                PAGE_NUM = 1;
                asyncGetTeacherItem();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                PAGE_NUM++;
                asyncGetTeacherItem();
            }
        });
        scrollView.setCallbacks(new ObservableScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int scrollY) {
                if (scrollY > 300) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent() {

            }
        });

        contentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList.get(position).getTeachActivityId() > 0) {
                    if (mList.get(position).getIsFree() == 0) {
                        clickedContentId = mList.get(position).getId() + "";
                        clickedActivityId = mList.get(position).getTeachActivityId() + "";
                        asynContentBrowseCheck();
                    } else {
                        Intent it = new Intent(mContext, CourseDetailsActivity.class);
                        it.putExtra("coursePlanId", mList.get(position).getId());
                        startActivity(it);
                    }
                } else {
                    Intent it = new Intent(mContext, CourseDetailsActivity.class);
                    it.putExtra("coursePlanId", mList.get(position).getId());
                    startActivity(it);
                }
            }
        });
    }

    public void asynGetTeacherDetail() {
        String wholeUrl = AppUrl.host + AppUrl.addTeachdetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerId", teachId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, teachdetaillistener);
    }

    BaseRequestListener teachdetaillistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            dDialog.showDialog("提示", errorMsg, "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        asynGetTeacherDetail();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            contentRecommendBean = gson.fromJson(jsonResult.toString(), TeacherTitleBean.class);
            if(contentRecommendBean.getTrainerCourseSeriesList() != null && contentRecommendBean.getTrainerCourseSeriesList().size() != 0){
                courseArea.setVisibility(View.VISIBLE);
                mCourse.addAll(contentRecommendBean.getTrainerCourseSeriesList());
                tAdapter.notifyDataSetChanged();
            }else{
                //隐藏课程列表
                courseArea.setVisibility(View.GONE);
            }

            ImageManager.getInstance().displayImg(iv_teachpic, contentRecommendBean.getBigPictureUrl());
            tv_teachername.setText(contentRecommendBean.getName());
            tv_desc.setText("简介: " + contentRecommendBean.getDescription());
            tv_renqi.setText(contentRecommendBean.getPopularity());
            asyncGetFollowStatus();
            asyncGetTeacherItem();
        }
    };

    /**
     * 查询是否已关注专家
     */
    public void asyncGetFollowStatus(){
        String url = AppUrl.host + AppUrl.isFollowById;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", teachId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, isFollowListener);
    }

    BaseRequestListener isFollowListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String result = jsonResult.optString("data");
            if("true".equalsIgnoreCase(result)){
                tv_follow.setText("已关注");
                tv_follow.setBackgroundResource(R.drawable.whiteboder_corner_shape);
                status = true;
            }else{
                status = false;
                tv_follow.setText("+关注");
                tv_follow.setBackgroundResource(R.drawable.red_corner_shape);
            }
            tv_follow.setEnabled(true);
        }

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            tv_follow.setEnabled(true);
        }
    };

    public void asyncGetTeacherItem() {
        AddStatistics("teachHome");
        String wholeUrl = AppUrl.host + AppUrl.addTeacherList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", teachId);
        map.put("sort", "create_date");
        map.put("order", "desc");
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, courselistener);
    }

    BaseRequestListener courselistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            mPullRefreshScrollView.onRefreshComplete();
            if (PAGE_NUM > 1) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshScrollView.onRefreshComplete();
            ArrayList<TeacherItemBean> currentPageList = new ArrayList<TeacherItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                //ViewInitTool.setListEmptyView(mContext, actualListView);
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                contentArea.setVisibility(View.GONE);
                return;
            }
            contentArea.setVisibility(View.VISIBLE);
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount")) {
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            for (int i = 0; i < cmtJay.length(); i++) {
                JSONObject actJot = cmtJay.optJSONObject(i);
                TeacherItemBean p = gson.fromJson(actJot.toString(), TeacherItemBean.class);
                currentPageList.add(p);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 验证内容是否已经付费
     */
    public void asynContentBrowseCheck() {
        String wholeUrl = AppUrl.host + AppUrl.contentBrowseCheck;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", clickedActivityId);
        map.put("contentId", clickedContentId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }


    BaseRequestListener clistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000042008".equals(errorCode) || "0000042007".equals(errorCode)) {//未支付且存在未支付订单;未支付&未下单
                dDialog.showDialog("此内容为活动付费内容，打赏付费后可查看所有活动内容", "", "取消", "打赏入场",
                        view -> dDialog.dismissDialog(),
                        view -> {
                            Intent intent = new Intent(TeachHouseActivity.this, ActivityWebViewActivity.class);
                            intent.putExtra("activityId", FU.paseInt(clickedActivityId));
                            startActivity(intent);
                        });
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Intent it = new Intent(mContext, CourseDetailsActivity.class);
            it.putExtra("coursePlanId", FU.paseInt(clickedContentId));
            startActivity(it);
        }
    };

    /**
     * 关注、取消关注专家
     */
    private void followExpert(){
        String url = AppUrl.host + AppUrl.followTeacher;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", teachId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, followListener);
    }

    BaseRequestListener followListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            status = true;
            tv_follow.setText("已关注");
            tv_follow.setBackgroundResource(R.drawable.whiteboder_corner_shape);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void unfollow(){
        String url = AppUrl.host + AppUrl.unfollowTeacher;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", teachId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, unfollowListener);
    }

    BaseRequestListener unfollowListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            status = false;
            tv_follow.setText("+关注");
            tv_follow.setBackgroundResource(R.drawable.red_corner_shape);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    @Override
    public void onClick(View view) {
        int viewie = view.getId();
        if (viewie == R.id.iv_redshre) {
            toShare();
        } else if (viewie == R.id.desc_area) {
            Intent intent = new Intent(this, TeachDetailActivity.class);
            intent.putExtra("h5Content", contentRecommendBean.getContent());
            startActivity(intent);
        } else if (R.id.iv_back == viewie) {
            finish();
        } else if (R.id.iv_teachback == viewie) {
            finish();
        } else if (R.id.iv_hadershare == viewie) {
            toShare();
        } else if(R.id.follow_tv == viewie){
            if(status){
                unfollow();
            }else{
                followExpert();
            }
        }
    }

    public void AddStatistics(String type) {
        if ("teachHome".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            MobclickAgent.onEventValue(this, "Trainer", map, 0);
        }
    }

    public void toShare() {
        Intent its = new Intent(mContext, SharePopWin.class);
//        String shareUrl = "http://m.61park.cn/teach/#/activity/teacherindex/" + teachId;
        String shareUrl = "http://m.61park.cn/teach/#/expert/people/" + teachId;
        String picUrl = contentRecommendBean.getBigPictureUrl();
        String title = contentRecommendBean.getName();
        String description = contentRecommendBean.getDescription();
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", picUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }

}
