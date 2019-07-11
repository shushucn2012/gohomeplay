package com.park61.teacherhelper.module.clazz.bean;

public class ClazzQRCodeBean {


    /**
     * backgroundUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20181121160057393_912.png
     * qrCodeUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20181127_714538.png
     * userHeadPic : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/81adecf0-b4fd-4f34-aa23-c2017f397a6a.jpg?x-oss-process=style/compress_nologo
     * userName : 18062511980
     * teachGroupName : 555
     * teachClassName : 超级小二班
     */

    private String backgroundUrl;
    private String qrCodeUrl;
    private String userHeadPic;
    private String userName;
    private String teachGroupName;
    private String teachClassName;

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getUserHeadPic() {
        return userHeadPic;
    }

    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeachGroupName() {
        return teachGroupName;
    }

    public void setTeachGroupName(String teachGroupName) {
        this.teachGroupName = teachGroupName;
    }

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }
}
