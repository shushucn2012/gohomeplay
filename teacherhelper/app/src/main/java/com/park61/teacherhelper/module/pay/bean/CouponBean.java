package com.park61.teacherhelper.module.pay.bean;

/**
 * Created by shubei on 2018/1/26.
 */

public class CouponBean {

    /**
     * id : 133
     * userId : null
     * childId : null
     * receiveTime : null
     * limitType : 1
     * startTime : 1516935885000
     * endTime : 1548990286000
     * useTime : null
     * status : null
     * orderId : null
     * code : null
     * ruleId : null
     * officeId : null
     * activityApplyId : null
     * writeOffUser : null
     * writeOffTime : null
     * updateTime : null
     * updateBy : null
     * remarks : 童子满10元减1.98
     * type : null
     * checkCoupon : 0
     * title : 童子满10元减1.98
     * ruleValue2 : 2
     * showTime : 2018.01.26--2019.02.01
     */

    private int id;//用户优惠券id
    private int couponId;//可用课程列表筛选字段
    private String remarks;//备注
    private String title;//标题
    private String ruleValue2;//优惠金额
    private String showTime;//展示时间
    private String showPrice;//展示金额
    //是否选中
    private int checkCoupon;

    private String limitStart;
    private String limitEnd;
    private int couponReceiveStatus;
    private String ruleValue1;//满金额
    private String useRange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuleValue2() {
        return ruleValue2;
    }

    public void setRuleValue2(String ruleValue2) {
        this.ruleValue2 = ruleValue2;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public int getCheckCoupon() {
        return checkCoupon;
    }

    public void setCheckCoupon(int checkCoupon) {
        this.checkCoupon = checkCoupon;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }

    public int getCouponReceiveStatus() {
        return couponReceiveStatus;
    }

    public void setCouponReceiveStatus(int couponReceiveStatus) {
        this.couponReceiveStatus = couponReceiveStatus;
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

    public String getRuleValue1() {
        return ruleValue1;
    }

    public void setRuleValue1(String ruleValue1) {
        this.ruleValue1 = ruleValue1;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }
}
