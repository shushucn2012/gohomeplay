package com.park61.teacherhelper.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.module.activity.ActivityList;
import com.park61.teacherhelper.module.activity.ActivityWebViewActivity;
import com.park61.teacherhelper.module.activity.ChristmasActMainActivity;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.course.CourseSearchResultActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.widget.pw.SharePopWin;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 处理js回调传回的json数据
 * Created by shubei on 2018/9/4.
 */
public class ComJsInterface {

    private Activity mActivity;
    private ShareInfoBean mShareInfoBean;

    public ComJsInterface(Activity activity, ShareInfoBean shareInfoBean) {
        this.mActivity = activity;
        this.mShareInfoBean = shareInfoBean;
    }


    // 在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
    @JavascriptInterface
    public void call(String jsonStr) {
        Log.out("jsonStr====" + jsonStr);
        //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject data = jsonObject.optJSONObject("data");
        String forwardtype = data.optString("forwardtype");
        Log.out("forwardtype======" + forwardtype);
        if ("getToken".equals(forwardtype)) {
            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        } else if ("setShare".equals(forwardtype)) {
            Intent it = new Intent(mActivity, SharePopWin.class);
            it.putExtra("shareUrl", mShareInfoBean.getShareUrl());
            it.putExtra("picUrl", mShareInfoBean.getPicUrl());
            it.putExtra("title", mShareInfoBean.getTitle());
            it.putExtra("description", mShareInfoBean.getDescription());
            mActivity.startActivity(it);
        } else if ("back".equals(forwardtype)) {
            mActivity.finish();
        } else if ("search".equals(forwardtype)) {
            JSONObject param = data.optJSONObject("params");
            String id = param.optString("id");
            String level = param.optString("level");
            Intent intent = new Intent(mActivity, CourseSearchResultActivity.class);
            intent.putExtra("id", FU.paseInt(id));
            intent.putExtra("level", FU.paseInt(level));
            mActivity.startActivity(intent);
        } else if ("content_detail".equals(forwardtype)) {
            JSONObject param = data.optJSONObject("params");
            String content_id = param.optString("content_id");
            Intent intent = new Intent(mActivity, CourseDetailsActivity.class);
            intent.putExtra("coursePlanId", FU.paseInt(content_id));
            mActivity.startActivity(intent);
        } else if ("cms".equals(forwardtype)) {
            JSONObject param = data.optJSONObject("params");
            String cms_id = param.optString("cms_id");
            Intent intent = new Intent(mActivity, ActivityList.class);
            intent.putExtra("pageId", FU.paseInt(cms_id));
            mActivity.startActivity(intent);
        } else if ("activity".equals(forwardtype)) {
            JSONObject param = data.optJSONObject("params");
            String activityId = param.optString("activityId");
            Intent intent = new Intent(mActivity, ActivityWebViewActivity.class);
            intent.putExtra("activityId", FU.paseInt(activityId));
            mActivity.startActivity(intent);
        } else if ("typenine".equals(forwardtype)) {
            JSONObject param = data.optJSONObject("params");
            String linkData = param.optString("ninedata");
            try {
                JSONObject jsonData = new JSONObject(linkData);
                int id = jsonData.optInt("id");
                int type = jsonData.optInt("type");
                switch (type) {
                    case 1://峰会活动
                        Intent intent = new Intent(mActivity, ActivityWebViewActivity.class);
                        intent.putExtra("activityId", id);
                        mActivity.startActivity(intent);
                        break;
                    case 2://培训活动
                        Intent itt = new Intent(mActivity, ActivityList.class);
                        itt.putExtra("pageId", id);
                        mActivity.startActivity(itt);
                        break;
                    case 3://点赞活动（圣诞）
                        Intent it = new Intent(mActivity, ChristmasActMainActivity.class);
                        it.putExtra("activityId", id);
                        mActivity.startActivity(it);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ("appUrl".equals(forwardtype)) {//app内部跳转
            JSONObject param = data.optJSONObject("params");
            String linkUrl = param.optString("linkUrl");
            try {
                Uri mUri = Uri.parse(linkUrl);
                ViewInitTool.appUrlGo(mUri, mActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
