package com.park61.teacherhelper.module.home.bean;

/**
 * Created by shubei on 2017/12/15.
 */

public class AdvertsieBean {


    /**
     * id : 14
     * code : androidStartAdPage
     * title : 贼标准的贼厉害的贼牛逼的15字
     * picUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20171215105709908_334.png?x-oss-process=style/compress_nologo
     * linkData :
     * linkType : 1
     * linkUrl : http://www.baidu.com
     */

    private int id;
    private String code;
    private String title;
    private String picUrl;
    private String linkData;
    private int linkType;
    private String linkUrl;
    private String picLocalPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLinkData() {
        return linkData;
    }

    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPicLocalPath() {
        return picLocalPath;
    }

    public void setPicLocalPath(String picLocalPath) {
        this.picLocalPath = picLocalPath;
    }
}
