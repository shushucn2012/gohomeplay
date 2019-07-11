package com.park61.teacherhelper.module.clazz.bean;

import java.util.List;

public class FamilyClazzComtItem {


    /**
     * id : 28
     * teachGroupId : null
     * teachClassId : null
     * content : 哈哈，就是这么吊
     * issueBy : 11
     * issueName : 18062511980
     * issuePic : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/81adecf0-b4fd-4f34-aa23-c2017f397a6a.jpg
     * showIssueDate : 刚刚
     * teachCommentChildList : [{"userChildId":1628,"userChildName":"哈哈一号","picUrl":"http://m.61park.cn/images/laz/mr_ower@3x.png","isSelected":0}]
     * teachCommentSourceList : [{"id":153,"teachCommentId":28,"sourceType":0,"source":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/2492a454-c747-4c22-957d-872ab190ffec.jpg","videoImg":null,"sourceName":null,"sourceSize":null,"sourceTime":null},{"id":154,"teachCommentId":28,"sourceType":0,"source":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/4dbf952c-9f41-4d16-a8ae-ecc82d802ca8.jpg","videoImg":null,"sourceName":null,"sourceSize":null,"sourceTime":null},{"id":155,"teachCommentId":28,"sourceType":0,"source":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/dfc4ad56-22ff-42af-ae5d-1ad2ec104570.jpg","videoImg":null,"sourceName":null,"sourceSize":null,"sourceTime":null}]
     * isCommentCanEdit : true
     * picList : null
     * videoId : null
     */

    private int id;
    private int teachGroupId;
    private int teachClassId;
    private String content;
    private int issueBy;
    private String issueName;
    private String issuePic;
    private String showIssueDate;
    private boolean isCommentCanEdit;
    private String picList;
    private String videoId;
    private List<TeachCommentChildListBean> teachCommentChildList;
    private List<TeachCommentSourceListBean> teachCommentSourceList;
    private Boolean isShowExpand;//是否显示展开
    private boolean isExpand;//文字是否展开状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(int issueBy) {
        this.issueBy = issueBy;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssuePic() {
        return issuePic;
    }

    public void setIssuePic(String issuePic) {
        this.issuePic = issuePic;
    }

    public String getShowIssueDate() {
        return showIssueDate;
    }

    public void setShowIssueDate(String showIssueDate) {
        this.showIssueDate = showIssueDate;
    }

    public boolean isIsCommentCanEdit() {
        return isCommentCanEdit;
    }

    public void setIsCommentCanEdit(boolean isCommentCanEdit) {
        this.isCommentCanEdit = isCommentCanEdit;
    }



    public List<TeachCommentChildListBean> getTeachCommentChildList() {
        return teachCommentChildList;
    }

    public void setTeachCommentChildList(List<TeachCommentChildListBean> teachCommentChildList) {
        this.teachCommentChildList = teachCommentChildList;
    }

    public List<TeachCommentSourceListBean> getTeachCommentSourceList() {
        return teachCommentSourceList;
    }

    public void setTeachCommentSourceList(List<TeachCommentSourceListBean> teachCommentSourceList) {
        this.teachCommentSourceList = teachCommentSourceList;
    }

    public int getTeachGroupId() {
        return teachGroupId;
    }

    public void setTeachGroupId(int teachGroupId) {
        this.teachGroupId = teachGroupId;
    }

    public int getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(int teachClassId) {
        this.teachClassId = teachClassId;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public Boolean isShowExpand() {
        return isShowExpand;
    }

    public void setShowExpand(Boolean showExpand) {
        isShowExpand = showExpand;
    }

    public static class TeachCommentChildListBean {
        /**
         * userChildId : 1628
         * userChildName : 哈哈一号
         * picUrl : http://m.61park.cn/images/laz/mr_ower@3x.png
         * isSelected : 0
         */

        private int userChildId;
        private String userChildName;
        private String picUrl;
        private int isSelected;

        public int getUserChildId() {
            return userChildId;
        }

        public void setUserChildId(int userChildId) {
            this.userChildId = userChildId;
        }

        public String getUserChildName() {
            return userChildName;
        }

        public void setUserChildName(String userChildName) {
            this.userChildName = userChildName;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(int isSelected) {
            this.isSelected = isSelected;
        }
    }

    public static class TeachCommentSourceListBean {
        /**
         * id : 153
         * teachCommentId : 28
         * sourceType : 0
         * source : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/2492a454-c747-4c22-957d-872ab190ffec.jpg
         * videoImg : null
         * sourceName : null
         * sourceSize : null
         * sourceTime : null
         */

        private int id;
        private int teachCommentId;
        private int sourceType;
        private String source;
        private Object videoImg;
        private Object sourceName;
        private Object sourceSize;
        private Object sourceTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTeachCommentId() {
            return teachCommentId;
        }

        public void setTeachCommentId(int teachCommentId) {
            this.teachCommentId = teachCommentId;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Object getVideoImg() {
            return videoImg;
        }

        public void setVideoImg(Object videoImg) {
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
    }
}
