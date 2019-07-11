package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2018/3/12.
 */

public class MServiceItem implements Serializable {


    /**
     * id : 13
     * batchId : 18
     * userId : null
     * applyName : null
     * applyMobile : null
     * schoolName : 哈哈哈幼儿园
     * receiverAddrId : 13
     * status : 0
     * value : null
     * description : null
     * acceptanceDate : null
     * completeDate : null
     * rejectReason : null
     * delFlag : 0
     * createDate : 2018-03-12 17:59:17
     * createBy : null
     * updateDate : null
     * updateBy : null
     * provinceId : null
     * cityId : null
     * countyId : null
     * statusName : 待受理
     * batchCode : etMuptLM
     * addressName : 湖北省武汉市江岸区
     * rightsId : null
     * rightsUrl : null
     */

    private int id;
    private int batchId;
    private long userId;
    private String applyName;
    private String applyMobile;
    private String schoolName;
    private int receiverAddrId;
    private int status;// 状态:0:待受理;1:已受理;2:已完成;3:已驳回
    private String value;
    private String description;
    private String acceptanceDate;
    private String completeDate;
    private String rejectReason;
    private String delFlag;
    private String createDate;
    private String createBy;
    private String updateDate;
    private String updateBy;
    private int provinceId;
    private int cityId;
    private int countyId;
    private String statusName;
    private String batchCode;
    private String addressName;
    private int rightsId;
    private String rightsUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyMobile() {
        return applyMobile;
    }

    public void setApplyMobile(String applyMobile) {
        this.applyMobile = applyMobile;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getReceiverAddrId() {
        return receiverAddrId;
    }

    public void setReceiverAddrId(int receiverAddrId) {
        this.receiverAddrId = receiverAddrId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(String acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public int getRightsId() {
        return rightsId;
    }

    public void setRightsId(int rightsId) {
        this.rightsId = rightsId;
    }

    public String getRightsUrl() {
        return rightsUrl;
    }

    public void setRightsUrl(String rightsUrl) {
        this.rightsUrl = rightsUrl;
    }
}
