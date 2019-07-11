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
import com.park61.teacherhelper.module.home.ExpertListActivity;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.module.home.adapter.ExpertOpinionAdapter;
import com.park61.teacherhelper.module.home.adapter.TodayRecommentAdapter;
import com.park61.teacherhelper.module.home.bean.ExpertOpinionBean;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.widget.listview.HorizontalListViewV2;

import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-专家观点
 * Created by shubei on 2018/4/16.
 */

public class ExpertOpinionModule extends LinearLayout {

    private HorizontalListViewV2 todayHoriztLv;
    private ExpertOpinionAdapter tAdapter;

    public ExpertOpinionModule(Context context, List<ExpertOpinionBean> todayList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.expert_opinion_module, this, true);

        ((TextView) findViewById(R.id.tv_today_rc)).setText(moduleName);
        todayHoriztLv = (HorizontalListViewV2) findViewById(R.id.horilistview);
        tAdapter = new ExpertOpinionAdapter(context, todayList);
        todayHoriztLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent it = new Intent(context, ExpertCourseListActivity.class);
                it.putExtra("courseId", todayList.get(position).getId());
                context.startActivity(it);*/

                AddStatistics(context, todayList.get(position).getId(), "ClickExpertView");
                Intent intent = new Intent(context, TeachHouseActivity.class);
                intent.putExtra("teachId", todayList.get(position).getId());
                context.startActivity(intent);
            }
        });
        findViewById(R.id.area_lookmore).setOnClickListener(v -> {
            context.startActivity(new Intent(context, ExpertListActivity.class));
        });
        todayHoriztLv.setAdapter(tAdapter);
    }
}
