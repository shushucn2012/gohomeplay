package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.adapter.TodayRecommentAdapter;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.widget.listview.HorizontalListViewV2;

import java.util.List;

/**
 * 首页组件化-今日推荐
 * Created by shubei on 2018/4/16.
 */

public class CourseRecommendModule extends LinearLayout {

    private HorizontalListViewV2 todayHoriztLv;
    private TodayRecommentAdapter tAdapter;

    public CourseRecommendModule(Context context, List<TeacherCourseBean> todayList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.course_recommend_module, this, true);

        ((TextView) findViewById(R.id.tv_today_rc)).setText(moduleName);
        todayHoriztLv = (HorizontalListViewV2) findViewById(R.id.horilistview);
        tAdapter = new TodayRecommentAdapter(context, todayList);
        todayHoriztLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(context, ExpertCourseListActivity.class);
                it.putExtra("courseId", todayList.get(position).getId());
                context.startActivity(it);
            }
        });
        todayHoriztLv.setAdapter(tAdapter);
    }
}
