package com.park61.teacherhelper.module.clazz.bean;

import java.util.List;

public class FamilyConBookComtTempBean {


    /**
     * title : 情商
     * teachCommentTemplateDetailList : [{"content":"情商一级棒棒棒"},{"content":"情商二级棒"}]
     */

    private String title;
    private List<TeachCommentTemplateDetailListBean> teachCommentTemplateDetailList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TeachCommentTemplateDetailListBean> getTeachCommentTemplateDetailList() {
        return teachCommentTemplateDetailList;
    }

    public void setTeachCommentTemplateDetailList(List<TeachCommentTemplateDetailListBean> teachCommentTemplateDetailList) {
        this.teachCommentTemplateDetailList = teachCommentTemplateDetailList;
    }

    public static class TeachCommentTemplateDetailListBean {
        /**
         * content : 情商一级棒棒棒
         */

        private String content;
        private boolean isChecked;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
