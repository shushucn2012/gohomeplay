package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/2.
 */

public class TopIndacaterBean implements Serializable {

private int createBy;
    private String createDate;
    private String linkData;
    private String linkPic;
    private int linkType; //linkType 字段的说明如下，请各位惠存：链接类型 1：外部链接 2：APP内链 3：搜索页面（可对应定位分类） 4：峰会活动详情 5：具体内容 6：培训活动 7：引用模块 8：引用页面
    private int selected;
    private String selectedPicUrl;
    private int updateBy;
    private String updateDate;

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

    public String getLinkData() {
        return linkData;
    }

    public String getLinkPic() {
        return linkPic;
    }

    public String getSelectedPicUrl() {
        return selectedPicUrl;
    }

    public String getUpdateDate() {
        return updateDate;
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

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setSelectedPicUrl(String selectedPicUrl) {
        this.selectedPicUrl = selectedPicUrl;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
