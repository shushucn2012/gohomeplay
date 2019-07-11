// Copyright 2012 Square, Inc.

package com.park61.teacherhelper.module.workplan.calendarview;

import com.park61.teacherhelper.module.workplan.bean.TaskCellBean;

import java.util.Date;

/**
 * Describes the state of a particular date cell in a {@link MonthView}.
 */
public class MonthCellDescriptor {

    private final Date date; // 日期
    private final int value;//月号值，如：23号
    private boolean isSelectable;//是否是当月日期，当月日历上可能有上月或下月的日期，这些日期不可选
    private TaskCellBean taskCellBean;

    public MonthCellDescriptor(Date date, int value, boolean selectable, TaskCellBean taskCellBean) {
        this.date = date;
        this.value = value;
        this.isSelectable = selectable;
        this.taskCellBean = taskCellBean;
    }

    public Date getDate() {
        return date;
    }


    public int getValue() {
        return value;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public TaskCellBean getTaskCellBean() {
        return taskCellBean;
    }

    public void setTaskCellBean(TaskCellBean taskCellBean) {
        this.taskCellBean = taskCellBean;
    }
}
