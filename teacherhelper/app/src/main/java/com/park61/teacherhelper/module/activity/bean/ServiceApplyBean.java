package com.park61.teacherhelper.module.activity.bean;



public class ServiceApplyBean {


    /**
     * id : 322
     * kindergartenId : null
     * kindergartenName : null
     * kindergartenMobile : null
     * agentName : null
     * agentMobile : null
     * groupId : null
     * groupName : 1016教研组
     * status : 0
     * auditBy : null
     * auditDate : null
     * groupType : 0
     * contractImg : null
     * contractImgList : null
     * delFlag : 0
     * userId : null
     * createBy : null
     * createDate : 2019-03-08
     * updateBy : null
     * updateDate : null
     * areaAddress : null
     * streetAddress : null
     * address : null
     * purchaseSuit : null
     */

    private int id;
    private int kindergartenId;
    private String kindergartenName;
    private String kindergartenMobile;
    private String agentName;
    private String agentMobile;
    private int groupId;
    private String groupName;
    private int status;//审核状态（0:待审核;1:审核通过;2:审核拒绝）
    private int groupType;
    private String purchaseSuit;
    private String createDate;
    private String areaAddress;
    private String streetAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKindergartenId() {
        return kindergartenId;
    }

    public void setKindergartenId(int kindergartenId) {
        this.kindergartenId = kindergartenId;
    }

    public String getKindergartenName() {
        return kindergartenName;
    }

    public void setKindergartenName(String kindergartenName) {
        this.kindergartenName = kindergartenName;
    }

    public String getKindergartenMobile() {
        return kindergartenMobile;
    }

    public void setKindergartenMobile(String kindergartenMobile) {
        this.kindergartenMobile = kindergartenMobile;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getPurchaseSuit() {
        return purchaseSuit;
    }

    public void setPurchaseSuit(String purchaseSuit) {
        this.purchaseSuit = purchaseSuit;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public void setAreaAddress(String areaAddress) {
        this.areaAddress = areaAddress;
    }
}
