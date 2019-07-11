package com.park61.teacherhelper.module.clazz.bean;

/**
 * Created by chenlie on 2018/5/16.
 *
 */

public class IconBean {

//            "id": 1,
//            "imgUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20171120135232375_204.png",
//            "isFinished": 1, 0未完成，1已完成
//            "itemId": 1,
//            "name": "荣誉资质",
//            "sort": 1

    private int id;
    private String imgUrl;
    private int isFinished;
    private int itemId;
    private String name;
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
