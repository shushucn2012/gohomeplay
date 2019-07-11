package com.park61.teacherhelper.module.course.bean;

/**
 * Created by shubei on 2017/8/23.
 */

public class StudyRecordBean {


    /**
     * id : 71
     * courseId : 120
     * courseName : 大视频
     * courseAuthor : 呼呼
     * userId : 11
     * createDate : 2017-08-23 15:35:46
     * updateDate : null
     * createBy : null
     * updateBy : null
     * delFlag : 0
     * coverUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170817180139607_389.jpg
     * viewNum : 3
     */

    private int id;
    private int courseId;
    private String courseName;
    private String courseAuthor;
    private int userId;
    private String createDate;
    private String updateDate;
    private String createBy;
    private String updateBy;
    private String delFlag;
    private String coverUrl;
    private int viewNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseAuthor() {
        return courseAuthor;
    }

    public void setCourseAuthor(String courseAuthor) {
        this.courseAuthor = courseAuthor;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }
}
