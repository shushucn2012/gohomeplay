package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class TopBanerListBean implements Serializable {

    private String moduleName;
    private String templeteCode;
    private List<BannerListDataBean>templeteData;

    public List<BannerListDataBean> getTempleteData() {
        return templeteData;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getTempleteCode() {
        return templeteCode;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setTempleteCode(String templeteCode) {
        this.templeteCode = templeteCode;
    }

    public void setTempleteData(List<BannerListDataBean> templeteData) {
        this.templeteData = templeteData;
    }
}
