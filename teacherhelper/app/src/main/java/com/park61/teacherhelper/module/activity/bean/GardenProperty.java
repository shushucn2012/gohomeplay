package com.park61.teacherhelper.module.activity.bean;

public class GardenProperty {

    private int id;
    private String name;
    private boolean isChecked;

    public GardenProperty() {
    }

    public GardenProperty(int id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
