package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2018/2/1.
 */

public class VideoItemSource implements Serializable {


    /**
     * sourceId : 49
     * title : 视频名称
     * time : 00:00
     * viewNum : 0
     * sourceSize : 922.89KB
     */

    private int sourceId;
    private String title;
    private String time;
    private String dateTime;
    private String viewNum;
    private String sourceSize;
    private boolean isPlaying;
    private String picUrl;
    private int status;//下载状态：-1未下载，0未完成, 1完成， 2下载中

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public String getSourceSize() {
        return sourceSize;
    }

    public void setSourceSize(String sourceSize) {
        this.sourceSize = sourceSize;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
