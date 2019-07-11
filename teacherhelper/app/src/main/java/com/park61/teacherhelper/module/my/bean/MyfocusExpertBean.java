package com.park61.teacherhelper.module.my.bean;

/**
 * 我关注的专家bean
 * Created by shubei on 2018/3/28.
 */

public class MyfocusExpertBean {

    private int id;
    private String name;
    private String description;
    private String headPictureUrl;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }
}
