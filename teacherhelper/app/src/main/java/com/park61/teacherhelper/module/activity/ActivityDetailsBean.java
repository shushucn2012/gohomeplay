package com.park61.teacherhelper.module.activity;

import java.util.List;

/**
 * Created by shubei on 2017/10/24.
 */

public class ActivityDetailsBean {


    /**
     * id : 1
     * type : 1
     * name : 测试
     * introduction : 测试
     * applyStartTime : 2017-10-20 10:29:28
     * applyEndTime : 2017-10-20 11:29:31
     * startTime : 2017-10-20 10:29:38
     * endTime : 2017-10-20 11:29:41
     * coverImg : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20171020102946706_124.png?x-oss-process=style/logo_img
     * content : null
     * price : 1000.0
     * status : 2
     * applyNum : 0
     * delFlag : 0
     * createBy : null
     * createDate : 2017-10-20 10:29:52
     * updateBy : null
     * updateDate : 2017-10-20 10:29:52
     * trainers : []
     * contents : []
     * couponAmount : null
     * activityAmount : null
     * orderAmount : null
     * userMobile : null
     * userName : null
     * userId : null
     * clientName : null
     * teachActivityApplyId : null
     * paymentStatus : 0
     */

    private int id;
    private int type;
    private String name;
    private String introduction;
    private String applyStartTime;
    private String applyEndTime;
    private String startTime;
    private String endTime;
    private String coverImg;
    private String content;
    private double price;
    private int status;
    private int applyNum;
    private String delFlag;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String couponAmount;
    private String activityAmount;
    private String orderAmount;
    private String userMobile;
    private String userName;
    private long userId;
    private String clientName;
    private long teachActivityApplyId;
    private int paymentStatus;
    private List<?> trainers;
    private List<?> contents;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(String applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getActivityAmount() {
        return activityAmount;
    }

    public void setActivityAmount(String activityAmount) {
        this.activityAmount = activityAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getTeachActivityApplyId() {
        return teachActivityApplyId;
    }

    public void setTeachActivityApplyId(long teachActivityApplyId) {
        this.teachActivityApplyId = teachActivityApplyId;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<?> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<?> trainers) {
        this.trainers = trainers;
    }

    public List<?> getContents() {
        return contents;
    }

    public void setContents(List<?> contents) {
        this.contents = contents;
    }
}
