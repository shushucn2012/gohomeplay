// Copyright 2013 Square, Inc.

package com.park61.teacherhelper.module.workplan.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;


public class CalendarCellView extends LinearLayout {

    private TextView tv_date;
    private LinearLayout area_cell_bg;
    private View red_dot;//红点，当天有多个任务时显示
    private int marginlrValue = DevAttr.dip2px(getContext(), 2);//左右间距
    private int margintbValue = DevAttr.dip2px(getContext(), 4);//上下间距

    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.calendar_cellview_plan, this, true);
        tv_date = (TextView) findViewById(R.id.tv_date);
        area_cell_bg = (LinearLayout) findViewById(R.id.area_cell_bg);
        red_dot = findViewById(R.id.red_dot);
    }

    public TextView getTvDate() {
        return tv_date;
    }

    /**
     * 设置任务单元格样式
     *
     * @param level        任务状态：-1逾期；0待办；1完成
     * @param cellstyle    单元格样式 0开始；1中间；2尾部；3独个
     * @param isShowRedDot 是否显示红点
     */
    public void setTaskCellBg(int level, int cellstyle, boolean isShowRedDot) {
        if (isShowRedDot) {
            red_dot.setVisibility(VISIBLE);
        } else {
            red_dot.setVisibility(GONE);
        }
        tv_date.setTextColor(getResources().getColor(R.color.gffffff));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) area_cell_bg.getLayoutParams();
        if (cellstyle == MonthView.TaskStyle.CELL_STYLE_START) {//开始样式
            lp.setMargins(marginlrValue, margintbValue, 0, margintbValue);
        } else if (cellstyle == MonthView.TaskStyle.CELL_STYLE_MID) {//中间样式
            lp.setMargins(0, margintbValue, 0, margintbValue);
        } else if (cellstyle == MonthView.TaskStyle.CELL_STYLE_END) {//结束样式
            lp.setMargins(0, margintbValue, marginlrValue, margintbValue);
        } else {//独自样式
            lp.setMargins(marginlrValue, margintbValue, marginlrValue, margintbValue);
        }
        area_cell_bg.setLayoutParams(lp);
        if (cellstyle == MonthView.TaskStyle.CELL_STYLE_START) {//开始样式
            switch (level) {
                case MonthView.TaskState.DELAY://逾期
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gfa7f94_stroke_gfa7f94_solid_cornerleft5);
                    break;
                case MonthView.TaskState.TODO://待办
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gf8a311_stroke_gf8a311_solid_cornerleft5);
                    break;
                case MonthView.TaskState.FINISH://完成
                    area_cell_bg.setBackgroundResource(R.drawable.rec_g3ec6ff_stroke_g3ec6ff_solid_cornerleft5);
                    break;
            }
        } else if (cellstyle == MonthView.TaskStyle.CELL_STYLE_MID) {//中间样式
            switch (level) {
                case MonthView.TaskState.DELAY://逾期
                    area_cell_bg.setBackgroundColor(getResources().getColor(R.color.gfa7f94));
                    break;
                case MonthView.TaskState.TODO://待办
                    area_cell_bg.setBackgroundColor(getResources().getColor(R.color.gf8a311));
                    break;
                case MonthView.TaskState.FINISH://完成
                    area_cell_bg.setBackgroundColor(getResources().getColor(R.color.g3ec6ff));
                    break;
            }
        } else if (cellstyle == MonthView.TaskStyle.CELL_STYLE_END) {//结束样式
            switch (level) {
                case MonthView.TaskState.DELAY://逾期
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gfa7f94_stroke_gfa7f94_solid_cornerright5);
                    break;
                case MonthView.TaskState.TODO://待办
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gf8a311_stroke_gf8a311_solid_cornerright5);
                    break;
                case MonthView.TaskState.FINISH://完成
                    area_cell_bg.setBackgroundResource(R.drawable.rec_g3ec6ff_stroke_g3ec6ff_solid_cornerright5);
                    break;
            }
        } else {//独自样式
            switch (level) {
                case MonthView.TaskState.DELAY://逾期
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gfa7f94_stroke_gfa7f94_solid_corner5);
                    break;
                case MonthView.TaskState.TODO://待办
                    area_cell_bg.setBackgroundResource(R.drawable.rec_gf8a311_stroke_gf8a311_solid_corner5);
                    break;
                case MonthView.TaskState.FINISH://完成
                    area_cell_bg.setBackgroundResource(R.drawable.rec_g3ec6ff_stroke_g3ec6ff_solid_corner5);
                    break;
            }
        }
    }

}
