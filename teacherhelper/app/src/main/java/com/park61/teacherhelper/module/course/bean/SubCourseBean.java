package com.park61.teacherhelper.module.course.bean;

/**
 * Created by chenlie on 2018/3/23.
 * <p>
 * 专家课程 课时列表bean
 */

public class SubCourseBean {

    //详情
    private String content;
    //列表封面图
    private String coverImg;
    private String createBy;
    private String createDate;
    private String delFlag;
    private String detailImg;
    //课时id
    private int id;
    //0不可试读;1可试读
    private int isProbation;
    private int learningNum;
    private int putawayStatus;
    //0,待审核;1,审核通过;-1,驳回
    private int status;
    private String stringUpdateDate;
    //摘要
    private String summary;
    private String title;
    private String updateBy;
    private String updateDate;
    private int type;
    private int viewNum;
    private int trainerCourseSeriesId;
    private SubCourseContent trainerCourseResource;
    private int isInterest;// 0：不是兴趣课1：兴趣课

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public void setDetailImg(String detailImg) {
        this.detailImg = detailImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsProbation() {
        return isProbation;
    }

    public void setIsProbation(int isProbation) {
        this.isProbation = isProbation;
    }

    public int getLearningNum() {
        return learningNum;
    }

    public void setLearningNum(int learningNum) {
        this.learningNum = learningNum;
    }

    public int getPutawayStatus() {
        return putawayStatus;
    }

    public void setPutawayStatus(int putawayStatus) {
        this.putawayStatus = putawayStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStringUpdateDate() {
        return stringUpdateDate;
    }

    public void setStringUpdateDate(String stringUpdateDate) {
        this.stringUpdateDate = stringUpdateDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTrainerCourseSeriesId() {
        return trainerCourseSeriesId;
    }

    public void setTrainerCourseSeriesId(int trainerCourseSeriesId) {
        this.trainerCourseSeriesId = trainerCourseSeriesId;
    }

    public SubCourseContent getTrainerCourseResource() {
        return trainerCourseResource;
    }

    public void setTrainerCourseResource(SubCourseContent trainerCourseResource) {
        this.trainerCourseResource = trainerCourseResource;
    }

    public int getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(int isInterest) {
        this.isInterest = isInterest;
    }

    /**
     * "createBy": 1,
     * "createDate": "2018-03-20 16:04:00",
     * "delFlag": "0",
     * "id": 1,
     * "name": "test",
     * "size": 0,
     * "time": 0,
     * "trainerCourseId": 1,
     * "type": 3, 资源类型[1视频2音频3图片4office9其他]
     * "updateBy": 1,
     * "updateDate": "2018-03-20 16:02:57",
     * "url": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20171016155709008_224.png",
     * "viewNum": 0
     */
     public class SubCourseContent {
        //创建人id
        private int createBy;
        private String createDate;
        private String delFlag;
        private int id;
        private String name;
        private int size;
        private int time;
        private int trainerCourseId;
        private int type;
        private int updateBy;
        private String updateDate;
        private String url;
        private String videoPlayAuth;
        private int viewNum;
        private String updateTime;

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getTrainerCourseId() {
            return trainerCourseId;
        }

        public void setTrainerCourseId(int trainerCourseId) {
            this.trainerCourseId = trainerCourseId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(int updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVideoPlayAuth() {
            return videoPlayAuth;
        }

        public void setVideoPlayAuth(String videoPlayAuth) {
            this.videoPlayAuth = videoPlayAuth;
        }

        public int getViewNum() {
            return viewNum;
        }

        public void setViewNum(int viewNum) {
            this.viewNum = viewNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
