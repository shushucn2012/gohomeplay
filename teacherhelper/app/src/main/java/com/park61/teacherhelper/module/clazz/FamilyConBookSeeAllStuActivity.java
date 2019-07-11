package com.park61.teacherhelper.module.clazz;

import android.view.View;
import android.widget.ListView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.clazz.adapter.FamilyChoseStuListAdapter;
import com.park61.teacherhelper.module.clazz.adapter.FamilySeeAllStuListAdapter;
import com.park61.teacherhelper.module.clazz.bean.StudentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 家园联系薄-查看评语发布的学生
 *
 * @author shubei
 * @time 2018/11/26 11:17
 */
public class FamilyConBookSeeAllStuActivity extends BaseActivity {

    private List<StudentBean> sList = new ArrayList<>();

    private ListView lv_whole;
    private FamilySeeAllStuListAdapter myListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_familyconbook_seeall);
    }

    @Override
    public void initView() {
        setPagTitle("全部学生");
        lv_whole = findViewById(R.id.lv_whole);
        findViewById(R.id.area_right).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        sList = (List<StudentBean>) getIntent().getSerializableExtra("STUDENT_LIST");
        myListAdapter = new FamilySeeAllStuListAdapter(this, sList);
        lv_whole.setAdapter(myListAdapter);
    }

    @Override
    public void initListener() {

    }
}
