package com.park61.teacherhelper.module.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.AttachmentDLActivity;
import com.park61.teacherhelper.module.course.bean.ContentRecommendBean;
import com.park61.teacherhelper.module.home.adapter.ContentCommentListAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.park61.teacherhelper.module.home.adapter.FileSourcesAdapter;
import com.park61.teacherhelper.module.home.bean.CommentListBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.SpaceItemDecoration;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.HttpUrl;
import tyrantgit.widget.HeartLayout;

/**
 * 普通内容-文字详情
 * Created by shubei on 2017/8/16.
 */

public class CourseTextDetailsActivity extends BaseActivity {

    private int[] paopaoResArray = {R.mipmap.banana, R.mipmap.cherry, R.mipmap.nnut, R.mipmap.polo, R.mipmap.rraspberry,
            R.mipmap.cake, R.mipmap.devil, R.mipmap.love_heart, R.mipmap.moon_stars, R.mipmap.ttaxi,
            R.mipmap.advance, R.mipmap.gifts, R.mipmap.roles, R.mipmap.shanzu, R.mipmap.spring,
            R.mipmap.tiqing, R.mipmap.wen, R.mipmap.xiangshui, R.mipmap.yazi, R.mipmap.yule, R.mipmap.zuifan};

    private ImageView img_sc, civ_icon, iv_message, iv_attent;
    public RelativeLayout rl_contenttitle, rl_edit;
    public LinearLayout ll_title, ll_commentbottom;
    private ShowImageWebView wvContent;
    private RecyclerView gv_recommend;
    public ListView lv_comment;
    private EditText et_commentwrite;
    private View area_recommend, area_real_input, area_show_input, area_do_praise, area_commentsend;
    private TextView title, tv_course_titles, tv_titlename, tv_titletime, tv_messagecount, tv_attentcount, tv_commentsend;
    private HeartLayout mHeartLayout;

    private ContentCommentListAdapter contentCommentListAdapter;
    private int collect;
    public int coursePlanId;
    private CourseSectionBean curCourseSectionBean;
    private boolean isPriseLimt;

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_course_details_text);
    }

    @Override
    public void initView() {
        area_recommend = findViewById(R.id.area_recommend);
        title = (TextView) findViewById(R.id.t);
        wvContent = (ShowImageWebView) findViewById(R.id.wvContent);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        rl_contenttitle = (RelativeLayout) findViewById(R.id.rl_contenttitle);
        img_sc = (ImageView) findViewById(R.id.img_sc);
        gv_recommend = (RecyclerView) findViewById(R.id.id_recyclerview);
        lv_comment = (ListView) findViewById(R.id.lv_comment);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        gv_recommend.setLayoutManager(manager);

        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        tv_attentcount = (TextView) findViewById(R.id.tv_attentcount);
        iv_attent = (ImageView) findViewById(R.id.iv_attent);
        civ_icon = (ImageView) findViewById(R.id.civ_icon);
        tv_course_titles = (TextView) findViewById(R.id.tv_course_titles);
        tv_titlename = (TextView) findViewById(R.id.tv_titlename);
        tv_titletime = (TextView) findViewById(R.id.tv_titletime);
        area_do_praise = findViewById(R.id.area_do_praise);
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
        area_real_input = findViewById(R.id.area_real_input);
        area_show_input = findViewById(R.id.area_show_input);
        area_commentsend = findViewById(R.id.area_commentsend);
    }

    @Override
    public void initData() {
        coursePlanId = getIntent().getIntExtra("coursePlanId", -1);

        curCourseSectionBean = (CourseSectionBean) getIntent().getSerializableExtra("courseBean");
        if (curCourseSectionBean == null) {
            asyncGetTeachCourseItem();
        } else {
            fillData();
        }
    }

    @Override
    public void initListener() {
        findViewById(R.id.iv_redshre).setOnClickListener(v -> {
            if (curCourseSectionBean == null)
                return;
            if (curCourseSectionBean.getIsMember() != 0) {
                dDialog.showDialog("提示", "只有指定人员能够分享此内容", "取消", "确认", null, null);
                return;
            }
            ViewInitTool.AddStatistics(mContext, "contentshare");
            showShareDialog("http://m.61park.cn/teach/#/activity/activitycontent/" + coursePlanId,
                    curCourseSectionBean.getCoverImg(),
                    curCourseSectionBean.getTitle(),
                    curCourseSectionBean.getIntro());
        });
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });
        iv_message.setOnClickListener(v -> {
            Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
            it.putExtra("itemId", coursePlanId);
            startActivity(it);
        });
        img_sc.setOnClickListener(v -> store());
        area_do_praise.setOnClickListener(v -> mHeartLayout.post(new Runnable() {
            @Override
            public void run() {
                mHeartLayout.addHeart(mContext.getResources().getColor(R.color.transparent), paopaoResArray[new Random().nextInt(20)], R.mipmap.icon_trans);
                admire();
            }
        }));
        rl_edit.setOnClickListener(v -> {
            showComtArea();
        });
        tv_commentsend.setOnClickListener(view -> {
            if (CommonMethod.checkUserLogin(mContext)) {
                if (TextUtils.isEmpty(et_commentwrite.getText().toString().trim())) {
                    showShortToast("请输入评论内容");
                    return;
                }
                sedCommend();
            }
        });
        findViewById(R.id.area_attachment_dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AttachmentDLActivity.class);
                intent.putExtra("coursePlanId", coursePlanId);
                startActivity(intent);
            }
        });
    }

    //收藏
    public void store() {
        if (curCourseSectionBean == null) {
            return;
        }
        if (GlobalParam.userToken == null) {// 没有登录则跳去登录
            startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        if (collect == 1) {
            asyncCancelCollect();
        } else {
            asyncAddCollect();
        }
    }

    private void asyncGetTeachCourseItem() {
        ViewInitTool.AddStatistics(mContext, "contentdetails");
        String wholeUrl = AppUrl.host + AppUrl.findContentDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
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
            dDialog.showDialog("提示", "网络请求失败", "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        asyncGetTeachCourseItem();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            curCourseSectionBean = gson.fromJson(jsonResult.toString(), CourseSectionBean.class);
            fillData();
        }
    };

    public void fillData() {
        if (curCourseSectionBean.getIsCollect()) {
            collect = 1;
            img_sc.setImageResource(R.mipmap.corse_store);
        } else {
            collect = 0;
            img_sc.setImageResource(R.mipmap.content_redrestore);
        }

        //自定义webview初始化
        ViewInitTool.initShowimgWebview(wvContent);
        //加载数据
        ViewInitTool.setWebData(wvContent, curCourseSectionBean.getContent());

        tv_attentcount.setText(curCourseSectionBean.getPraiseNumDsc());
        if (curCourseSectionBean.getIsPraised()) {
            iv_attent.setImageDrawable(getResources().getDrawable(R.mipmap.comment_noattend));
        } else {
            iv_attent.setImageDrawable(getResources().getDrawable(R.mipmap.comment_noattend));
        }
        title.setText(TextUtils.isEmpty(curCourseSectionBean.getLevel1CateName()) ? "活动内容" : curCourseSectionBean.getLevel1CateName());
        if (!TextUtils.isEmpty(curCourseSectionBean.getAuthorPic())) {
            ImageManager.getInstance().displayImg(civ_icon, curCourseSectionBean.getAuthorPic());
        }
        tv_course_titles.setText(curCourseSectionBean.getTitle());
        tv_titlename.setText(curCourseSectionBean.getAuthorName());
        tv_titletime.setText(curCourseSectionBean.getShowDate());

        ansycGetFileList();
        asyncGetComment();
        asynGetRecomment();
    }

    /**
     * 获取附件列表
     */
    public void ansycGetFileList() {
        String wholeUrl = AppUrl.host + AppUrl.getAttachmentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, flister);
    }

    BaseRequestListener flister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0) {
                findViewById(R.id.area_attachment_dl).setVisibility(View.GONE);
            } else {
                findViewById(R.id.area_attachment_dl).setVisibility(View.VISIBLE);
            }
        }
    };

    private void asyncAddCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_add;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
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
            //收藏，变成已收藏
            collect = 1;
            img_sc.setImageResource(R.mipmap.corse_store);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            Toast.makeText(CourseTextDetailsActivity.this, "已收藏", Toast.LENGTH_LONG).show();
        }
    };

    private void asyncCancelCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_cancel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
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
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //取消收藏，变成未收藏
            collect = 0;
            img_sc.setImageResource(R.mipmap.content_redrestore);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            Toast.makeText(CourseTextDetailsActivity.this, "已取消收藏", Toast.LENGTH_LONG).show();
        }
    };

    public void asyncGetComment() {
        String wholeUrl = AppUrl.host + AppUrl.addContentComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", coursePlanId);
        map.put("sort", "create_date");
        map.put("order", "desc");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commentlistener);
    }

    BaseRequestListener commentlistener = new JsonRequestListener() {
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
            CommentListBean commentListBean = gson.fromJson(jsonResult.toString(), CommentListBean.class);
            initCommentList(commentListBean);
        }
    };

    public void initCommentList(CommentListBean commentListBean) {
        contentCommentListAdapter = new ContentCommentListAdapter(this, commentListBean.getRows());
        lv_comment.setAdapter(contentCommentListAdapter);
        tv_messagecount.setText(commentListBean.getTotal() + "");
        if (commentListBean.getRows().size() == 0) {
            ((TextView) findViewById(R.id.tv_newcommend)).setText("暂无评论");
            findViewById(R.id.area_commt_empty).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.tv_newcommend)).setText("最新评论");
            findViewById(R.id.area_commt_empty).setVisibility(View.GONE);
        }
    }

    public void sedCommend() {
        String wholeUrl = AppUrl.host + AppUrl.addContentCommentSend;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", coursePlanId);
        map.put("content", et_commentwrite.getText().toString().trim());
        map.put("parentId", "");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commentsedlistener);
    }

    BaseRequestListener commentsedlistener = new JsonRequestListener() {

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
            asyncGetComment();
            area_show_input.setVisibility(View.VISIBLE);
            area_real_input.setVisibility(View.GONE);
            et_commentwrite.setText("");
            showShortToast("评论成功");
        }
    };

    public void admire() {
        String wholeUrl = AppUrl.host + AppUrl.praise;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, admirelistener);
    }

    BaseRequestListener admirelistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            if (errorCode.equals("0000000004") && !isPriseLimt) {
                showShortToast(errorMsg);
                isPriseLimt = true;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            tv_attentcount.setText(jsonResult.optString("data"));
        }
    };

    public void asynGetRecomment() {
        String wholeUrl = AppUrl.host + AppUrl.addContentRecommend;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", coursePlanId);
        map.put("level1CateId", curCourseSectionBean.getLevel1CateId());
        if (curCourseSectionBean.getTeachActivityId() != 0) {
            map.put("teachActivityId", curCourseSectionBean.getTeachActivityId());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, recommendlistener);
    }

    BaseRequestListener recommendlistener = new JsonRequestListener() {
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
            ContentRecommendBean contentRecommendBean = gson.fromJson(jsonResult.toString(), ContentRecommendBean.class);
            ContentRecomendAdapter contentRecomendAdapter = new ContentRecomendAdapter(CourseTextDetailsActivity.this, contentRecommendBean.getRows());
            gv_recommend.setAdapter(contentRecomendAdapter);
            gv_recommend.addItemDecoration(new SpaceItemDecoration(-10));
            if (contentRecommendBean.getRows().size() == 0) {
                area_recommend.setVisibility(View.GONE);
            } else {
                area_recommend.setVisibility(View.VISIBLE);
            }
            contentRecomendAdapter.setOnItemClickListener((view, position) -> {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", contentRecommendBean.getRows().get(position).getId());
                startActivity(it);
            });
        }
    };

    /**
     * 点击评论按钮，显示评论输入框
     */
    public void showComtArea() {
        // 点击评论按钮，显示评论输入框
        area_real_input.setVisibility(View.VISIBLE);
        area_show_input.setVisibility(View.GONE);
        et_commentwrite.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(et_commentwrite, 0);
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = area_commentsend;// getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                area_show_input.setVisibility(View.VISIBLE);
                area_real_input.setVisibility(View.GONE);
                et_commentwrite.setText("");
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

}
