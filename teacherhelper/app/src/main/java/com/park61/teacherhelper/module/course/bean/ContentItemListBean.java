package com.park61.teacherhelper.module.course.bean;

import java.util.List;

/**
 * Created by nieyu on 2017/10/14.
 */

public class ContentItemListBean {

    private  int contentId;
    private int createBy;
    private String createDate;
    private String delFlag;
    private int id;
    private String name;
    private int updateBy;
    private String updateDate;
    private List<ContentSourseBean>contentSourceList;

    public String getCreateDate() {
        return createDate;
    }

    public int getContentId() {
        return contentId;
    }

    public int getCreateBy() {
        return createBy;
    }

    public int getId() {
        return id;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public List<ContentSourseBean> getContentSourceList() {
        return contentSourceList;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getName() {
        return name;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setContentSourceList(List<ContentSourseBean> contentSourceList) {
        this.contentSourceList = contentSourceList;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
