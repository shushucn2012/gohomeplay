package com.park61.teacherhelper.module.workplan.bean;

import java.util.List;

public class WorkPlanCateBean {

    private int taskCalendarCategoryId;
    private String categoryName;
    private String name;
    private String categoryUrl;
    private String coverUrl;
    private String intro;
    private int putawayStatus;
    private List<TaskCalendarTemplatesBean> taskCalendarTemplates;

    public int getTaskCalendarCategoryId() {
        return taskCalendarCategoryId;
    }

    public void setTaskCalendarCategoryId(int taskCalendarCategoryId) {
        this.taskCalendarCategoryId = taskCalendarCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getPutawayStatus() {
        return putawayStatus;
    }

    public void setPutawayStatus(int putawayStatus) {
        this.putawayStatus = putawayStatus;
    }

    public List<TaskCalendarTemplatesBean> getTaskCalendarTemplates() {
        return taskCalendarTemplates;
    }

    public void setTaskCalendarTemplates(List<TaskCalendarTemplatesBean> taskCalendarTemplates) {
        this.taskCalendarTemplates = taskCalendarTemplates;
    }

    public static class TaskCalendarTemplatesBean {

        private int id;
        private String name;
        private String coverUrl;
        private int putawayStatus;
        private int memberTag;//是否显示vip标签，0,不显示；1，显示

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

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public int getPutawayStatus() {
            return putawayStatus;
        }

        public void setPutawayStatus(int putawayStatus) {
            this.putawayStatus = putawayStatus;
        }

        public int getMemberTag() {
            return memberTag;
        }

        public void setMemberTag(int memberTag) {
            this.memberTag = memberTag;
        }
    }
}
