package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;


public class MeEditActivity extends BaseActivity {

    private EditText edit_info;
    private Button btn_save;
    private String oldData;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_me_edit);
    }

    @Override
    public void initView() {
        setPagTitle("编辑资料");
        edit_info = (EditText) findViewById(R.id.edit_info);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    public void initData() {
        oldData = getIntent().getStringExtra("current_data");
        if (!"未填写".equals(oldData))
            edit_info.setText(oldData);
    }

    @Override
    public void initListener() {
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String newData = "";
                Intent backData = new Intent();
                newData = edit_info.getText().toString();
                if (TextUtils.isEmpty(newData)) {
                    showShortToast("未填写数据！");
                    return;
                }
                backData.putExtra("new_data", newData);
                setResult(RESULT_OK, backData);
                finish();
            }
        });
    }

}
