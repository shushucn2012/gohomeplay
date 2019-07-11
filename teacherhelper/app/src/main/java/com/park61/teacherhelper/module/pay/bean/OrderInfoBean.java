package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 订单ID */
	public long id;
	/** 订单状态 */
	public int orderStatus;
	/** 订单总额 */
	public float orderAmount;
	/** 订单创建时间 */
	public long orderCreateTime;

	//订单取消原因
	public String orderCancelReason;

	/** 订单状态描述 */
	public String orderStatusName;

	/** 商品 */
	public List<GoodsInfoBean> items;
	// 子单
	private List<OrderInfoBean> childOrders;

	/** 省 */
	public String goodReceiverProvince;
	/** 城市 */
	public String goodReceiverCity;
	/** 区 */
	public String goodReceiverCounty;
	/** 地址 */
	public String goodReceiverAddress;

	/** 姓名 */
	public String goodReceiverName;
	/** 电话 */
	public String goodReceiverMobile;

	/** 节省 */
	public float orderDeliveryFee;

	/** 产品总额 */
	public float productAmount;

	public float couponAmount;// 抵用券金额
	public float orderToPayAmount;// 实际支付或待支付金额
	public int orderItemNum;// 商品数量
	public int packageNum;// 包裹数
	public ArrayList<String> picUrlList=new ArrayList<>();// 商品图片
	public float discountAmount;//满减金额

	public Long fightGroupOpenId;//拼团id

	public int orderType;//1：普通订单，2：拼团订单，3：会员订单

	public boolean returnFlag;//true:付款后可以取消订单,false:款后不能取消订单

	public String countDownTime;//倒计时

	public String getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(String countDownTime) {
		this.countDownTime = countDownTime;
	}

	public String getOrderCancelReason() {
		return orderCancelReason;
	}

	public void setOrderCancelReason(String orderCancelReason) {
		this.orderCancelReason = orderCancelReason;
	}

	public boolean isReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(boolean returnFlag) {
		this.returnFlag = returnFlag;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public Long getFightGroupOpenId() {
		return fightGroupOpenId;
	}

	public void setFightGroupOpenId(Long fightGroupOpenId) {
		this.fightGroupOpenId = fightGroupOpenId;
	}

	public float getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}

	public int getIsEvaluated() {
		return isEvaluated;
	}

	public void setIsEvaluated(int isEvaluated) {
		this.isEvaluated = isEvaluated;
	}

	public int isEvaluated;// 已评论1，未评论0

	public int getOrderItemNum() {
		return orderItemNum;
	}

	public void setOrderItemNum(int orderItemNum) {
		this.orderItemNum = orderItemNum;
	}

	public int getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(int packageNum) {
		this.packageNum = packageNum;
	}

	public ArrayList<String> getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(ArrayList<String> picUrlList) {
		this.picUrlList = picUrlList;
	}

	public String getGoodReceiverProvince() {
		return goodReceiverProvince;
	}

	public void setGoodReceiverProvince(String goodReceiverProvince) {
		this.goodReceiverProvince = goodReceiverProvince;
	}

	public String getGoodReceiverCity() {
		return goodReceiverCity;
	}

	public void setGoodReceiverCity(String goodReceiverCity) {
		this.goodReceiverCity = goodReceiverCity;
	}

	public String getGoodReceiverCounty() {
		return goodReceiverCounty;
	}

	public void setGoodReceiverCounty(String goodReceiverCounty) {
		this.goodReceiverCounty = goodReceiverCounty;
	}

	public String getGoodReceiverAddress() {
		return goodReceiverAddress;
	}

	public void setGoodReceiverAddress(String goodReceiverAddress) {
		this.goodReceiverAddress = goodReceiverAddress;
	}

	public String getGoodReceiverName() {
		return goodReceiverName;
	}

	public void setGoodReceiverName(String goodReceiverName) {
		this.goodReceiverName = goodReceiverName;
	}

	public String getGoodReceiverMobile() {
		return goodReceiverMobile;
	}

	public void setGoodReceiverMobile(String goodReceiverMobile) {
		this.goodReceiverMobile = goodReceiverMobile;
	}

	public float getOrderDeliveryFee() {
		return orderDeliveryFee;
	}

	public void setOrderDeliveryFee(float orderDeliveryFee) {
		this.orderDeliveryFee = orderDeliveryFee;
	}

	public float getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(float productAmount) {
		this.productAmount = productAmount;
	}

	public List<OrderInfoBean> getChildOrders() {
		return childOrders;
	}

	public void setChildOrders(List<OrderInfoBean> childOrders) {
		this.childOrders = childOrders;
	}

	public float getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(float couponAmount) {
		this.couponAmount = couponAmount;
	}

	public float getOrderToPayAmount() {
		return orderToPayAmount;
	}

	public void setOrderToPayAmount(float orderToPayAmount) {
		this.orderToPayAmount = orderToPayAmount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public float getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(float orderAmount) {
		this.orderAmount = orderAmount;
	}

	public long getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(long orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public List<GoodsInfoBean> getItems() {
		return items;
	}

	public void setItems(ArrayList<GoodsInfoBean> items) {
		this.items = items;
	}

	public String getOrderStateString() {
		switch (this.orderStatus) {
		case OrderState.ORDER_STATUS_ORDERED_WAITING_FOR_PAYMENT:
			return "待付款";
		case OrderState.ORDER_STATUS_ORDERED_PAYED:
			return "待发货";
		case OrderState.ORDER_STATUS_OUT_OF_WH:
			return "待收货";
		case OrderState.ORDER_STATUS_CUSTOM_RECEIVED:
			return "待评价";
		case OrderState.ORDER_STATUS_ORDER_CANCELED:
			return "已取消";

		default:
			break;
		}
		return "无状态";
	}

	public String getOrderAddress() {
		return this.goodReceiverProvince + this.goodReceiverCity + this.goodReceiverCounty + this.goodReceiverAddress;
	}

	// public static class GoodsInfoBean implements Serializable {
	//
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	// // /**商品ID**/
	// // public int id;
	// /** 商品图片URL **/
	// public String productPicUrl;
	// /** 商品name **/
	// public String productCname;
	// /** 商品总额 **/
	// public float orderItemAmount;
	// /** 商品单价 **/
	// public float orderItemPrice;
	// /** 商品数量 **/
	// public int orderItemNum;
	//
	// /** 商品颜色 **/
	// public String productColor;
	// /** 商品尺码 **/
	// public String productSize;
	// /** 商品ID **/
	// public long pmInfoId;
	// /** 商品基础信息编号 **/
	// public long productId;
	//
	// public String getProductPicUrl() {
	// return productPicUrl;
	// }
	//
	// public void setProductPicUrl(String productPicUrl) {
	// this.productPicUrl = productPicUrl;
	// }
	//
	// public long getProductId() {
	// return productId;
	// }
	//
	// public void setProductId(long productId) {
	// this.productId = productId;
	// }
	//
	// public String getProductCname() {
	// return productCname;
	// }
	//
	// public void setProductCname(String productCname) {
	// this.productCname = productCname;
	// }
	//
	// public float getOrderItemAmount() {
	// return orderItemAmount;
	// }
	//
	// public void setOrderItemAmount(float orderItemAmount) {
	// this.orderItemAmount = orderItemAmount;
	// }
	//
	// public float getOrderItemPrice() {
	// return orderItemPrice;
	// }
	//
	// public void setOrderItemPrice(float orderItemPrice) {
	// this.orderItemPrice = orderItemPrice;
	// }
	//
	// public int getOrderItemNum() {
	// return orderItemNum;
	// }
	//
	// public void setOrderItemNum(int orderItemNum) {
	// this.orderItemNum = orderItemNum;
	// }
	//
	// public String getProductColor() {
	// return productColor;
	// }
	//
	// public void setProductColor(String productColor) {
	// this.productColor = productColor;
	// }
	//
	// public String getProductSize() {
	// return productSize;
	// }
	//
	// public void setProductSize(String productSize) {
	// this.productSize = productSize;
	// }
	//
	// public long getPmInfoId() {
	// return pmInfoId;
	// }
	//
	// public void setPmInfoId(long pmInfoId) {
	// this.pmInfoId = pmInfoId;
	// }
	// }

}
