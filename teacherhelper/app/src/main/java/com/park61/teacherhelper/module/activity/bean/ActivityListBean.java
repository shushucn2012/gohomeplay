package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/3.
 */

public class ActivityListBean implements Serializable {

     private String address;
    private String applyEndTime;
    private int applyNum;
    private Double applyPer;
    private String applyStartTime;
    private String btnName;
    private int btnStatus;
    private String coverImg;
    private int id;
    private int leftSecond;
    private Boolean limit;
    private int maxApplyNum;
    private String name;
    private Double originalPrice;
    private Double price;
    private int status;
  private String applyStartDate;
    private String applyStartShowTime;
    public Boolean getLimit() {
        return limit;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public Double getApplyPer() {
        return applyPer;
    }

    public String getBtnName() {
        return btnName;
    }

    public int getBtnStatus() {

        return btnStatus;
    }

    public int getId() {
        return id;
    }

    public int getLeftSecond() {
        return leftSecond;
    }

    public int getMaxApplyNum() {
        return maxApplyNum;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public int getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public String getApplyStartTime() {
        return applyStartTime;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public void setApplyPer(Double applyPer) {
        this.applyPer = applyPer;
    }

    public void setApplyStartTime(String applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public void setBtnStatus(int btnStatus) {
        this.btnStatus = btnStatus;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeftSecond(int leftSecond) {
        this.leftSecond = leftSecond;
    }

    public void setLimit(Boolean limit) {
        this.limit = limit;
    }

    public void setMaxApplyNum(int maxApplyNum) {
        this.maxApplyNum = maxApplyNum;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(String applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public String getApplyStartShowTime() {
        return applyStartShowTime;
    }

    public void setApplyStartShowTime(String applyStartShowTime) {
        this.applyStartShowTime = applyStartShowTime;
    }
}
