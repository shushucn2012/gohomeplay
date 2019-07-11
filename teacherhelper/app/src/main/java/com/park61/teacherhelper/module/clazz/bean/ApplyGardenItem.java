package com.park61.teacherhelper.module.clazz.bean;

import java.util.List;

/**
 * Created by shubei on 2018/5/28.
 */

public class ApplyGardenItem {


    /**
     * id : 312
     * applyId : 108
     * classifyId : 25
     * detail : 黑底
     * sourceType : 0
     * createDate : 2018-05-28 18:06:39
     * createBy : null
     * updateDate : null
     * updateBy : null
     * delFlag : 0
     * sourceList : [{"id":464,"applyId":108,"applyItemId":312,"source":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20180528_996152.jpg","videoImg":null,"sourceName":null,"sourceSize":null,"sourceTime":null,"createDate":"2018-05-28 18:06:39","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0"},{"id":465,"applyId":108,"applyItemId":312,"source":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20180528_663465.jpg","videoImg":null,"sourceName":null,"sourceSize":null,"sourceTime":null,"createDate":"2018-05-28 18:06:39","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0"}]
     * picList : null
     * videoId : null
     * classifyName : null
     */

    private int id;
    private int applyId;
    private int classifyId;
    private String detail;
    private int sourceType;
    private String createDate;
    private Object createBy;
    private Object updateDate;
    private Object updateBy;
    private String delFlag;
    private Object picList;
    private Object videoId;
    private Object classifyName;
    private List<SourceListBean> sourceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Object getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }

    public Object getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Object updateBy) {
        this.updateBy = updateBy;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Object getPicList() {
        return picList;
    }

    public void setPicList(Object picList) {
        this.picList = picList;
    }

    public Object getVideoId() {
        return videoId;
    }

    public void setVideoId(Object videoId) {
        this.videoId = videoId;
    }

    public Object getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(Object classifyName) {
        this.classifyName = classifyName;
    }

    public List<SourceListBean> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<SourceListBean> sourceList) {
        this.sourceList = sourceList;
    }

    public static class SourceListBean {
        /**
         * id : 464
         * applyId : 108
         * applyItemId : 312
         * source : http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20180528_996152.jpg
         * videoImg : null
         * sourceName : null
         * sourceSize : null
         * sourceTime : null
         * createDate : 2018-05-28 18:06:39
         * createBy : null
         * updateDate : null
         * updateBy : null
         * delFlag : 0
         */

        private int id;
        private int applyId;
        private int applyItemId;
        private String source;
        private String videoImg;
        private Object sourceName;
        private Object sourceSize;
        private Object sourceTime;
        private String createDate;
        private Object createBy;
        private Object updateDate;
        private Object updateBy;
        private String delFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getApplyId() {
            return applyId;
        }

        public void setApplyId(int applyId) {
            this.applyId = applyId;
        }

        public int getApplyItemId() {
            return applyItemId;
        }

        public void setApplyItemId(int applyItemId) {
            this.applyItemId = applyItemId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getVideoImg() {
            return videoImg;
        }

        public void setVideoImg(String videoImg) {
            this.videoImg = videoImg;
        }

        public Object getSourceName() {
            return sourceName;
        }

        public void setSourceName(Object sourceName) {
            this.sourceName = sourceName;
        }

        public Object getSourceSize() {
            return sourceSize;
        }

        public void setSourceSize(Object sourceSize) {
            this.sourceSize = sourceSize;
        }

        public Object getSourceTime() {
            return sourceTime;
        }

        public void setSourceTime(Object sourceTime) {
            this.sourceTime = sourceTime;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
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
