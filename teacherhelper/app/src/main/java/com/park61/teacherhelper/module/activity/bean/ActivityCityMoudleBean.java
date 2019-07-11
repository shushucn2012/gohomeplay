package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class ActivityCityMoudleBean implements Serializable {
    private List<ActivityCityTempBean>modules;
   private  ActivityCityColorBean pageProperties;
    public List<ActivityCityTempBean> getModules() {
        return modules;
    }

    public void setModules(List<ActivityCityTempBean> modules) {
        this.modules = modules;
    }

    public ActivityCityColorBean getPageProperties() {
        return pageProperties;
    }

    public void setPageProperties(ActivityCityColorBean pageProperties) {
        this.pageProperties = pageProperties;
    }
}
