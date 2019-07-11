package com.park61.teacherhelper.module.home.bean;

/**
 * Created by shubei on 2017/8/15.
 */

public class CourseBean {

    private String name;
    private String img;
    private String desc;

    public CourseBean() {
    }

    public CourseBean(String name, String img, String desc) {
        this.name = name;
        this.img = img;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
