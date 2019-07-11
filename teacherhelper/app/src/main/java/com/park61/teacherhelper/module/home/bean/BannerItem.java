package com.park61.teacherhelper.module.home.bean;

import java.io.Serializable;

public class BannerItem implements Serializable {


	/**
	 * id : 13
	 * name : 百度
	 * bannerId : 7
	 * bannerPositionPic : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170816182136209_966.png
	 * bannerPositionWebsite : http://www.baidu.com/
	 * startTime : 2017-08-16 18:22:05
	 * endTime : 2017-09-16 18:22:08
	 * createBy : 1
	 * createDate : 2017-08-16 18:22:15
	 * updateBy : null
	 * updateDate : null
	 * isDelete : 0
	 * remark : null
	 * cityId : null
	 */

	private int id;
	private String name;
	private int bannerId;
	private String bannerPositionPic;
	private String bannerPositionWebsite;
	private String startTime;
	private String endTime;
	private String createBy;
	private String createDate;
	private String updateBy;
	private String updateDate;
	private int isDelete;
	private String remark;
	private String cityId;

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

	public int getBannerId() {
		return bannerId;
	}

	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerPositionPic() {
		return bannerPositionPic;
	}

	public void setBannerPositionPic(String bannerPositionPic) {
		this.bannerPositionPic = bannerPositionPic;
	}

	public String getBannerPositionWebsite() {
		return bannerPositionWebsite;
	}

	public void setBannerPositionWebsite(String bannerPositionWebsite) {
		this.bannerPositionWebsite = bannerPositionWebsite;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
