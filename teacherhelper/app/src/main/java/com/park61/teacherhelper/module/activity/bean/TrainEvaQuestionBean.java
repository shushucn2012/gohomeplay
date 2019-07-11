package com.park61.teacherhelper.module.activity.bean;

import android.widget.EditText;

import java.util.List;

public class TrainEvaQuestionBean {

    private int id;//问题id
    private String questionName;//问题标题
    private String headline;//问题分类名称
    private int type;//问题类型 题目类型:1:评分型题目;2:单选题;3:多选题;4:开放型题目
    private int sort;//题目序号
    private List<TrainEvaAnswerBean> listAnswer;//选项列表
    private int chosenAnswerIndex;//选择的答案等级index，4，3，2，1
    private String answerContent;
    //private EditText editText;//内容输入框

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<TrainEvaAnswerBean> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<TrainEvaAnswerBean> listAnswer) {
        this.listAnswer = listAnswer;
    }

    public int getChosenAnswerIndex() {
        return chosenAnswerIndex;
    }

    public void setChosenAnswerIndex(int chosenAnswerIndex) {
        this.chosenAnswerIndex = chosenAnswerIndex;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

   /* public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }*/
}
