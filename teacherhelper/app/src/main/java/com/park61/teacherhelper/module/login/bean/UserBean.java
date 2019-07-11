package com.park61.teacherhelper.module.login.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    /**
     * id : null
     * userId : 11
     * sysUserId : null
     * groupId : null
     * mobile : 18062511980
     * name : 学长大人
     * duties : null
     * sex : null
     * isAdmin : 0
     * isInvite : 0
     * sendMessage : 0
     * status : null
     * birthday : null
     * certificate : null
     * education : null
     * workLife : null
     * delFlag : 0
     * createDate : null
     * createBy : null
     * updateDate : null
     * updateBy : null
     * petName : 18062511980
     * pictureUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/9eafc1d8-6941-4a9e-b1b7-cd5be18fe3d7.jpg
     * address : null
     * teachMemberId : null
     */

    private long id;
    private long userId;
    private long sysUserId;
    private long groupId;
    private String mobile;
    private String name;
    private int duties;
    private int sex;//性别 1:男 2:女
    private int isAdmin;
    private int isInvite;
    private int sendMessage;
    private int status;
    private String birthday;
    private String certificate;
    private String education;
    private String workLife;
    private int delFlag;
    private String createDate;
    private String createBy;
    private String updateDate;
    private String updateBy;
    private String petName;
    private String pictureUrl;
    private String address;
    private long teachMemberId;
    private String schoolName;
    private String schoolAddress;
    private int passNum;
    private String dutyName;
    private String userDuty;
    private String expireDate;
    private int isMember;//-1非会员 0 体验会员 1正式会员
    private int isTeachGroupAuthShow;//会员中心是否显示会员认证广告
    private int memberRemainDays;//会员剩余多少天

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsInvite() {
        return isInvite;
    }

    public void setIsInvite(int isInvite) {
        this.isInvite = isInvite;
    }

    public int getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(int sendMessage) {
        this.sendMessage = sendMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWorkLife() {
        return workLife;
    }

    public void setWorkLife(String workLife) {
        this.workLife = workLife;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
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

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTeachMemberId() {
        return teachMemberId;
    }

    public void setTeachMemberId(long teachMemberId) {
        this.teachMemberId = teachMemberId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public int getDuties() {
        return duties;
    }

    public void setDuties(int duties) {
        this.duties = duties;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getUserDuty() {
        return userDuty;
    }

    public void setUserDuty(String userDuty) {
        this.userDuty = userDuty;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getIsTeachGroupAuthShow() {
        return isTeachGroupAuthShow;
    }

    public void setIsTeachGroupAuthShow(int isTeachGroupAuthShow) {
        this.isTeachGroupAuthShow = isTeachGroupAuthShow;
    }

    public int getMemberRemainDays() {
        return memberRemainDays;
    }

    public void setMemberRemainDays(int memberRemainDays) {
        this.memberRemainDays = memberRemainDays;
    }
}
