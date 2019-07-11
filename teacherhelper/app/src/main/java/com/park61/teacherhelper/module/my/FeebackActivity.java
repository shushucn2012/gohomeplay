package com.park61.teacherhelper.module.my;

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
import android.widget.GridView;
import android.widget.ImageView;

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

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by shubei on 2017/8/22.
 */

public class FeebackActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> mSelectPath;
    private ArrayList<String> mSelectPathAdd = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private String content;
    private int suggestType;

    private ImageView img_add_pic, img_ckb1, img_ckb2, img_ckb3;
    private GridView gv_input_pic;// 图片展示gridview
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private View area_right;
    private EditText edit_input_word;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_feeback);
    }

    @Override
    public void initView() {
        img_add_pic = (ImageView) findViewById(R.id.img_add_pic);
        gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
        area_right = findViewById(R.id.area_right);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        img_ckb1 = (ImageView) findViewById(R.id.img_ckb1);
        img_ckb2 = (ImageView) findViewById(R.id.img_ckb2);
        img_ckb3 = (ImageView) findViewById(R.id.img_ckb3);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(FeebackActivity.this);
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
                content = edit_input_word.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showShortToast("请输入反馈内容");
                    return;
                }

                if (suggestType == 0) {
                    showShortToast("请选择反馈问题的类型");
                    return;
                }

                // 图片为空，文字不为空
                if (CommonMethod.isListEmpty(mSelectPath)) {
                    asyncAddEvaluate();
                    return;
                }
                // 图片不为空时,异步压缩再上传
                new CompressNUploadTask().execute();
            }
        });
        img_ckb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestType = 1;
                img_ckb1.setImageResource(R.mipmap.icon_xuanzhong_focus);
                img_ckb2.setImageResource(R.mipmap.icon_xuanzhong_default);
                img_ckb3.setImageResource(R.mipmap.icon_xuanzhong_default);
            }
        });
        img_ckb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestType = 2;
                img_ckb1.setImageResource(R.mipmap.icon_xuanzhong_default);
                img_ckb2.setImageResource(R.mipmap.icon_xuanzhong_focus);
                img_ckb3.setImageResource(R.mipmap.icon_xuanzhong_default);
            }
        });
        img_ckb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestType = 3;
                img_ckb1.setImageResource(R.mipmap.icon_xuanzhong_default);
                img_ckb2.setImageResource(R.mipmap.icon_xuanzhong_default);
                img_ckb3.setImageResource(R.mipmap.icon_xuanzhong_focus);
            }
        });
    }

    /**
     * 去相册
     */
    public void goToMultiImageSelector() {
        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(true);
        selector.count(4);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(FeebackActivity.this, REQUEST_IMAGE);
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
                    ImageManager.getInstance().displayImg(img_input, "file:///" + mSelectPathAdd.get(position));
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
                            MultiImageSelector selector = MultiImageSelector.create();
                            selector.showCamera(true);
                            selector.count(4);
                            selector.multi();
                            selector.origin(mSelectPath);
                            selector.start(FeebackActivity.this, REQUEST_IMAGE);
                        }
                    });
                    break;
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
            for (String wholePath : mSelectPath) {
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
            new AliyunUploadUtils(FeebackActivity.this).uploadPicList(resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

                @Override
                public void onError(String path) {
                    showShortToast("上传失败！");
                }

                @Override
                public void onSuccess(List<String> urllist) {
                    urlList.clear();
                    urlList.addAll(urllist);
                    asyncAddEvaluate();
                }
            });
        }
    }

    private void asyncAddEvaluate() {
        String wholeUrl = AppUrl.host + AppUrl.addFeedBack;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", suggestType);
        map.put("content", content);
        if (!CommonMethod.isListEmpty(urlList)) {
            StringBuilder sb = new StringBuilder();
            for (String p : urlList) {
                sb.append(p);
                sb.append(",");
            }
            map.put("imgList", sb.substring(0, sb.length() - 1).toString());
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
            showShortToast("反馈成功！");
            finish();
        }
    };
}
