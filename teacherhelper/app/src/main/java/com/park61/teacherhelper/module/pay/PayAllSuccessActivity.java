package com.park61.teacherhelper.module.pay;

import android.view.View;
import android.widget.Button;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;


/**
 * Created by shushucn2012 on 2016/6/14.
 */
public class PayAllSuccessActivity extends BaseActivity {
    private Button btn_finish;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_payall_success);
    }

    @Override
    public void initView() {
        btn_finish = (Button) findViewById(R.id.btn_finish);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
