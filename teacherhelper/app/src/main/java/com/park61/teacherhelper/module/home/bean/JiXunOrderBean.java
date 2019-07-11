package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * 集训活动订单bean
 * Created by shubei on 2018/9/13
 */

public class JiXunOrderBean implements Serializable {

    private int pmInfoId;
    private int canUserCouponNum;
    private double couponAmount;
    private int couponUseId;
    private String userMobile;
    private String userName;
    private String teachGroupName;
    private int orderType;
    private String imgUrl;
    private double originalPrice;
    private double salePrice;
    private String title;
    private int userId;

    public int getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(int pmInfoId) {
        this.pmInfoId = pmInfoId;
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

    public String getTeachGroupName() {
        return teachGroupName;
    }

    public void setTeachGroupName(String teachGroupName) {
        this.teachGroupName = teachGroupName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getCanUserCouponNum() {
        return canUserCouponNum;
    }

    public void setCanUserCouponNum(int canUserCouponNum) {
        this.canUserCouponNum = canUserCouponNum;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCouponUseId() {
        return couponUseId;
    }

    public void setCouponUseId(int couponUseId) {
        this.couponUseId = couponUseId;
    }
}
