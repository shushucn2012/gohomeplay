package com.park61.teacherhelper.module.okdownload.widget;

import java.io.Serializable;

/**
 * Created by chenlie on 2018/2/5.
 *
 */

public class SimpleTask implements Serializable {


    private static final long serialVersionUID = -8064363014518062342L;
    private String vid;
    private String title;
    private String size;
    private boolean isCheck = false;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
