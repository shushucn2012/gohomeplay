package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.adapter.FastGoGvAdapter;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;


/**
 * 首页组件化-快捷入口组件，用网格实现
 * Created by shubei on 2018/4/16.
 */

public class FastGoGridModule extends LinearLayout {

    private GridViewForScrollView gv_fg;
    private FastGoGvAdapter adapter;

    public FastGoGridModule(Context context, List<CmsItem> quickList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fastgo_grid_module, this, true);
        gv_fg = (GridViewForScrollView) findViewById(R.id.gv_fg);
        gv_fg.setFocusable(false);
        adapter = new FastGoGvAdapter(context, quickList);
        gv_fg.setAdapter(adapter);
        gv_fg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewInitTool.AddStatistics(context, quickList.get(position), false);
                ViewInitTool.judgeGoWhere(quickList.get(position), context);
            }
        });
    }

}
