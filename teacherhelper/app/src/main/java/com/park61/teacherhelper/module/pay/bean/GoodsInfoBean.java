package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;

public class GoodsInfoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	/**商品ID**/
//	public int id;
	/**商品图片URL**/
	public String productPicUrl;
	/**商品name**/
	public String productCname;
	/**商品总额**/
	public float orderItemAmount;
	/**商品单价**/
	public float orderItemPrice;
	/**商品数量**/
	public int orderItemNum;
	
	/**商品颜色**/
	public String productColor;
	/**商品尺码**/
	public String productSize;
	/**商品ID**/
	public long pmInfoId;
	/**商品基础信息编号**/
	public long productId;

	private int isGrfGoods;//是否已经申请售后(1-是 0-否)

	public int getIsGrfGoods() {
		return isGrfGoods;
	}

	public void setIsGrfGoods(int isGrfGoods) {
		this.isGrfGoods = isGrfGoods;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductCname() {
		return productCname;
	}

	public void setProductCname(String productCname) {
		this.productCname = productCname;
	}

	public float getOrderItemAmount() {
		return orderItemAmount;
	}

	public void setOrderItemAmount(float orderItemAmount) {
		this.orderItemAmount = orderItemAmount;
	}

	public float getOrderItemPrice() {
		return orderItemPrice;
	}

	public void setOrderItemPrice(float orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}

	public int getOrderItemNum() {
		return orderItemNum;
	}

	public void setOrderItemNum(int orderItemNum) {
		this.orderItemNum = orderItemNum;
	}

	public String getProductColor() {
		return productColor;
	}

	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}
	
	

	
}
