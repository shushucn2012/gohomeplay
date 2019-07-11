// Copyright 2012 Square, Inc.
package com.park61.teacherhelper.module.workplan.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.workplan.bean.TaskCellBean;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthView extends LinearLayout {
    private TextView title;
    private CalendarGridView grid;
    private Listener listener;

    public static MonthView create(ViewGroup parent, LayoutInflater inflater, DateFormat weekdayNameFormat, Listener listener, Calendar today) {
        final MonthView view = (MonthView) inflater.inflate(R.layout.month_plan, parent, false);
        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + offset);
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText("周" + weekdayNameFormat.format(today.getTime()).substring(1));
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        return view;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells, boolean displayOnly) {
        title.setText(month.getLabel());
        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            FrameLayout frameLayout = (FrameLayout) grid.getChildAt(i + 1);
            CalendarRowView weekRow = (CalendarRowView) frameLayout.getChildAt(0);
            FrameLayout textRow = (FrameLayout) frameLayout.getChildAt(1);
            weekRow.setListener(listener);
            if (i < numRows) {
                frameLayout.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(c);
                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);
                    cellView.getTvDate().setText(Integer.toString(cell.getValue()));
                    cellView.setClickable(!displayOnly);
                    cellView.setTag(cell);
                    if (!cell.isSelectable()) {
                        cellView.getTvDate().setText("");
                    }
                    if (cell.isSelectable() && DateTool.isToday(cell.getDate())) {//日期与今天相等，设置黑色圆背景
                        cellView.getTvDate().setBackgroundResource(R.drawable.circle_shape_black);
                        cellView.getTvDate().setTextColor(getResources().getColor(R.color.gffffff));
                        if (cell.getValue() > 9) {
                            int paddinglrValue = DevAttr.dip2px(getContext(), 2);
                            int paddingtbValue = DevAttr.dip2px(getContext(), 1);
                            cellView.getTvDate().setPadding(paddinglrValue, paddingtbValue, paddinglrValue, paddingtbValue);
                        } else {
                            int paddingValue = DevAttr.dip2px(getContext(), 4);
                            cellView.getTvDate().setPadding(paddingValue, 0, paddingValue, 0);
                        }
                    }
                    TaskCellBean taskBean = cell.getTaskCellBean();
                    TextView cellText = (TextView) textRow.getChildAt(c);
                    if (taskBean != null) {
                        int showStyle = getTaskCellStyleInRow(week, c);
                        if (showStyle == TaskStyle.CELL_STYLE_START) {
                            cellView.setTaskCellBg(taskBean.getStatus(), TaskStyle.CELL_STYLE_START, taskBean.getIsMulti());
                            int last = getTaskLastDaysInRow(week, c);
                            setTextStr(cellText, taskBean.getName(), last, c);
                        } else if (showStyle == TaskStyle.CELL_STYLE_MID) {
                            cellView.setTaskCellBg(taskBean.getStatus(), TaskStyle.CELL_STYLE_MID, taskBean.getIsMulti());
                        } else if (showStyle == TaskStyle.CELL_STYLE_END) {
                            cellView.setTaskCellBg(taskBean.getStatus(), TaskStyle.CELL_STYLE_END, taskBean.getIsMulti());
                        } else {
                            cellView.setTaskCellBg(taskBean.getStatus(), TaskStyle.CELL_STYLE_ONLY, taskBean.getIsMulti());
                            int last = getTaskLastDaysInRow(week, c);
                            setTextStr(cellText, taskBean.getName(), last, c);
                        }
                    }
                }
            } else {
                frameLayout.setVisibility(GONE);
            }
        }
    }

    public interface Listener {
        void handleClick(MonthCellDescriptor cell);
    }

    public final static class TaskStyle {
        public final static int CELL_STYLE_START = 0;//开始样式
        public final static int CELL_STYLE_MID = 1;//中间样式
        public final static int CELL_STYLE_END = 2;//结束样式
        public final static int CELL_STYLE_ONLY = 3;//独自样式
    }

    public final static class TaskState {
        public final static int DELAY = -1;//逾期
        public final static int TODO = 0;//待办
        public final static int FINISH = 1;//完成
    }

    /**
     * 判断该任务在本行是什么样式
     *
     * @param week
     * @param curIndex
     * @return
     */
    public int getTaskCellStyleInRow(List<MonthCellDescriptor> week, int curIndex) {
        if (curIndex == 0 || week.get(curIndex).getValue() == 1) { // 一周的第一个或者一月的第一个
            if (curIndex == 6)// 最后一个
                return TaskStyle.CELL_STYLE_ONLY;
            TaskCellBean thisBean = week.get(curIndex).getTaskCellBean();
            TaskCellBean lastBean = week.get(curIndex + 1).getTaskCellBean();
            if (lastBean != null && thisBean.getId() == lastBean.getId()) {//下一个任务和这个任务连续，返回开始样式
                return TaskStyle.CELL_STYLE_START;
            } else {//下一个任务和这个任务不连续，返回独自样式
                return TaskStyle.CELL_STYLE_ONLY;
            }
        }
        if (curIndex > 0 && curIndex < 6) {// 第2-6个
            TaskCellBean preBean = week.get(curIndex - 1).getTaskCellBean();
            TaskCellBean thisBean = week.get(curIndex).getTaskCellBean();
            TaskCellBean lastBean = week.get(curIndex + 1).getTaskCellBean();
            if(preBean == null){//当前任务和上一个任务不同
                if (lastBean != null && thisBean.getId() == lastBean.getId()) {//下一个任务和这个任务连续，返回开始样式
                    return TaskStyle.CELL_STYLE_START;
                } else {//下一个任务和这个任务不连续，返回独自样式
                    return TaskStyle.CELL_STYLE_ONLY;
                }
            }
            if (preBean != null && thisBean.getId() != preBean.getId()) {//当前任务和上一个任务不同
                if (lastBean != null && thisBean.getId() == lastBean.getId()) {//下一个任务和这个任务连续，返回开始样式
                    return TaskStyle.CELL_STYLE_START;
                } else {//下一个任务和这个任务不连续，返回独自样式
                    return TaskStyle.CELL_STYLE_ONLY;
                }
            } else {//当前任务和上一个任务相同
                if (lastBean != null && thisBean.getId() == lastBean.getId()) {//下一个任务和这个任务连续，返回中间样式
                    return TaskStyle.CELL_STYLE_MID;
                } else {//下一个任务和这个任务不连续，返回结束样式
                    return TaskStyle.CELL_STYLE_END;
                }
            }
        }
        if (curIndex == 6) { // 最后一个
            TaskCellBean preBean = week.get(curIndex - 1).getTaskCellBean();
            TaskCellBean thisBean = week.get(curIndex).getTaskCellBean();
            if (preBean != null && thisBean.getId() == preBean.getId()) {//上一个任务和这个任务连续，返回开始样式
                return TaskStyle.CELL_STYLE_END;
            } else {//上一个任务和这个任务不连续，返回独自样式
                return TaskStyle.CELL_STYLE_ONLY;
            }
        }
        return TaskStyle.CELL_STYLE_ONLY;
    }

    /**
     * 判断该任务在本行持续几天
     *
     * @param week
     * @param curIndex
     * @return
     */
    public int getTaskLastDaysInRow(List<MonthCellDescriptor> week, int curIndex) {
        int last = 0;
        TaskCellBean taskBeanCur = week.get(curIndex).getTaskCellBean();
        for (int i = curIndex + 1; i < week.size(); i++) {
            TaskCellBean taskBeanNow = week.get(i).getTaskCellBean();
            if (taskBeanNow == null)
                break;
            if (taskBeanNow.getId() == taskBeanCur.getId()) {
                last++;
            } else {
                break;
            }
        }
        return last;
    }

    public void setTextStr(TextView cellText, String name, int last, int index) {
        int screenWidth = DevAttr.getScreenWidth(getContext());
        int cWidthWhole = screenWidth - DevAttr.dip2px(getContext(), 20) * 2;
        int itemWidth = cWidthWhole / 7;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) cellText.getLayoutParams();
        lp.height = DevAttr.dip2px(getContext(), 60);
        lp.width = itemWidth * (last + 1) - DevAttr.dip2px(getContext(), 8);
        lp.leftMargin = index * itemWidth + DevAttr.dip2px(getContext(), 6);
        cellText.setLayoutParams(lp);
        cellText.setText(name);
    }
}
