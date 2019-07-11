package com.park61.teacherhelper.module.workplan.bean;

import java.util.List;

public class MySubWorkPlanCateBean {

    private String categoryName;
    private String categoryUrl;
    private List<TempBean> subscribeTaskCalendars;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public List<TempBean> getSubscribeTaskCalendars() {
        return subscribeTaskCalendars;
    }

    public void setSubscribeTaskCalendars(List<TempBean> subscribeTaskCalendars) {
        this.subscribeTaskCalendars = subscribeTaskCalendars;
    }
}
