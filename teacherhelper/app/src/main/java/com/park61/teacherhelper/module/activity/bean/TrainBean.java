package com.park61.teacherhelper.module.activity.bean;

public class TrainBean {

    /**
     * id : 1
     * startTime : 2019-03-07 09:00:00
     * endTime : 2019-03-07 12:00:00
     * duration : 2
     * trainerName : 小明
     * trainerMobile : 13163387036
     * status : 3
     * trainingType : 一阶入园
     * isEvaluate : 0
     * showStartTime : 2019年03月07日 09:00
     * showEndTime : 2019年03月07日 12:00
     * dictValue : 1
     * trainer : mzm78725_atsj
     */

    private int id;
    private String startTime;
    private String endTime;
    private int duration;
    private String trainerName;
    private String trainerMobile;
    private int status;//1待排期 2待培训 3已完成 4已取消
    private String trainingType;
    private int isEvaluate;//0，评价反馈，1，已评价
    private String showStartTime;
    private String showEndTime;
    private String dictValue;
    private String trainer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerMobile() {
        return trainerMobile;
    }

    public void setTrainerMobile(String trainerMobile) {
        this.trainerMobile = trainerMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public int getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(int isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(String showEndTime) {
        this.showEndTime = showEndTime;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }
}
