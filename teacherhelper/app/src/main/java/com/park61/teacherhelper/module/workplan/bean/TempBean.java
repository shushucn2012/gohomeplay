package com.park61.teacherhelper.module.workplan.bean;

/**
 * 模板b
 * Created by shubei on 2018/7/27.
 */

public class TempBean {


    /**
     * classCount : 1
     * endDate : 2018-07-31
     * id : 2
     * startDate : 2018-07-22
     * taskCount : 1
     * title : 模版2
     */

    private int classCount;
    private String endDate;
    private int id;
    private String startDate;
    private int taskCount;
    private String title;
    private String templateUrl;
    private String templateName;
    private int finishNum;
    private int unFinishNum;
    private int taskCalendarTemplateId;

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public int getUnFinishNum() {
        return unFinishNum;
    }

    public void setUnFinishNum(int unFinishNum) {
        this.unFinishNum = unFinishNum;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getTaskCalendarTemplateId() {
        return taskCalendarTemplateId;
    }

    public void setTaskCalendarTemplateId(int taskCalendarTemplateId) {
        this.taskCalendarTemplateId = taskCalendarTemplateId;
    }
}
