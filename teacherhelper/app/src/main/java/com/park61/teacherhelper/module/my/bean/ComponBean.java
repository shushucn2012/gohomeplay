package com.park61.teacherhelper.module.my.bean;

/**
 * Created by chenlie on 2018/5/8.
 *
 */

public class ComponBean {

    private int id;
    private int couponId;//可用课程列表筛选字段
    private String limitEnd;
    private String limitStart;
    //优惠券参数，（体验天数、满额等）
    private int ruleValue1;
    //优惠券参数（折扣额、减金额）
    private double ruleValue2;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(String limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(String limitStart) {
        this.limitStart = limitStart;
    }

    public int getRuleValue1() {
        return ruleValue1;
    }

    public void setRuleValue1(int ruleValue1) {
        this.ruleValue1 = ruleValue1;
    }

    public double getRuleValue2() {
        return ruleValue2;
    }

    public void setRuleValue2(double ruleValue2) {
        this.ruleValue2 = ruleValue2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
}
