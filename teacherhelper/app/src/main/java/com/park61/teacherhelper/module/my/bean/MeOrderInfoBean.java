package com.park61.teacherhelper.module.my.bean;

import java.io.Serializable;
import java.util.List;

public class MeOrderInfoBean implements Serializable {


	/**
	 * id : 101760075088
	 * endUserId : null
	 * orderAmount : 0.02
	 * productAmount : null
	 * orderDeliveryFee : null
	 * orderToPayAmount : 0.02
	 * couponAmount : null
	 * discountAmount : null
	 * orderStatus : 10
	 * orderStatusName : 订单取消
	 * orderType : 0
	 * orderRemark : null
	 * orderCreateTime : 2018-01-11 11:19:31
	 * paidOnlineThreshold : null
	 * parentSoId : null
	 * isLeaf : null
	 * goodReceiverName : null
	 * goodReceiverProvince : null
	 * goodReceiverCity : null
	 * goodReceiverCounty : null
	 * goodReceiverTown : null
	 * goodReceiverAddress : null
	 * goodReceiverPostcode : null
	 * goodReceiverMobile : null
	 * deliveryRemark : null
	 * countDownTime : 00:00:00
	 * items : null
	 * isEvaluated : null
	 * childOrders : null
	 * picUrlList : ["http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105237232_118.png"]
	 * packageNum : 1
	 * orderItemNum : 1
	 * fightGroupOpenId : null
	 * fightGroupOpensStatus : null
	 * orderCancelReason : 支付超时，交易关闭
	 * returnFlag : true
	 * merchantId : null
	 * thirdPartyOrderId : null
	 * orderDeliveryCode : null
	 * orderDeliveryCompany : null
	 * merchantName : null
	 * returnAmount : null
	 * merchantOrder : null
	 */

	private long id;
	private String endUserId;
	private double orderAmount;
	private String productAmount;
	private String orderDeliveryFee;
	private double orderToPayAmount;
	private String couponAmount;
	private String discountAmount;
	private int orderStatus;
	private String orderStatusName;
	private int orderType;
	private String orderRemark;
	private String orderCreateTime;
	private String paidOnlineThreshold;
	private String parentSoId;
	private String isLeaf;
	private String goodReceiverName;
	private String goodReceiverProvince;
	private String goodReceiverCity;
	private String goodReceiverCounty;
	private String goodReceiverTown;
	private String goodReceiverAddress;
	private String goodReceiverPostcode;
	private String goodReceiverMobile;
	private String deliveryRemark;
	private String countDownTime;
	private String items;
	private String isEvaluated;
	private String childOrders;
	private int packageNum;
	private int orderItemNum;
	private String fightGroupOpenId;
	private String fightGroupOpensStatus;
	private String orderCancelReason;
	private boolean returnFlag;
	private String merchantId;
	private String thirdPartyOrderId;
	private String orderDeliveryCode;
	private String orderDeliveryCompany;
	private String merchantName;
	private String returnAmount;
	private String merchantOrder;
	private List<String> picUrlList;
	private String coverUrl;
	private String amount;
	private int status;//订单状态 0：未支付 1：订单取消 2：订单完成
	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(String endUserId) {
		this.endUserId = endUserId;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(String productAmount) {
		this.productAmount = productAmount;
	}

	public String getOrderDeliveryFee() {
		return orderDeliveryFee;
	}

	public void setOrderDeliveryFee(String orderDeliveryFee) {
		this.orderDeliveryFee = orderDeliveryFee;
	}

	public double getOrderToPayAmount() {
		return orderToPayAmount;
	}

	public void setOrderToPayAmount(double orderToPayAmount) {
		this.orderToPayAmount = orderToPayAmount;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public String getPaidOnlineThreshold() {
		return paidOnlineThreshold;
	}

	public void setPaidOnlineThreshold(String paidOnlineThreshold) {
		this.paidOnlineThreshold = paidOnlineThreshold;
	}

	public String getParentSoId() {
		return parentSoId;
	}

	public void setParentSoId(String parentSoId) {
		this.parentSoId = parentSoId;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getGoodReceiverName() {
		return goodReceiverName;
	}

	public void setGoodReceiverName(String goodReceiverName) {
		this.goodReceiverName = goodReceiverName;
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

	public String getGoodReceiverTown() {
		return goodReceiverTown;
	}

	public void setGoodReceiverTown(String goodReceiverTown) {
		this.goodReceiverTown = goodReceiverTown;
	}

	public String getGoodReceiverAddress() {
		return goodReceiverAddress;
	}

	public void setGoodReceiverAddress(String goodReceiverAddress) {
		this.goodReceiverAddress = goodReceiverAddress;
	}

	public String getGoodReceiverPostcode() {
		return goodReceiverPostcode;
	}

	public void setGoodReceiverPostcode(String goodReceiverPostcode) {
		this.goodReceiverPostcode = goodReceiverPostcode;
	}

	public String getGoodReceiverMobile() {
		return goodReceiverMobile;
	}

	public void setGoodReceiverMobile(String goodReceiverMobile) {
		this.goodReceiverMobile = goodReceiverMobile;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}

	public String getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(String countDownTime) {
		this.countDownTime = countDownTime;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getIsEvaluated() {
		return isEvaluated;
	}

	public void setIsEvaluated(String isEvaluated) {
		this.isEvaluated = isEvaluated;
	}

	public String getChildOrders() {
		return childOrders;
	}

	public void setChildOrders(String childOrders) {
		this.childOrders = childOrders;
	}

	public int getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(int packageNum) {
		this.packageNum = packageNum;
	}

	public int getOrderItemNum() {
		return orderItemNum;
	}

	public void setOrderItemNum(int orderItemNum) {
		this.orderItemNum = orderItemNum;
	}

	public String getFightGroupOpenId() {
		return fightGroupOpenId;
	}

	public void setFightGroupOpenId(String fightGroupOpenId) {
		this.fightGroupOpenId = fightGroupOpenId;
	}

	public String getFightGroupOpensStatus() {
		return fightGroupOpensStatus;
	}

	public void setFightGroupOpensStatus(String fightGroupOpensStatus) {
		this.fightGroupOpensStatus = fightGroupOpensStatus;
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getThirdPartyOrderId() {
		return thirdPartyOrderId;
	}

	public void setThirdPartyOrderId(String thirdPartyOrderId) {
		this.thirdPartyOrderId = thirdPartyOrderId;
	}

	public String getOrderDeliveryCode() {
		return orderDeliveryCode;
	}

	public void setOrderDeliveryCode(String orderDeliveryCode) {
		this.orderDeliveryCode = orderDeliveryCode;
	}

	public String getOrderDeliveryCompany() {
		return orderDeliveryCompany;
	}

	public void setOrderDeliveryCompany(String orderDeliveryCompany) {
		this.orderDeliveryCompany = orderDeliveryCompany;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getMerchantOrder() {
		return merchantOrder;
	}

	public void setMerchantOrder(String merchantOrder) {
		this.merchantOrder = merchantOrder;
	}

	public List<String> getPicUrlList() {
		return picUrlList;
	}

	public void setPicUrlList(List<String> picUrlList) {
		this.picUrlList = picUrlList;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
