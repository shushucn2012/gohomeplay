package com.park61.teacherhelper.widget.pw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SharePopWin extends BaseActivity implements OnClickListener {

    public static final int IMAGE_SIZE = 32768;//微信分享图片大小限制
    public Tencent mTencent;
    public IWXAPI wxApi;

    private View img_close;
    private View view_wx_friend, view_qq_friend, view_wx_friends, view_qq_space, view_copy;
    private View area_outside;

    private String shareUrl;
    private String picUrl;
    private Bitmap weixinBmp;
    private String shareTitle;
    private String description;

    @Override
    public void setLayout() {
        setContentView(R.layout.popwin_share);
    }

    @Override
    public void initView() {
        img_close = findViewById(R.id.img_close);
        view_wx_friend = findViewById(R.id.view_wx_friend);
        view_wx_friends = findViewById(R.id.view_wx_friends);
        view_qq_friend = findViewById(R.id.view_qq_friend);
        view_qq_space = findViewById(R.id.view_qq_space);
        view_copy = findViewById(R.id.view_copy);
        area_outside = findViewById(R.id.area_outside);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void initData() {
        shareUrl = getIntent().getStringExtra("shareUrl");
        picUrl = getIntent().getStringExtra("picUrl");
        logout("==================picUrl==========================" + picUrl);
        if (TextUtils.isEmpty(picUrl)) {//如果为空给默认图片就是应用图标
            picUrl = AppUrl.SHARE_APP_ICON;
        }
        shareTitle = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(shareTitle)) {//如果为空给默认应用名称
            shareTitle = "61学院";
        }
        description = getIntent().getStringExtra("description");
        if (TextUtils.isEmpty(description)) {//如果为空给默认
            description = "更多精彩，尽在61学院！";
        }
        ImageLoader.getInstance().loadImage(picUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                weixinBmp = loadedImage;
            }
        });
    }

    @Override
    public void initListener() {
        img_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_wx_friends.setOnClickListener(this);
        view_wx_friend.setOnClickListener(this);
        view_qq_friend.setOnClickListener(this);
        view_qq_space.setOnClickListener(this);
        view_copy.setOnClickListener(this);
        area_outside.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_wx_friends:
                shareToWeiXin(1);
                break;
            case R.id.view_wx_friend:
                shareToWeiXin(0);
                break;
            case R.id.view_qq_friend:
                shareToQQ();
                break;
            case R.id.view_qq_space:
                shareToQzone();
                break;
            case R.id.view_copy:
                ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", shareUrl);
                cbm.setPrimaryClip(myClip);
                Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void shareToWeiXin(int way) {
        if (wxApi == null) {
            // 实例化
            wxApi = WXAPIFactory.createWXAPI(this, GlobalParam.WX_APP_ID);
            wxApi.registerApp(GlobalParam.WX_APP_ID);
        }
        if (!wxApi.isWXAppInstalled()) {
            showShortToast("您还没有安装微信，请下载安装后再试！");
            return;
        }
        wechatShare(way);// 0分享到微信好友;1分享到微信朋友圈
    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = description;
        msg.thumbData = bitmap2Bytes(weixinBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    public void shareToQQ() {
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID, mContext);
        Bundle bundle = new Bundle();
        // 这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        // 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        // 分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
        // 分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareTitle);
        // 标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, getString(R.string.app_name) + GlobalParam.TENCENT_APP_ID);
        mTencent.shareToQQ(this, bundle, new BaseUiListener());
    }

    public void shareToQzone() {
        if (mTencent == null)
            mTencent = Tencent.createInstance(GlobalParam.TENCENT_APP_ID, mContext);
        Bundle params = new Bundle();
        // 分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);// 必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, description);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);// 必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(picUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone(this, params, new BaseUiListener());
    }

    public class BaseUiListener implements IUiListener {

        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object arg0) {
        }

        @Override
        public void onError(UiError arg0) {
        }
    }

    public byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_img);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > IMAGE_SIZE && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        Log.out("===============output.toByteArray().length===============" + output.toByteArray().length);
        return output.toByteArray();
    }

}
