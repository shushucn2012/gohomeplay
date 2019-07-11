package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

public class CmsItem implements Serializable {


    /**
     * id : null
     * moduleId : null
     * locationId : null
     * title : null
     * linkPic : http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20171018145559880_670.jpg?x-oss-process=style/logo_img
     * linkUrl : aa
     * linkType : 1
     * linkData :
     * effectiveStartTime : null
     * effectiveEndTime : null
     * createDate : 2017-10-18 15:26:55
     * updateDate : 2017-10-18 15:26:55
     * createBy : 1
     * updateBy : 1
     * delFlag : null
     * locationIdArr : null
     * limitNum : null
     */

    private int id;
    private int moduleId;
    private int locationId;
    private String title;
    private String linkPic;
    private String linkUrl;
    private int linkType; //1
    private String linkData;
    private String effectiveStartTime;
    private String effectiveEndTime;
    private String createDate;
    private String updateDate;
    private int createBy;
    private int updateBy;
    private String delFlag;
    private String locationIdArr;
    private String limitNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getLinkData() {
        return linkData;
    }

    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    public String getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(String effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public String getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(String effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLocationIdArr() {
        return locationIdArr;
    }

    public void setLocationIdArr(String locationIdArr) {
        this.locationIdArr = locationIdArr;
    }

    public String getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(String limitNum) {
        this.limitNum = limitNum;
    }

    @Override
    public String toString() {
        return "CmsItem{" +
                "id=" + id +
                ", moduleId=" + moduleId +
                ", locationId=" + locationId +
                ", title='" + title + '\'' +
                ", linkPic='" + linkPic + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", linkType=" + linkType +
                ", linkData='" + linkData + '\'' +
                ", effectiveStartTime='" + effectiveStartTime + '\'' +
                ", effectiveEndTime='" + effectiveEndTime + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", delFlag='" + delFlag + '\'' +
                ", locationIdArr='" + locationIdArr + '\'' +
                ", limitNum='" + limitNum + '\'' +
                '}';
    }
}
