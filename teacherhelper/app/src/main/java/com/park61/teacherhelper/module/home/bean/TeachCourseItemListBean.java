package com.park61.teacherhelper.module.home.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubei on 2017/8/17.
 */

public class TeachCourseItemListBean {
    /**
     * id : 146
     * teachCourseId : 106
     * name : 1图片
     * sortName : null
     * delFlag : 0
     * createTime : 2017-08-05 21:28:08
     * createBy : 1
     * updateTime : 2017-08-05 21:28:08
     * updateBy : 1
     * bei1 : null
     * bei2 : null
     * bei3 : null
     * teachCourseSourceList : [{"id":214,"teachCourseId":106,"teachCourseItemId":146,"sourceName":"bd3eb13533fa828b1d0546eafa1f4134970a5a91.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212345601_871.jpg","videoImg":"","sourceType":2,"sourceSize":38059,"sourceTime":41,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-09 17:24:50","updateBy":"1","playAuth":null}]
     */

    private int id;
    private int teachCourseId;
    private String name;
    private Object sortName;
    private String delFlag;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private Object bei1;
    private Object bei2;
    private Object bei3;
    private List<TeachCourseSourceListBean> teachCourseSourceList;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeachCourseId() {
        return teachCourseId;
    }

    public void setTeachCourseId(int teachCourseId) {
        this.teachCourseId = teachCourseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameX) {
        this.name = nameX;
    }

    public Object getSortName() {
        return sortName;
    }

    public void setSortName(Object sortName) {
        this.sortName = sortName;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Object getBei1() {
        return bei1;
    }

    public void setBei1(Object bei1) {
        this.bei1 = bei1;
    }

    public Object getBei2() {
        return bei2;
    }

    public void setBei2(Object bei2) {
        this.bei2 = bei2;
    }

    public Object getBei3() {
        return bei3;
    }

    public void setBei3(Object bei3) {
        this.bei3 = bei3;
    }

    public List<TeachCourseSourceListBean> getTeachCourseSourceList() {
        return teachCourseSourceList;
    }

    public void setTeachCourseSourceList(List<TeachCourseSourceListBean> teachCourseSourceList) {
        this.teachCourseSourceList = teachCourseSourceList;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
