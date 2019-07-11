package com.park61.teacherhelper.module.clazz;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.AliyunUploadUtils;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 照片发布页
 * Created by shubei on 2017/8/24.
 */

public class PhotoPublishNewActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> mSelectPath;
    private ArrayList<String> mSelectPathAdd = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> gotList = new ArrayList<String>();//传过来的url列表

    private int applyId;//申请id
    private int classifyId;//分类项id
    private int id;//申请分类id
    private String title;//页面标题

    private ImageView img_add_pic;
    private GridViewForScrollView gv_input_pic;// 图片展示gridview
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private EditText edit_info;
    private TextView tv_right;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_photo_publish2);
    }

    @Override
    public void initView() {
        img_add_pic = (ImageView) findViewById(R.id.img_add_pic);
        gv_input_pic = (GridViewForScrollView) findViewById(R.id.gv_input_pic);
        edit_info = (EditText) findViewById(R.id.edit_info);


        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("确认");
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

        mSelectPath = getIntent().getStringArrayListExtra("selectPath");
        img_add_pic.setVisibility(View.GONE);
        gv_input_pic.setVisibility(View.VISIBLE);
        mSelectPathAdd.clear();
        mSelectPathAdd.addAll(mSelectPath);
        mSelectPathAdd.add("+");
        adapter = new InputPicAdapter();
        gv_input_pic.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(this);
        img_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToMultiImageSelector();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToMultiImageSelector();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToMultiImageSelector();
                    } else {//没有全部授权过，申请
                        permissionHelper.applyPermission();
                    }
                }
            }
        });
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_info.getText().toString().trim())) {
                    showShortToast("请输入相关介绍");
                    return;
                }
                if (mSelectPath == null || mSelectPath.size() <= 0) {
                    showShortToast("请选择需要发布的照片！");
                    return;
                }
                boolean isAllUrl = true;//是否全部为编辑时传过来的url
                for (String imgPath : mSelectPath) {
                    if (!imgPath.startsWith("http:"))
                        isAllUrl = false;
                }
                if (isAllUrl) {//如果全部是编辑的url直接提交
                    urlList.addAll(mSelectPath);
                    asyncIssuePhoto();
                } else {//有新选择的文件，异步压缩再上传
                    new CompressNUploadTask().execute();
                }
            }
        });
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edit_info.getText().toString().trim())
                        || !(mSelectPath == null || mSelectPath.size() <= 0)) {
                    dDialog.showDialog("提示", "退出此次编辑？", "取消", "退出", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard();
                            finish();
                        }
                    });
                } else {
                    hideKeyboard();
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(edit_info.getText().toString().trim())
                || !(mSelectPath == null || mSelectPath.size() <= 0)) {
            dDialog.showDialog("提示", "退出此次编辑？", "取消", "退出", null, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    /**
     * 去相册
     */
    public void goToMultiImageSelector() {
        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(true);
        selector.count(9);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(this, REQUEST_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                img_add_pic.setVisibility(View.GONE);
                gv_input_pic.setVisibility(View.VISIBLE);
                mSelectPathAdd.clear();
                mSelectPathAdd.addAll(mSelectPath);
                mSelectPathAdd.add("+");
                adapter = new InputPicAdapter();
                gv_input_pic.setAdapter(adapter);
            }
        } else if (resultCode == RESULT_OK && requestCode == 11) {

        }
    }

    private class InputPicAdapter extends BaseAdapter {

        private static final int TYPE_COM = 0;
        private static final int TYPE_ADD = 1;

        @Override
        public int getCount() {
            return mSelectPathAdd.size();
        }

        @Override
        public String getItem(int position) {
            return mSelectPathAdd.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mSelectPathAdd.size() - 1) {
                return TYPE_ADD;
            }
            return TYPE_COM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_COM:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_inputpic_item, null);
                    ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
                    ImageManager.getInstance().displayImg(img_input, (mSelectPathAdd.get(position).startsWith("http:") ? "" : "file:///") + mSelectPathAdd.get(position));
                    ImageView img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
                    img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSelectPath.remove(position);
                            mSelectPathAdd.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    break;
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_add_item, null);
                    View area_add_pic = convertView.findViewById(R.id.area_add_pic);
                    area_add_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!CommonMethod.isListEmpty(mSelectPath)) {//已经选择了图片，直接进入图片选择器
                                MultiImageSelector selector = MultiImageSelector.create();
                                selector.showCamera(true);
                                selector.count(9);
                                selector.multi();
                                selector.origin(mSelectPath);
                                selector.start(PhotoPublishNewActivity.this, REQUEST_IMAGE);
                            } else {//未选择，或者是删除所有以后，点击进入混合选择器
                                Intent intent = new Intent(mContext, ChoosePublishTypeActivity.class);
                                //intent.putExtra("selectPath", mSelectPath); 不需要传已选，因为到混合选择器的必然没有已选
                                intent.putExtra("title", title);
                                intent.putExtra("applyId", applyId);
                                intent.putExtra("classifyId", classifyId);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        }
                    });
                    break;
            }
            if (position == 9) {
                convertView.setVisibility(View.GONE);
            } else {
                convertView.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    /**
     * 压缩再上传
     */
    private class CompressNUploadTask extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // 准备将上传的已压缩图片的文件路径
            ArrayList<String> resizedPathList = new ArrayList<String>();
            gotList.clear();
            for (String wholePath : mSelectPath) {
                if (wholePath.startsWith("http:")) {
                    gotList.add(wholePath);
                    continue;
                }
                File temp = new File(Environment.getExternalStorageDirectory().getPath() + "/GHPCacheFolder/Format");// 自已缓存文件夹
                if (!temp.exists()) {
                    temp.mkdirs();
                }
                String filePath = temp.getAbsolutePath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                File tempFile = new File(filePath);
                // 图像保存到文件中
                try {
                    ImageManager.compressBmpToFile(tempFile, wholePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    showShortToast("图片压缩失败！");
                    finish();
                }
                // 将压缩后的地址放入集合
                resizedPathList.add(filePath);
            }
            return resizedPathList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resizedPathList) {
            dismissDialog();
            new AliyunUploadUtils(PhotoPublishNewActivity.this).uploadPicList(resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

                @Override
                public void onError(String path) {
                    showShortToast("上传失败！");
                }

                @Override
                public void onSuccess(List<String> urllist) {
                    urlList.clear();
                    if (!CommonMethod.isListEmpty(gotList))
                        urlList.addAll(gotList);
                    urlList.addAll(urllist);
                    asyncIssuePhoto();
                }
            });
        }
    }

    /**
     * 发布照片
     */
    private void asyncIssuePhoto() {
        String wholeUrl = AppUrl.host + AppUrl.addApplyItem;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("classifyId", classifyId);
        if (id > 0) {//id有就传，第一次没有，修改时有
            map.put("id", id);
        }
        map.put("detail", edit_info.getText().toString().trim());
        if (!CommonMethod.isListEmpty(urlList)) {
            StringBuilder sb = new StringBuilder();
            for (String p : urlList) {
                sb.append(p);
                sb.append(",");
            }
            map.put("picList", sb.substring(0, sb.length() - 1).toString());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
}
