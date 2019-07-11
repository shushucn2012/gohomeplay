package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/2.
 */

public class BannerBean implements Serializable {

    private int createBy;
    private String createDate;
    private String linkData;
    private String linkPic;
    private int linkType;
    private String linkUrl;
    private int selected;
    private String selectedPicUrl;
    private int updateBy;
    private String updateDate;


    public String getUpdateDate() {
        return updateDate;
    }

    public String getSelectedPicUrl() {
        return selectedPicUrl;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public String getLinkData() {
        return linkData;
    }

    public int getCreateBy() {
        return createBy;
    }

    public int getLinkType() {
        return linkType;
    }

    public int getSelected() {
        return selected;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public void setSelectedPicUrl(String selectedPicUrl) {
        this.selectedPicUrl = selectedPicUrl;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
