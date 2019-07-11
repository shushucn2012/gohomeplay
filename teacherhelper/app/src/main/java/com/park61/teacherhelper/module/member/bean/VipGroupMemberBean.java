package com.park61.teacherhelper.module.member.bean;


/**
 * 会员成员bean
 *
 * @author shubei
 * @time 2019/1/19 11:48
 */
public class VipGroupMemberBean {


    /**
     * userId : 12815
     * userName : 啊啊啊
     * pictureUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20181130113737429_341.png
     * dutyName : 主班老师
     */
    private int id;
    private int userId;
    private String userName;
    private String pictureUrl;
    private String dutyName;
    private String userMobile;
    private int status;//状态1,待审核2，审核通过3，审核拒绝
    private int isAdmin;//是否是管理员

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
