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
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.adapter.ShowListAdapter;
import com.park61.teacherhelper.module.home.bean.ContentItem;

import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-更多内容列表组件
 * Created by shubei on 2018/4/16.
 */

public class MoreContentModule extends LinearLayout {

    private ListView lv_show;
    private ShowListAdapter mShowListAdapter;

    public MoreContentModule(Context context, List<ContentItem> sList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.more_content_module, this, true);
        ((TextView) findViewById(R.id.tv_teacher_show)).setText(moduleName);
        lv_show = (ListView) findViewById(R.id.lv_show);
        lv_show.setFocusable(false);
        mShowListAdapter = new ShowListAdapter(context, sList);
        lv_show.setAdapter(mShowListAdapter);
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddStatistics(context, sList.get(position).getId(), "more");
                Intent it = new Intent(context, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", sList.get(position).getId());
                context.startActivity(it);
            }
        });
    }
}
