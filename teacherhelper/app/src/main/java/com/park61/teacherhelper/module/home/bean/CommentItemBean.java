package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/10/16.
 */

public class CommentItemBean implements Serializable {

    private String content;
    private String  createDate;
    private String currDate;
    private  int id;
    private  Boolean isReply;
    private int itemId;
    private  long mobile;
    private  int requirementId;
    private  int userId;
    private String userName;
    private String userUrl;
    private String createTime;
    public Boolean getReply() {
        return isReply;
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequirementId() {
        return requirementId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCurrDate() {
        return currDate;
    }

    public long getMobile() {
        return mobile;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public void setReply(Boolean reply) {
        isReply = reply;
    }

    public void setRequirementId(int requirementId) {
        this.requirementId = requirementId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
