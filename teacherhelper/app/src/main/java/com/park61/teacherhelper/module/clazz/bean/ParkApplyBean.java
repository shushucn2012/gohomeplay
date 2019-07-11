package com.park61.teacherhelper.module.clazz.bean;

/**
 * Created by chenlie on 2018/5/16.
 */

public class ParkApplyBean {

    /**
     * "id": 2,
     * "schoolName": "爱丽尔的幼儿园2",
     * "status": 0,
     * "statusName": "待提交",
     * "totalAddress": "湖北省武汉市江夏区光谷大道金融港",
     * "type": 1, 基地园
     * "typeName": "基地园"
     */
    private int id;
    private String schoolName;
    private int status;
    private String statusName;
    private String totalAddress;
    private int type;
    private String typeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public String getTotalAddress() {
        return totalAddress;
    }

    public void setTotalAddress(String totalAddress) {
        this.totalAddress = totalAddress;
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
}
