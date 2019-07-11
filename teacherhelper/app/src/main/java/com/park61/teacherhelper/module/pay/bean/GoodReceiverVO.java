package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;

public class GoodReceiverVO implements Serializable {

	private static final long serialVersionUID = -1202880469358707538L;
	private Long id;// 主键id
	private Long endUserId;// 用户id
	private String goodReceiverName;// 收货人姓名
	private String goodReceiverAddress;// 收货人地址
	private String goodReceiverMobile;// 收货人手机号
	private Long goodReceiverCountryId;// 国家id
	private Long goodReceiverProvinceId;// 省份id
	private String goodReceiverProvinceName;// 省份名称
	private Long goodReceiverCityId;// 城市id
	private String goodReceiverCityName;// 城市名称
	private Long goodReceiverCountyId;// 区县id
	private String goodReceiverCountyName;// 区县名称
	private String goodReceiverTownName;//乡镇名称
	private Long goodReceiverTownId;//乡镇id
	private String goodReceiverPostCode;// 邮编
	private Integer goodReceiverType;// 0-快递 1-自提
	private String storeId;// 店铺主键id，sys_office
	private String storeName;// 店铺名称
	private String storePhone;// 店铺联系方式
	private Integer isDefault;// 是否默认收货地址 0-否 1-是

	public String getGoodReceiverTownName() {
		return goodReceiverTownName;
	}

	public void setGoodReceiverTownName(String goodReceiverTownName) {
		this.goodReceiverTownName = goodReceiverTownName;
	}

	public Long getGoodReceiverTownId() {
		return goodReceiverTownId;
	}

	public void setGoodReceiverTownId(Long goodReceiverTownId) {
		this.goodReceiverTownId = goodReceiverTownId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}

	public String getGoodReceiverName() {
		return goodReceiverName;
	}

	public void setGoodReceiverName(String goodReceiverName) {
		this.goodReceiverName = goodReceiverName;
	}

	public String getGoodReceiverAddress() {
		return goodReceiverAddress;
	}

	public void setGoodReceiverAddress(String goodReceiverAddress) {
		this.goodReceiverAddress = goodReceiverAddress;
	}

	public String getGoodReceiverMobile() {
		return goodReceiverMobile;
	}

	public void setGoodReceiverMobile(String goodReceiverMobile) {
		this.goodReceiverMobile = goodReceiverMobile;
	}

	public Long getGoodReceiverCountryId() {
		return goodReceiverCountryId;
	}

	public void setGoodReceiverCountryId(Long goodReceiverCountryId) {
		this.goodReceiverCountryId = goodReceiverCountryId;
	}

	public Long getGoodReceiverProvinceId() {
		return goodReceiverProvinceId;
	}

	public void setGoodReceiverProvinceId(Long goodReceiverProvinceId) {
		this.goodReceiverProvinceId = goodReceiverProvinceId;
	}

	public String getGoodReceiverProvinceName() {
		return goodReceiverProvinceName;
	}

	public void setGoodReceiverProvinceName(String goodReceiverProvinceName) {
		this.goodReceiverProvinceName = goodReceiverProvinceName;
	}

	public Long getGoodReceiverCityId() {
		return goodReceiverCityId;
	}

	public void setGoodReceiverCityId(Long goodReceiverCityId) {
		this.goodReceiverCityId = goodReceiverCityId;
	}

	public String getGoodReceiverCityName() {
		return goodReceiverCityName;
	}

	public void setGoodReceiverCityName(String goodReceiverCityName) {
		this.goodReceiverCityName = goodReceiverCityName;
	}

	public Long getGoodReceiverCountyId() {
		return goodReceiverCountyId;
	}

	public void setGoodReceiverCountyId(Long goodReceiverCountyId) {
		this.goodReceiverCountyId = goodReceiverCountyId;
	}

	public String getGoodReceiverCountyName() {
		return goodReceiverCountyName;
	}

	public void setGoodReceiverCountyName(String goodReceiverCountyName) {
		this.goodReceiverCountyName = goodReceiverCountyName;
	}

	public String getGoodReceiverPostCode() {
		return goodReceiverPostCode;
	}

	public void setGoodReceiverPostCode(String goodReceiverPostCode) {
		this.goodReceiverPostCode = goodReceiverPostCode;
	}

	public Integer getGoodReceiverType() {
		return goodReceiverType;
	}

	public void setGoodReceiverType(Integer goodReceiverType) {
		this.goodReceiverType = goodReceiverType;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

}
