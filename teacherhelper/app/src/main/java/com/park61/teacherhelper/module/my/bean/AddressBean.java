package com.park61.teacherhelper.module.my.bean;

/**
 * Created by shubei on 2017/10/13.
 */

public class AddressBean {


    /**
     * id : 8
     * parentId : 1
     * name : 吉林省
     * type : 2
     * provinceId : 8
     * province : 吉林省
     * cityId : null
     * city : null
     * countyId : null
     * county : null
     */

    private long id;
    private long parentId;
    private String name;
    private String type;
    private long provinceId;
    private String province;
    private long cityId;
    private String city;
    private long countyId;
    private String county;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getCountyId() {
        return countyId;
    }

    public void setCountyId(long countyId) {
        this.countyId = countyId;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
