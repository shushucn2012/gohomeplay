package com.park61.teacherhelper.module.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.park61.teacherhelper.AudioBaseActivity;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.AttachmentDLActivity;
import com.park61.teacherhelper.module.course.bean.ContentRecommendBean;
import com.park61.teacherhelper.module.home.adapter.CommentAllAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentCommentListAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.park61.teacherhelper.module.home.adapter.VideoSourcesAdapter;
import com.park61.teacherhelper.module.home.bean.CommentItemBean;
import com.park61.teacherhelper.module.home.bean.CommentListBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.home.bean.VideoItemSource;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.AppBarStateChangeListener;
import com.park61.teacherhelper.widget.FastBlur;
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
import java.util.logging.Handler;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.HttpUrl;
import tyrantgit.widget.HeartLayout;

import static com.park61.teacherhelper.module.home.CourseAudioDetailsActivity.ACTION_DOWNLOAD_FINISHED;

public class CourseAudioDetailActivity extends AudioBaseActivity {

    private int[] paopaoResArray = {R.mipmap.banana, R.mipmap.cherry, R.mipmap.nnut, R.mipmap.polo, R.mipmap.rraspberry,
            R.mipmap.cake, R.mipmap.devil, R.mipmap.love_heart, R.mipmap.moon_stars, R.mipmap.ttaxi,
            R.mipmap.advance, R.mipmap.gifts, R.mipmap.roles, R.mipmap.shanzu, R.mipmap.spring,
            R.mipmap.tiqing, R.mipmap.wen, R.mipmap.xiangshui, R.mipmap.yazi, R.mipmap.yule, R.mipmap.zuifan};

    private AppBarLayout app_bar;
    private View area_gongneng, area_top, rl_edit;
    private TextView page_title, tv_scannumber, tv_title, tv_time, tv_source_num, tv_attentcount, tv_messagecount;
    private ImageView img_topbg, img_auth, img_audio, img_collect, img_cache, img_dl_animate;
    private ListView lv_video_source, lv_comment;
    private ShowImageWebView wv_content;
    private RecyclerView rv_recommend;
    private EditText et_commentwrite;
    private HeartLayout mHeartLayout;
    private NestedScrollView content_sroll;
    private AnimationDrawable animationDrawable;

    private int coursePlanId;
    private String topTitleStr = "";
    private ContentCommentListAdapter contentCommentListAdapter;
    private List<CommentItemBean> mList = new ArrayList<>();
    private List<VideoItemSource> sList = new ArrayList<>();
    private VideoSourcesAdapter videoSourcesAdapter;
    private CourseSectionBean curCourseSectionBean;
    private ContentRecommendBean contentRecommendBean;
    public int curPlayId;//当前播放资源id
    private boolean isShowed;//是否显示下载提示
    private boolean isPlayAll = false;//是否进行全部顺序播放
    private boolean isPriseLimt;//是否到达点赞上限

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_course_audio_detail);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        app_bar = findViewById(R.id.app_bar);
        area_gongneng = findViewById(R.id.area_gongneng);
        page_title = findViewById(R.id.page_title);
        area_top = findViewById(R.id.area_top);
        img_topbg = findViewById(R.id.img_topbg);

        lv_comment = (ListView) findViewById(R.id.lv_comment);
        tv_scannumber = findViewById(R.id.tv_scannumber);
        tv_title = findViewById(R.id.tv_title);
        tv_time = findViewById(R.id.tv_time);
        img_auth = findViewById(R.id.img_auth);
        img_audio = findViewById(R.id.img_audio);
        wv_content = findViewById(R.id.wv_content);
        tv_source_num = findViewById(R.id.tv_source_num);
        lv_video_source = findViewById(R.id.lv_video_source);

        rv_recommend = (RecyclerView) findViewById(R.id.rv_recommend);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        rv_recommend.setLayoutManager(manager);

        rl_edit = findViewById(R.id.rl_edit);
        et_commentwrite = findViewById(R.id.et_commentwrite);
        tv_attentcount = findViewById(R.id.tv_attentcount);
        img_collect = findViewById(R.id.img_collect);
        mHeartLayout = findViewById(R.id.heart_layout);
        tv_messagecount = findViewById(R.id.tv_messagecount);
        content_sroll = findViewById(R.id.content_sroll);
        img_cache = findViewById(R.id.img_cache);
        img_dl_animate = findViewById(R.id.img_dl_animate);

        img_dl_animate.setImageResource(R.drawable.dl_animation_list_loading);
        animationDrawable = (AnimationDrawable) img_dl_animate.getDrawable();

        initVodPlayer();
    }

    @Override
    public void initData() {
        coursePlanId = getIntent().getIntExtra("coursePlanId", -1);
       /* mAdapter = new CommentAllAdapter(this, mList);
        lv_comment.setAdapter(mAdapter);*/

        videoSourcesAdapter = new VideoSourcesAdapter(mContext, sList);
        lv_video_source.setAdapter(videoSourcesAdapter);

        curCourseSectionBean = (CourseSectionBean) getIntent().getSerializableExtra("courseBean");
        if (curCourseSectionBean == null) {
            anyscGetVido();
        } else {
            fillData();
        }
    }

    private void blur(Bitmap bkg, ImageView img) {
        bkg = ImageManager.getInstance().reduceBitmapForWx(bkg);
        long startMs = System.currentTimeMillis();
        //float scaleFactor = 8;//图片缩放比例；
        float radius = 20;//模糊程度

        Bitmap overlay = Bitmap.createBitmap((int) (bkg.getWidth()), (int) (bkg.getHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        //canvas.translate(-img.getLeft() / scaleFactor, -img.getTop() / scaleFactor);
        //canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);

        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        img.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        Log.e("jerome", "blur time:" + (System.currentTimeMillis() - startMs));
    }


    @Override
    public void initListener() {
        app_bar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    area_gongneng.setVisibility(View.VISIBLE);
                    page_title.setText(topTitleStr);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    area_gongneng.setVisibility(View.GONE);
                    if (curCourseSectionBean != null)
                        page_title.setText(curCourseSectionBean.getTitle());
                } else {
                    //中间状态
                    area_gongneng.setVisibility(View.VISIBLE);
                    page_title.setText(topTitleStr);
                }
            }
        });
        rl_edit.setOnClickListener(v -> {
            showComtArea();
        });
        findViewById(R.id.tv_commentsend).setOnClickListener(view -> {
            if (CommonMethod.checkUserLogin(mContext)) {
                if (TextUtils.isEmpty(et_commentwrite.getText().toString().trim())) {
                    showShortToast("请输入评论内容");
                    return;
                }
                sedCommend();
            }
        });

        lv_video_source.setOnItemClickListener((parent, view, position, id) -> {
            if (curPlayId == sList.get(position).getSourceId()) {//如果点击歌曲是当前播放的歌曲
                mPlayerState = aliyunVodPlayer.getPlayerState();
                if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {//如果已经开始，就暂停
                    aliyunVodPlayer.pause();
                    sList.get(position).setPlaying(false);
                    videoSourcesAdapter.notifyDataSetChanged();
                } else {//其他状态
                    aliyunVodPlayer.start();
                    sList.get(position).setPlaying(true);
                    videoSourcesAdapter.notifyDataSetChanged();
                }
            } else {//如果点击歌曲不是当前播放的歌曲
                curPlayId = sList.get(position).getSourceId();
                ansycGetVideoAuth();
            }
        });
        videoSourcesAdapter.setOnDownLoadClickedLsner(index -> {
            if (!CommonMethod.checkUserLogin(mContext))
                return;
            if (sList.get(index).getStatus() == 1 || sList.get(index).getStatus() == 2) {
                return;
            }
            //img_cache.setImageResource(R.mipmap.icon_caching);
            startDownLoadingAnimate();
            ansycGetDownLoadAuth(sList.get(index).getSourceId());
        });
        findViewById(R.id.area_to_play_all).setOnClickListener(v -> {
            isPlayAll = true;
            curPlayId = sList.get(0).getSourceId();
            ansycGetVideoAuth();
        });
        aliyunVodPlayer.setOnCompletionListener(() -> {
            if (isPlayAll) {
                curPlayId = getNextPlayId();
                ansycGetVideoAuth();
            }
        });
        findViewById(R.id.area_do_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mHeartLayout.addHeart(mContext.getResources().getColor(R.color.transparent), paopaoResArray[new Random().nextInt(20)], R.mipmap.icon_trans);
                        ansyToAdmire();
                    }
                });
            }
        });
        findViewById(R.id.iv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
                it.putExtra("itemId", coursePlanId);
                startActivity(it);
            }
        });
        findViewById(R.id.iv_whitestore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curCourseSectionBean.getIsCollect()) {
                    asyncCancelCollect();
                } else {
                    asyncAddCollect();
                }
            }
        });
        findViewById(R.id.iv_contentshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curCourseSectionBean == null)
                    return;
                if (curCourseSectionBean.getIsMember() != 0) {
                    dDialog.showDialog("提示", "只有指定人员能够分享此内容", "取消", "确认", null, null);
                    return;
                }
                ViewInitTool.AddStatistics(mContext, "contentshare");
                showShareDialog("http://m.61park.cn/teach/#/activity/activityAudio/" + coursePlanId,
                        curCourseSectionBean.getCoverImg(),
                        curCourseSectionBean.getTitle(),
                        curCourseSectionBean.getIntro());
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

        findViewById(R.id.iv_intro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethod.isListEmpty(sList))
                    return;

                //CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) app_bar.getLayoutParams()).getBehavior();

                int srolly = sList.size() * DevAttr.dip2px(mContext, 72) + DevAttr.dip2px(mContext, 235);
                //content_sroll.scrollTo(0, 500);
                //content_sroll.fullScroll()
                content_sroll.fling(0);
                content_sroll.smoothScrollTo(0, srolly);
                //behavior.onNestedPreScroll(findViewById(R.id.roo_view), app_bar, content_sroll, 0, DevAttr.dip2px(mContext, 335), new int[]{0, 0}, ViewCompat.TYPE_TOUCH);
                //新增一个参数： ViewCompat.TYPE_TOUCH


                //CoordinatorLayout.Behavior behavior1 = ((CoordinatorLayout.LayoutParams) app_bar.getLayoutParams()).getBehavior();
               /* if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
                    if (topAndBottomOffset != 0) {
                        appBarLayoutBehavior.setTopAndBottomOffset(0);
                    }
                }*/
            }
        });
    }

    /**
     * 请求详情数据
     */
    public void anyscGetVido() {
        String wholeUrl = AppUrl.host + AppUrl.findContentDetailById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, vidodatalisenter);
    }

    BaseRequestListener vidodatalisenter = new JsonRequestListener() {

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
                        anyscGetVido();
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
        topTitleStr = TextUtils.isEmpty(curCourseSectionBean.getLevel1CateName()) ? "活动内容" : curCourseSectionBean.getLevel1CateName();
        page_title.setText(topTitleStr);
        tv_scannumber.setText(curCourseSectionBean.getPlayTotalNum());
        tv_title.setText(curCourseSectionBean.getTitle());
        tv_time.setText(curCourseSectionBean.getAuthorName() + " · " + curCourseSectionBean.getShowDate());
        ImageManager.getInstance().displayImg(img_auth, curCourseSectionBean.getAuthorPic());
        ImageManager.getInstance().displayImg(img_audio, curCourseSectionBean.getCoverImg());
        //ImageManager.getInstance().displayImg(img_topbg, curCourseSectionBean.getCoverImg());

        ImageLoader.getInstance().loadImage(curCourseSectionBean.getCoverImg(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                blur(loadedImage, img_topbg);
            }
        });

        tv_attentcount.setText(curCourseSectionBean.getPraiseNumDsc());
        if (curCourseSectionBean.getIsCollect()) {
            img_collect.setImageResource(R.mipmap.icon_collect_focus_new);
        } else {
            img_collect.setImageResource(R.mipmap.icon_collect_normal_new_white);
        }

        //歌单展示后再显示webview
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewInitTool.initShowimgWebview(wv_content);
                ViewInitTool.setWebData(wv_content, curCourseSectionBean.getContent());
            }
        }, 2000);

        //视频详情加载完了再加载评论数据,视频资源列表
        asynGetRecommend();
        ansycGetComment();
        ansycGetVideoList();
        ansycGetFileList();
    }

    /**
     * 获取音频列表
     */
    public void ansycGetVideoList() {
        String wholeUrl = AppUrl.host + AppUrl.getAudioList;
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
            tv_source_num.setText("(共" + jsonResult.optInt("total") + "首)");
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

            videoSourcesAdapter.notifyDataSetChanged();

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
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            videoId = jsonResult.optString("videoId");
            authInfo = jsonResult.optString("videoPlayAuth");
            setPlaySource();
            audioStartPlay();
            changePlayingState();
            if (GlobalParam.userToken != null) {
                ansycAddViewNum();
            }
        }
    };

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
            it.putExtra("type", DownloadService.TYPE_AUDIO);
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
     * 获取当前视频下一个视频的id
     */
    public int getNextPlayId() {
        int nextIndex = 0;
        for (int i = 0; i < sList.size(); i++) {
            if (curPlayId == sList.get(i).getSourceId()) {
                nextIndex = i + 1;
            }
        }
        //如果下一个曲目的序号比最后一个还大，将下一个置成0
        return sList.get(nextIndex > sList.size() - 1 ? 0 : nextIndex).getSourceId();
    }

    /**
     * 请求评论数据
     */
    private void ansycGetComment() {
        String wholeUrl = AppUrl.host + AppUrl.addContentComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", coursePlanId);
        map.put("sort", "create_date");
        map.put("order", "desc");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commlister);
    }

    BaseRequestListener commlister = new JsonRequestListener() {

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

    /**
     * 点赞
     */
    public void ansyToAdmire() {
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
            if ("0000000004".equals(errorCode) && !isPriseLimt) {
                showShortToast(errorMsg);
                isPriseLimt = true;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            tv_attentcount.setText(jsonResult.optString("data"));
        }
    };

    /**
     * 收藏
     */
    private void asyncAddCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_add;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clister);
    }

    BaseRequestListener clister = new JsonRequestListener() {

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
            curCourseSectionBean.setCollect(true);
            img_collect.setImageResource(R.mipmap.icon_collect_focus_new);
            showShortToast("已收藏");
        }
    };

    /**
     * 取消收藏
     */
    private void asyncCancelCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_cancel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", coursePlanId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cclister);
    }

    BaseRequestListener cclister = new JsonRequestListener() {

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
            curCourseSectionBean.setCollect(false);
            img_collect.setImageResource(R.mipmap.icon_collect_normal_new_white);
            showShortToast("已取消收藏");
        }
    };


    /**
     * 发送评论
     */
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
            findViewById(R.id.area_show_input).setVisibility(View.VISIBLE);
            findViewById(R.id.area_real_input).setVisibility(View.GONE);
            et_commentwrite.setText("");
            showShortToast("评论成功");
            ansycGetComment();
        }
    };

    /**
     * 请求相关推荐
     */
    public void asynGetRecommend() {
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
            ContentRecomendAdapter contentRecomendAdapter = new ContentRecomendAdapter(mContext, contentRecommendBean.getRows());
            rv_recommend.setAdapter(contentRecomendAdapter);
            rv_recommend.addItemDecoration(new SpaceItemDecoration(-10));
            if (contentRecommendBean.getRows().size() == 0) {
                findViewById(R.id.area_recommend).setVisibility(View.GONE);
            } else {
                findViewById(R.id.area_recommend).setVisibility(View.VISIBLE);
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
            //接到下载完成的通知时，重新获取列表，更新下载状态
            ansycGetVideoList();
        }
    };

    /**
     * 点击评论按钮，显示评论输入框
     */
    public void showComtArea() {
        // 点击评论按钮，显示评论输入框
        findViewById(R.id.area_real_input).setVisibility(View.VISIBLE);
        findViewById(R.id.area_show_input).setVisibility(View.GONE);
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
            View v = findViewById(R.id.area_commentsend);// getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                findViewById(R.id.area_show_input).setVisibility(View.VISIBLE);
                findViewById(R.id.area_real_input).setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadChangeReceiver);
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
            img_cache.setImageResource(R.mipmap.icon_to_cache_white);
        } else if (finishDlCount == sList.size()) {//全部为已下载，当前为已完成状态
            img_cache.setImageResource(R.mipmap.icon_cache_done_white);
            stopDownLoadingAnimate();
        } else if (downingCount > 0) {//下载中状态
            //img_cache.setImageResource(R.mipmap.icon_caching);
            startDownLoadingAnimate();
        } else {//部分下载完成
            img_cache.setImageResource(R.mipmap.icon_to_cache_white);
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
