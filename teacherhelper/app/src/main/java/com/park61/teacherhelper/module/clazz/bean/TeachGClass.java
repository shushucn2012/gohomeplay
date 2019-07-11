package com.park61.teacherhelper.module.clazz.bean;

import java.io.Serializable;
import java.util.Date;

/**
* 教研组班级
*/
public class TeachGClass implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键Id
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 班级类型dict_value
	 */
	private String typeValue;
	/**
	 * 班级类型dict_lable
	 */
	private String typeName;
	/**
	 * 教研组id
	 */
	private Long groupId;
	/**
	 * 幼儿园所名称
	 */
	private String schoolName;
	/**
	 * 全称
	 */
	private String fullName;
	/**
	 * 删除标记(0正常1删除)
	 */
	private String delFlag="0";

    //额外属性
    private Integer countTeacher;//教师数
    private Integer countChild;//宝宝数

	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 访问下载download服务器的HTML
	 */
	private String url;

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
	 * 二维码
	 */
	private String qrCode;

    private boolean isChosen;
	
	public void setId(Long id){
		this.id=id;
	}
	public Long getId(){
		return this.id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	
	public void setTypeValue(String typeValue){
		this.typeValue=typeValue;
	}
	public String getTypeValue(){
		return this.typeValue;
	}
	
	public void setTypeName(String typeName){
		this.typeName=typeName;
	}
	public String getTypeName(){
		return this.typeName;
	}
	
	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}
	public Long getGroupId(){
		return this.groupId;
	}
	
	public void setSchoolName(String schoolName){
		this.schoolName=schoolName;
	}
	public String getSchoolName(){
		return this.schoolName;
	}
	
	public void setFullName(String fullName){
		this.fullName=fullName;
	}
	public String getFullName(){
		return this.fullName;
	}
	
	public void setDelFlag(String delFlag){
		this.delFlag=delFlag;
	}
	public String getDelFlag(){
		return this.delFlag;
	}

    public Integer getCountTeacher() {
        return countTeacher;
    }

    public void setCountTeacher(Integer countTeacher) {
        this.countTeacher = countTeacher;
    }

    public Integer getCountChild() {
        return countChild;
    }

    public void setCountChild(Integer countChild) {
        this.countChild = countChild;
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

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("TeachGClass[");
		sb.append("id=");
		sb.append(id);
		sb.append(",name=");
		sb.append(name);
		sb.append(",typeValue=");
		sb.append(typeValue);
		sb.append(",typeName=");
		sb.append(typeName);
		sb.append(",groupId=");
		sb.append(groupId);
		sb.append(",schoolName=");
		sb.append(schoolName);
		sb.append(",fullName=");
		sb.append(fullName);
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

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
