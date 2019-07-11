package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by shubei on 2018/4/28.
 */

public class TaskLogBean {


    /**
     * userName : 崔夏青
     * taskCalendarId : 2
     * type : 0
     * id : 1
     * updateDate : 2018-04-26 14:36:19
     * intro : 1
     * createDate : 2018-04-26 14:36:19
     * createDateStr : 3分钟之前
     * createBy : 168
     * userId : 1
     * delFlag : 0
     * updateBy : 168
     */

    private String userName;
    private int taskCalendarId;
    private int type;//0日志完成,1留言,2日志重启,3任务共享,4任务调整
    private int id;
    private String updateDate;
    private String intro;
    private String createDate;
    private String createDateStr;
    private int createBy;
    private int userId;
    private int delFlag;
    private int updateBy;
    private String pictureUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTaskCalendarId() {
        return taskCalendarId;
    }

    public void setTaskCalendarId(int taskCalendarId) {
        this.taskCalendarId = taskCalendarId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
