package com.park61.teacherhelper.module.my;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.databinding.ActivityCompleteInfoBinding;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by chenlie on 2018/5/8.
 *
 * 完善信息引导页面
 */

public class CompleteInfoActivity extends BaseActivity {

    ActivityCompleteInfoBinding binding;

    @Override
    public void setLayout() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_info);
    }

    @Override
    public void initView() {
        setPagTitle("");
    }

    @Override
    public void initData() {

        IntentFilter f = new IntentFilter();
        f.addAction("RECEIVE_SUCCESS");
        registerReceiver(myReceive, f);
    }

    BroadcastReceiver myReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void initListener() {

        //userDuty 1园长，2教师，3代理商，4其他
        binding.setGardenClick(v -> {
            Intent it = new Intent(mContext, KidGardenActivity.class);
            it.putExtra("userDuty", 1);
            startActivity(it);
        });
        binding.setTeacherClick(v -> {
            Intent it = new Intent(mContext, KidGardenActivity.class);
            it.putExtra("userDuty", 2);
            startActivity(it);
        });
        binding.setAgentClick(v -> {
            Intent it = new Intent(mContext, AgentInforActivity.class);
            it.putExtra("userDuty", 3);
            startActivity(it);
        });
        binding.setOtherClick(v -> {
            Intent it = new Intent(mContext, AgentInforActivity.class);
            it.putExtra("userDuty", 4);
            startActivity(it);
        });
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "确定退出登录吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MobclickAgent.onKillProcess(mContext);
                        //ExitAppUtils.getInstance().exit();
                        dDialog.dismissDialog();
                        asyncLoginOut();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dDialog.showDialog("提示", "确定退出登录吗？", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MobclickAgent.onKillProcess(mContext);
                //ExitAppUtils.getInstance().exit();
                dDialog.dismissDialog();
                asyncLoginOut();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceive);
    }

    /**
     * 退出登录
     */
    private void asyncLoginOut() {
        String wholeUrl = AppUrl.host + AppUrl.LOGIN_OUT;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, loginOutLsner);
    }

    BaseRequestListener loginOutLsner = new JsonRequestListener() {

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
            showShortToast("已退出，请重新登录！");
            GlobalParam.userToken = null;
            // ---清除用户登录信息缓存---start---//
            SharedPreferences spf = getSharedPreferences(LoginActivity.FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.putString("logindate", "");
            editor.putString("usertoken", "");
            editor.commit();
            // ---清除用户登录信息缓存---end---//
            for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
                if (mActivity != null) {// 关闭所有页面
                    mActivity.finish();
                }
            }
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
    };
}
