package com.park61.teacherhelper.module.my.bean;

/**
 * Created by chenlie on 2017/12/27.
 * <p>
 * 教师活动列表bean
 */

public class SignDataBean {

    /**
     * "buyStatus": 1, 1已购买装备 0未购买装备
     * "childId": 1,
     * "childName": "小张",
     * "relationName": "外公",
     * "status": 0, 1已签到 0未签到
     * "userName": "张三",
     * "userTel": "13911111111"
     */

    private int buyStatus;
    private int childId;
    private String childName;
    private String userName;
    private String relationName;
    private String userTel;
    private double productPaidAccount;
    private int status;

    public int getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(int buyStatus) {
        this.buyStatus = buyStatus;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public double getProductPaidAccount() {
        return productPaidAccount;
    }

    public void setProductPaidAccount(double productPaidAccount) {
        this.productPaidAccount = productPaidAccount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
