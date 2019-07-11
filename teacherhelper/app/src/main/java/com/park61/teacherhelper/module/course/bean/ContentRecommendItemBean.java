package com.park61.teacherhelper.module.course.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/10/17.
 */

public class ContentRecommendItemBean implements Serializable {


    private String adaptAge;
    private int authorId;
    private int commentNum;
    private String content;
    private int contentType;
    private String coverImg;
    private int createBy;
    private String createDate;
    private String delFlag;
    private int id;
    private int isFine;
    private int isFree;
    private int level1CateId;
    private int level2CateId;
    private int praiseNum;
    private int shareNum;
    private int sort;
    private String sortTime;
    private int status;
    private String title;
    private String updateDate;
    private int viewNum;

    public int getContentType() {
        return contentType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getCreateBy() {
        return createBy;
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

    public int getLevel1CateId() {
        return level1CateId;
    }

    public int getLevel2CateId() {
        return level2CateId;
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

    public String getAdaptAge() {
        return adaptAge;
    }

    public String getContent() {
        return content;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setAdaptAge(String adaptAge) {

        this.adaptAge = adaptAge;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
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

    public void setLevel1CateId(int level1CateId) {
        this.level1CateId = level1CateId;
    }

    public void setLevel2CateId(int level2CateId) {
        this.level2CateId = level2CateId;
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

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }
}
