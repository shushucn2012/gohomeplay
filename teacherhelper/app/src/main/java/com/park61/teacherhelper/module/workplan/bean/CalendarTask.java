package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by chenlie on 2018/4/25.
 */

public class CalendarTask {
    //0显示一天的日期1显示一天任务2显示empty
    private int type = 1;
    private String leftTopTime;
    private int id;
    private String intro;
    //是否可勾选  0不可 1可勾选
    private int isEnableCheck;
    //否显示关键目标0不显示1显示
    private int isKeyView;
    private String level1Name;
    private String level2Name;
    private String name;
    //任务状态 0待办，1已完成
    private int status;
    private TaskCalendarLog taskCalendarLog;
    private String taskOwnerDuty;
    private int teachGroupId;
    private String teachClassName;
    private String taskExecutorsDescription;
    private String taskStatusDescription;
    private int templateTaskType;//任务类型，0工作任务1学习任务
    private int isOwnTask;//是否是自己的任务


    public CalendarTask() {
    }

    public CalendarTask(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLeftTopTime() {
        return leftTopTime;
    }

    public void setLeftTopTime(String leftTopTime) {
        this.leftTopTime = leftTopTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIsEnableCheck() {
        return isEnableCheck;
    }

    public void setIsEnableCheck(int isEnableCheck) {
        this.isEnableCheck = isEnableCheck;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TaskCalendarLog getTaskCalendarLog() {
        return taskCalendarLog;
    }

    public void setTaskCalendarLog(TaskCalendarLog taskCalendarLog) {
        this.taskCalendarLog = taskCalendarLog;
    }

    public String getTaskOwnerDuty() {
        return taskOwnerDuty;
    }

    public void setTaskOwnerDuty(String taskOwnerDuty) {
        this.taskOwnerDuty = taskOwnerDuty;
    }

    public int getTeachGroupId() {
        return teachGroupId;
    }

    public void setTeachGroupId(int teachGroupId) {
        this.teachGroupId = teachGroupId;
    }

    public String getTaskExecutorsDescription() {
        return taskExecutorsDescription;
    }

    public void setTaskExecutorsDescription(String taskExecutorsDescription) {
        this.taskExecutorsDescription = taskExecutorsDescription;
    }

    public String getTaskStatusDescription() {
        return taskStatusDescription;
    }

    public void setTaskStatusDescription(String taskStatusDescription) {
        this.taskStatusDescription = taskStatusDescription;
    }

    public int getTemplateTaskType() {
        return templateTaskType;
    }

    public void setTemplateTaskType(int templateTaskType) {
        this.templateTaskType = templateTaskType;
    }

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }

    public int getIsOwnTask() {
        return isOwnTask;
    }

    public void setIsOwnTask(int isOwnTask) {
        this.isOwnTask = isOwnTask;
    }

    public class TaskCalendarLog {
        private String intro;
        private String showUpdateDate;
        private int type;
        private String userName;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getShowUpdateDate() {
            return showUpdateDate;
        }

        public void setShowUpdateDate(String showUpdateDate) {
            this.showUpdateDate = showUpdateDate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
