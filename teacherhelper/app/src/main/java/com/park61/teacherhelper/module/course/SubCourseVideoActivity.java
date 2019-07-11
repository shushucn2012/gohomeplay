package com.park61.teacherhelper.module.course;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.VideoBaseActivity;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.UIUtil;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.course.bean.SubCourseBean;
import com.park61.teacherhelper.module.course.bean.SubCourseListBean;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.FhDetailsCommtActivity;
import com.park61.teacherhelper.module.home.adapter.ContentCommentListAdapter;
import com.park61.teacherhelper.module.home.adapter.FileSourcesAdapter;
import com.park61.teacherhelper.module.home.bean.CommentListBean;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.net.request.SimpleRequestListener;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ObservableScrollView;
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
 * 专家课程详情视频
 * Created by shubei on 2017/8/16.
 */

public class SubCourseVideoActivity extends VideoBaseActivity {

    private int[] paopaoResArray = {R.mipmap.banana, R.mipmap.cherry, R.mipmap.nnut, R.mipmap.polo, R.mipmap.rraspberry,
            R.mipmap.cake, R.mipmap.devil, R.mipmap.love_heart, R.mipmap.moon_stars, R.mipmap.ttaxi,
            R.mipmap.advance, R.mipmap.gifts, R.mipmap.roles, R.mipmap.shanzu, R.mipmap.spring,
            R.mipmap.tiqing, R.mipmap.wen, R.mipmap.xiangshui, R.mipmap.yazi, R.mipmap.yule, R.mipmap.zuifan};

    private ImageView img_back, img_sc, iv_whitestore;
    private RoundedImageView img_recom_one, img_recom_two;
    public View paly_backveiw, area_do_praise, area_recommend, area_commentsend, area_real_input, area_show_input, recom_area_one, recom_area_two;
    public RelativeLayout rl_contenttitle, area_top, rl_edit;
    public LinearLayout ll_textcoutent, ll_commentbottom;
    public ListView lv_comment, lv_files;
    private ShowImageWebView wvContent;
    private EditText et_commentwrite;
    private ContentCommentListAdapter contentCommentListAdapter;
    private ObservableScrollView sv;
    private ImageView iv_message, iv_attent;
    private TextView tv_course_titles, tv_titlename, tv_titletime, tv_messagecount, tv_attentcount, tv_title, tv_commentsend, recom_tv_one, recom_tv_two;
    private HeartLayout mHeartLayout;

    private String authInfo;
    private String videoId;
    private int subCourseId;
    public int courseContentId;
    private SubCourseBean subCourseBean;
    private SubCourseListBean recomOne, recomTwo;
    private int collect;//0 未收藏，1 已收藏
    private boolean isPriseLimt;
    private int curPlayId;//当前播放资源id
    private int taskCalendarId;//学习任务id,可以判断是否记录学习时间
    private long startTime, endTime;//开始时间
    private boolean isShowed;//是否显示下载提示

    private List<FileItemSource> fList = new ArrayList<>();
    private FileSourcesAdapter fileSourcesAdapter;
    private List<FileItemSource> showFList = new ArrayList<>();

    @Override
    public void setLayout() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_subcourse_video);
    }

    @Override
    public void initView() {
        area_recommend = findViewById(R.id.area_recommend);
        recom_area_one = findViewById(R.id.recomm_area_one);
        recom_area_two = findViewById(R.id.recomm_area_two);
        tv_title = (TextView) findViewById(R.id.tv_title);
        wvContent = (ShowImageWebView) findViewById(R.id.wvContent);
        area_top = (RelativeLayout) findViewById(R.id.area_top);
        rl_contenttitle = (RelativeLayout) findViewById(R.id.rl_contenttitle);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_sc = (ImageView) findViewById(R.id.img_sc);
        iv_whitestore = (ImageView) findViewById(R.id.iv_whitestore);
        paly_backveiw = findViewById(R.id.paly_backveiw);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        mAliyunVodPlayerView.setTitleBarCanShow(false);
        mAliyunVodPlayerView.setControlBarCanShow(false);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.HORIZONTAL);

        area_commentsend = findViewById(R.id.area_commentsend);
        ll_commentbottom = (LinearLayout) findViewById(R.id.ll_commentbottom);
        et_commentwrite = (EditText) findViewById(R.id.et_commentwrite);
        tv_commentsend = (TextView) findViewById(R.id.tv_commentsend);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        img_recom_one = (RoundedImageView) findViewById(R.id.recomm_img_one);
        img_recom_two = (RoundedImageView) findViewById(R.id.recomm_img_two);

        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        tv_attentcount = (TextView) findViewById(R.id.tv_attentcount);
        recom_tv_one = (TextView) findViewById(R.id.recomm_tv_one);
        recom_tv_two = (TextView) findViewById(R.id.recomm_tv_two);
        iv_attent = (ImageView) findViewById(R.id.iv_attent);
        sv = (ObservableScrollView) findViewById(R.id.sv);

        tv_course_titles = (TextView) findViewById(R.id.tv_course_titles);
        tv_titlename = (TextView) findViewById(R.id.tv_titlename);
        tv_titletime = (TextView) findViewById(R.id.tv_titletime);
        ll_textcoutent = (LinearLayout) findViewById(R.id.ll_textcoutent);
        area_do_praise = findViewById(R.id.area_do_praise);
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        area_real_input = findViewById(R.id.area_real_input);
        area_show_input = findViewById(R.id.area_show_input);
        lv_files = (ListView) findViewById(R.id.lv_files);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void initData() {
        taskCalendarId = getIntent().getIntExtra("taskCalendarId", -1);
        subCourseId = getIntent().getIntExtra("subCourseId", -1);

        fileSourcesAdapter = new FileSourcesAdapter(SubCourseVideoActivity.this, showFList);
        lv_files.setAdapter(fileSourcesAdapter);

        asyncGetSubCourse();
    }

    @Override
    public void initListener() {
        findViewById(R.id.iv_redshre).setOnClickListener(v -> {
            ViewInitTool.AddStatistics(mContext, "contentshare");
            showShareDialog("http://m.61park.cn/teach/#/expert/detail/" + subCourseId,
                    subCourseBean.getCoverImg(),
                    subCourseBean.getTitle(),
                    subCourseBean.getSummary());
        });
        findViewById(R.id.iv_contentshare).setOnClickListener(v -> {
            ViewInitTool.AddStatistics(mContext, "contentshare");
            showShareDialog("http://m.61park.cn/teach/#/expert/detail/" + subCourseId,
                    subCourseBean.getCoverImg(),
                    subCourseBean.getTitle(),
                    subCourseBean.getSummary());
        });
        //返回
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });
        iv_message.setOnClickListener(v -> {
            Intent it = new Intent(mContext, FhDetailsCommtActivity.class);
            it.putExtra("itemId", courseContentId);
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
        iv_whitestore.setOnClickListener(view -> store());
        img_sc.setOnClickListener(v -> store());
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
        sv.setScrollViewListener((x, y, oldx, oldy) -> {
            if (y == 0) {
                tran(area_top, 0, 0);
                tran(mAliyunVodPlayerView, 0, 0);
            } else {
                tran(area_top, -y, -oldy);
                tran(mAliyunVodPlayerView, -y, -oldy);
            }
            if (y > UIUtil.dp(mContext, 50)) {
                rl_contenttitle.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            } else {
                rl_contenttitle.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.area_to_download_all).setOnClickListener(v -> {
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
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    OkDownload.getInstance().startAll();
                    dismissDialog();
                }
            }.sendEmptyMessageDelayed(0, 1500);
        });
        findViewById(R.id.area_see_all_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFList.clear();
                showFList.addAll(fList);
                fileSourcesAdapter.notifyDataSetChanged();
                findViewById(R.id.area_see_all_files).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.area_how_to_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                it.putExtra("url", "http://m.61park.cn/teach/#/servicedict/index/355");
                startActivity(it);
            }
        });
    }

    /**
     * 获取课时详情
     */
    private void asyncGetSubCourse() {
        ViewInitTool.AddStatistics(mContext, "contentdetails");
        String wholeUrl = AppUrl.host + AppUrl.getSubCourseDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerCourseId", subCourseId);
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
            dDialog.showDialog("提示", errorMsg, "返回", "重试",
                    v -> {
                        dDialog.dismissDialog();
                        finish();
                    }, v -> {
                        dDialog.dismissDialog();
                        asyncGetSubCourse();
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            sv.smoothScrollTo(0, 0);
            subCourseBean = gson.fromJson(jsonResult.toString(), SubCourseBean.class);
            if (subCourseBean != null) {
                fillData();
            }
        }
    };

    //收藏
    public void store() {
        if (subCourseBean == null) {
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

    public void fillData() {
//        if (subCourseBean.getIsCollect()) {
//            img_sc.setImageResource(R.mipmap.corse_store);
//            iv_whitestore.setImageResource(R.mipmap.course_whitestor);
//            collect = 1;
//        } else {
//            img_sc.setImageResource(R.mipmap.content_redrestore);
//            iv_whitestore.setImageResource(R.mipmap.content_whit_restore);
//            collect = 0;
//        }
//        mAliyunVodPlayerView.setCoverUri(subCourseBean.getCoverImg());

        rl_contenttitle.setVisibility(View.GONE);
        mAliyunVodPlayerView.setVisibility(View.VISIBLE);
        paly_backveiw.setVisibility(View.VISIBLE);
        area_top.setVisibility(View.VISIBLE);

        ViewInitTool.initShowimgWebview(wvContent);
        setWebData();
//        tv_attentcount.setText(curCourseSectionBean.getPraiseNumDsc());
        tv_title.setText(subCourseBean.getTitle());
        tv_course_titles.setText(subCourseBean.getTitle());
        tv_titletime.setText(subCourseBean.getLearningNum() + "人学习");

        if (subCourseBean.getTrainerCourseResource() != null) {
            courseContentId = subCourseBean.getId();
            //日期格式
            tv_titlename.setText(subCourseBean.getTrainerCourseResource().getUpdateTime());
            //播放
            videoId = subCourseBean.getTrainerCourseResource().getUrl();
            authInfo = subCourseBean.getTrainerCourseResource().getVideoPlayAuth();
            initAliyunVodPlayerView(videoId, authInfo);
            if (GlobalParam.userToken != null) {
                ansycSavaStudyNum();
            }
        }
//        ansycGetVideoAuth();
//        asyncGetComment();
        ansycGetFileList();
        asynGetRecomment();
    }

    /**
     * 获取附件列表
     */
    public void ansycGetFileList() {
        String wholeUrl = AppUrl.host + AppUrl.trainerCourseAttachmentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerCourseId", subCourseId);
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
                findViewById(R.id.area_file_list).setVisibility(View.GONE);
                return;
            }
            findViewById(R.id.area_file_list).setVisibility(View.VISIBLE);
            fList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                FileItemSource source = gson.fromJson(jot.toString(), FileItemSource.class);
                source.setStatus(ViewInitTool.getFilesItemDownLoadStatus(source.getSourceUrl()));//下载状态
                fList.add(source);
            }
            ((TextView) findViewById(R.id.tv_files_num)).setText("相关课件(" + fList.size() + ")");
            showFList.clear();
            if (fList.size() > 3) {
                showFList.addAll(fList.subList(0, 3));
                findViewById(R.id.area_see_all_files).setVisibility(View.VISIBLE);
            } else {
                showFList.addAll(fList);
                findViewById(R.id.area_see_all_files).setVisibility(View.GONE);
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

                if (OkDownload.getInstance().getTask(fileItemSource.getSourceUrl()) != null) {
                    DownloadTask task = OkDownload.getInstance().getTask(fileItemSource.getSourceUrl());
                    task.register(new ListDownloadListener(fileItemSource));
                }

                fileItemSource.setStatus(0);//未下载完成状态
                fileItemSource.setTotalDownloadNum(FU.addDownLoadNumStr(fileItemSource.getTotalDownloadNum()));
                fileSourcesAdapter.notifyDataSetChanged();
                addDowLoadNum(fileItemSource.getId());
            });
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
                if (source == tag) {
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
     * 获取视频播放权证
     */
    public void ansycGetVideoAuth() {
        String wholeUrl = AppUrl.host + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", courseContentId);
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
            if (GlobalParam.userToken != null) {
                ansycSavaStudyNum();
            }
        }
    };

    /**
     * 播放一次，保存学习记录
     */
    public void ansycSavaStudyNum() {
        String wholeUrl = AppUrl.host + AppUrl.saveStudyRecord;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerCourseId", subCourseId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    private void asyncAddCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_add;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", courseContentId);
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
            iv_whitestore.setImageResource(R.mipmap.course_whitestor);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            Toast.makeText(SubCourseVideoActivity.this, "已收藏", Toast.LENGTH_LONG).show();
        }
    };

    private void asyncCancelCollect() {
        String wholeUrl = AppUrl.host + AppUrl.teachMyCourse_cancel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", courseContentId);
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
            iv_whitestore.setImageResource(R.mipmap.content_whit_restore);
            sendBroadcast(new Intent().setAction("ACTION_REFRESH_SKLIST"));
            Toast.makeText(SubCourseVideoActivity.this, "已取消收藏", Toast.LENGTH_LONG).show();
        }
    };

    public void asyncGetComment() {
        String wholeUrl = AppUrl.host + AppUrl.addContentComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", courseContentId);
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
        map.put("itemId", courseContentId);
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
        map.put("contentId", courseContentId);
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

    /**
     * 获取相关的推荐课程
     */
    public void asynGetRecomment() {
        String wholeUrl = AppUrl.host + AppUrl.getRecommendCourse;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainerCourseId", subCourseId);
        map.put("trainerCourseSeriesId", subCourseBean.getTrainerCourseSeriesId());
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
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && (taskCalendarId < 0 || subCourseBean.getIsInterest() == 0)) {//不是行事历跳转过来或者不是兴趣课才显示推荐
                area_recommend.setVisibility(View.VISIBLE);
                if (arr.length() > 0) {
                    recom_area_one.setVisibility(View.VISIBLE);
                    recomOne = gson.fromJson(arr.optJSONObject(0).toString(), SubCourseListBean.class);
                    recom_area_one.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(img_recom_one, recomOne.getCoverImg());
                    recom_tv_one.setText(recomOne.getTitle());
                    recom_area_one.setOnClickListener(v -> {
                        if (recomOne.isBuyStatus() || recomOne.getIsProbation() == 1) {
                            subCourseId = recomOne.getId();
                            asyncGetSubCourse();
                        } else {
                            //不可试看 且未购买
                            showShortToast("请购买该课程");
                        }
                    });
                }
                if (arr.length() > 1) {
                    recom_area_two.setVisibility(View.VISIBLE);
                    recomTwo = gson.fromJson(arr.optJSONObject(1).toString(), SubCourseListBean.class);
                    recom_area_two.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(img_recom_two, recomTwo.getCoverImg());
                    recom_tv_two.setText(recomTwo.getTitle());
                    recom_area_two.setOnClickListener(v -> {
                        if (recomTwo.isBuyStatus() || recomTwo.getIsProbation() == 1) {
                            subCourseId = recomTwo.getId();
                            asyncGetSubCourse();
                        } else {
                            //不可试看 且未购买
                            showShortToast("请购买该课程");
                        }
                    });
                }
            } else {
                area_recommend.setVisibility(View.GONE);
            }
        }
    };

    private void setWebData() {
        String htmlDetailsStr = "";
        try {
            String content = subCourseBean.getContent();
            if (content.length() < 100) {
                for (int i = 0; i < 400; i++) {
                    content = content + "     &nbsp";
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
//                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                showBar();

                //设置view的布局，宽高之类
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为小屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Small);

                sv.setVisibility(View.VISIBLE);
//                area_commentsend.setVisibility(View.VISIBLE);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
//                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                hideBar();

                //设置view的布局，宽高
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为全屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Full);

                sv.setVisibility(View.GONE);
//                area_commentsend.setVisibility(View.GONE);
                tran(mAliyunVodPlayerView, 0, 0);
                tran(area_top, 0, 0);
            }
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

    @Override
    protected void onPause() {
        super.onPause();
        endTime = System.currentTimeMillis();
        if (taskCalendarId > 0)
            studyTrainerCourse();
    }

    /**
     * 记录学习记录
     */
    public void studyTrainerCourse() {
        String wholeUrl = AppUrl.host + AppUrl.studyTrainerCourse;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarId", taskCalendarId);
        map.put("time", (endTime - startTime) / 1000);
        map.put("trainerCourseId", subCourseId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 增加下载次数
     */
    public void addDowLoadNum(int id) {
        String wholeUrl = AppUrl.host + AppUrl.addCourseDownLoadNum;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
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
