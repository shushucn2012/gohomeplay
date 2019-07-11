package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * 评论信息
 */
public class EvaluateItemInfo implements Serializable {

    /**
     * id : 168
     * parentId : null
     * teachCourseId : 145
     * userId : 11
     * userType : 0
     * status : 0
     * score : 4
     * content : 哈哈哈哈哈
     * onlyAuthorSee : 0
     * delFlag : 0
     * remarks : null
     * createTime : 2017-08-30 11:33:50
     * createBy : 11
     * updateTime : null
     * updateBy : null
     */

    private int id;
    private String parentId;
    private int teachCourseId;
    private int userId;
    private int userType;
    private String status;
    private int score;
    private String content;
    private int onlyAuthorSee;
    private String delFlag;
    private String remarks;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private String petName;
    private String pictureUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getTeachCourseId() {
        return teachCourseId;
    }

    public void setTeachCourseId(int teachCourseId) {
        this.teachCourseId = teachCourseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOnlyAuthorSee() {
        return onlyAuthorSee;
    }

    public void setOnlyAuthorSee(int onlyAuthorSee) {
        this.onlyAuthorSee = onlyAuthorSee;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
