package com.park61.teacherhelper.module.workplan.bean;

/**
 * 模板任务完成情况bean
 * Created by shubei on 2018/7/26.
 */

public class TaskDoneInfoBean {


    /**
     * id : 4
     * isInformDirector : 0
     * taskCalendarTemplateName : 模版1
     * taskCompletionRate : 0.46
     * taskOverdueNum : 11
     * teachClassId : 382
     * teachClassName : 咕噜咕噜小班
     * teachGroupName : 测试61学院教研组
     */

    private int id; // 模板id
    private int isInformDirector; // 是否通知过园长督办，0:否,1:是
    private String taskCalendarTemplateName; // 模板名称
    private double taskCompletionRate; // 任务完成进度
    private int taskOverdueNum; // 逾期任务数
    private int teachClassId; //幼儿园班级id
    private String teachClassName; // 班级名称
    private String teachGroupName; //幼儿园名称
    private int teachGroupId;//幼儿园id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsInformDirector() {
        return isInformDirector;
    }

    public void setIsInformDirector(int isInformDirector) {
        this.isInformDirector = isInformDirector;
    }

    public String getTaskCalendarTemplateName() {
        return taskCalendarTemplateName;
    }

    public void setTaskCalendarTemplateName(String taskCalendarTemplateName) {
        this.taskCalendarTemplateName = taskCalendarTemplateName;
    }

    public double getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(double taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public int getTaskOverdueNum() {
        return taskOverdueNum;
    }

    public void setTaskOverdueNum(int taskOverdueNum) {
        this.taskOverdueNum = taskOverdueNum;
    }

    public int getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(int teachClassId) {
        this.teachClassId = teachClassId;
    }

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }

    public String getTeachGroupName() {
        return teachGroupName;
    }

    public void setTeachGroupName(String teachGroupName) {
        this.teachGroupName = teachGroupName;
    }

    public int getTeachGroupId() {
        return teachGroupId;
    }

    public void setTeachGroupId(int teachGroupId) {
        this.teachGroupId = teachGroupId;
    }
}
