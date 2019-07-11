package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;

import java.io.File;
import java.util.ArrayList;

/**
 * 拍摄页面
 * Created by shubei on 2017/12/12.
 */

public class PhotoTakeActivity extends BaseActivity {

    JCameraView jCameraView;

    private int applyId;//申请id
    private int classifyId;//分类项id
    private int id;//申请分类id
    private String title;//标题

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_photo_take);
    }

    @Override
    public void initView() {
        applyId = getIntent().getIntExtra("applyId", -1);
        classifyId = getIntent().getIntExtra("classifyId", -1);
        id = getIntent().getIntExtra("id", -1);
        title  = getIntent().getStringExtra("title");

        //1.1.1
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);

        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.e("JCameraView", "bitmap = " + bitmap.getWidth());
                String photoPath = ImageManager.saveBitmapToSD(bitmap);

                File imageFile = new File(photoPath);
                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(mContext, "com.park61.teacherhelper.fileprovider", imageFile);
                } else {
                    uri = Uri.fromFile(imageFile);
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                ArrayList<String> mSelectPath = new ArrayList<String>();
                mSelectPath.add(photoPath);
                Intent intent = new Intent(mContext, PhotoPublishNewActivity.class);
                intent.putExtra("selectPath", mSelectPath);

                intent.putExtra("title", title);
                intent.putExtra("applyId", applyId);
                intent.putExtra("classifyId", classifyId);
                intent.putExtra("id", id);
                CommonMethod.startNewPublishActivity(mContext, PhotoPublishNewActivity.class, FhVideoPublishActivity.class, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.e("CJT", "url = " + url);
                Intent intent = new Intent(mContext, FhVideoPublishActivity.class);
                intent.putExtra("videopath", url);
                intent.putExtra("videoduration", 10);

                intent.putExtra("title", title);
                intent.putExtra("applyId", applyId);
                intent.putExtra("classifyId", classifyId);
                intent.putExtra("id", id);
                CommonMethod.startNewPublishActivity(mContext, PhotoPublishNewActivity.class, FhVideoPublishActivity.class, intent);
                finish();
            }
        });

        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
