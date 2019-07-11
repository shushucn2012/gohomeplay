package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/11.
 */

public class CourseOrderBean implements Serializable {

    private int canUserCouponNum;
    private double couponAmount;
    private int couponId;
    //课程系列id
    private int courseSeriesId;
    private String imgUrl;
    private double originalPrice;
    private double salePrice;
    private String title;
    private int userId;

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

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getCourseSeriesId() {
        return courseSeriesId;
    }

    public void setCourseSeriesId(int courseSeriesId) {
        this.courseSeriesId = courseSeriesId;
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
}
