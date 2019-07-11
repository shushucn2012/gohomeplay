package com.park61.teacherhelper.module.workplan.bean;

import java.util.List;

/**
 * Created by shubei on 2018/4/27.
 */

public class WorkMonthBean {

    /**
     * taskCalendarList : [{"status":1,"level2Name":"日常教学","name":"1.厨房卫生检查 2.全园打包被子","isEnableCheck":0,"teachGroupId":6,"id":56,"taskOwnerDuty":2,"taskCalendarLog":{"showUpdateDate":"31分钟之前","type":0,"intro":"任务完成","userName":"崔夏青"},"isKeyView":0,"level1Name":"教学活动","intro":"1.厨房卫生检查 2.全园打包被子"}]
     * showStartDate : 04.27
     * startDate : 1524758400000
     */

    private String showStartDate;
    private long startDate;
    private List<TaskCalendarListBean> taskCalendarList;

    public String getShowStartDate() {
        return showStartDate;
    }

    public void setShowStartDate(String showStartDate) {
        this.showStartDate = showStartDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public List<TaskCalendarListBean> getTaskCalendarList() {
        return taskCalendarList;
    }

    public void setTaskCalendarList(List<TaskCalendarListBean> taskCalendarList) {
        this.taskCalendarList = taskCalendarList;
    }


}
