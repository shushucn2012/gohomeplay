package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class TopBannerBean implements Serializable {

    private TopBannerPageBean pageProperties;
    private List<TopBanerListBean> modules;
    private String pageName;

    public List<TopBanerListBean> getModules() {
        return modules;
    }

    public TopBannerPageBean getPageProperties() {
        return pageProperties;
    }

    public void setModules(List<TopBanerListBean> modules) {
        this.modules = modules;
    }

    public void setPageProperties(TopBannerPageBean pageProperties) {
        this.pageProperties = pageProperties;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
