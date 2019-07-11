package com.park61.teacherhelper.module.activity.bean;

/**
 * Created by shubei on 2017/11/2.
 */

public class Double11ActDetailsBean {


    /**
     * address : 测试内容hujn
     * applyNum : 20303
     * content : {}
     * countDown : 测试内容j333
     * coverImg : 测试内容po1i
     * maxApplyNum : 10004
     * name : 测试内容d68t
     * originalPrice : 70478
     * price : 60663
     * score : 47531
     * startTime : 测试内容n3vn
     * status : 71286
     */

    private String address;
    private int applyNum;
    private String content;
    private long leftSecond;
    private String coverImg;
    private int maxApplyNum;
    private String name;
    private String originalPrice;
    private String price;
    private double applyPer;
    private String startTime;
    private String applyStartDate;
    private int status;
    private boolean limit;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getMaxApplyNum() {
        return maxApplyNum;
    }

    public void setMaxApplyNum(int maxApplyNum) {
        this.maxApplyNum = maxApplyNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(boolean limit) {
        this.limit = limit;
    }

    public String getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(String applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public static class ContentBean {
    }

    public long getLeftSecond() {
        return leftSecond;
    }

    public void setLeftSecond(long leftSecond) {
        this.leftSecond = leftSecond;
    }

    public double getApplyPer() {
        return applyPer;
    }

    public void setApplyPer(double applyPer) {
        this.applyPer = applyPer;
    }
}
