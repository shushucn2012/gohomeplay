package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/6.
 */

public class ActivityCityColorBean implements Serializable {
private String backgroundColor;
    private String backgroundUrl;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
}
