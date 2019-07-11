package com.park61.teacherhelper.module.workplan;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aliyun.vodplayerview.utils.ImageLoader;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 专业版行事历定制页面
 *
 * @author shubei
 * @time 2018/12/25 17:10
 */
public class SpecialWpSubsActivity extends BaseActivity {

    //private ShowImageWebView wv_content;
    private ImageView img_whole;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_special_wpsubs);
    }

    @Override
    public void initView() {
        setPagTitle("个性化定制");
        //wv_content = findViewById(R.id.wv_content);
        img_whole = findViewById(R.id.img_whole);
    }

    @Override
    public void initData() {
        asyncDetail();
    }

    @Override
    public void initListener() {
        findViewById(R.id.bottom_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncApplyTaskCalendar();
            }
        });
    }

    /**
     * 请求行事历模板详情
     */
    private void asyncDetail() {
        String url = AppUrl.host + AppUrl.getValueByType;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "task_calendar_intro_url");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //ViewInitTool.initShowimgWebview(wv_content);
            //ViewInitTool.setWebData(wv_content, jsonResult.optString("data"));
            String picUrl = jsonResult.optString("data");

            //ImageManager.getInstance().displayImg(img_whole, picUrl);

            //获取图片真正的宽高
            Glide.with(mContext).asBitmap().load(picUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    //Bitmap afterReduceBmp = ImageManager.getInstance().reduceBitmap(bitmap);

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
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    dismissDialog();
                    ImageManager.getInstance().displayImg(img_whole, picUrl);
                }

                @Override
                public void onDestroy() {
                    super.onDestroy();
                    logout("=================onDestroy===============");
                }

                @Override
                public void onStop() {
                    super.onStop();
                    logout("=================onStop===============");
                }
            });
        }
    };

    /**
     * 申请行事历
     */
    private void asyncApplyTaskCalendar() {
        String url = AppUrl.host + AppUrl.applyTaskCalendar;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "task_calendar_intro_url");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {
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
            showShortToast("申请成功");
            finish();
        }
    };
}
