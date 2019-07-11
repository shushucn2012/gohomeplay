package com.park61.teacherhelper.module.home.bean;

/**
 * Created by shubei on 2017/11/14.
 */

public class CheckVersionBean {


    /**
     * appType : 0
     * appVersionCode : 123
     * appVersionName : 2.3.0
     * changeLog : 测试更新信息
     * effectiveTime : 1510643027000
     * id : 82
     * isForce : 1
     * upgradeType : 2
     * upgradeUrl : http://a.app.qq.com/o/simple.jsp?pkgname=com.park61
     */

    private int appType;
    private int appVersionCode;
    private String appVersionName;
    private String changeLog;
    private long effectiveTime;
    private int id;
    private int isForce;
    private int upgradeType;
    private String upgradeUrl;

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public int getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getUpgradeUrl() {
        return upgradeUrl;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        this.upgradeUrl = upgradeUrl;
    }
}
