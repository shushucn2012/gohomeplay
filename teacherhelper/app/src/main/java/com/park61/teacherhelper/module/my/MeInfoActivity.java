package com.park61.teacherhelper.module.my;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.AliyunUploadUtils;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.PermissionHelper;
import com.park61.teacherhelper.module.login.bean.UserBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;
import com.park61.teacherhelper.widget.dialog.ComSelectDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MeInfoActivity extends BaseActivity implements OnClickListener {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQ_GET_PIC = 0;
    private static final int REQUEST_IMAGE = 2;
    private static final int REQ_CHOOSE_GROUP = 3;//去选择幼儿园

    private CircleImageView img_me_pic;
    private EditText et_name_value;
    private ImageView img_ckb1, img_ckb2;

    private View xueli_area, biye_area, teach_age_area, me_pic_area, phone_area, area_group, group_name_area, group_addr_area;
    private TextView tv_xueli_value, tv_teach_age_value, tv_phone_value, tv_group_name_value, tv_groupp_addr_value, tv_groupp_office_value;

    private String name, phoneNum, education, workLife;
    private int sex, educationIndex, workLifeIndex;
    private List<String> elist = new ArrayList<String>();
    private List<String> wlist = new ArrayList<String>();
    private ArrayList<String> mSelectPath;

    {
        elist.add("专科");
        elist.add("本科");
        elist.add("硕士研究生");
        elist.add("博士研究生");
        elist.add("其它");

        wlist.add("1年以下");
        wlist.add("1-3年");
        wlist.add("3-5年");
        wlist.add("5-10年");
        wlist.add("10年以上");
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_meinfo);
    }

    @Override
    public void initView() {
        area_right = findViewById(R.id.area_right);

        xueli_area = findViewById(R.id.xueli_area);
        teach_age_area = findViewById(R.id.teach_age_area);

        img_me_pic = (CircleImageView) findViewById(R.id.img_me_pic);
        img_ckb1 = (ImageView) findViewById(R.id.img_ckb1);
        img_ckb2 = (ImageView) findViewById(R.id.img_ckb2);
        et_name_value = (EditText) findViewById(R.id.et_name_value);
        tv_xueli_value = (TextView) findViewById(R.id.tv_xueli_value);
        tv_teach_age_value = (TextView) findViewById(R.id.tv_teach_age_value);
        teach_age_area = findViewById(R.id.teach_age_area);
        me_pic_area = findViewById(R.id.me_pic_area);
        area_group = findViewById(R.id.area_group);
        group_name_area = findViewById(R.id.group_name_area);
        group_addr_area = findViewById(R.id.group_addr_area);
        tv_group_name_value = (TextView) findViewById(R.id.tv_group_name_value);
        tv_groupp_addr_value = (TextView) findViewById(R.id.tv_groupp_addr_value);
        tv_groupp_office_value = (TextView) findViewById(R.id.tv_groupp_office_value);
    }

    @Override
    public void initData() {
        asyncGetUserInfo();
    }

    private void asyncGetUserInfo() {
        String wholeUrl = AppUrl.host + AppUrl.myInfor;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, mlistener);
    }

    BaseRequestListener mlistener = new JsonRequestListener() {

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
            UserBean userBean = gson.fromJson(jsonResult.toString(), UserBean.class);
            if (!TextUtils.isEmpty(userBean.getPictureUrl())) {
                ImageManager.getInstance().displayImg(img_me_pic, userBean.getPictureUrl(), R.mipmap.headimg_default_img);
            }
            if (!TextUtils.isEmpty(userBean.getName())) {
                et_name_value.setText(userBean.getName());
            }
            /*if (!TextUtils.isEmpty(userBean.getPetName())) {
                et_nickname_value.setText(userBean.getPetName());
            }*/
            sex = userBean.getSex();
            if (userBean.getSex() == 1) {//性别 1:男 2:女
                img_ckb1.setImageResource(R.mipmap.icon_sexy_man_chosen);
            } else {
                img_ckb2.setImageResource(R.mipmap.icon_sexy_woman_chosen);
            }
            /*if (!TextUtils.isEmpty(userBean.getMobile())) {
                tv_phone_value.setText(userBean.getMobile());
            }*/
            if (!TextUtils.isEmpty(userBean.getEducation())) {
                if (FU.paseInt(userBean.getEducation()) == 0 && !TextUtils.isEmpty(userBean.getEducation())) {

                } else {
                    if (elist.size() >= FU.paseInt(userBean.getEducation())) {
                        educationIndex = FU.paseInt(userBean.getEducation());
                        tv_xueli_value.setText(elist.get(FU.paseInt(userBean.getEducation()) - 1));
                        tv_xueli_value.setTextColor(getResources().getColor(R.color.g333333));
                    }
                }
            }
            if (!TextUtils.isEmpty(userBean.getWorkLife())) {
                if (FU.paseInt(userBean.getWorkLife()) == 0 && !TextUtils.isEmpty(userBean.getWorkLife())) {

                } else {
                    if (wlist.size() >= FU.paseInt(userBean.getWorkLife())) {
                        workLifeIndex = FU.paseInt(userBean.getWorkLife());
                        tv_teach_age_value.setText(wlist.get(FU.paseInt(userBean.getWorkLife()) - 1));
                        tv_teach_age_value.setTextColor(getResources().getColor(R.color.g333333));
                    }
                }
            }

            tv_group_name_value.setText(userBean.getSchoolName());
            tv_groupp_addr_value.setText(userBean.getSchoolAddress());
            //tv_group_manager_value.setText(userBean.getIsAdmin() == 0 ? "否" : "是");

            /*if (userBean.getDuties() == 1) {
                tv_groupp_office_value.setText("园长");
            } else if (userBean.getDuties() == 2) {
                tv_groupp_office_value.setText("教师");
            }*/
            if (!TextUtils.isEmpty(userBean.getDutyName())) {
                tv_groupp_office_value.setText(userBean.getDutyName());
            }
        }
    };

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(MeInfoActivity.this);
        area_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name_value.getText().toString().trim();
                //petName = et_nickname_value.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    showShortToast("姓名不能为空！");
                    return;
                }
                /*if (TextUtils.isEmpty(petName)) {
                    showShortToast("昵称不能为空！");
                    return;
                }*/
                //phoneNum = et_phone_value.getText().toString().trim();
                education = tv_xueli_value.getText().toString().trim();
                workLife = tv_teach_age_value.getText().toString().trim();
                asyncUpdateUserInfo();
            }
        });
        img_ckb1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ckb1.setImageResource(R.mipmap.icon_sexy_man_chosen);
                img_ckb2.setImageResource(R.mipmap.icon_sexy_woman);
                sex = 1;
            }
        });
        img_ckb2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ckb1.setImageResource(R.mipmap.icon_sexy_man);
                img_ckb2.setImageResource(R.mipmap.icon_sexy_woman_chosen);
                sex = 2;
            }
        });
        xueli_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ComSelectDialog mComSelectDialog = new ComSelectDialog(mContext, elist);
                mComSelectDialog.setOnItemSelectLsner(new ComSelectDialog.OnItemSelect() {
                    @Override
                    public void onSelect(int position) {
                        educationIndex = position + 1;
                        tv_xueli_value.setText(elist.get(position));
                        tv_xueli_value.setTextColor(getResources().getColor(R.color.g333333));
                    }
                });
                mComSelectDialog.showDialog();
            }
        });
        teach_age_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ComSelectDialog mComSelectDialog = new ComSelectDialog(mContext, wlist);
                mComSelectDialog.setOnItemSelectLsner(new ComSelectDialog.OnItemSelect() {
                    @Override
                    public void onSelect(int position) {
                        workLifeIndex = position + 1;
                        tv_teach_age_value.setText(wlist.get(position));
                        tv_teach_age_value.setTextColor(getResources().getColor(R.color.g333333));
                    }
                });
                mComSelectDialog.showDialog();
            }
        });
        me_pic_area.setOnClickListener(this);
        group_name_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ChooseGroupListActivity.class), REQ_CHOOSE_GROUP);
            }
        });
        group_addr_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ChooseGroupListActivity.class), REQ_CHOOSE_GROUP);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.me_pic_area) {// 头像
            permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                @Override
                public void OnAlterApplyPermission() {
                   /* Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                    startActivityForResult(it, REQ_GET_PIC);*/
                    goToMultiImageSelector();
                }
            });
            permissionHelper.setRequestPermission(permissionModels);
            if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                /*Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                startActivityForResult(it, REQ_GET_PIC);*/
                goToMultiImageSelector();
            } else {//6.0+ 需要动态申请
                //判断是否全部授权过
                if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                    /*Intent it = new Intent(mContext, SelectPicPopupWindow.class);
                    startActivityForResult(it, REQ_GET_PIC);*/
                    goToMultiImageSelector();
                } else {//没有全部授权过，申请
                    permissionHelper.applyPermission();
                }
            }
        }
    }

    /**
     * 去相册
     */
    public void goToMultiImageSelector() {
        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(true);
        selector.count(1);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(MeInfoActivity.this, REQUEST_IMAGE);
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
        if (resultCode != RESULT_OK || data == null)
            return;
        if (requestCode == REQ_GET_PIC) {
            String picPath = data.getStringExtra("path");
            logout("picPath======" + picPath);
            Bitmap bmp = ImageManager.getInstance().readFileBitMap(picPath);
            img_me_pic.setImageBitmap(bmp);
            new AliyunUploadUtils(MeInfoActivity.this).uploadPic(picPath,
                    new AliyunUploadUtils.OnUploadFinish() {

                        @Override
                        public void onSuccess(String picUrl) {
                            asyncUpdateUserHeader(picUrl);
                        }

                        @Override
                        public void onError() {
                            showShortToast("头像上传失败请重试！");
                        }
                    });
        } else if (requestCode == REQUEST_IMAGE) {
            mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

            // 根据Uri.fromFile(file)方法即可将path转为uri
            Uri sourceUri = Uri.fromFile(new File(mSelectPath.get(0)));
            // 创建裁剪照片之后保存的路径，也是先用path--->file--->Uri
            String saveDir = Environment.getExternalStorageDirectory() + "/crop";
            logout("=======saveDir==============" + saveDir);
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            Uri destinationUri = Uri.fromFile(new File(saveDir, "crop.jpg"));
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(400, 400)
                    .start(MeInfoActivity.this);
        } else if (requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(resultUri.getPath());
                img_me_pic.setImageBitmap(bitmap);
                new AliyunUploadUtils(MeInfoActivity.this).uploadPic(resultUri.getPath(),
                        new AliyunUploadUtils.OnUploadFinish() {

                            @Override
                            public void onSuccess(String picUrl) {
                                asyncUpdateUserHeader(picUrl);
                            }

                            @Override
                            public void onError() {
                                showShortToast("头像上传失败请重试！");
                            }
                        });
            } else {
                showShortToast("裁剪图片失败！");
            }
        } else if (requestCode == REQ_CHOOSE_GROUP) {//选择幼儿园返回后重刷数据
            asyncGetUserInfo();
        }
    }

    /**
     * 请求更新用户头像
     */
    private void asyncUpdateUserHeader(String picUrl) {
        String wholeUrl = AppUrl.host + AppUrl.updateMyInfor;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pictureUrl", picUrl);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, hlistener);
    }

    BaseRequestListener hlistener = new JsonRequestListener() {

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
            showShortToast("修改用户头像成功");
//            asyncGetUserInfo();
        }
    };

    /**
     * 请求更新信息除了头像
     */
    private void asyncUpdateUserInfo() {
        String wholeUrl = AppUrl.host + AppUrl.updateMyInfor;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        //map.put("petName", petName);
        map.put("sex", sex);
        map.put("education", educationIndex);
        map.put("workLife", workLifeIndex);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, ulistener);
    }

    BaseRequestListener ulistener = new JsonRequestListener() {

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
            showShortToast("修改用户信息成功");
            finish();
        }
    };

}
