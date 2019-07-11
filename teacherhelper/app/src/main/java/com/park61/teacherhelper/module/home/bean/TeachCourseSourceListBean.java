package com.park61.teacherhelper.module.home.bean;

/**
 * Created by shubei on 2017/8/17.
 */

public class TeachCourseSourceListBean {
    /**
     * id : 214
     * teachCourseId : 106
     * teachCourseItemId : 146
     * sourceName : bd3eb13533fa828b1d0546eafa1f4134970a5a91.jpg
     * type : 1
     * sourceUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212345601_871.jpg
     * videoImg :
     * sourceType : 2
     * sourceSize : 38059
     * sourceTime : 41
     * userViewNum : 0
     * delFlag : 0
     * remarks : null
     * createTime : 2017-08-05 21:28:08
     * createBy : 1
     * updateTime : 2017-08-09 17:24:50
     * updateBy : 1
     * playAuth : null
     */

    private int id;
    private int teachCourseId;
    private int teachCourseItemId;
    private String sourceName;
    private int type;
    private String sourceUrl;
    private String videoImg;
    private int sourceType;//1，视频；2，图片；3，音频；4，pdf
    private int sourceSize;
    private String sourceTime;
    private int userViewNum;
    private String delFlag;
    private String remarks;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private String playAuth;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeachCourseId() {
        return teachCourseId;
    }

    public void setTeachCourseId(int teachCourseId) {
        this.teachCourseId = teachCourseId;
    }

    public int getTeachCourseItemId() {
        return teachCourseItemId;
    }

    public void setTeachCourseItemId(int teachCourseItemId) {
        this.teachCourseItemId = teachCourseItemId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getSourceSize() {
        return sourceSize;
    }

    public void setSourceSize(int sourceSize) {
        this.sourceSize = sourceSize;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public int getUserViewNum() {
        return userViewNum;
    }

    public void setUserViewNum(int userViewNum) {
        this.userViewNum = userViewNum;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getPlayAuth() {
        return playAuth;
    }

    public void setPlayAuth(String playAuth) {
        this.playAuth = playAuth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
