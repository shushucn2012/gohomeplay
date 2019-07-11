package com.park61.teacherhelper.module.my.bean;

/**
 * Created by chenlie on 2017/12/27.
 * <p>
 * 教师活动列表bean
 */

public class ActivityBean {

    /**
     * "classId": 122, 班级id
     * "className": "小小",
     * "coverUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20171016183207850_390.jpg",
     * "id": 1, 活动id
     * "name": "亲子游活动",
     * "schoolName": "测试1228",
     * "showStatus": 0,  状态（0：报名中，1：进行中，2：已结束, 3:未开始）
     * "startDateStr": "2017/12/28 周四 10:21"
     */

    private int classId;
    private String className;
    private String coverUrl;
    private int id;
    private String intro;
    private String name;
    private String schoolName;
    private int showStatus;
    private String showStatusName;
    private String startDateStr;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public String getShowStatusName() {
        return showStatusName;
    }

    public void setShowStatusName(String showStatusName) {
        this.showStatusName = showStatusName;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }
}
