package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.course.ExpertCourseMainActivity;
import com.park61.teacherhelper.module.home.ExpertListActivity;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.module.home.adapter.ExpertCourseAdapter;
import com.park61.teacherhelper.module.home.adapter.ExpertListAdapter;
import com.park61.teacherhelper.module.home.bean.ExpertCourseBean;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;

import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-专家讲堂
 * Created by shubei on 2018/4/16.
 */

public class ExpertCourseModule extends LinearLayout {

    private ListView lv_teacher;
    private ExpertCourseAdapter mExpertCourseAdapter;
    private List<ExpertCourseBean> gList;

    public ExpertCourseModule(Context context, List<ExpertCourseBean> gList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.expert_course_module, this, true);
        this.gList = gList;
        ((TextView) findViewById(R.id.tv_gold_teacher)).setText(moduleName);
        lv_teacher = (ListView) findViewById(R.id.lv_teacher);
        lv_teacher.setFocusable(false);
        mExpertCourseAdapter = new ExpertCourseAdapter(context, gList);
        lv_teacher.setAdapter(mExpertCourseAdapter);

        lv_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddStatistics(context, gList.get(i).getId(), "goldTeach");
               /* Intent intent = new Intent(context, TeachHouseActivity.class);
                intent.putExtra("teachId", gList.get(i).getId());
                context.startActivity(intent);*/

                Intent it = new Intent(context, ExpertCourseListActivity.class);
                it.putExtra("courseId", gList.get(i).getId());
                context.startActivity(it);
            }
        });
        findViewById(R.id.area_see_more).setOnClickListener(v -> {
            context.startActivity(new Intent(context, ExpertCourseMainActivity.class));
        });
    }

}
