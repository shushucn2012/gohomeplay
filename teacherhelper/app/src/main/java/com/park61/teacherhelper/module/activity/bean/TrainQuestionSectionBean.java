package com.park61.teacherhelper.module.activity.bean;

import java.util.List;

/**
 * 问题分类实体类
 * @author shubei
 * @time 2019/3/11 9:58
 */
public class TrainQuestionSectionBean {

    private String headline;//分类标题
    private String createDate;//分类创建时间
    private List<TrainEvaQuestionBean> listQuestion;//分类下的问题列表

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<TrainEvaQuestionBean> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<TrainEvaQuestionBean> listQuestion) {
        this.listQuestion = listQuestion;
    }
}
