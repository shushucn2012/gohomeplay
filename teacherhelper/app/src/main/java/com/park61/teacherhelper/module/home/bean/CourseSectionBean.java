package com.park61.teacherhelper.module.home.bean;

import com.google.gson.annotations.SerializedName;
import com.park61.teacherhelper.module.course.bean.ContentItemListBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2017/8/15.
 */

public class CourseSectionBean implements Serializable{
    /**
     * id : 106
     * code : 0106
     * summary : 的萨芬大好时光反攻倒算可交付刚开始几点几分
     * typeDictId : 1-3
     * targetDictId : 3-1
     * coverUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170805212805135_38.png
     * description : <p><img src="http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170805/1501939351680069163.png?x-oss-process=style/logo_img" title="1501939351680069163.png" alt="image.png"/></p>
     * status : 2
     * copyright : 哦靠
     * evaluateValue : null
     * viewNum : null
     * delFlag : 0
     * remarks : null
     * createTime : 2017-08-05 21:28:08
     * createBy : 1
     * updateTime : 2017-08-05 21:31:05
     * updateBy : 1
     * rejectCause : null
     * rejectTime : null
     * rejectBy : null
     * teachCourseItemList : [{"id":146,"teachCourseId":106,"name":"1图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":214,"teachCourseId":106,"teachCourseItemId":146,"sourceName":"bd3eb13533fa828b1d0546eafa1f4134970a5a91.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212345601_871.jpg","videoImg":"","sourceType":2,"sourceSize":38059,"sourceTime":41,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-09 17:24:50","updateBy":"1","playAuth":null}]},{"id":147,"teachCourseId":106,"name":"2mp4","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":215,"teachCourseId":106,"teachCourseItemId":147,"sourceName":"IMG_4638.MP4","type":1,"sourceUrl":"675b02acf626460c85c2047a6457b924","videoImg":"","sourceType":1,"sourceSize":8452805,"sourceTime":41,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-09 17:24:37","updateBy":"1","playAuth":"eyJTZWN1cml0eVRva2VuIjoiQ0FJU3VBSjFxNkZ0NUIyeWZTaklyWkRERHZ2QWw3NUkvYWpaUlJUVm5GY3NRYlprMTZEU3RUejJJSHBLZVhkdUFlQVhzL28wbW1oWjcvWVlsck1xRU1jWUdCMmVNSlFvdGNvSHFscjVKcExGc3QySjZyOEpqc1ZLdXF3RnZWMnBzdlhKYXNEVkVmbkFHSjcwR1VpbSt3WjN4YnpsRHh2QU8zV3VMWnlPajdOK2M5MFRSWFBXUkRGYUJkQlFWRTBBenNvR0xpbnBNZis4UHdURG4zYlFiaTV0cGhFdXNXZDIrSVc1ek1yK2pCL0Nsdy9XeCtRUHRwenRBWlcxRmI0T1dxMXlTTkNveHVkN1c3UGMyU3BMa1hodytieHhrYlpQOUVXczNMamZJU0VJczByZGJiV0VyNFV4ZFZVbFB2VmxJY01lOHFpZ3o4OGZrL2ZJaW9INnh5eEtPZXhvU0NuRlRPaWl1cENlUnI3M2FJNXBKT3VsWUNxVmc0bmZiSU9KbWdjbGNHOGRDZ1JHUU5Nb01VUnNFaVlyVGp6SzJ3RlFQcVFob0d3YWdBRkc2dmgzd0FRTDVodEhjWWZUNGs5RjdWQ3p6YTF6WTNMRkZYNlQ5Q1hrR3g1QlhPSE1VVFpEV0FUVTE3Tmxtb1FLbDI1MTlQVFFkZXRxWHU5cEgwRUxib3JES1ZSOXh2OVJrNWpNVjVUZng0WEtZeVJrOEtxOGJneGZMKzRTZzNSWWZNaFppQlo2WmxEUWtpeDkvSzRONmNjM2ovdDI0aThTcHpWNXp0SGtidz09IiwiQXV0aEluZm8iOiJ7XCJFeHBpcmVUaW1lXCI6XCIyMDE3LTA4LTE3VDA5OjQ2OjQ4WlwiLFwiTWVkaWFJZFwiOlwiNjc1YjAyYWNmNjI2NDYwYzg1YzIwNDdhNjQ1N2I5MjRcIixcIlNpZ25hdHVyZVwiOlwiSTVBSmd6TnIrNmJ6SStJMDNqenlMa004VStBPVwifSIsIlZpZGVvTWV0YSI6eyJTdGF0dXMiOiJOb3JtYWwiLCJWaWRlb0lkIjoiNjc1YjAyYWNmNjI2NDYwYzg1YzIwNDdhNjQ1N2I5MjQiLCJUaXRsZSI6IuW4iOiurSIsIkNvdmVyVVJMIjoiaHR0cDovL3ZpZGVvLmJlaW1hLm5ldC9zbmFwc2hvdC82NzViMDJhY2Y2MjY0NjBjODVjMjA0N2E2NDU3YjkyNDAwMDAxLmpwZz9hdXRoX2tleT0xNTAyOTY2NzA4LTAtMC1mYzFlM2I1NzExZDEwZGExMjIyOWU3NzJmMjFhM2Q2ZSIsIkR1cmF0aW9uIjo0MS44M30sIkFjY2Vzc0tleUlkIjoiU1RTLkxTdkVBdHphaUpqMkcyZHhXeU05SDhleVciLCJQbGF5RG9tYWluIjoidmlkZW8uYmVpbWEubmV0IiwiQWNjZXNzS2V5U2VjcmV0IjoiRHFBNTNkRnN2em1BMmVGZXdZb1ZSeGVSYlVHdG9YaTU1bVBaOGlBUGJRdUYiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSIsIkN1c3RvbWVySWQiOjEzMDU1OTI1NzAzNjgzMTB9"}]},{"id":148,"teachCourseId":106,"name":"3MP4","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":216,"teachCourseId":106,"teachCourseItemId":148,"sourceName":"IMG_4638.MP4","type":1,"sourceUrl":"755456aac2404a18916551b72a279a99","videoImg":"","sourceType":1,"sourceSize":8452805,"sourceTime":41,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-09 17:24:40","updateBy":"1","playAuth":"eyJTZWN1cml0eVRva2VuIjoiQ0FJU3VBSjFxNkZ0NUIyeWZTaklyWkRERHZ2QWw3NUkvYWpaUlJUVm5GY3NRYlprMTZEU3RUejJJSHBLZVhkdUFlQVhzL28wbW1oWjcvWVlsck1xRU1jWUdCMmVNSlFvdGNvSHFscjVKcExGc3QySjZyOEpqc1ZLdXF3RnZWMnBzdlhKYXNEVkVmbkFHSjcwR1VpbSt3WjN4YnpsRHh2QU8zV3VMWnlPajdOK2M5MFRSWFBXUkRGYUJkQlFWRTBBenNvR0xpbnBNZis4UHdURG4zYlFiaTV0cGhFdXNXZDIrSVc1ek1yK2pCL0Nsdy9XeCtRUHRwenRBWlcxRmI0T1dxMXlTTkNveHVkN1c3UGMyU3BMa1hodytieHhrYlpQOUVXczNMamZJU0VJczByZGJiV0VyNFV4ZFZVbFB2VmxJY01lOHFpZ3o4OGZrL2ZJaW9INnh5eEtPZXhvU0NuRlRPaWl1cENlUnI3M2FJNXBKT3VsWUNxVmc0bmZiSU9KbWdjbGNHOGRDZ1JHUU5Nb01VUnNFaVlyVGp6SzJ3RlFQcVFob0d3YWdBRkc2dmgzd0FRTDVodEhjWWZUNGs5RjdWQ3p6YTF6WTNMRkZYNlQ5Q1hrR3g1QlhPSE1VVFpEV0FUVTE3Tmxtb1FLbDI1MTlQVFFkZXRxWHU5cEgwRUxib3JES1ZSOXh2OVJrNWpNVjVUZng0WEtZeVJrOEtxOGJneGZMKzRTZzNSWWZNaFppQlo2WmxEUWtpeDkvSzRONmNjM2ovdDI0aThTcHpWNXp0SGtidz09IiwiQXV0aEluZm8iOiJ7XCJFeHBpcmVUaW1lXCI6XCIyMDE3LTA4LTE3VDA5OjQ2OjQ4WlwiLFwiTWVkaWFJZFwiOlwiNzU1NDU2YWFjMjQwNGExODkxNjU1MWI3MmEyNzlhOTlcIixcIlNpZ25hdHVyZVwiOlwieHdjdTlQc1RlNWxxMVRGY0gzQ2Q0OGttQU9jPVwifSIsIlZpZGVvTWV0YSI6eyJTdGF0dXMiOiJOb3JtYWwiLCJWaWRlb0lkIjoiNzU1NDU2YWFjMjQwNGExODkxNjU1MWI3MmEyNzlhOTkiLCJUaXRsZSI6IuW4iOiurSIsIkNvdmVyVVJMIjoiaHR0cDovL3ZpZGVvLmJlaW1hLm5ldC9zbmFwc2hvdC83NTU0NTZhYWMyNDA0YTE4OTE2NTUxYjcyYTI3OWE5OTAwMDAxLmpwZz9hdXRoX2tleT0xNTAyOTY2NzA4LTAtMC1hMzRjYTlkZTkzNjc2OTZjMGUzZjgwN2Y1YzdmY2ViOSIsIkR1cmF0aW9uIjo0MS44M30sIkFjY2Vzc0tleUlkIjoiU1RTLkxTdkVBdHphaUpqMkcyZHhXeU05SDhleVciLCJQbGF5RG9tYWluIjoidmlkZW8uYmVpbWEubmV0IiwiQWNjZXNzS2V5U2VjcmV0IjoiRHFBNTNkRnN2em1BMmVGZXdZb1ZSeGVSYlVHdG9YaTU1bVBaOGlBUGJRdUYiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSIsIkN1c3RvbWVySWQiOjEzMDU1OTI1NzAzNjgzMTB9"}]},{"id":149,"teachCourseId":106,"name":"4图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":217,"teachCourseId":106,"teachCourseItemId":149,"sourceName":"d8f9d72a6059252d882dc60d339b033b5bb5b985.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212634872_83.jpg","videoImg":"","sourceType":2,"sourceSize":44753,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":150,"teachCourseId":106,"name":"5图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":218,"teachCourseId":106,"teachCourseItemId":150,"sourceName":"d8f9d72a6059252d882dc60d339b033b5bb5b985.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212649650_417.jpg","videoImg":"","sourceType":2,"sourceSize":44753,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":151,"teachCourseId":106,"name":"6图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":219,"teachCourseId":106,"teachCourseItemId":151,"sourceName":"377adab44aed2e7397205e258001a18b86d6faea.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212715910_138.jpg","videoImg":"","sourceType":2,"sourceSize":112527,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":152,"teachCourseId":106,"name":"7图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":220,"teachCourseId":106,"teachCourseItemId":152,"sourceName":"960a304e251f95ca136148bfce177f3e67095227.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212722900_118.jpg","videoImg":"","sourceType":2,"sourceSize":64245,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":153,"teachCourseId":106,"name":"8图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":221,"teachCourseId":106,"teachCourseItemId":153,"sourceName":"377adab44aed2e7397205e258001a18b86d6faea.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212727980_429.jpg","videoImg":"","sourceType":2,"sourceSize":112527,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":154,"teachCourseId":106,"name":"9图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":222,"teachCourseId":106,"teachCourseItemId":154,"sourceName":"默认标题_多图文公众号首图_2017.08.04 (1).png","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212733803_544.png","videoImg":"","sourceType":2,"sourceSize":83412,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]},{"id":155,"teachCourseId":106,"name":"1o图片","sortName":null,"delFlag":"0","createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","bei1":null,"bei2":null,"bei3":null,"teachCourseSourceList":[{"id":223,"teachCourseId":106,"teachCourseItemId":155,"sourceName":"d8f9d72a6059252d882dc60d339b033b5bb5b985.jpg","type":1,"sourceUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170805212739029_966.jpg","videoImg":"","sourceType":2,"sourceSize":44753,"sourceTime":null,"userViewNum":0,"delFlag":"0","remarks":null,"createTime":"2017-08-05 21:28:08","createBy":"1","updateTime":"2017-08-05 21:28:08","updateBy":"1","playAuth":null}]}]
     * teachCourseSourceList : null
     * labelList : null
     * typeName : null
     */

    private int id;
    private int teachCourseId;
    private String code;
    private String name;
    private String summary;
    private String typeDictId;
    private String targetDictId;
    private String coverUrl;
    private String description;
    private int status;
    private String copyright;
    private String evaluateValue;
    private String viewNum;
    private String delFlag;
    private String remarks;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private String rejectCause;
    private String rejectTime;
    private String rejectBy;
    private List<TeachCourseSourceListBean> teachCourseSourceList;
    private List<String> labelList;
    private String typeName;
    private List<TeachCourseItemListBean> teachCourseItemList;
    private boolean isExpan;
    private String level1CateName;
    private String adaptAge;
    private int authorId;
    private int commentNum;
    private String content;
    private List<ContentItemListBean> contentItemList;
    private String coverImg;
    private String authorName;
    private String authorPic;
    private int contentType;//0图文，1视频, 2音频
    private String createDate;
    private int isFine;
    private int isFree;
    private int level1CateId;
    private int level2CateId;
    private int praiseNum;
    private int shareNum;
    private int sort;
    private String sortTime;
    private String title;
    private boolean isPraised;
    private boolean isCollect;
    private String showDate;
    private String intro;
    private String praiseNumDsc;
    private int teachActivityId;
    private String playTotalNum;
    private int focusNum;
    private String putawayStatus;
    private int isTag;//是否显示vip标签
    private int isView;//当前用户是否可以浏览内容
    private int viewCode;//可见范围1不是会员2不是指定组会员
    private int isMember;//用户可见权限

    public String getAdaptAge() {
        return adaptAge;
    }

    public void setAdaptAge(String adaptAge) {
        this.adaptAge = adaptAge;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getIsFine() {
        return isFine;
    }

    public void setIsFine(int isFine) {
        this.isFine = isFine;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getLevel1CateId() {
        return level1CateId;
    }

    public void setLevel1CateId(int level1CateId) {
        this.level1CateId = level1CateId;
    }

    public int getLevel2CateId() {
        return level2CateId;
    }

    public void setLevel2CateId(int level2CateId) {
        this.level2CateId = level2CateId;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSortTime() {
        return sortTime;
    }

    public void setSortTime(String sortTime) {
        this.sortTime = sortTime;
    }

    public Boolean getIsPraised() {
        return isPraised;
    }

    public void setIsPraised(Boolean isPraised) {
        isPraised = isPraised;
    }

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTypeDictId() {
        return typeDictId;
    }

    public void setTypeDictId(String typeDictId) {
        this.typeDictId = typeDictId;
    }

    public String getTargetDictId() {
        return targetDictId;
    }

    public void setTargetDictId(String targetDictId) {
        this.targetDictId = targetDictId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Object getEvaluateValue() {
        return evaluateValue;
    }

    public void setEvaluateValue(String evaluateValue) {
        this.evaluateValue = evaluateValue;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Object getRejectCause() {
        return rejectCause;
    }

    public void setRejectCause(String rejectCause) {
        this.rejectCause = rejectCause;
    }

    public Object getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(String rejectTime) {
        this.rejectTime = rejectTime;
    }

    public Object getRejectBy() {
        return rejectBy;
    }

    public void setRejectBy(String rejectBy) {
        this.rejectBy = rejectBy;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<TeachCourseItemListBean> getTeachCourseItemList() {
        return teachCourseItemList;
    }

    public void setTeachCourseItemList(List<TeachCourseItemListBean> teachCourseItemList) {
        this.teachCourseItemList = teachCourseItemList;
    }


    public List<TeachCourseSourceListBean> getTeachCourseSourceList() {
        return teachCourseSourceList;
    }

    public void setTeachCourseSourceList(List<TeachCourseSourceListBean> teachCourseSourceList) {
        this.teachCourseSourceList = teachCourseSourceList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpan() {
        return isExpan;
    }

    public void setExpan(boolean expan) {
        isExpan = expan;
    }

    public int getTeachCourseId() {
        return teachCourseId;
    }

    public void setTeachCourseId(int teachCourseId) {
        this.teachCourseId = teachCourseId;
    }

    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPic() {
        return authorPic;
    }

    public void setAuthorPic(String authorPic) {
        this.authorPic = authorPic;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<ContentItemListBean> getContentItemList() {
        return contentItemList;
    }

    public void setContentItemList(List<ContentItemListBean> contentItemList) {
        this.contentItemList = contentItemList;
    }

    public String getLevel1CateName() {
        return level1CateName;
    }

    public void setLevel1CateName(String level1CateName) {
        this.level1CateName = level1CateName;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPraiseNumDsc() {
        return praiseNumDsc;
    }

    public void setPraiseNumDsc(String praiseNumDsc) {
        this.praiseNumDsc = praiseNumDsc;
    }

    public int getTeachActivityId() {
        return teachActivityId;
    }

    public void setTeachActivityId(int teachActivityId) {
        this.teachActivityId = teachActivityId;
    }

    public String getPlayTotalNum() {
        return playTotalNum;
    }

    public void setPlayTotalNum(String playTotalNum) {
        this.playTotalNum = playTotalNum;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public String getPutawayStatus() {
        return putawayStatus;
    }

    public void setPutawayStatus(String putawayStatus) {
        this.putawayStatus = putawayStatus;
    }

    public int getIsView() {
        return isView;
    }

    public void setIsView(int isView) {
        this.isView = isView;
    }

    public int getIsTag() {
        return isTag;
    }

    public void setIsTag(int isTag) {
        this.isTag = isTag;
    }

    public int getViewCode() {
        return viewCode;
    }

    public void setViewCode(int viewCode) {
        this.viewCode = viewCode;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }
}
