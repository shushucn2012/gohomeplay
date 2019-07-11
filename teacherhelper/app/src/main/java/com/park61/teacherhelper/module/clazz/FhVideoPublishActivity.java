package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.dmcbig.mediapicker.entity.Media;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.FilesManager;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.CircleProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.FilesManager.DeleteFile;
import static com.park61.teacherhelper.common.tool.ImageManager.saveBitmapToSD;

/**
 * 视频发布页
 * Created by shubei on 2017/6/13.
 */
public class FhVideoPublishActivity extends BaseActivity {

    private final static int UPLOAD_SUCCESS = 0;//上传成功消息
    private final static int UPLOAD_FAILED = 2;//上传失败消息
    private final static int UPLOAD_PROGRESS = 1;//上传进度消息

    private ImageView img_video_cover, img_delete, img_add_video;
    private TextView tv_video_duration;
    private EditText edit_info;
    private TextView tv_right;
    private CircleProgressDialog mCircleProgressDialog;
    private View area_add_new;

    private int applyId;//申请id
    private int classifyId;//分类项id
    private int id;//申请分类id
    private String title;//标题
    private String path, duration, size, coverPath, gotCover;
    //private VODUploadClient uploader;
    //private VODUploadCallback callback;
    //private String uploadAuth, uploadAddress;
    private String mVideoId, desStr;

    private VODSVideoUploadClient vodsVideoUploadClient;

    //以下四个值由开发者的服务端提供,参考文档：https://help.aliyun.com/document_detail/28756.html（STS介绍）
    // AppServer STS SDK参考：https://help.aliyun.com/document_detail/28788.html
    private String accessKeyId = "STS.HNx7kDUhNKaV42psc898WPh49";//子accessKeyId
    private String accessKeySecret = "4i5dtPtQMLcFif7WcvHzqpVEFPuWr2cMS64mTJsjdpwX";//子accessKeySecret
    private String securityToken = "CAIShwJ1q6Ft5B2yfSjIqY3NfNHwuLdv/KO9NhTBl2NtNbd7v62f2zz2IHtKenZsCegav/Q3nW1V7vsdlrBtTJNJSEnDKNF36pkS6g66eIvGvYmz5LkJ0AMvx7J3T0yV5tTbRsmkZvW/E67fSjKpvyt3xqSAAlfGdle5MJqPpId6Z9AMJGeRZiZHA9EkSWkPtsgWZzmrQpTLCBPxhXfKB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392qesP1P5UyZ8YvC4nthLRMG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLGKrIIzfFclN/hiQvMZ9KWjj55mveDfmoHw0RFJMPGNr7Ie1VZgqhqAAa8uMRKc9yPV0xCYbp/geizLRhkXAasL6q73vyZOyMbrb9a1hV41EE8o1t3+VWZ1Og41gxDoR304xHvPksNXUcioLA2UH7LjVA5kOVDUvCAxXJ/D++N0I7lK68yXwgSXKV8uYqD7I1+Dpco/IDxVZWhjQQApQk81JepCHOaIqEig";
    private String expriedTime = "2018-01-05T12:31:08Z";//STS的过期时间

    private String requestID = null;//传空或传递访问STS返回的requestID

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_SUCCESS) {//上传成功
                mCircleProgressDialog.dialogDismiss();
                /*showDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        asyncFabu();
                    }
                }, 5000);//延时以避免阿里云封面图不能获取*/
                asyncFabu();
            } else if (msg.what == UPLOAD_PROGRESS) {//回显进度
                mCircleProgressDialog.setProgress(msg.arg1);
            } else if (msg.what == UPLOAD_FAILED) {
                showShortToast("上传失败，请重试！");
                mCircleProgressDialog.dialogDismiss();
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fh_video_publish);
    }

    @Override
    public void initView() {
        mCircleProgressDialog = new CircleProgressDialog(mContext);
        img_video_cover = (ImageView) findViewById(R.id.img_video_cover);
        tv_video_duration = (TextView) findViewById(R.id.tv_video_duration);
        edit_info = (EditText) findViewById(R.id.edit_info);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        area_add_new = findViewById(R.id.area_add_new);
        img_add_video = (ImageView) findViewById(R.id.img_add_video);

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("确认");
        //初始化上传器
        vodsVideoUploadClient = new VODSVideoUploadClientImpl(this.getApplicationContext());
        vodsVideoUploadClient.init();
    }

    @Override
    public void initData() {
        applyId = getIntent().getIntExtra("applyId", -1);
        classifyId = getIntent().getIntExtra("classifyId", -1);
        id = getIntent().getIntExtra("id", -1);
        //设置标题
        title = getIntent().getStringExtra("title");
        setPagTitle(title);
        edit_info.setText(getIntent().getStringExtra("content"));

        mVideoId = getIntent().getStringExtra("videoId");
        gotCover = getIntent().getStringExtra("videoImg");
        if (!TextUtils.isEmpty(gotCover)) {//有封面则已经传过
            ImageManager.getInstance().displayImg(img_video_cover, gotCover);
        }

        if (TextUtils.isEmpty(mVideoId)) {//第一次传
            path = getIntent().getStringExtra("videopath");
            duration = getIntent().getStringExtra("videoduration");
            size = getIntent().getStringExtra("videosize");
            if (TextUtils.isEmpty(size)) {
                try {
                    size = FilesManager.getFileSize(path) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv_video_duration.setText(duration);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
            Bitmap bitmap = media.getFrameAtTime();
            img_video_cover.setImageBitmap(bitmap);
            coverPath = saveBitmapToSD(bitmap);
        }
    }

    @Override
    public void initListener() {
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.showDialog("提示", "退出此次编辑？", "取消", "退出", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dDialog.dismissDialog();
                        hideKeyboard();
                        finish();
                    }
                });
            }
        });
        /*callback = new VODUploadCallback() {

            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                uploader.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
                logout("=======================================开始上传==========================================");
                mCircleProgressDialog.showDialog();
            }

            *//**
         * 上传成功回调
         *//*
            public void onUploadSucceed(UploadFileInfo info) {
                logout("=======================================上传成功==========================================");
                handler.sendEmptyMessage(0);
            }

            *//**
         * 上传失败
         *//*
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                logout("=======================================上传失败==========================================");
                handler.sendEmptyMessage(2);
            }

            *//**
         * 回调上传进度
         * @param uploadedSize 已上传字节数
         * @param totalSize 总共需要上传字节数
         *//*
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = (int) ((uploadedSize * 100) / totalSize);
                handler.sendMessage(msg);
                logout("=======================================onUploadProgress==========================================" + uploadedSize + "/" + totalSize);
            }

            *//**
         * 上传凭证过期后，会回调这个接口
         * 可在这个回调中获取新的上传，然后调用resumeUploadWithAuth继续上传
         *//*
            public void onUploadTokenExpired() {
                logout("=======================================onUploadTokenExpired==========================================");
            }

            *//**
         * 上传过程中，状态由正常切换为异常时触发
         *//*
            public void onUploadRetry(String code, String message) {
                logout("=======================================onUploadRetry==========================================");
            }

            *//**
         * 上传过程中，从异常中恢复时触发
         *//*
            public void onUploadRetryResume() {
                logout("=======================================onUploadRetryResume==========================================");
            }
        };*/
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mVideoId)) {//新进来
                    if (TextUtils.isEmpty(path)) {//没有选择视频，已经传过
                        showShortToast("请选择上传的视频");
                        return;
                    }
                    if (FU.paseLong(size) > 75 * 1024 * 1024) {
                        showShortToast("不能上传大于75M的视频");
                        return;
                    }
                    desStr = edit_info.getText().toString().trim();
                    if (TextUtils.isEmpty(desStr)) {
                        showShortToast("请填写视频描述");
                        return;
                    }
                    //asyncDetailsData();
                    asyncVideoUploadSTS();
                } else {//编辑进来
                    desStr = edit_info.getText().toString().trim();
                    if (TextUtils.isEmpty(desStr)) {
                        showShortToast("请填写视频描述");
                        return;
                    }
                    asyncFabu();
                }
            }
        });
        img_video_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(path)) {
                    Intent it = new Intent(Intent.ACTION_VIEW);

               /* Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(mContext, "com.asuper.angelgroup.FileProvider", imageFile);
                } else {
                    uri = Uri.fromFile(imageFile);
                }*/

                    Uri uri = Uri.parse(path);
                    it.setDataAndType(uri, "video/mp4");
                    startActivity(it);
                }
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_add_new.setVisibility(View.GONE);
                img_add_video.setVisibility(View.VISIBLE);
            }
        });
        img_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ChoosePublishTypeActivity.class);
                it.putExtra("title", title);
                it.putExtra("applyId", applyId);
                it.putExtra("classifyId", classifyId);
                it.putExtra("id", id);
                startActivity(it);
            }
        });
    }

    /**
     * 请求视频上传权限密钥
     */
    private void asyncVideoUploadSTS() {
        String wholeUrl = AppUrl.host + AppUrl.getSTSToken;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, new JsonRequestListener() {
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
                accessKeyId = jsonResult.optString("accessKeyId");
                accessKeySecret = jsonResult.optString("accessKeySecret");
                expriedTime = jsonResult.optString("expiration");
                securityToken = jsonResult.optString("securityToken");
                doUploadNew();
            }
        });
    }


   /* private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle(desStr);
        vodInfo.setDesc(desStr);
        vodInfo.setCateId(0);
        vodInfo.setIsProcess(true);
        vodInfo.setCoverUrl("https://www.baidu.com/img/bd_logo1.png?where=super");
        List<String> tags = new ArrayList<>();
        tags.add("61区");
        vodInfo.setTags(tags);
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        return vodInfo;
    }*/

    /**
     * 请求详情数据
     *//*
    private void asyncDetailsData() {
        String wholeUrl = AppUrl.host + AppUrl.getAliyunAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("localPath", path);
        map.put("videoSize", size);
        map.put("title", desStr);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

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
            uploadAuth = jsonResult.optString("uploadAuth");
            uploadAddress = jsonResult.optString("uploadAddress");
            videoId = jsonResult.optString("videoId");

            uploader = new VODUploadClientImpl(mContext);
            uploader.init(callback);
            uploader.addFile(path, getVodInfo());
            uploader.start();
        }
    };*/


    /**
     * 调用sdk上传视频
     */
    public void doUploadNew() {
        //参数请确保存在，如不存在SDK内部将会直接将错误throw Exception
        // 文件路径保证存在之外因为Android 6.0之后需要动态获取权限，请开发者自行实现获取"文件读写权限".
        VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
                .setMaxRetryCount(1)
                .setConnectionTimeout(10 * 1000)
                .setSocketTimeout(10 * 1000)
                .build();

        SvideoInfo svideoInfo = new SvideoInfo();
        svideoInfo.setTitle(new File(path).getName());
        svideoInfo.setDesc(desStr);
        svideoInfo.setCateId(1);

        VodSessionCreateInfo vodSessionCreateInfo = new VodSessionCreateInfo.Builder()
                .setImagePath(coverPath)
                .setVideoPath(path)
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setSecurityToken(securityToken)
                .setRequestID(requestID)
                .setExpriedTime(expriedTime)
                .setIsTranscode(true)
                .setSvideoInfo(svideoInfo)
                .setPartSize(500 * 1024)
                .setVodHttpClientConfig(vodHttpClientConfig)
                .build();
        mCircleProgressDialog.showDialog();
        vodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
            @Override
            public void onUploadSucceed(String videoId, String imageUrl) {
                mVideoId = videoId;
                logout("onUploadSucceed" + "videoId:" + videoId + "imageUrl" + imageUrl);
                handler.sendEmptyMessage(UPLOAD_SUCCESS);
            }

            @Override
            public void onUploadFailed(String code, String message) {
                logout("onUploadFailed" + "code" + code + "message" + message);
                handler.sendEmptyMessage(UPLOAD_FAILED);
            }

            @Override
            public void onUploadProgress(long uploadedSize, long totalSize) {
                logout("onUploadProgress" + uploadedSize * 100 / totalSize);
                Message msg = new Message();
                msg.what = UPLOAD_PROGRESS;
                msg.arg1 = (int) ((uploadedSize * 100) / totalSize);
                handler.sendMessage(msg);
            }

            @Override
            public void onSTSTokenExpried() {
                logout("onSTSTokenExpried");
                handler.sendEmptyMessage(UPLOAD_FAILED);
            }

            @Override
            public void onUploadRetry(String code, String message) {
                //上传重试的提醒
                logout("onUploadRetry" + "code" + code + "message" + message);
            }

            @Override
            public void onUploadRetryResume() {
                //上传重试成功的回调.告知用户重试成功
                logout("onUploadRetryResume");
            }
        });
    }

    /**
     * 提交信息
     */
    private void asyncFabu() {
        String wholeUrl = AppUrl.host + AppUrl.addApplyItemVideo;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("classifyId", classifyId);
        map.put("detail", edit_info.getText().toString().trim());
        map.put("videoId", mVideoId);
        if (id > 0) {//id有就传，第一次没有，修改时有
            map.put("id", id);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, fLsner);
    }

    BaseRequestListener fLsner = new JsonRequestListener() {

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
            showShortToast("上传成功！");
            sendBroadcast(new Intent("ACTION_REFRESH_SUBMIT_ITEM"));
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        dDialog.showDialog("提示", "退出此次编辑？", "取消", "退出", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.dismissDialog();
                hideKeyboard();
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(coverPath)) {//销毁创建的封面图片，避免占用用户空间
            DeleteFile(new File(coverPath));
        }
        vodsVideoUploadClient.release();//释放上传器资源
    }

}
