package com.park61.teacherhelper.module.my.bean;

/**
 * Created by shubei on 2018/3/29.
 */

public class RecentlyItemBean {


    /**
     * id : null
     * trainerCourseId : 1
     * userId : null
     * name : 不才 - 参商(纯歌版)
     * type : 3
     * studyNum : 4
     * createBy : null
     * createDate : null
     * studyDate : 2018-03-27 14:52:59
     * studyTime : 03月27日 14:52
     * coverImg : http://thirdwx.qlogo.cn/mmopen/vi_32/msXibhSIFVLqGADeedDgOAqzdHY1fC495QAef0oM5xoROA1Np14QaU8REvS18MFs9636FJqiaofgDibE901QTXYVA/132
     */

    private int trainerCourseId;
    private String name;
    private int type;//资源类型[1视频2图片3音频4office9其他]
    private int studyNum;
    private String studyDate;
    private String studyTime;
    private String coverImg;
    private String title;

    public int getTrainerCourseId() {
        return trainerCourseId;
    }

    public void setTrainerCourseId(int trainerCourseId) {
        this.trainerCourseId = trainerCourseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStudyNum() {
        return studyNum;
    }

    public void setStudyNum(int studyNum) {
        this.studyNum = studyNum;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
