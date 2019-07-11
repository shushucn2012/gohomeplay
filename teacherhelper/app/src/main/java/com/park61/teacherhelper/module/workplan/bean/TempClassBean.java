package com.park61.teacherhelper.module.workplan.bean;

/**
 * Created by shubei on 2018/7/30.
 */

public class TempClassBean {


    /**
     * fullName : 偷渡的小班
     * id : 429
     * isAssignedTemplate : 0
     * name : 偷渡的
     * typeName : 小班
     */

    private String fullName;
    private int id;
    private int isAssignedTemplate;//是否已被分配模板,0:否、1:是
    private String name;
    private String typeName;
    private boolean isChecked;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAssignedTemplate() {
        return isAssignedTemplate;
    }

    public void setIsAssignedTemplate(int isAssignedTemplate) {
        this.isAssignedTemplate = isAssignedTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
