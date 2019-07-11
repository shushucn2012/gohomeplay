package com.park61.teacherhelper.module.clazz;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.PermissionHelper;

import java.util.ArrayList;

/**
 * 选择发布类型页面
 */
public class ChoosePublishTypeActivity extends BaseActivity implements OnClickListener {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机"),
            new PermissionHelper.PermissionModel(1, Manifest.permission.RECORD_AUDIO, "录音")
    };

    private PermissionHelper permissionHelper;

    private View area_camera, area_photo;

    private ArrayList<Media> select;
    private int applyId;//申请id
    private int classifyId;//分类项id
    private int id;//申请分类id
    private String title;//标题

    @Override
    public void setLayout() {
        setContentView(R.layout.media_select_dialog);
    }

    @Override
    public void initView() {
        area_camera = findViewById(R.id.area_camera);
        area_photo = findViewById(R.id.area_photo);
    }

    @Override
    public void initData() {
        applyId = getIntent().getIntExtra("applyId", -1);
        classifyId = getIntent().getIntExtra("classifyId", -1);
        id = getIntent().getIntExtra("id", -1);
        title  = getIntent().getStringExtra("title");
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(ChoosePublishTypeActivity.this);
        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        area_camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToRecord();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToRecord();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToRecord();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                        //showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
            }
        });
        area_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(mContext, PhotoPublishActivity.class);
                startActivity(intent);
                finish();*/
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToPhoto();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToPhoto();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToPhoto();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                        //showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
            }
        });
    }

    /**
     * 去拍摄
     */
    public void goToRecord() {
        Intent it = new Intent(mContext, PhotoTakeActivity.class);
        it.putExtra("title", title);
        it.putExtra("applyId", applyId);
        it.putExtra("classifyId", classifyId);
        it.putExtra("id", id);
        startActivity(it);
        finish();
    }

    /**
     * 去相册
     */
    public void goToPhoto() {
        Intent intent = new Intent(ChoosePublishTypeActivity.this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select); // (Optional)
        startActivityForResult(intent, 200);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            ArrayList<String> mSelectPath = new ArrayList<>();
            for (Media media : select) {
                Log.e("media", media.path);
                mSelectPath.add(media.path);
                Log.e("media", "s:" + media.size);
                if (media.mediaType == 3) {
                    Intent intent = new Intent(mContext, FhVideoPublishActivity.class);
                    intent.putExtra("videopath", media.path);
                    intent.putExtra("videoduration", media.time);
                    intent.putExtra("videosize", media.size);

                    intent.putExtra("title", title);
                    intent.putExtra("applyId", applyId);
                    intent.putExtra("classifyId", classifyId);
                    intent.putExtra("id", id);
                    CommonMethod.startNewPublishActivity(mContext, PhotoPublishNewActivity.class, FhVideoPublishActivity.class, intent);
                    finish();
                    return;
                }
            }
            Intent intent = new Intent(mContext, PhotoPublishNewActivity.class);
            intent.putExtra("selectPath", mSelectPath);

            intent.putExtra("title", title);
            intent.putExtra("applyId", applyId);
            intent.putExtra("classifyId", classifyId);
            intent.putExtra("id", id);
            CommonMethod.startNewPublishActivity(mContext, PhotoPublishNewActivity.class, FhVideoPublishActivity.class, intent);
            finish();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            ArrayList<String> mSelectPath = new ArrayList<>();
            for (Media media : select) {
                android.util.Log.e("media", media.path);
                mSelectPath.add(media.path);
                logout("media===============s:" + media.size);
                if (media.mediaType == 3) {
                    Intent intent = new Intent(mContext, FhVideoPublishActivity.class);
                    intent.putExtra("videopath", media.path);
                    intent.putExtra("videoduration", media.time);
                    intent.putExtra("videosize", media.size);
                    intent.putExtra("title", list.get(chosenPos).getName());
                    intent.putExtra("applyId", applyId);
                    intent.putExtra("classifyId", list.get(chosenPos).getId());
                    intent.putExtra("id", list.get(chosenPos).getItemId());
                    startActivityForResult(intent, TO_PUBLISH);
                    return;
                }
            }
            Intent intent = new Intent(mContext, PhotoPublishNewActivity.class);
            intent.putExtra("selectPath", mSelectPath);
            intent.putExtra("title", list.get(chosenPos).getName());
            intent.putExtra("applyId", applyId);
            intent.putExtra("classifyId", list.get(chosenPos).getId());
            intent.putExtra("id", list.get(chosenPos).getItemId());
            startActivityForResult(intent, TO_PUBLISH);
        } else if (requestCode == TO_PUBLISH && resultCode == RESULT_OK) {//发布后刷新页面
            asyncMediaItem();
        }
    }*/
}
