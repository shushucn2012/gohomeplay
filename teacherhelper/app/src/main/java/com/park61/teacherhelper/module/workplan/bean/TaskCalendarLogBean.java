package com.park61.teacherhelper.module.workplan.bean;

public class TaskCalendarLogBean {
    /**
     * showUpdateDate : 31分钟之前
     * type : 0
     * intro : 任务完成
     * userName : 崔夏青
     */

    private String showUpdateDate;
    private int type;
    private String intro;
    private String userName;

    public String getShowUpdateDate() {
        return showUpdateDate;
    }

    public void setShowUpdateDate(String showUpdateDate) {
        this.showUpdateDate = showUpdateDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}