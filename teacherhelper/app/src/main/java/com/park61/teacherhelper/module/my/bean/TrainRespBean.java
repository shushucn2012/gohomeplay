package com.park61.teacherhelper.module.my.bean;

/**
 * Created by shubei on 2018/3/30.
 */

public class TrainRespBean {

    /**
     * endTime : 2018-03-21 10:32:28
     * id : 1
     * name : 罗杰测试
     * serviceEvaluateNum : 3
     * startTime : 2018-03-20 10:32:28
     */

    private String endTime;
    private int id;
    private String name;
    private int serviceEvaluateNum;
    private String startTime;
    private String startEndTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

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

    public int getServiceEvaluateNum() {
        return serviceEvaluateNum;
    }

    public void setServiceEvaluateNum(int serviceEvaluateNum) {
        this.serviceEvaluateNum = serviceEvaluateNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(String startEndTime) {
        this.startEndTime = startEndTime;
    }
}
