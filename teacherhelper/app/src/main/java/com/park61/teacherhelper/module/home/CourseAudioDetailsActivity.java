package com.park61.teacherhelper.module.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.park61.teacherhelper.AudioBaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.bean.ContentRecommendBean;
import com.park61.teacherhelper.module.home.adapter.CommentAllAdapter;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.park61.teacherhelper.module.home.adapter.FileSourcesAdapter;
import com.park61.teacherhelper.module.home.adapter.VideoSourcesAdapter;
import com.park61.teacherhelper.module.home.bean.CommentItemBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.home.bean.VideoItemSource;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.SpaceItemDecoration;
import com.park61.teacherhelper.widget.listview.ListViewScrollView;
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
 * 普通内容音频播放详情
 * Created by super on 2018/1/31
 */
public class CourseAudioDetailsActivity extends AudioBaseActivity {

    private int[] paopaoResArray = {R.mipmap.banana, R.mipmap.cherry, R.mipmap.nnut, R.mipmap.polo, R.mipmap.rraspberry,
            R.mipmap.cake, R.mipmap.devil, R.mipmap.love_heart, R.mipmap.moon_stars, R.mipmap.ttaxi,
            R.mipmap.advance, R.mipmap.gifts, R.mipmap.roles, R.mipmap.shanzu, R.mipmap.spring,
            R.mipmap.tiqing, R.mipmap.wen, R.mipmap.xiangshui, R.mipmap.yazi, R.mipmap.yule, R.mipmap.zuifan};

    public static final String ACTION_DOWNLOAD_FINISHED = "ACTION_DOWNLOAD_FINISHED";

    private static final int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;
    private int rootBottom = Integer.MIN_VALUE;

    private TextView tv_title, tv_scannumber, tv_auth_name, tv_time, tv_attentcount, tv_messagecount, tv_commentsend, tv_newcommend, tv_source_num, tv_files_num;
    private ImageView img_auth, img_audio_cover, img_sc, img_redshare, img_audio_center, iv_message;
    ;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private LinearLayout ll_commentbottom, ll_newcomment;
    private EditText et_commentwrite;
    private View cityhot_header_blank, area_to_play_all, area_details_content, area_video_list, area_files_list, area_do_praise, area_recommend, area_to_download_all,
            area_real_input, area_show_input, area_commentsend, area_see_all_files, area_how_to_save;
    private RelativeLayout rl_parent, rl_edit;
    private ListViewScrollView lv_video_source, lv_files;
    private ShowImageWebView wv_content;
    private RadioGroup rg;//标签组
    private View[] stickArray = new View[3];//下划线
    private HeartLayout mHeartLayout;
    private RecyclerView rv_recommend;

    private int coursePlanId;
    private CommentAllAdapter mAdapter;
    private List<CommentItemBean> mList = new ArrayList<>();
    private List<VideoItemSource> sList = new ArrayList<>();
    private VideoSourcesAdapter videoSourcesAdapter;
    private List<FileItemSource> fList = new ArrayList<>();
    private FileSourcesAdapter fileSourcesAdapter;
    private List<FileItemSource> showFList = new ArrayList<>();
    ;
    private CourseSectionBean curCourseSectionBean;
    private ContentRecommendBean contentRecommendBean;
    private int parentid = -1;//父评论id
    private int isreply = 0;//是否是回复 0不是；1是
    private int curPlayId;//当前播放资源id
    private boolean isPlayAll = false;//是否进行全部顺序播放
    private int curDownLoadId;//当前下载资源id
    private boolean isPriseLimt;//是否到达点赞上限
    private boolean isShowed;//是否显示下载提示

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.content_audio);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        initHeadView();

        img_sc = (ImageView) findViewById(R.id.img_sc);
        img_redshare = (ImageView) findViewById(R.id.img_redshare);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        tv_attentcount = (TextView) findViewById(R.id.tv_attentcount);
        iv_message = (ImageView) findViewById(R.id.iv_message);

        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        area_do_praise = findViewById(R.id.area_do_praise);
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        area_real_input = findViewById(R.id.area_real_input);
        area_show_input = findViewById(R.id.area_show_input);
        area_commentsend = findViewById(R.id.area_commentsend);

        initVodPlayer();
    }

    /**
     * 列表头部-视频详情界面-初始化
     */
    private void initHeadView() {
        cityhot_header_blank = LayoutInflater.from(mContext).inflate(R.layout.activity_audiodetail_haslist, actualListView, false);

        area_to_play_all = cityhot_header_blank.findViewById(R.id.area_to_play_all);
        area_details_content = cityhot_header_blank.findViewById(R.id.area_details_content);
        area_video_list = cityhot_header_blank.findViewById(R.id.area_video_list);
        area_files_list = cityhot_header_blank.findViewById(R.id.area_files_list);
        area_to_download_all = cityhot_header_blank.findViewById(R.id.area_to_download_all);

        ll_newcomment = (LinearLayout) cityhot_header_blank.findViewById(R.id.ll_newcomment);
        tv_newcommend = (TextView) cityhot_header_blank.findViewById(R.id.tv_newcommend);
        area_recommend = cityhot_header_blank.findViewById(R.id.area_recommend);

        rv_recommend = (RecyclerView) cityhot_header_blank.findViewById(R.id.rv_recommend);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);
        rv_recommend.setLayoutManager(manager);

        tv_auth_name = (TextView) cityhot_header_blank.findViewById(R.id.tv_auth_name);
        img_auth = (ImageView) cityhot_header_blank.findViewById(R.id.img_auth);
        img_audio_cover = (ImageView) cityhot_header_blank.findViewById(R.id.img_audio_cover);
        img_audio_center = (ImageView) cityhot_header_blank.findViewById(R.id.img_audio_center);
        tv_scannumber = (TextView) cityhot_header_blank.findViewById(R.id.tv_scannumber);
        tv_title = (TextView) cityhot_header_blank.findViewById(R.id.tv_title);
        tv_time = (TextView) cityhot_header_blank.findViewById(R.id.tv_time);
        lv_video_source = (ListViewScrollView) cityhot_header_blank.findViewById(R.id.lv_video_source);
        lv_files = (ListViewScrollView) cityhot_header_blank.findViewById(R.id.lv_files);
        area_see_all_files = cityhot_header_blank.findViewById(R.id.area_see_all_files);
        area_how_to_save = cityhot_header_blank.findViewById(R.id.area_how_to_save);

        tv_source_num = (TextView) cityhot_header_blank.findViewById(R.id.tv_source_num);
        tv_files_num = (TextView) cityhot_header_blank.findViewById(R.id.tv_files_num);
        wv_content = (ShowImageWebView) cityhot_header_blank.findViewById(R.id.wv_content);
        rg = (RadioGroup) cityhot_header_blank.findViewById(R.id.rg);
        stickArray[0] = cityhot_header_blank.findViewById(R.id.stick0);
        stickArray[1] = cityhot_header_blank.findViewById(R.id.stick1);
        stickArray[2] = cityhot_header_blank.findViewById(R.id.stick2);
        stickArray[0].setVisibility(View.VISIBLE);
        initActTabs();

        actualListView.addHeaderView(cityhot_header_blank);
        fileSourcesAdapter = new FileSourcesAdapter(CourseAudioDetailsActivity.this, showFList);
        lv_files.setAdapter(fileSourcesAdapter);
    }

    @Override
    public void initData() {
        coursePlanId = getIntent().getIntExtra("coursePlanId", -1);
        mAdapter = new CommentAllAdapter(this, mList);
        actualListView.setAdapter(mAdapter);

        curCourseSectionBean = (CourseSectionBean) getIntent().getSerializableExtra("courseBean");
        if (curCourseSectionBean == null) {
            anyscGetVido();
        } else {
            fillData();
        }
    }

    @Override
    public void initListener() {
       /* mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                ansycGetComment();
            }
        });*/
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
        mAdapter.setOnReplyClickedLsner((view, position) -> {
            /*InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            isreply = 1;
            parentid = mList.get(position).getId();*/
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
        area_to_play_all.setOnClickListener(v -> {
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
        area_do_praise.setOnClickListener(new View.OnClickListener() {
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
        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
                it.putExtra("itemId", coursePlanId);
                startActivity(it);
            }
        });
        img_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curCourseSectionBean.getIsCollect()) {
                    asyncCancelCollect();
                } else {
                    asyncAddCollect();
                }
            }
        });
        img_redshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewInitTool.AddStatistics(mContext, "contentshare");
                showShareDialog("http://m.61park.cn/teach/#/activity/activityAudio/" + coursePlanId,
                        curCourseSectionBean.getCoverImg(),
                        curCourseSectionBean.getTitle(),
                        curCourseSectionBean.getIntro());
            }
        });
        area_to_download_all.setOnClickListener(v -> {
            if (!CommonMethod.checkUserLogin(mContext))
                return;
            //下载全部 添加进度框
            showDownLoadTip();
            showDialog();
            for (int i = 0; i < fList.size(); i++) {
                FileItemSource fileItemSource = fList.get(i);
                if (fileItemSource.getStatus() != -1 || HttpUrl.parse(fileItemSource.getSourceUrl()) == null) {//已经开始下载了就跳过
                    continue;
                }

                //下载附件
                FileModel fileModel = new FileModel();
                fileModel.setUrl(fileItemSource.getSourceUrl());
                fileModel.setName(fileItemSource.getSourceName());
                fileModel.setCreateTime(DownloadService.fomatCurrentTime());
                fileModel.setPath(OkDownload.getInstance().getFolder() + fileItemSource.getSourceName());
                fileModel.setSize(fileItemSource.getShowFileSize());
                fileModel.setIconUrl(fileItemSource.getIconUrl());
                GetRequest<File> request = OkGo.<File>get(fileModel.getUrl());
                OkDownload.request(fileModel.getUrl(), request).extra1(fileModel).save();

                if (OkDownload.getInstance().getTask(fileItemSource.getSourceUrl()) != null) {
                    DownloadTask task = OkDownload.getInstance().getTask(fileItemSource.getSourceUrl());
                    task.register(new ListDownloadListener(fileItemSource));
                }

                fileItemSource.setStatus(0);//未下载完成状态
                fileItemSource.setTotalDownloadNum(FU.addDownLoadNumStr(fileItemSource.getTotalDownloadNum()));
                addDowLoadNum(fileItemSource.getId());
            }
            fileSourcesAdapter.notifyDataSetChanged();
            new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    OkDownload.getInstance().startAll();
                    dismissDialog();
                }
            }.sendEmptyMessageDelayed(0, 1500);
        });
        area_see_all_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFList.clear();
                showFList.addAll(fList);
                fileSourcesAdapter.notifyDataSetChanged();
                area_see_all_files.setVisibility(View.GONE);
            }
        });
        area_how_to_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                it.putExtra("url", "http://m.61park.cn/teach/#/servicedict/index/355");
                startActivity(it);
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
        tv_scannumber.setText(curCourseSectionBean.getPlayTotalNum());
        tv_title.setText(curCourseSectionBean.getTitle());
        tv_time.setText(curCourseSectionBean.getShowDate());
        ImageManager.getInstance().displayImg(img_auth, curCourseSectionBean.getAuthorPic());
        tv_auth_name.setText(curCourseSectionBean.getAuthorName());
        ImageManager.getInstance().displayImg(img_audio_cover, curCourseSectionBean.getCoverImg());
        ImageManager.getInstance().displayImg(img_audio_center, curCourseSectionBean.getCoverImg());
        ViewInitTool.initShowimgWebview(wv_content);
        ViewInitTool.setWebData(wv_content, curCourseSectionBean.getContent());
        tv_attentcount.setText(curCourseSectionBean.getPraiseNumDsc());
        if (curCourseSectionBean.getIsCollect()) {
            img_sc.setImageResource(R.mipmap.corse_store);
        } else {
            img_sc.setImageResource(R.mipmap.content_redrestore);
        }

        //视频详情加载完了再加载评论数据,视频资源列表
        asynGetRecommend();
        ansycGetComment();
        ansycGetVideoList();
        ansycGetFileList();
    }

    /**
     * 初始化标签
     */
    private void initActTabs() {
        rg.check(R.id.rb_intro);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            int checkedIndex = 0;
            for (int i = 0; i < group.getChildCount(); i++) {
                if (checkedId == group.getChildAt(i).getId()) {
                    checkedIndex = i;
                }
            }
            showStick(checkedIndex);
            swithPage(checkedIndex);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rg.check(R.id.rb_list);
            }
        }, 500);
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 切换详情和游戏评价
     */
    public void swithPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                area_details_content.setVisibility(View.VISIBLE);
                area_video_list.setVisibility(View.GONE);
                area_files_list.setVisibility(View.GONE);
                break;
            case 1:
                area_details_content.setVisibility(View.GONE);
                area_video_list.setVisibility(View.VISIBLE);
                area_files_list.setVisibility(View.GONE);
                break;
            case 2:
                area_details_content.setVisibility(View.GONE);
                area_video_list.setVisibility(View.GONE);
                area_files_list.setVisibility(View.VISIBLE);
                break;
        }
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
            tv_source_num.setText("歌单(" + jsonResult.optInt("total") + ")");
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
            videoSourcesAdapter = new VideoSourcesAdapter(mContext, sList);
            lv_video_source.setAdapter(videoSourcesAdapter);

            videoSourcesAdapter.setOnDownLoadClickedLsner(index -> {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                if (sList.get(index).getStatus() == 1 || sList.get(index).getStatus() == 2) {
                    return;
                }
                curDownLoadId = sList.get(index).getSourceId();
                ansycGetDownLoadAuth();
            });

            if (curPlayId == 0) {//刚进来还没有点播，播放第一个
                curPlayId = sList.get(0).getSourceId();
                ansycGetVideoAuth();
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
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0) {
                cityhot_header_blank.findViewById(R.id.rb_files).setVisibility(View.GONE);
                cityhot_header_blank.findViewById(R.id.area_stick2).setVisibility(View.GONE);
                return;
            }
            fList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                FileItemSource source = gson.fromJson(jot.toString(), FileItemSource.class);
                source.setStatus(ViewInitTool.getFilesItemDownLoadStatus(source.getSourceUrl()));//下载状态
                fList.add(source);
            }
            tv_files_num.setText("相关课件(" + fList.size() + ")");
            showFList.clear();
            if (fList.size() > 3) {
                showFList.addAll(fList.subList(0, 3));
                area_see_all_files.setVisibility(View.VISIBLE);
            } else {
                showFList.addAll(fList);
                area_see_all_files.setVisibility(View.GONE);
            }

            fileSourcesAdapter.notifyDataSetChanged();
            fileSourcesAdapter.setOnDownLoadClickedLsner(index -> {
                if (!CommonMethod.checkUserLogin(mContext))
                    return;
                FileItemSource fileItemSource = fList.get(index);
                if (fileItemSource.getStatus() != -1) {
                    return;
                }
                if (HttpUrl.parse(fileItemSource.getSourceUrl()) == null) {
                    showShortToast("文件地址有误，无法下载");
                    return;
                }

                showDownLoadTip();

                //下载附件
                FileModel fileModel = new FileModel();
                fileModel.setUrl(fileItemSource.getSourceUrl());
                fileModel.setName(fileItemSource.getSourceName());
                fileModel.setCreateTime(DownloadService.fomatCurrentTime());
                fileModel.setPath(OkDownload.getInstance().getFolder() + fileItemSource.getSourceName());
                fileModel.setSize(fileItemSource.getShowFileSize());
                fileModel.setIconUrl(fileItemSource.getIconUrl());
                GetRequest<File> request = OkGo.<File>get(fileModel.getUrl());
                OkDownload.request(fileModel.getUrl(), request).extra1(fileModel).save().start();
                //showShortToast("开始下载");

                if (OkDownload.getInstance().getTask(fileItemSource.getSourceUrl()) != null) {
                    DownloadTask task = OkDownload.getInstance().getTask(fileItemSource.getSourceUrl());
                    task.register(new ListDownloadListener(fileItemSource));
                }
                addDowLoadNum(fileItemSource.getId());
                fileItemSource.setStatus(0);//未下载完成状态
                fileItemSource.setTotalDownloadNum(FU.addDownLoadNumStr(fileItemSource.getTotalDownloadNum()));
                fileSourcesAdapter.notifyDataSetChanged();
            });
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
    public void ansycGetDownLoadAuth() {
        String wholeUrl = AppUrl.host + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curDownLoadId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, adlister);
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
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
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
            addDowLoadNum();
            showDownLoadTip();
        }
    };

    /**
     * 增加下载次数
     */
    public void addDowLoadNum() {
        String wholeUrl = AppUrl.host + AppUrl.addDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curDownLoadId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    public void addDowLoadNum(int id) {
        String wholeUrl = AppUrl.host + AppUrl.addDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

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
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
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
            mPullRefreshListView.onRefreshComplete();
            if (PAGE_NUM >= 0) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            ArrayList<CommentItemBean> currentPageList = new ArrayList<CommentItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);

                ((TextView) findViewById(R.id.tv_newcommend)).setText("暂无评论");
                findViewById(R.id.area_commt_empty).setVisibility(View.VISIBLE);

                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                mList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
            }
            for (int i = 0; i < cmtJay.length(); i++) {
                JSONObject actJot = cmtJay.optJSONObject(i);
                CommentItemBean p = gson.fromJson(actJot.toString(), CommentItemBean.class);
                currentPageList.add(p);
            }
            tv_messagecount.setText(jsonResult.optInt("total") + "");

            ((TextView) findViewById(R.id.tv_newcommend)).setText("最新评论");
            findViewById(R.id.area_commt_empty).setVisibility(View.GONE);

            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

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
            if (errorCode.equals("0000000004") && !isPriseLimt) {
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
            img_sc.setImageResource(R.mipmap.corse_store);
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
            img_sc.setImageResource(R.mipmap.content_redrestore);
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
        if (isreply == 1) {
            map.put("parentId", parentid);
        }
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
            area_show_input.setVisibility(View.VISIBLE);
            area_real_input.setVisibility(View.GONE);
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

    private class ListDownloadListener extends DownloadListener {

        public ListDownloadListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //更新列表
            for (int i = 0; i < fList.size(); i++) {
                FileItemSource source = fList.get(i);
                if(source == tag){
                    source.setStatus(1);
                    break;
                }
            }
            fileSourcesAdapter.notifyDataSetChanged();
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

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
            showShortToast("已加入下载列表，可在我的-我的下载中查看");
            isShowed = true;
        }
    }

}
