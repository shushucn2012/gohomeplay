package com.park61.teacherhelper.module.activity.bean;

public class TrainEvaAnswerBean {

    private int id;//答案id
    private int trainingQuestionId;//问题id
    private int score;//分数
    private int sort;//序号
    private String name;//答案内容
    private boolean isChosen;//是否选中

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainingQuestionId() {
        return trainingQuestionId;
    }

    public void setTrainingQuestionId(int trainingQuestionId) {
        this.trainingQuestionId = trainingQuestionId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
