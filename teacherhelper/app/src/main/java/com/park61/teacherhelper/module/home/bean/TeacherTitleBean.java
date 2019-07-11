package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/10/20.
 */

public class TeacherTitleBean implements Serializable {

private  String bigPictureUrl;
    private String content;
    private String contentUpdateTime;
    private String createDate;
    private int delFlag;
    private String description;
    private String headPictureUrl;
    private int id;
    private String name;
    private int status;
    private String subhead;
    private String updateDate;
    private String popularity;
    private String viewNumber;
    private List<TeacherCourseBean> trainerCourseSeriesList;

    public String getCreateDate() {
        return createDate;
    }

    public int getStatus() {
        return status;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public int getId() {
        return id;
    }

    public String getBigPictureUrl() {
        return bigPictureUrl;
    }

    public String getContent() {
        return content;
    }

    public String getContentUpdateTime() {
        return contentUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getSubhead() {
        return subhead;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getViewNumber() {
        return viewNumber;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        this.bigPictureUrl = bigPictureUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentUpdateTime(String contentUpdateTime) {
        this.contentUpdateTime = contentUpdateTime;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setViewNumber(String viewNumber) {
        this.viewNumber = viewNumber;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public List<TeacherCourseBean> getTrainerCourseSeriesList() {
        return trainerCourseSeriesList;
    }

    public void setTrainerCourseSeriesList(List<TeacherCourseBean> trainerCourseSeriesList) {
        this.trainerCourseSeriesList = trainerCourseSeriesList;
    }
}
