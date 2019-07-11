package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by shubei on 2018/7/27.
 */

public class TempTaskBean {


    /**
     * categoryName : 分类名称
     * id : 1
     * title : 任务1
     * type : 0
     */

    private String categoryName;
    private int id;
    private String title;
    private int type;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
