package com.park61.teacherhelper.module.workplan.bean;

import java.io.Serializable;

/**
 * Created by chenlie on 2018/4/26.
 *
 * 知识库列表对象
 */

public class KnowledgeBean implements Serializable{

    //内容类型:1:视频,2:话题,3:音频
    private int courseType;
    private String coverImg;
    private int id;
    private String showDate;
    private String stringUpdateDate;
    private String title;
    private int contentType;
    private boolean isChecked;
    private int learningNum;
    private long learningTime;

    public KnowledgeBean() {
    }

    public KnowledgeBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStringUpdateDate() {
        return stringUpdateDate;
    }

    public void setStringUpdateDate(String stringUpdateDate) {
        this.stringUpdateDate = stringUpdateDate;
    }

    public int getLearningNum() {
        return learningNum;
    }

    public void setLearningNum(int learningNum) {
        this.learningNum = learningNum;
    }

    public long getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(long learningTime) {
        this.learningTime = learningTime;
    }
}
