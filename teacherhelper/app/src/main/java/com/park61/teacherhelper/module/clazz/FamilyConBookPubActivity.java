package com.park61.teacherhelper.module.clazz;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.AliyunUploadUtils;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.park61.teacherhelper.module.clazz.bean.FamilyClazzComtItem;
import com.park61.teacherhelper.module.workplan.WorkPlanTaskChooseManActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 家园联系薄发布评语页
 *
 * @author shubei
 * @time 2018/11/22 12:23
 */
public class FamilyConBookPubActivity extends BaseActivity {

    private static final int REQ_CHOOSE_STUDENT = 1000;
    private static final int REQ_GET_TEMP = 1001;

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> mSelectPathAdd = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> gotList = new ArrayList<String>();//传过来的url列表

    private int id;//评语id，编辑时传过来
    private String teachClassId;//班级id
    private String userChildIds = "";
    private boolean canSaveTmp = true;//是否可以保存模板，保存成功后不能再次保存

    private GridViewForScrollView gv_input_pic;// 图片展示gridview
    private InputPicAdapter adapter;// 图片展示gridview适配器
    private EditText edit_info;
    private TextView tv_right, tv_chosen_num;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_familyconbook_pub);
    }

    @Override
    public void initView() {
        gv_input_pic = (GridViewForScrollView) findViewById(R.id.gv_input_pic);
        edit_info = (EditText) findViewById(R.id.edit_info);

        setPagTitle("选择评语");

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("发布");
        tv_chosen_num = (TextView) findViewById(R.id.tv_chosen_num);
    }

    @Override
    public void initData() {

        id = getIntent().getIntExtra("id", -1);
        teachClassId = getIntent().getStringExtra("teachClassId");

        if (id > 0) {
            asyncGetDetail();
        } else {
            mSelectPathAdd.clear();
            mSelectPathAdd.addAll(mSelectPath);
            if (mSelectPath.size() < 9) {
                mSelectPathAdd.add("+");
            }
            adapter = new InputPicAdapter();
            gv_input_pic.setAdapter(adapter);
        }
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(this);
        /*img_add_pic.setOnClickListener(new View.OnClickListener() {
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
        });*/
        findViewById(R.id.area_choose_student).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FamilyConBookChoseStuActivity.class);
                intent.putExtra("userChildIds", userChildIds);
                intent.putExtra("teachClassId", teachClassId);
                startActivityForResult(intent, REQ_CHOOSE_STUDENT);
            }
        });
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_info.getText().toString().trim())) {
                    showShortToast("请输入评语内容！");
                    return;
                }
                /*if (mSelectPath == null || mSelectPath.size() <= 0) {
                    showShortToast("请选择需要发布的照片！");
                    return;
                }*/
                if (TextUtils.isEmpty(userChildIds)) {
                    showShortToast("请选择学生！");
                    return;
                }
                if (CommonMethod.isListEmpty(mSelectPath)) {//未选中照片也可以发布
                    asyncIssueComment();
                    return;
                }

                boolean isAllUrl = true;//是否全部为编辑时传过来的url
                for (String imgPath : mSelectPath) {
                    if (!imgPath.startsWith("http:"))
                        isAllUrl = false;
                }
                if (isAllUrl) {//如果全部是编辑的url直接提交
                    urlList.addAll(mSelectPath);
                    asyncIssueComment();
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
                    dDialog.showDialog("提示", "返回后内容无法保存，是否确认返回？", "取消", "确定", null, new View.OnClickListener() {
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
        findViewById(R.id.cardview_chose_temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FamilyConBookComtTempsActivity.class);
                startActivityForResult(it, REQ_GET_TEMP);
            }
        });
        findViewById(R.id.cardview_save_temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSaveTmp) {
                    if (TextUtils.isEmpty(edit_info.getText().toString().trim())) {
                        showShortToast("请输入评语内容！");
                        return;
                    }
                    asyncSaveUserCommentTemplate();
                }
            }
        });
    }

    /**
     * 编辑时获取详情
     */
    private void asyncGetDetail() {
        String wholeUrl = AppUrl.host + AppUrl.teachCommentDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
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
            FamilyClazzComtItem familyClazzComtItem = gson.fromJson(jsonResult.toString(), FamilyClazzComtItem.class);
            //设置内容
            edit_info.setText(familyClazzComtItem.getContent());
            //设置所选学生
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < familyClazzComtItem.getTeachCommentChildList().size(); i++) {
                sb.append(familyClazzComtItem.getTeachCommentChildList().get(i).getUserChildId());
                sb.append(",");
            }
            if (!TextUtils.isEmpty(sb)) {
                userChildIds = sb.substring(0, sb.length() - 1);
            }
            tv_chosen_num.setText(familyClazzComtItem.getTeachCommentChildList().size() + "人");
            //设置所选图片
            for (int i = 0; i < familyClazzComtItem.getTeachCommentSourceList().size(); i++) {
                mSelectPath.add(familyClazzComtItem.getTeachCommentSourceList().get(i).getSource());
            }
            mSelectPathAdd.clear();
            mSelectPathAdd.addAll(mSelectPath);
            if (mSelectPath.size() < 9) {
                mSelectPathAdd.add("+");
            }
            adapter = new InputPicAdapter();
            gv_input_pic.setAdapter(adapter);
        }
    };

    /**
     * 保存评语模板
     */
    private void asyncSaveUserCommentTemplate() {
        String wholeUrl = AppUrl.host + AppUrl.saveUserCommentTemplate;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", edit_info.getText().toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dLsner);
    }

    BaseRequestListener dLsner = new JsonRequestListener() {

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
            showShortToast("保存成功！");
            ((TextView) findViewById(R.id.tv_save_temp)).setTextColor(getResources().getColor(R.color.com_orange));
            canSaveTmp = false;
        }
    };

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(edit_info.getText().toString().trim())
                || !(mSelectPath == null || mSelectPath.size() <= 0)) {
            dDialog.showDialog("提示", "返回后内容无法保存，是否确认返回？", "取消", "确定", null, new View.OnClickListener() {
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
                mSelectPathAdd.clear();
                mSelectPathAdd.addAll(mSelectPath);
                if (mSelectPath.size() < 9) {
                    mSelectPathAdd.add("+");
                }
                adapter = new InputPicAdapter();
                gv_input_pic.setAdapter(adapter);
            }
        } else if (requestCode == REQ_CHOOSE_STUDENT) {
            if (resultCode == RESULT_OK) {
                userChildIds = data.getStringExtra("userChildIds");
                String[] strings = userChildIds.split(",");
                if (strings != null)
                    tv_chosen_num.setText(strings.length + "人");
                else
                    tv_chosen_num.setText("");
            }
        } else if (requestCode == REQ_GET_TEMP) {
            if (resultCode == RESULT_OK) {
                String backStr = data.getStringExtra("backStr");
                String oldStr = edit_info.getText().toString();
                String wholeStr = oldStr + backStr;
                logout("===================oldStr=================" + oldStr);
                logout("===================backStr=================" + backStr);
                logout("===================wholeStr=================" + wholeStr);
                edit_info.setText(wholeStr);
                if (wholeStr.length() < 500)
                    edit_info.setSelection(wholeStr.length());
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
            if ("+".equals(mSelectPathAdd.get(position))) {
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_family_pub_item, null);
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_family_pub_add_item, null);
                    View area_add_pic = convertView.findViewById(R.id.area_add_pic);
                    area_add_pic.setOnClickListener(new View.OnClickListener() {
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
            new AliyunUploadUtils(FamilyConBookPubActivity.this).uploadPicList(resizedPathList, new AliyunUploadUtils.OnUploadListFinish() {

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
                    asyncIssueComment();
                }
            });
        }
    }

    /**
     * 发布评语
     */
    private void asyncIssueComment() {
        String wholeUrl = AppUrl.host + AppUrl.issueComment;
        Map<String, Object> map = new HashMap<String, Object>();
        if (id > 0) {//id有就传，第一次没有，修改时有
            map.put("id", id);
        }
        map.put("teachClassId", teachClassId);
        map.put("content", edit_info.getText().toString().trim().replace("\n", "<br/>"));
        if (!CommonMethod.isListEmpty(urlList)) {
            StringBuilder sb = new StringBuilder();
            for (String p : urlList) {
                sb.append(p);
                sb.append(",");
            }
            map.put("picList", sb.substring(0, sb.length() - 1).toString());
        }
        map.put("userChildIds", userChildIds);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cLsner);
    }

    BaseRequestListener cLsner = new JsonRequestListener() {

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
            showShortToast("发布成功！");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new MessageEvent("REFRESH_FAMILYCONBOOK_MAIN"));
                }
            }, 1000);
            finish();
        }
    };
}
