package com.park61.teacherhelper.module.pay;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.module.activity.Double11ActTicketActivity;

public class PaySuccessActivity extends BaseActivity {

    private Button btn_finish;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pay_success);
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
        btn_finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParam.ORDER_TYPE.equals("double11")) {
                    startActivity(new Intent(mContext, Double11ActTicketActivity.class));
                }
                finish();
            }
        });
    }

}
