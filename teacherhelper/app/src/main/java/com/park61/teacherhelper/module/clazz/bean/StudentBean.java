package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;

public class StudentBean implements Serializable {

    private int gClassId;
    private int userChildId;
    private String name;
    private String pictureUrl;
    private boolean isChecked;

    public int getgClassId() {
        return gClassId;
    }

    public void setgClassId(int gClassId) {
        this.gClassId = gClassId;
    }

    public int getUserChildId() {
        return userChildId;
    }

    public void setUserChildId(int userChildId) {
        this.userChildId = userChildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
