package com.park61.teacherhelper.module.workplan.bean;

public class TaskCalendarListBean {
    /**
     * status : 1
     * level2Name : 日常教学
     * name : 1.厨房卫生检查 2.全园打包被子
     * isEnableCheck : 0
     * teachGroupId : 6
     * id : 56
     * taskOwnerDuty : 2
     * taskCalendarLog : {"showUpdateDate":"31分钟之前","type":0,"intro":"任务完成","userName":"崔夏青"}
     * isKeyView : 0
     * level1Name : 教学活动
     * intro : 1.厨房卫生检查 2.全园打包被子
     */

    private int status;
    private String level2Name;
    private String name;
    private int isEnableCheck;//关键任务是否可勾选 0不可 1可勾选
    private int teachGroupId;
    private int id;
    private String taskOwnerDuty;
    private TaskCalendarLogBean taskCalendarLog;
    private int isKeyView;//是否显示关键目标0不显示1显示
    private String level1Name;
    private String intro;
    private String teachClassName;
    private int templateTaskType;//任务类型，0工作任务1学习任务
    private int isOwnTask;//是否是自己的任务

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLevel2Name() {
        return level2Name;
    }

    public void setLevel2Name(String level2Name) {
        this.level2Name = level2Name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsEnableCheck() {
        return isEnableCheck;
    }

    public void setIsEnableCheck(int isEnableCheck) {
        this.isEnableCheck = isEnableCheck;
    }

    public int getTeachGroupId() {
        return teachGroupId;
    }

    public void setTeachGroupId(int teachGroupId) {
        this.teachGroupId = teachGroupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskOwnerDuty() {
        return taskOwnerDuty;
    }

    public void setTaskOwnerDuty(String taskOwnerDuty) {
        this.taskOwnerDuty = taskOwnerDuty;
    }

    public TaskCalendarLogBean getTaskCalendarLog() {
        return taskCalendarLog;
    }

    public void setTaskCalendarLog(TaskCalendarLogBean taskCalendarLog) {
        this.taskCalendarLog = taskCalendarLog;
    }

    public int getIsKeyView() {
        return isKeyView;
    }

    public void setIsKeyView(int isKeyView) {
        this.isKeyView = isKeyView;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }

    public int getTemplateTaskType() {
        return templateTaskType;
    }

    public void setTemplateTaskType(int templateTaskType) {
        this.templateTaskType = templateTaskType;
    }

    public int getIsOwnTask() {
        return isOwnTask;
    }

    public void setIsOwnTask(int isOwnTask) {
        this.isOwnTask = isOwnTask;
    }
}