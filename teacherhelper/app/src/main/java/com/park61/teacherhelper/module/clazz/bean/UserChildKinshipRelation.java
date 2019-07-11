package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;

/**
 * Created by zhangchi on 2017/8/21.
 */

public class UserChildKinshipRelation implements Serializable {
    private int id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 关系名称
     */
    private String relationConstantName;
    /**
     * 手机号
     */
    private String userMobile;
    /**
     * 是否删除
     */
    private Integer isDelete;
    private String pictureUrl;//图片链接
    private String kinship;

    public String getRelationConstantName() {
        return relationConstantName;
    }

    public void setRelationConstantName(String relationConstantName) {
        this.relationConstantName = relationConstantName;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKinship() {
        return kinship;
    }

    public void setKinship(String kinship) {
        this.kinship = kinship;
    }
}
