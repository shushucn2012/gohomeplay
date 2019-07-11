package com.park61.teacherhelper.module.workplan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2018/7/2.
 */

public class TaskRole implements Serializable{

    private int id;
    private String title;
    private boolean isChecked;
    private List<TaskPerson> persons;

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

    public List<TaskPerson> getPersons() {
        return persons;
    }

    public void setPersons(List<TaskPerson> persons) {
        this.persons = persons;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
