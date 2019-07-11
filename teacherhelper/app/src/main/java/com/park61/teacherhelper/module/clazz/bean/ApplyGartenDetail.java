package com.park61.teacherhelper.module.clazz.bean;

import java.util.List;

/**
 * Created by shubei on 2018/5/29.
 */

public class ApplyGartenDetail {


    /**
     * id : 108
     * userId : 11
     * applyName : 海燕
     * applyMobile : 18062511980
     * schoolName : 哈哈
     * provinceId : 18
     * cityId : 205
     * countyId : 1936
     * addressDetail : 到处都是
     * totalAddress : 湖北省武汉市江岸区到处都是
     * status : 1
     * statusName : 待审核
     * type : 1
     * typeName : 基地园
     * applyDate : 2018-05-28 11:32:37
     * auditBy : null
     * auditDate : null
     * rejectReason : 申请已经提交，请等待审核。
     * delFlag : 0
     * createDate : 2018-05-28 11:32:37
     * createBy : 11
     * updateDate : 2018-05-29 13:06:39
     * updateBy : 11
     * provinceName : 湖北省
     * cityName : 武汉市
     * countyName : 江岸区
     * itemList : [{"id":312,"applyId":108,"classifyId":25,"detail":"萝莉控呀","sourceType":0,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":["http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/73de7b92-71a8-4464-bc09-e8565508e0ec.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/08fc8929-8195-4f14-908a-9bba14ce69c5.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/8d241d83-578f-4ddf-90c5-c9adc68bdaa7.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/39e4543a-921e-4836-864a-b1fcb0d9dd01.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/68d7f56d-f03e-4b5a-a23d-7780784f79a1.jpg"],"videoId":null,"classifyName":"荣誉资质"},{"id":321,"applyId":108,"classifyId":27,"detail":"哈摆龙门阵","sourceType":1,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":["http://video.61park.cn/snapshot/f8355e2385274c7f99a3cb0286996f9d00001.jpg"],"videoId":null,"classifyName":"室内展示"},{"id":322,"applyId":108,"classifyId":28,"detail":"网易新闻","sourceType":1,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":[null],"videoId":null,"classifyName":"功能室展示"},{"id":323,"applyId":108,"classifyId":29,"detail":"我一直现在","sourceType":0,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":["http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/66e03c9c-911d-4d61-a631-41c2e165d6b2.jpg"],"videoId":null,"classifyName":"特色积木"},{"id":327,"applyId":108,"classifyId":30,"detail":"恐龙","sourceType":1,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":[null],"videoId":null,"classifyName":"区角展示"},{"id":328,"applyId":108,"classifyId":31,"detail":"我想我们","sourceType":0,"createDate":"2018-05-29 17:27:38","createBy":null,"updateDate":null,"updateBy":null,"delFlag":"0","sourceList":null,"picList":["http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/a69b07d0-1f61-4d20-a1fe-b10ddd467ae5.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/5732cc9c-44d7-4617-be45-7060dd459311.jpg"],"videoId":null,"classifyName":"师训"}]
     */

    private int id;
    private int userId;
    private String applyName;
    private String applyMobile;
    private String schoolName;
    private int provinceId;
    private int cityId;
    private int countyId;
    private String addressDetail;
    private String totalAddress;
    private int status;
    private String statusName;
    private int type;
    private String typeName;
    private String applyDate;
    private Object auditBy;
    private Object auditDate;
    private String rejectReason;
    private String delFlag;
    private String createDate;
    private String createBy;
    private String updateDate;
    private String updateBy;
    private String provinceName;
    private String cityName;
    private String countyName;
    private List<ItemListBean> itemList;

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

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyMobile() {
        return applyMobile;
    }

    public void setApplyMobile(String applyMobile) {
        this.applyMobile = applyMobile;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getTotalAddress() {
        return totalAddress;
    }

    public void setTotalAddress(String totalAddress) {
        this.totalAddress = totalAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public Object getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Object auditBy) {
        this.auditBy = auditBy;
    }

    public Object getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Object auditDate) {
        this.auditDate = auditDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean {
        /**
         * id : 312
         * applyId : 108
         * classifyId : 25
         * detail : 萝莉控呀
         * sourceType : 0
         * createDate : 2018-05-29 17:27:38
         * createBy : null
         * updateDate : null
         * updateBy : null
         * delFlag : 0
         * sourceList : null
         * picList : ["http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/73de7b92-71a8-4464-bc09-e8565508e0ec.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/08fc8929-8195-4f14-908a-9bba14ce69c5.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/8d241d83-578f-4ddf-90c5-c9adc68bdaa7.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/39e4543a-921e-4836-864a-b1fcb0d9dd01.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/68d7f56d-f03e-4b5a-a23d-7780784f79a1.jpg"]
         * videoId : null
         * classifyName : 荣誉资质
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
        private Object sourceList;
        private Object videoId;
        private String classifyName;
        private List<String> picList;

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

        public Object getSourceList() {
            return sourceList;
        }

        public void setSourceList(Object sourceList) {
            this.sourceList = sourceList;
        }

        public Object getVideoId() {
            return videoId;
        }

        public void setVideoId(Object videoId) {
            this.videoId = videoId;
        }

        public String getClassifyName() {
            return classifyName;
        }

        public void setClassifyName(String classifyName) {
            this.classifyName = classifyName;
        }

        public List<String> getPicList() {
            return picList;
        }

        public void setPicList(List<String> picList) {
            this.picList = picList;
        }
    }
}
