package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by shubei on 2018/6/30.
 */

public class TaskLevelBean {

    public TaskLevelBean(){

    }

    public TaskLevelBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;//类目id
    private String name;//分类名称

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
}
