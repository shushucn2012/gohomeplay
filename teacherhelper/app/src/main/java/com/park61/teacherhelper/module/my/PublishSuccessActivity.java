package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityPublishSucBinding;

/**
 * Created by chenlie on 2018/4/12.
 *
 * 发布活动成功结果页面， 关闭发布界面，刷新我的活动页面
 */

public class PublishSuccessActivity extends BaseActivity {

    ActivityPublishSucBinding bind;
    public static final String PUBLISH_SUCCESS = "publish_success";

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_publish_suc);
    }

    @Override
    public void initView() {
        setPagTitle("完成");
        bind.head.areaRight.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        //发广播通知
        sendBroadcast(new Intent(PUBLISH_SUCCESS));
    }

    @Override
    public void initListener() {
        bind.finish.setOnClickListener(v -> finish());
    }
}
