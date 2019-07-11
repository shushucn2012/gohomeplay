package com.park61.teacherhelper.module.home;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.android.volley.Request;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.VideoBaseActivity;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.AttachmentDLActivity;
import com.park61.teacherhelper.module.course.bean.ContentRecommendBean;
import com.park61.teacherhelper.module.home.adapter.ContentCommentListAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentVideoSourceAdapter;
import com.park61.teacherhelper.module.home.bean.CommentListBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.module.home.bean.VideoItemSource;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.SpaceItemDecoration;
import com.park61.teacherhelper.widget.dialog.CanRefreshProgressDialog;
import com.park61.teacherhelper.widget.listview.ObservableScrollView;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tyrantgit.widget.HeartLayout;

import static com.park61.teacherhelper.module.home.CourseAudioDetailsActivity.ACTION_DOWNLOAD_FINISHED;

/**
 * 普通内容详情视频1231
 * Created by shubei on 2017/8/16.
 */

public class CourseDetailsActivity extends VideoBaseActivity {

    private int[] paopaoResArray = {R.mipmap.banana, R.mipmap.cherry, R.mipmap.nnut, R.mipmap.polo, R.mipmap.rraspberry,
            R.mipmap.cake, R.mipmap.devil, R.mipmap.love_heart, R.mipmap.moon_stars, R.mipmap.ttaxi,
            R.mipmap.advance, R.mipmap.gifts, R.mipmap.roles, R.mipmap.shanzu, R.mipmap.spring,
            R.mipmap.tiqing, R.mipmap.wen, R.mipmap.xiangshui, R.mipmap.yazi, R.mipmap.yule, R.mipmap.zuifan};

    //整页的进度框，在页面渲染之后消失，或者页面关闭之后自动关闭，可能会出现异常（已捕获处理），为了实现更好的即时效果
    private CanRefreshProgressDialog mCanRefreshProgressDialog;
    private ImageView img_back, iv_message, img_collect, img_cache, img_dl_animate;
    public View paly_backveiw, area_do_praise, area_recommend, area_commentsend, area_real_input, area_show_input, iv_whitestore;
    public RelativeLayout area_top, rl_edit;
    public LinearLayout ll_title, ll_commentbottom;
    public ListView lv_files, lv_comment;
    private ShowImageWebView wvContent;
    private RecyclerView rv_video_source, rv_recommend;
    private EditText et_commentwrite;
    private ContentCommentListAdapter contentCommentListAdapter;
    private ObservableScrollView sv;
    private TextView tv_course_title, tv_read_num, tv_messagecount, tv_attentcount, tv_commentsend;
    private HeartLayout mHeartLayout;
    private AnimationDrawable animationDrawable;

    private String videoId, authInfo;
    public int coursePlanId;
    private CourseSectionBean curCourseSectionBean;
    private ContentRecommendBean contentRecommendBean;
    private int collect;//0 未收藏，1 已收藏
    private boolean isPriseLimt;
    private List<VideoItemSource> sList = new ArrayList<>();
    //private VideoSourcesAdapter videoSourcesAdapter;
    private ContentVideoSourceAdapter videoSourcesAdapter;
    private int curPlayId;//当前播放资源id
    //private int curDownLoadId;//当前下载资源id
    //private boolean isPlayAll = false;//是否进行全部顺序播放
    private boolean isShowed;//是否显示下载提示

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_course_details);
    }

    @Override
    public void initView() {
        mCanRefreshProgressDialog = new CanRefreshProgressDialog(mContext);
        registerRefreshReceiver();
        area_recommend = findViewById(R.id.area_recommend);
        wvContent = (ShowImageWebView) findViewById(R.id.wvContent);
        lv_files = (ListView) findViewById(R.id.lv_files);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        area_top = (RelativeLayout) findViewById(R.id.area_top);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_collect = (ImageView) findViewById(R.id.img_collect);
        iv_whitestore = findViewById(R.id.iv_whitestore);
        paly_backveiw = findViewById(R.id.paly_backveiw);
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_read_num = (TextView) findViewById(R.id.tv_read_num);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        mAliyunVodPlayerView.setTitleBarCanShow(false);
        mAliyunVodPlayerView.setControlBarCanShow(false);

        rv_video_source = findViewById(R.id.rv_video_source);
        LinearLayoutManager manager_rv_video_source = new LinearLayoutManager(mContext);
        manager_rv_video_source.setOrientation(LinearLayout.HORIZONTAL);
        rv_video_source.setLayoutManager(manager_rv_video_source);

        rv_recommend = (RecyclerView) findViewById(R.id.rv_recommend);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        rv_recommend.setLayoutManager(manager);

        lv_comment = (ListView) findViewById(R.id.lv_comment);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);

        area_commentsend = findViewById(R.id.area_commentsend);
        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        iv_message = (ImageView) findViewById(R.id.iv_message);

        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        tv_attentcount = (TextView) findViewById(R.id.tv_attentcount);
        sv = (ObservableScrollView) findViewById(R.id.sv);

        area_do_praise = findViewById(R.id.area_do_praise);
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        area_real_input = findViewById(R.id.area_real_input);
        area_show_input = findViewById(R.id.area_show_input);
        img_cache = findViewById(R.id.img_cache);
        img_dl_animate = findViewById(R.id.img_dl_animate);

        img_dl_animate.setImageResource(R.drawable.dl_animation_list_loading_black);
        animationDrawable = (AnimationDrawable) img_dl_animate.getDrawable();
    }

    @Override
    public void initData() {
        coursePlanId = getIntent().getIntExtra("coursePlanId", -1);
        asyncGetTeachCourseItem();
    }

    @Override
    public void initListener() {
        findViewById(R.id.iv_contentshare).setOnClickListener(v -> {
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
        iv_message.setOnClickListener(v -> {
            Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
            it.putExtra("itemId", coursePlanId);
            startActivity(it);
        });
        img_back.setOnClickListener(v -> {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            } else {
                finish();
            }
        });
        iv_whitestore.setOnClickListener(view -> {
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
        });
        area_do_praise.setOnClickListener(v -> mHeartLayout.post(new Runnable() {
            @Override
            public void run() {
                mHeartLayout.addHeart(mContext.getResources().getColor(R.color.transparent), paopaoResArray[new Random().nextInt(20)], R.mipmap.icon_trans);
                admire();
            }
        }));
        tv_commentsend.setOnClickListener(view -> {
            if (CommonMethod.checkUserLogin(mContext)) {
                if (TextUtils.isEmpty(et_commentwrite.getText().toString().trim())) {
                    showShortToast("请输入评论内容");
                    return;
                }
                sedCommend();
            }
        });
        rl_edit.setOnClickListener(v -> {
            showComtArea();
        });
        findViewById(R.id.area_attachment_dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AttachmentDLActivity.class);
                intent.putExtra("coursePlanId", coursePlanId);
                startActivity(intent);
            }
        });
        findViewById(R.id.area_download_all_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                if (animationDrawable.isRunning())//正在下载动画运行，代表正在下载，不能重复点击
                    return;
                for (int index = 0; index < sList.size(); index++) {
                    if (sList.get(index).getStatus() == 1 || sList.get(index).getStatus() == 2) {
                        return;
                    }
                    //img_cache.setImageResource(R.mipmap.icon_caching);
                    startDownLoadingAnimate();
                    ansycGetDownLoadAuth(sList.get(index).getSourceId());
                }
            }
        });
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
            //showDialog();
            mCanRefreshProgressDialog.showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
            mCanRefreshProgressDialog.dialogDismiss();
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
            //dismissDialog();
            curCourseSectionBean = gson.fromJson(jsonResult.toString(), CourseSectionBean.class);
            if (curCourseSectionBean.getIsView() == 0) {
                if (curCourseSectionBean.getViewCode() == 1) {
                    dDialog.showDialog("提示", "只有会员可查看此内容，是否马上开通会员？", "退出", "去开通",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    finish();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(mContext, MemberMainActivity.class));
                                    finish();
                                }
                            });
                } else {
                    dDialog.showDialog("提示", "只有指定人员能够查看内容", "退出", "确认",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    finish();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    finish();
                                }
                            });
                }
                return;
            }

            if (curCourseSectionBean.getContentType() == 0) {//图文跳到图文页
                Intent it = new Intent(mContext, CourseTextDetailsActivity.class);
                it.putExtra("coursePlanId", coursePlanId);
                it.putExtra("courseBean", curCourseSectionBean);
                startActivity(it);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            } else if (curCourseSectionBean.getContentType() == 1) {
                fillData();
                mCanRefreshProgressDialog.dialogDismiss();
            } else if (curCourseSectionBean.getContentType() == 2) {//跳到音频页
                Intent it = new Intent(mContext, CourseAudioDetailActivity.class);
                it.putExtra("coursePlanId", coursePlanId);
                it.putExtra("courseBean", curCourseSectionBean);
                startActivity(it);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }
    };

    public void fillData() {
        tv_course_title.setText(curCourseSectionBean.getTitle());
        tv_read_num.setText(FU.paseLong(curCourseSectionBean.getViewNum()) + "次浏览");
        if (curCourseSectionBean.getIsCollect()) {
            img_collect.setImageResource(R.mipmap.icon_collect_focus_new);
            collect = 1;
        } else {
            img_collect.setImageResource(R.mipmap.icon_collect_normal_new);
            collect = 0;
        }
        mAliyunVodPlayerView.setCoverUri(curCourseSectionBean.getCoverImg());

        mAliyunVodPlayerView.setVisibility(View.VISIBLE);
        paly_backveiw.setVisibility(View.VISIBLE);
        area_top.setVisibility(View.VISIBLE);
        ll_title.setVisibility(View.VISIBLE);
        findViewById(R.id.area_gongneng).setVisibility(View.VISIBLE);

        ViewInitTool.initShowimgWebview(wvContent);
        setWebData();
        tv_attentcount.setText(curCourseSectionBean.getPraiseNumDsc());

        ansycGetVideoList();
        ansycGetFileList();
        asyncGetComment();
        asynGetRecomment();
    }

    /**
     * 获取视频列表
     */
    public void ansycGetVideoList() {
        String wholeUrl = AppUrl.host + AppUrl.findContentVideoList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vlister);
    }

    BaseRequestListener vlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("rows");
            if (actJay == null || actJay.length() == 0)
                return;
            sList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                VideoItemSource source = gson.fromJson(jot.toString(), VideoItemSource.class);
                source.setPicUrl(curCourseSectionBean.getCoverImg());
                source.setStatus(ViewInitTool.getSourceItemDownLoadStatus(source.getSourceId(), coursePlanId + ""));//下载状态
                source.setPlaying(source.getSourceId() == curPlayId ? true : false);//播放状态
                sList.add(source);
            }

            jugdeVideoDlStatus();

            videoSourcesAdapter = new ContentVideoSourceAdapter(mContext, sList);
            rv_video_source.setAdapter(videoSourcesAdapter);
            videoSourcesAdapter.setOnItemClickListener(new ContentVideoSourceAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    curPlayId = sList.get(position).getSourceId();
                    ansycGetVideoAuth();
                }
            });

            if (curPlayId == 0) {//刚进来还没有点播，播放第一个
                curPlayId = sList.get(0).getSourceId();
                ansycGetVideoAuth();
            }
        }
    };

    /**
     * 获取视频播放权证
     */
    public void ansycGetVideoAuth() {
        String wholeUrl = AppUrl.host + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curPlayId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alister);
    }

    BaseRequestListener alister = new JsonRequestListener() {

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
            videoId = jsonResult.optString("videoId");
            authInfo = jsonResult.optString("videoPlayAuth");
            initAliyunVodPlayerView(videoId, authInfo);
            changePlayingState();
            if (GlobalParam.userToken != null) {
                ansycAddViewNum();
            }
        }
    };

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
                findViewById(R.id.area_attachment_dl).setOnClickListener(null);
                findViewById(R.id.area_attachment_dl).setAlpha(0.3f);
            }
        }
    };

    /**
     * 改变正在播放的图标
     */
    private void changePlayingState() {
        for (int i = 0; i < sList.size(); i++) {
            if (curPlayId == sList.get(i).getSourceId()) {
                sList.get(i).setPlaying(true);
            } else {
                sList.get(i).setPlaying(false);
            }
        }
        videoSourcesAdapter.notifyDataSetChanged();
    }

    /**
     * 增加播放次数
     */
    public void ansycAddViewNum() {
        String wholeUrl = AppUrl.host + AppUrl.addViewNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curPlayId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 获取视频下载权证
     */
    public void ansycGetDownLoadAuth(int curDownLoadId) {
        String wholeUrl = AppUrl.host + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curDownLoadId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, curDownLoadId, adlister);
    }

    BaseRequestListener adlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int curDownLoadId, String url, JSONObject jsonResult) {
            String mVideoId = jsonResult.optString("videoId");
            String mAuthInfo = jsonResult.optString("videoPlayAuth");

            int curDownLoadIndex = 0;
            for (int i = 0; i < sList.size(); i++) {
                if (curDownLoadId == sList.get(i).getSourceId()) {
                    curDownLoadIndex = i;
                }
            }

            Intent it = new Intent(mContext, DownloadService.class);
            VideoItemSource itemSource = sList.get(curDownLoadIndex);
            it.putExtra("title", itemSource.getTitle());
            it.putExtra("sid", FU.paseLong(itemSource.getSourceId() + ""));
            it.putExtra("vid", mVideoId);
            it.putExtra("contentId", (long) coursePlanId);
            it.putExtra("type", DownloadService.TYPE_VIDEO);
            it.putExtra("playAuth", mAuthInfo);
            it.putExtra("icon", itemSource.getPicUrl());
            startService(it);
            itemSource.setStatus(2);//未下载完成状态
            videoSourcesAdapter.notifyDataSetChanged();
            addDowLoadNum(curDownLoadId);
            showDownLoadTip();
        }
    };


    public void addDowLoadNum(int id) {
        String wholeUrl = AppUrl.host + AppUrl.addDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

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
            img_collect.setImageResource(R.mipmap.icon_collect_focus_new);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            showShortToast("已收藏");
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
            img_collect.setImageResource(R.mipmap.icon_collect_normal_new);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            showShortToast("已取消收藏");
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
            contentRecommendBean = gson.fromJson(jsonResult.toString(), ContentRecommendBean.class);
            ContentRecomendAdapter contentRecomendAdapter = new ContentRecomendAdapter(CourseDetailsActivity.this, contentRecommendBean.getRows());
            rv_recommend.setAdapter(contentRecomendAdapter);
            rv_recommend.addItemDecoration(new SpaceItemDecoration(-10));
            if (contentRecommendBean.getRows().size() == 0) {
                area_recommend.setVisibility(View.GONE);
            } else {
                area_recommend.setVisibility(View.VISIBLE);
            }
            contentRecomendAdapter.setOnItemClickListener(new ContentRecomendAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent it = new Intent(mContext, CourseDetailsActivity.class);
                    it.putExtra("coursePlanId", contentRecommendBean.getRows().get(position).getId());
                    startActivity(it);
                }
            });
        }
    };

    private void setWebData() {
        String htmlDetailsStr = "";
        try {
            String content = curCourseSectionBean.getContent();
            if (curCourseSectionBean.getContentType() == 1) {
                if (content.length() < 100) {
                    for (int i = 0; i < 400; i++) {
                        content = content + "     &nbsp";
                    }
                }
            } else {
                if (content.length() < 100) {
                    for (int i = 0; i < 600; i++) {
                        content = content + "     &nbsp";
                    }
                }
            }
            htmlDetailsStr = HtmlParserTool.replaceImgStr(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wvContent.loadDataWithBaseURL("file:///android_asset", htmlDetailsStr, "text/html", "UTF-8", "");
    }

    public void tran(View view, float a, float b) {
        ObjectAnimator.ofFloat(view, "translationY", a, b)
                .setDuration(0)// 设置执行时间(1000ms)
                .start(); // 开始动画
    }

    public void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
                showBar();

                //设置view的布局，宽高之类
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为小屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Small);

                sv.setVisibility(View.VISIBLE);
                area_commentsend.setVisibility(View.VISIBLE);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
                hideBar();

                //设置view的布局，宽高
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为全屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Full);

                sv.setVisibility(View.GONE);
                area_commentsend.setVisibility(View.GONE);
                tran(mAliyunVodPlayerView, 0, 0);
                tran(area_top, 0, 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadChangeReceiver);
    }

    /**
     * 注册下载完成广播接收器
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_FINISHED);
        registerReceiver(downloadChangeReceiver, filter);
    }

    private BroadcastReceiver downloadChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //showShortToast("下载完成");
            ansycGetVideoList();
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

    /**
     * 显示下载提示
     */
    private void showDownLoadTip() {
        if (!isShowed) {
            showShortToast("已加入缓存列表，可在我的-我的缓存中查看");
            isShowed = true;
        }
    }

    /**
     * 判断并设置当前下载状态
     */
    private void jugdeVideoDlStatus() {
        int nostartCount = 0;//未下载视频数量
        int finishDlCount = 0;//下载完成视频数量
        int downingCount = 0;//下载中的数量

        for (int i = 0; i < sList.size(); i++) {
            if (sList.get(i).getStatus() == -1) {//统计未下载数量
                nostartCount++;
            } else if (sList.get(i).getStatus() == 1) {//统计已完成数量
                finishDlCount++;
            } else {
                downingCount++;
            }
        }

        logout("===================nostartCount===============" + nostartCount);
        logout("===================finishDlCount===============" + finishDlCount);
        logout("===================downingCount===============" + downingCount);

        if (nostartCount == sList.size()) {//全部为未下载，当前为未下载状态
            img_cache.setImageResource(R.mipmap.icon_to_cache);
        } else if (finishDlCount == sList.size()) {//全部为已下载，当前为已完成状态
            img_cache.setImageResource(R.mipmap.icon_cache_done);
            stopDownLoadingAnimate();
        } else if (downingCount > 0) {//下载中状态
            //img_cache.setImageResource(R.mipmap.icon_caching);
            startDownLoadingAnimate();
        } else {//下载中状态
            img_cache.setImageResource(R.mipmap.icon_to_cache);
            stopDownLoadingAnimate();
        }
    }

    private void startDownLoadingAnimate() {
        img_dl_animate.setVisibility(View.VISIBLE);
        if (!animationDrawable.isRunning())
            animationDrawable.start();
    }

    private void stopDownLoadingAnimate() {
        img_dl_animate.setVisibility(View.GONE);
        if (!animationDrawable.isRunning())
            animationDrawable.stop();
    }


}
