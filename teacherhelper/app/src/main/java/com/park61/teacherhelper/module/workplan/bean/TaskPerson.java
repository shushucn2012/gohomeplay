package com.park61.teacherhelper.module.workplan.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2018/7/2.
 */

public class TaskPerson implements Serializable{

    private int id;
    private int userId;
    private String name;
    private String dutyName;
    private String pictureUrl;
    private boolean isChecked;
    private String[] className;
    private String mobile;

    public TaskPerson() {
    }

    public TaskPerson(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String[] getClassName() {
        return className;
    }

    public void setClassName(String[] className) {
        this.className = className;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
