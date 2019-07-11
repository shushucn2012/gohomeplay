package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/2.
 */

public class ActivityCityListBean implements Serializable {

    private String backgroundColor;
    private String backgroundColorActive;
    private String fontColor;
    private String fontColorActive;
    private Boolean hasActive;
    private int moduleId;
    private String name;


    public Boolean getHasActive() {
        return hasActive;
    }

    public int getModuleId() {
        return moduleId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundColorActive() {
        return backgroundColorActive;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getFontColorActive() {
        return fontColorActive;
    }

    public String getName() {
        return name;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColorActive(String backgroundColorActive) {
        this.backgroundColorActive = backgroundColorActive;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontColorActive(String fontColorActive) {
        this.fontColorActive = fontColorActive;
    }

    public void setHasActive(Boolean hasActive) {
        this.hasActive = hasActive;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
