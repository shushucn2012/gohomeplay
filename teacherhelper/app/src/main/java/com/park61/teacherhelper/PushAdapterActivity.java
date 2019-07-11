package com.park61.teacherhelper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.activity.Double11ActDetailsActivity;
import com.park61.teacherhelper.module.clazz.ParkApplyActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.ServiceApplyActivity;
import com.park61.teacherhelper.module.umeng.UmengResponse;
import com.park61.teacherhelper.module.workplan.WorkPlanMainActivity;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

import java.util.List;

/**
 * 推送消息分发处理界面
 * Created by zhangchi on 2017/9/18.
 */
public class PushAdapterActivity extends UmengNotifyClickActivity {

    private static String TAG = PushAdapterActivity.class.getName();
    private static final String WEB_VIEW_URL = "web_view_url";

    private Context mContext;

    //定义Handler对象
    private Handler handler = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            String webUrl = msg.obj.toString();
            if (isAppRunning(mContext, "com.park61.teacherhelper")) {//如果正在运行就直接打开页面
                Uri mUri = Uri.parse(webUrl);
                if ("http".equals(mUri.getScheme()) || "https".equals(mUri.getScheme())) {
                    Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                    intent.putExtra("url", webUrl);
                    startActivity(intent);
                } else if ("teach61".equals(mUri.getScheme())) {
                    if ("serviceRights".equals(mUri.getHost())) {//服务权益
                        startActivity(new Intent(mContext, ServiceApplyActivity.class));
                    } else if ("Double11ActDetail".equals(mUri.getHost())) {//双11活动详情
                        Intent it = new Intent(mContext, Double11ActDetailsActivity.class);
                        it.putExtra("activityId", FU.paseInt(mUri.getQueryParameter("activityId")));
                        startActivity(it);
                    } else if ("open61teach".equals(mUri.getHost())) {//课程
                        String coursePlanId = mUri.getQueryParameter("id");
                        Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                        intent.putExtra("coursePlanId", FU.paseInt(coursePlanId));
                        startActivity(intent);
                    } else if ("taskCalendar".equals(mUri.getHost())) {//行事历
                        startActivity(new Intent(mContext, WorkPlanMainActivity.class));
                    } else if ("parkApply".equals(mUri.getHost())) {//基地圆申请
                        startActivity(new Intent(mContext, ParkApplyActivity.class));
                    }
                }
            } else {//如果没有启动就启动后再打开
                Intent it = new Intent(mContext, LoadingPushActivity.class);
                it.putExtra("WEB_VIEW_URL", webUrl);
                startActivity(it);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
        mContext = this;
        Log.out("==============onCreateonCreateonCreateonCreate==============");
    }

    @Override
    public void onMessage(Intent intent) {
        if (intent == null)
            return;
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.out("==============onMessage====body==========" + body);
        //APP离线时收到消息的处理流程
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        UmengResponse umengResponse = gson.fromJson(body, new TypeToken<UmengResponse>() {
        }.getType());
        String url = umengResponse.getExtra().get(WEB_VIEW_URL);
        Message message = Message.obtain();
        message.obj = url;
        handler.sendMessage(message);
    }

    @Override
    protected void onResume() { //APP在线时收到消息的处理流程
        super.onResume();
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            for (String key : bun.keySet()) {
                if (WEB_VIEW_URL.equals(key)) {
                    final String url = bun.getString(key);
                    //APP离线时收到消息的处理流程
                    Message message = Message.obtain();
                    message.obj = url;
                    handler.sendMessage(message);
                    break;
                }
            }
        }
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            Log.out("==============info==============" + info.baseActivity.getClassName());
            Log.out("==============info==============" + info.baseActivity.getPackageName());
            if (info.baseActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getClassName().equals("com.park61.teacherhelper.MainTabActivity")) {
                return true;
            }
        }
        return false;
    }

}
