package com.park61.teacherhelper.module.my.bean;

import java.util.List;

/**
 * Created by shubei on 2018/8/15.
 */

public class InterestingClassActBean {


    /**
     * id : 6
     * title : 提姆暑期兴趣班清爽来袭
     * detailUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170618/1497771725017088091.png
     * coverUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20180814150605900_312.jpg
     * summary : 我是摘要的估计撒大口径按时打卡斯柯达~！@#￥%……&*（）——+{}|“：？》《m
     * content : null
     * updateBy : null
     * interestClassList : null
     * status : 1
     */

    private int id;
    private String title;
    private String detailUrl;
    private String coverUrl;
    private String summary;
    private int status;//1：进行中，2：已结束
    private String content;
    private List<InterestingCourseBean> interestClassList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<InterestingCourseBean> getInterestClassList() {
        return interestClassList;
    }

    public void setInterestClassList(List<InterestingCourseBean> interestClassList) {
        this.interestClassList = interestClassList;
    }
}
