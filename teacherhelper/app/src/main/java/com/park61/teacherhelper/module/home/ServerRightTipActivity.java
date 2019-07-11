package com.park61.teacherhelper.module.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityServerTipsBinding;

/**
 * Created by chenlie on 2018/3/7.
 *
 * 服务权益 首页蒙层提醒
 */

public class ServerRightTipActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityServerTipsBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_server_tips);

        bind.setNextPage(v -> {
            startActivity(new Intent(this, RightTipTwoActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
