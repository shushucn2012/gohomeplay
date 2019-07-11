package com.park61.teacherhelper.module.workplan.bean;


import java.util.List;

/**
 * 订阅任务
 * @author shubei
 * @time 2018/12/25 11:08
 */
public class SubsTaskBean {

    /**
     * taskCalendarId : 30
     * taskCalendarTemplateDetailId : 1
     * templateName : 学习模板3
     * templateDetailName : 画画基础课程
     * templateDetailIntro : 我是事项描述哦，哈哈
     * taskType : 1
     * status : 0
     * subscribeTaskCalendarTemplateDetailResources : [{"id":null,"taskCalendarTemplateDetailId":1,"sourceType":0,"relationId":18,"sourceName":"学习学习","sourceUrl":null,"createBy":null,"createDate":"2018-12-25 10:55:27","updateBy":null,"updateDate":null},{"id":null,"taskCalendarTemplateDetailId":1,"sourceType":1,"relationId":70,"sourceName":"学习一下","sourceUrl":null,"createBy":null,"createDate":"2018-12-25 10:55:27","updateBy":null,"updateDate":null},{"id":null,"taskCalendarTemplateDetailId":1,"sourceType":2,"relationId":null,"sourceName":"好好学习","sourceUrl":"teach61://contackBook","createBy":null,"createDate":"2018-12-25 10:55:27","updateBy":null,"updateDate":null}]
     */

    private int taskCalendarId;
    private int taskCalendarTemplateDetailId;
    private String templateName;
    private String templateDetailName;
    private String templateDetailIntro;
    private int taskType;
    private int status;//0待办，1已完成
    private List<SubscribeTaskCalendarTemplateDetailResourcesBean> subscribeTaskCalendarTemplateDetailResources;

    public int getTaskCalendarId() {
        return taskCalendarId;
    }

    public void setTaskCalendarId(int taskCalendarId) {
        this.taskCalendarId = taskCalendarId;
    }

    public int getTaskCalendarTemplateDetailId() {
        return taskCalendarTemplateDetailId;
    }

    public void setTaskCalendarTemplateDetailId(int taskCalendarTemplateDetailId) {
        this.taskCalendarTemplateDetailId = taskCalendarTemplateDetailId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDetailName() {
        return templateDetailName;
    }

    public void setTemplateDetailName(String templateDetailName) {
        this.templateDetailName = templateDetailName;
    }

    public String getTemplateDetailIntro() {
        return templateDetailIntro;
    }

    public void setTemplateDetailIntro(String templateDetailIntro) {
        this.templateDetailIntro = templateDetailIntro;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SubscribeTaskCalendarTemplateDetailResourcesBean> getSubscribeTaskCalendarTemplateDetailResources() {
        return subscribeTaskCalendarTemplateDetailResources;
    }

    public void setSubscribeTaskCalendarTemplateDetailResources(List<SubscribeTaskCalendarTemplateDetailResourcesBean> subscribeTaskCalendarTemplateDetailResources) {
        this.subscribeTaskCalendarTemplateDetailResources = subscribeTaskCalendarTemplateDetailResources;
    }

    public static class SubscribeTaskCalendarTemplateDetailResourcesBean {
        /**
         * id : null
         * taskCalendarTemplateDetailId : 1
         * sourceType : 0
         * relationId : 18
         * sourceName : 学习学习
         * sourceUrl : null
         * createBy : null
         * createDate : 2018-12-25 10:55:27
         * updateBy : null
         * updateDate : null
         */

        private int id;
        private int taskCalendarTemplateDetailId;
        private int sourceType;
        private int relationId;
        private String sourceName;
        private String sourceUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTaskCalendarTemplateDetailId() {
            return taskCalendarTemplateDetailId;
        }

        public void setTaskCalendarTemplateDetailId(int taskCalendarTemplateDetailId) {
            this.taskCalendarTemplateDetailId = taskCalendarTemplateDetailId;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public int getRelationId() {
            return relationId;
        }

        public void setRelationId(int relationId) {
            this.relationId = relationId;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }
    }
}
