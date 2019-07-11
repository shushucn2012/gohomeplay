package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;

/**
 * 选择发布类型页面
 */
public class ChooseTaskTypeActivity extends BaseActivity {

    private View area_work, area_study;

    private int taskCalendarClassId;//传过来的模板分配班级id
    private int teachGroupId;

    @Override
    public void setLayout() {
        setContentView(R.layout.choose_task_type_dialog);
    }

    @Override
    public void initView() {
        area_work = findViewById(R.id.area_work);
        area_study = findViewById(R.id.area_study);
    }

    @Override
    public void initData() {
        taskCalendarClassId = getIntent().getIntExtra("taskCalendarClassId", -1);
        teachGroupId = getIntent().getIntExtra("teachGroupId", -1);
    }

    @Override
    public void initListener() {
        findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        area_work.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddWorkTaskActivity.class);
                intent.putExtra("taskCalendarClassId", taskCalendarClassId);
                intent.putExtra("teachGroupId", teachGroupId);
                startActivity(intent);
                finish();
            }
        });
        area_study.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddStudyTaskActivity.class);
                intent.putExtra("taskCalendarClassId", taskCalendarClassId);
                intent.putExtra("teachGroupId", teachGroupId);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.root_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
