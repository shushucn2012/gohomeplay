package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by shubei on 2018/7/31.
 */

public class TaskCellBean {

    private int id;
    private String name;
    private String intro;
    private String level1Name;
    private long startDate;
    private long endDate;
    private int status;//状态：-1 逾期 0待办，1已完成
    private int templateTaskType;//执行任务类型：0任务类，1学习类
    private int taskCalendarClassId;
    private boolean isMulti;//是否有多个任务
    private boolean isToday;//是否是今天

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTemplateTaskType() {
        return templateTaskType;
    }

    public void setTemplateTaskType(int templateTaskType) {
        this.templateTaskType = templateTaskType;
    }

    public boolean getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(boolean isMulti) {
        this.isMulti = isMulti;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public int getTaskCalendarClassId() {
        return taskCalendarClassId;
    }

    public void setTaskCalendarClassId(int taskCalendarClassId) {
        this.taskCalendarClassId = taskCalendarClassId;
    }
}
