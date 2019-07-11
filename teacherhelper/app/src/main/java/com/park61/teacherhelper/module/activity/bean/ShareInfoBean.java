package com.park61.teacherhelper.module.activity.bean;

/**
 * 分享信息数据bean
 * Created by shubei on 2018/9/4.
 */

public class ShareInfoBean {

    public ShareInfoBean() {
    }

    public ShareInfoBean(String shareUrl, String picUrl, String title, String description) {
        this.shareUrl = shareUrl;
        this.picUrl = picUrl;
        this.title = title;
        this.description = description;
    }

    private String shareUrl;
    private String picUrl;
    private String title;
    private String description;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
