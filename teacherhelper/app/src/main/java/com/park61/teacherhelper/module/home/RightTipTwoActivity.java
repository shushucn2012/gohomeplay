package com.park61.teacherhelper.module.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityServerTips2Binding;

/**
 * Created by chenlie on 2018/3/7.
 *
 * 服务权益 首页蒙层提醒
 */

public class RightTipTwoActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityServerTips2Binding bind = DataBindingUtil.setContentView(this, R.layout.activity_server_tips2);

        bind.setNextPage(v -> {
            SharedPreferences sp = getSharedPreferences(HomeNewActivity.SP_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("needShow", false);
            edit.apply();
            finish();
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
