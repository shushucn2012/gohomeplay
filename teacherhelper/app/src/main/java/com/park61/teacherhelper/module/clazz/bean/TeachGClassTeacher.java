package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;
import java.util.Date;

/**
* 教研组班级_教师
*/
public class TeachGClassTeacher implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键Id
	 */
	private Long id;
	/**
	 * 教研组班级id
	 */
	private Long gClassId;
	/**
	 * user_kinship_ID
	 */
	private Long userId;
	/**
	 * 删除标记(0正常1删除)
	 */
	private String delFlag="0";

	private String name;//姓名
	private String userMobile;//手机号
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 创建时间
	 */
	private Date createDate=new Date();
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 头像
	 */
	private String pictureUrl;
	
	public void setId(Long id){
		this.id=id;
	}
	public Long getId(){
		return this.id;
	}

	public Long getgClassId() {
		return gClassId;
	}

	public void setgClassId(Long gClassId) {
		this.gClassId = gClassId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}
	public Long getUserId(){
		return this.userId;
	}
	
	public void setDelFlag(String delFlag){
		this.delFlag=delFlag;
	}
	public String getDelFlag(){
		return this.delFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public String getRemarks(){
		return this.remarks;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	public Date getCreateDate(){
		return this.createDate;
	}
	
	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}
	public String getCreateBy(){
		return this.createBy;
	}
	
	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}
	public Date getUpdateDate(){
		return this.updateDate;
	}
	
	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}
	public String getUpdateBy(){
		return this.updateBy;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("TeachGClassTeacher[");
		sb.append("id=");
		sb.append(id);
		sb.append(",gClassId=");
		sb.append(gClassId);
		sb.append(",userId=");
		sb.append(userId);
		sb.append(",delFlag=");
		sb.append(delFlag);
		sb.append(",remarks=");
		sb.append(remarks);
		sb.append(",createDate=");
		sb.append(createDate);
		sb.append(",createBy=");
		sb.append(createBy);
		sb.append(",updateDate=");
		sb.append(updateDate);
		sb.append(",updateBy=");
		sb.append(updateBy);
		sb.append("]");
		return sb.toString();
	}
}
