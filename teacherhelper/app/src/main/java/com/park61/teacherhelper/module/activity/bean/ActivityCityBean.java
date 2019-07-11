package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class ActivityCityBean implements Serializable {

    private String code;
    private int ext;
    private int msg;
    private ActivityCityMoudleBean data;

    public ActivityCityMoudleBean getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getExt() {
        return ext;
    }

    public int getMsg() {
        return msg;
    }

    public void setData(ActivityCityMoudleBean data) {
        this.data = data;
    }

    public void setExt(int ext) {
        this.ext = ext;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
}
