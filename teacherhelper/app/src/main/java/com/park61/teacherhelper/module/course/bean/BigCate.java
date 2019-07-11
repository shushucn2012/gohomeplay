package com.park61.teacherhelper.module.course.bean;

import java.util.List;

/**
 * Created by shubei on 2017/10/12.
 */

public class BigCate {

    /**
     * id : 26
     * name : 10月
     * level : 1
     * status : 0
     * sort : 1
     * parentId : 0
     * isLeaf : 0
     * delFlag : 0
     * listContentTag : [{"id":2,"name":"男生","status":0,"delFlag":"0","cnt":1,"createDate":"2017-10-17 11:49:06","createBy":0,"updateDate":"2017-10-17 11:49:06","updateBy":0},{"id":5,"name":"现代活动","status":0,"delFlag":"0","cnt":0,"createDate":"2017-10-17 11:49:06","createBy":0,"updateDate":"2017-10-17 11:49:06","updateBy":0},{"id":3,"name":"女生","status":0,"delFlag":"0","cnt":0,"createDate":"2017-10-17 11:49:06","createBy":0,"updateDate":"2017-10-17 11:49:06","updateBy":0}]
     * createDate : 2017-10-17 11:49:06
     * createBy : 0
     * updateDate : 2017-10-17 11:49:06
     * updateBy : 0
     */

    private int id;
    private String name;
    private int level;
    private int status;
    private int sort;
    private int parentId;
    private int isLeaf;
    private String delFlag;
    private String createDate;
    private int createBy;
    private String updateDate;
    private int updateBy;
    private boolean isSelected;
    private List<ListContentTagBean> listContentTag;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public List<ListContentTagBean> getListContentTag() {
        return listContentTag;
    }

    public void setListContentTag(List<ListContentTagBean> listContentTag) {
        this.listContentTag = listContentTag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static class ListContentTagBean {
        /**
         * id : 2
         * name : 男生
         * status : 0
         * delFlag : 0
         * cnt : 1
         * createDate : 2017-10-17 11:49:06
         * createBy : 0
         * updateDate : 2017-10-17 11:49:06
         * updateBy : 0
         */

        private int id;
        private String name;
        private int status;
        private String delFlag;
        private int cnt;
        private String createDate;
        private int createBy;
        private String updateDate;
        private int updateBy;
        private boolean isSelected;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getCreateBy() {
            return createBy;
        }

        public void setCreateBy(int createBy) {
            this.createBy = createBy;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public int getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(int updateBy) {
            this.updateBy = updateBy;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
