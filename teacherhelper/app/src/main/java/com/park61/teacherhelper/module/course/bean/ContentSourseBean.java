package com.park61.teacherhelper.module.course.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/10/14.
 */

public class ContentSourseBean implements Serializable {

    private int contentId;
    private int contentItemId;
    private int createBy;
    private String createTime;
    private String delFlag;
    private int id;
    private String sourceName;
    private long sourceSize;
    private String sourceTime;
    private int sourceType;
    private String sourceUrl;
    private int updateBy;
    private String updateTime;
    private String videoImg;
    private int viewNum;
private String playAuth;

    public String getDelFlag() {
        return delFlag;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public int getId() {
        return id;
    }

    public int getContentId() {
        return contentId;
    }

    public int getContentItemId() {
        return contentItemId;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public int getSourceType() {
        return sourceType;
    }

    public int getViewNum() {
        return viewNum;
    }

    public long getSourceSize() {
        return sourceSize;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setContentItemId(int contentItemId) {
        this.contentItemId = contentItemId;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setSourceSize(long sourceSize) {
        this.sourceSize = sourceSize;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public String getPlayAuth() {
        return playAuth;
    }

    public void setPlayAuth(String playAuth) {
        this.playAuth = playAuth;
    }
}
