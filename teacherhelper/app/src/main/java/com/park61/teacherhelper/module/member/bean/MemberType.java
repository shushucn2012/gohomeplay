package com.park61.teacherhelper.module.member.bean;

public class MemberType {


    /**
     * id : 1
     * name : 个人会员
     * num : 1
     * salePrice : 199.0
     */

    private int id;
    private String name;
    private int num;
    private double salePrice;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}
