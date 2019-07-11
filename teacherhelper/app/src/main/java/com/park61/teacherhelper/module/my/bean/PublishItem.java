package com.park61.teacherhelper.module.my.bean;

import java.util.List;

/**
 * Created by shubei on 2017/8/24.
 */

public class PublishItem {


    /**
     * id : 1
     * userId : 8
     * content : null
     * createTime : null
     * createBy : null
     * updateTime : null
     * updateBy : null
     * delFlag : 0
     * classIds : null
     * urlStr : null
     * userName : 爱丽尔
     * userPictureUrl :
     * className : 哈弗神童
     * teachIssuePhotoSourceList : [{"id":1,"issueId":1,"imageUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115307835_521.jpg","createTime":"2017-08-16 19:16:12","createBy":8,"updateTime":"2017-08-16 19:16:13","updateBy":8,"delFlag":"0"},{"id":2,"issueId":1,"imageUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115823111_789.mp3","createTime":"2017-08-16 19:16:20","createBy":8,"updateTime":"2017-08-16 19:16:21","updateBy":8,"delFlag":"0"}]
     */

    private int id;
    private int userId;
    private String title;
    private String content;
    private String createTime;
    private String showDate;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private String delFlag;
    private String classIds;
    private String urlStr;
    private String userName;
    private String userPictureUrl;
    private String className;
    private List<TeachIssuePhotoSourceListBean> teachIssuePhotoSourceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getClassIds() {
        return classIds;
    }

    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPictureUrl() {
        return userPictureUrl;
    }

    public void setUserPictureUrl(String userPictureUrl) {
        this.userPictureUrl = userPictureUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<TeachIssuePhotoSourceListBean> getTeachIssuePhotoSourceList() {
        return teachIssuePhotoSourceList;
    }

    public void setTeachIssuePhotoSourceList(List<TeachIssuePhotoSourceListBean> teachIssuePhotoSourceList) {
        this.teachIssuePhotoSourceList = teachIssuePhotoSourceList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public static class TeachIssuePhotoSourceListBean {
        /**
         * id : 1
         * issueId : 1
         * imageUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115307835_521.jpg
         * createTime : 2017-08-16 19:16:12
         * createBy : 8
         * updateTime : 2017-08-16 19:16:13
         * updateBy : 8
         * delFlag : 0
         */

        private int id;
        private int issueId;
        private String imageUrl;
        private String createTime;
        private int createBy;
        private String updateTime;
        private int updateBy;
        private String delFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIssueId() {
            return issueId;
        }

        public void setIssueId(int issueId) {
            this.issueId = issueId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(int updateBy) {
            this.updateBy = updateBy;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }
    }
}
