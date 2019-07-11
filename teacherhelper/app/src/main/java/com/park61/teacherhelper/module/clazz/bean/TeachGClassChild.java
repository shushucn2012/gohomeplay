package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 教研组班级_宝宝
*/
public class TeachGClassChild implements Serializable {

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
	 * 宝宝user_child_id
	 */
	private Long userChildId;
	/**
	 * 删除标记(0正常1删除)
	 */
	private String delFlag="0";

	private String name;//姓名
	private String pictureUrl;//图片链接
	private String birthday;//生日
	private String sexName;//性别
	private List<UserChildKinshipRelation> listRelation=new ArrayList<>();//亲属关系列表
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

	public void setUserChildId(Long userChildId){
		this.userChildId=userChildId;
	}
	public Long getUserChildId(){
		return this.userChildId;
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

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public List<UserChildKinshipRelation> getListRelation() {
		return listRelation;
	}

	public void setListRelation(List<UserChildKinshipRelation> listRelation) {
		this.listRelation = listRelation;
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

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("TeachGClassChild[");
		sb.append("id=");
		sb.append(id);
		sb.append(",gClassId=");
		sb.append(gClassId);
		sb.append(",userChildId=");
		sb.append(userChildId);
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
