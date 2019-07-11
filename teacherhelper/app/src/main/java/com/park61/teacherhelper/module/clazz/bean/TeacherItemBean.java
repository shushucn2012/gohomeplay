package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/10/19.
 */

public class TeacherItemBean implements Serializable {
    private int commentNum;
    private int contentType;
    private String coverImg;
    private String createDate;
    private String delFlag;
    private int id;
    private int isFine;
    private int isFree;
    private int praiseNum;
    private int shareNum;
    private int sort;
    private String sortTime;
    private int status;
    private String title;
    private int viewNum;
    private int teachActivityId;
    private String showDate;

    public int getContentType() {
        return contentType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getId() {
        return id;
    }

    public int getIsFine() {
        return isFine;
    }

    public int getIsFree() {

        return isFree;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public int getSort() {
        return sort;
    }

    public int getStatus() {
        return status;
    }

    public int getViewNum() {
        return viewNum;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getSortTime() {
        return sortTime;
    }

    public String getTitle() {
        return title;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsFine(int isFine) {
        this.isFine = isFine;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public void setSortTime(String sortTime) {
        this.sortTime = sortTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getTeachActivityId() {
        return teachActivityId;
    }

    public void setTeachActivityId(int teachActivityId) {
        this.teachActivityId = teachActivityId;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }
}
