package com.park61.teacherhelper.module.my.bean;

/**
 * Created by shubei on 2018/3/29.
 */

public class MeOrderDetailBean {


    /**
     * goodReceiverMobile : 13344443333
     * amount : 6
     * couponAmount : 1
     * status : 1
     * goodReceiverName : zhangsan
     * id : 2
     * payDate : 2018.03.19 16:07:06
     * type : 1
     * productAmount : 7
     * coverUrl : http://thirdwx.qlogo.cn/mmopen/vi_32/msXibhSIFVLqGADeedDgOAqzdHY1fC495QAef0oM5xoROA1Np14QaU8REvS18MFs9636FJqiaofgDibE901QTXYVA/132
     * createDate : 2018.03.19 16:07:06
     * title : NANA
     */

    private String goodReceiverMobile;
    private String amount;
    private String couponAmount;
    private int status;
    private String goodReceiverName;
    private String id;
    private String payDate;
    private int type;
    private String productAmount;
    private String coverUrl;
    private String createDate;
    private String title;

    public String getGoodReceiverMobile() {
        return goodReceiverMobile;
    }

    public void setGoodReceiverMobile(String goodReceiverMobile) {
        this.goodReceiverMobile = goodReceiverMobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGoodReceiverName() {
        return goodReceiverName;
    }

    public void setGoodReceiverName(String goodReceiverName) {
        this.goodReceiverName = goodReceiverName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
