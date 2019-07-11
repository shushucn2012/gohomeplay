package com.park61.teacherhelper.module.workplan.bean;

import java.util.List;

/**
 * Created by shubei on 2018/4/28.
 */

public class TaskInfoBean {


    /**
     * endShowDate : 2018/01/29
     * id : 1
     * intro : 订购新学期家联本、新五大领域教师用
     * name : 订购新学期家联本、新五大领域教师用
     * startShowDate : 2018/01/29
     * status : 0
     * statusName : 未完成
     * targetNum : 111
     * teachGroupId : 176
     */

    private String endShowDate;
    private int id;
    private String intro;
    private String name;
    private String startShowDate;
    private int status;//状态：0待办，1已完成
    private String statusName;
    private String targetNum;
    private int teachGroupId;
    private long startDate;
    private long endDate;
    private int isKeyView;
    private int isEnableCheck;
    private int level1;
    private String level1Name;
    private List<String> taskExecutors;
    private List<String> taskExecutorUserIds;
    private int isImportTask;
    private List<KnowledgeBean> trainerCourseList;
    private int isOwnTask;//是否是自己的任务
    private int templateTaskType;//

    public int getIsEnableCheck() {
        return isEnableCheck;
    }

    public void setIsEnableCheck(int isEnableCheck) {
        this.isEnableCheck = isEnableCheck;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getEndShowDate() {
        return endShowDate;
    }

    public void setEndShowDate(String endShowDate) {
        this.endShowDate = endShowDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartShowDate() {
        return startShowDate;
    }

    public void setStartShowDate(String startShowDate) {
        this.startShowDate = startShowDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(String targetNum) {
        this.targetNum = targetNum;
    }

    public int getTeachGroupId() {
        return teachGroupId;
    }

    public void setTeachGroupId(int teachGroupId) {
        this.teachGroupId = teachGroupId;
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

    public List<String> getTaskExecutors() {
        return taskExecutors;
    }

    public void setTaskExecutors(List<String> taskExecutors) {
        this.taskExecutors = taskExecutors;
    }

    public int getIsImportTask() {
        return isImportTask;
    }

    public void setIsImportTask(int isImportTask) {
        this.isImportTask = isImportTask;
    }

    public List<String> getTaskExecutorUserIds() {
        return taskExecutorUserIds;
    }

    public void setTaskExecutorUserIds(List<String> taskExecutorUserIds) {
        this.taskExecutorUserIds = taskExecutorUserIds;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public List<KnowledgeBean> getTrainerCourseList() {
        return trainerCourseList;
    }

    public void setTrainerCourseList(List<KnowledgeBean> trainerCourseList) {
        this.trainerCourseList = trainerCourseList;
    }

    public int getIsOwnTask() {
        return isOwnTask;
    }

    public void setIsOwnTask(int isOwnTask) {
        this.isOwnTask = isOwnTask;
    }

    public int getTemplateTaskType() {
        return templateTaskType;
    }

    public void setTemplateTaskType(int templateTaskType) {
        this.templateTaskType = templateTaskType;
    }
}
