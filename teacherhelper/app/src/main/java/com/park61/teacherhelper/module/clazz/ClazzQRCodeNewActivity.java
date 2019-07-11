package com.park61.teacherhelper.module.clazz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.QRCodeCreator;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.clazz.bean.ClazzQRCodeBean;
import com.park61.teacherhelper.module.clazz.bean.StudentBean;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.wxapi.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.FilesManager.DeleteFile;

/**
 * 班级二维码
 *
 * @author shubei
 * @time 2018/11/27 10:54
 */
public class ClazzQRCodeNewActivity extends BaseActivity {

    private static final int THUMB_SIZE = 100;

    private ImageView img_whole, img_qrcode, img_user_header;
    private TextView tv_username, tv_group_name, tv_class_name;
    private View area_share_whole;

    private String teachClassId;//班级id
    private String haiBaoPath;//海报保存本地路径
    private ClazzQRCodeBean mClazzQRCodeBean;//获取的二维码页面信息

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_qrcode_new);
    }

    @Override
    public void initView() {
        img_whole = findViewById(R.id.img_whole);
        img_qrcode = findViewById(R.id.img_qrcode);
        img_user_header = findViewById(R.id.img_user_header);
        tv_username = findViewById(R.id.tv_username);
        tv_group_name = findViewById(R.id.tv_group_name);
        tv_class_name = findViewById(R.id.tv_class_name);
        area_share_whole = findViewById(R.id.area_share_whole);
    }

    @Override
    public void initData() {
        teachClassId = getIntent().getStringExtra("teachClassId");
        asyncGetQRCodeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void initListener() {
        findViewById(R.id.view_wx_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap shareBmp = getBitmapByView(area_share_whole);
                getAndSaveBitmapByView(area_share_whole);
                Log.out("========================shareBmp===========================" + haiBaoPath);
                shareToWx(0);
            }
        });
        findViewById(R.id.view_wx_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap shareBmp = getBitmapByView(area_share_whole);
                getAndSaveBitmapByView(area_share_whole);
                Log.out("========================shareBmp===========================" + haiBaoPath);
                shareToWx(1);
            }
        });
        findViewById(R.id.area_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 请求数据
     */
    private void asyncGetQRCodeInfo() {
        String wholeUrl = AppUrl.host + AppUrl.shareMiniProgramCreateQRCodeInfo;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teachClassId", teachClassId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog("二维码海报生成中...");
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast("二维码海报生成失败");
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mClazzQRCodeBean = gson.fromJson(jsonResult.toString(), ClazzQRCodeBean.class);
            //获取图片真正的宽高
            Glide.with(mContext).asBitmap().load(mClazzQRCodeBean.getBackgroundUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    Log.out("width ===============" + width); //900px
                    Log.out("height ================" + height); //500px

                    if (height > width) {
                        ViewGroup.LayoutParams layoutParams = img_whole.getLayoutParams();
                        layoutParams.width = DevAttr.getScreenWidth(mContext);
                        double percent = height * 1.0 / width;
                        layoutParams.height = (int) (layoutParams.width * percent);
                    }
                    img_whole.setImageBitmap(bitmap);

                    dismissDialog();
                    findViewById(R.id.area_cover).setVisibility(View.GONE);
                }
            });
            //ImageManager.getInstance().displayScaleImage(mContext, img_qrcode, mClazzQRCodeBean.getQrCodeUrl(), null);
            Glide.with(mContext).asBitmap().load(mClazzQRCodeBean.getQrCodeUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    img_qrcode.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    showShortToast("二维码海报生成失败");
                    finish();
                }
            });


            ImageManager.getInstance().displayScaleImage(mContext, img_user_header, mClazzQRCodeBean.getUserHeadPic(), null);
            tv_username.setText(mClazzQRCodeBean.getUserName());
            tv_group_name.setText(mClazzQRCodeBean.getTeachGroupName());
            tv_class_name.setText(mClazzQRCodeBean.getTeachClassName());
        }
    };

    public void getAndSaveBitmapByView(View view) {
        Bitmap shareBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(shareBitmap);
        view.draw(c);
        haiBaoPath = ImageManager.saveBitmapToSD(shareBitmap);
    }

    /**
     * 分享到微信
     */
    public void shareToWx(int flag) {
        // 实例化
        IWXAPI wxApi = WXAPIFactory.createWXAPI(mContext, GlobalParam.WX_APP_ID);
        wxApi.registerApp(GlobalParam.WX_APP_ID);
        if (!wxApi.isWXAppInstalled()) {
            showShortToast("您还没有安装微信，请下载安装后再试！");
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(haiBaoPath);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bitmap = BitmapFactory.decodeFile(haiBaoPath);

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();

        msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(haiBaoPath)) {//销毁创建的封面图片，避免占用用户空间
            DeleteFile(new File(haiBaoPath));
        }
    }

}

