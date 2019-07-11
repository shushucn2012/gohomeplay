package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.adapter.DateGvAdapter;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-猜你喜欢（四宫格组件）
 * Created by shubei on 2018/4/16.
 */

public class GuessYourLoveModule extends LinearLayout {

    private GridViewForScrollView gv_date;
    private DateGvAdapter mDateGvAdapter;
    private List<ContentItem> dList;
    private GoldTeacherModule.OnChangeClickCallBack callBack;

    public GuessYourLoveModule(Context context, List<ContentItem> dList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.shophome_date_layout, this, true);
        this.dList = dList;
        ((TextView) findViewById(R.id.tv_guess_title)).setText(moduleName);
        gv_date = (GridViewForScrollView) findViewById(R.id.gv_date);
        gv_date.setFocusable(false);
        mDateGvAdapter = new DateGvAdapter(context, dList);
        gv_date.setAdapter(mDateGvAdapter);
        gv_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddStatistics(context, dList.get(position).getId(), "guess");
                Intent it = new Intent(context, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", dList.get(position).getId());
                context.startActivity(it);
            }
        });
        findViewById(R.id.area_change_guess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onChange();
            }
        });
    }

    /**
     * 切换数据
     */
    public void changeData(List<ContentItem> list) {
        dList.clear();
        dList.addAll(list);
        mDateGvAdapter.notifyDataSetChanged();
    }

    public interface OnChangeClickCallBack {
        void onChange();
    }

    public void setOnChangeClickCallBack(GoldTeacherModule.OnChangeClickCallBack c) {
        this.callBack = c;
    }
}
