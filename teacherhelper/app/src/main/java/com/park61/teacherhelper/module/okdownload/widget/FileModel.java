package com.park61.teacherhelper.module.okdownload.widget;

import java.io.Serializable;

/**
 * Created by chenlie on 2018/2/24.
 *
 * 课件下载model
 */

public class FileModel implements Serializable{

    private static final long serialVersionUID = -4844921439922098889L;

    private String name;
    private String createTime;
    private String url;
    private String size;
    private String path;
    //iconUrl
    private String iconUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
