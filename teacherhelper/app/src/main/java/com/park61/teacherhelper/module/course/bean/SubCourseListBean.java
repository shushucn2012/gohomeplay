package com.park61.teacherhelper.module.course.bean;

/**
 * Created by chenlie on 2018/3/23.
 * <p>
 * 专家课程 课时列表bean
 */

public class SubCourseListBean {

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
    private int trainerCourseResource;
    private int trainerCourseSeriesId;
    private boolean buyStatus;

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

    public int getTrainerCourseResource() {
        return trainerCourseResource;
    }

    public void setTrainerCourseResource(int trainerCourseResource) {
        this.trainerCourseResource = trainerCourseResource;
    }

    public int getTrainerCourseSeriesId() {
        return trainerCourseSeriesId;
    }

    public void setTrainerCourseSeriesId(int trainerCourseSeriesId) {
        this.trainerCourseSeriesId = trainerCourseSeriesId;
    }

    public boolean isBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(boolean buyStatus) {
        this.buyStatus = buyStatus;
    }
}
