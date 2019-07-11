package com.park61.teacherhelper.module.workplan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2018/8/5.
 */

public class CourseSeriesBean implements Serializable{

    private int id;
    private String title;
    private List<KnowledgeBean> trainerCourseList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<KnowledgeBean> getTrainerCourseList() {
        return trainerCourseList;
    }

    public void setTrainerCourseList(List<KnowledgeBean> trainerCourseList) {
        this.trainerCourseList = trainerCourseList;
    }
}
