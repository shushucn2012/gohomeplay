package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 结算订单V1版
 * 
 * @author wangfei
 * @date 2016年3月6日
 * 
 */
public class TradeOrder implements Serializable {

	private static final long serialVersionUID = 6444361801681508702L;
	public static final String BUY_FROM_DETAIL = "detail";
	public static final String BUY_FROM_CART = "cart";
	public static final Integer SPLIT_ORDER = 1;
	public static final Integer UNSPLIT_ORDER = 2;
	/** 以下为结算页展示数据 */
	// 默认收货地址
	private GoodReceiverVO goodReceiver;
	// 订单明细
	private List<TradeOrderItem> orderItemList;
	// 订单金额=商品总金额+运费
	private BigDecimal orderAmount;
	// 待支付金额
	private BigDecimal orderToPayAmount;
	// 商品总金额
	private BigDecimal productAmount;
	// 优惠券抵扣金额
	private BigDecimal couponAmount;
	// 运费
	private BigDecimal orderDeliveryFee;
	// 订单重量
	private BigDecimal orderWeight;
	// 推荐码
	private String recommenderCode;
	// 优惠券id
	private Long couponUseId;
	// 订单可用优惠券数量
	private Integer couponCount;
	// 商家id
	private Long merchantId;
	// 商家类型 0-自营 1-商家
	private Integer merchantType;
	// 默认供应商id
	private Long supplierId;
	// 是否拆单 0-否 1-是
	private Integer isSplit;
	// 子单
	private List<TradeOrder> childOrders;
	// 用户id
	private Long userId;
	private int orderItemNum;//单个包裹商品总数

	/** 以下为提交订单成功后支付页展示数据 */
	// 订单ID
	private Long orderId;
	// 支付方式
	private List<PaymentMethod> paymentMethodList;
	// 账户余额
	BigDecimal accountBalance;
	private Long fightGroupOpenId;
	private String isFightGroup;
	private ArrayList<TradeOrderConfirm> merchantOrderList;
	private int containOverSeas;// 是否包含海淘商品 0 不包含，1 包含

	public int getContainOverSeas() {
		return containOverSeas;
	}

	public void setContainOverSeas(int containOverSeas) {
		this.containOverSeas = containOverSeas;
	}

	public ArrayList<TradeOrderConfirm> getMerchantOrderList() {
		return merchantOrderList;
	}

	public void setMerchantOrderList(ArrayList<TradeOrderConfirm> merchantOrderList) {
		this.merchantOrderList = merchantOrderList;
	}

	public Long getFightGroupOpenId() {
		return fightGroupOpenId;
	}

	public void setFightGroupOpenId(Long fightGroupOpenId) {
		this.fightGroupOpenId = fightGroupOpenId;
	}

	public String getIsFightGroup() {
		return isFightGroup;
	}

	public void setIsFightGroup(String isFightGroup) {
		this.isFightGroup = isFightGroup;
	}

	public BigDecimal discountAmount;//满减金额

	public int getOrderItemNum() {
		return orderItemNum;
	}

	public void setOrderItemNum(int orderItemNum) {
		this.orderItemNum = orderItemNum;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public GoodReceiverVO getGoodReceiver() {
		return goodReceiver;
	}

	public void setGoodReceiver(GoodReceiverVO goodReceiver) {
		this.goodReceiver = goodReceiver;
	}

	public List<TradeOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TradeOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public BigDecimal getOrderAmount() {
		orderAmount = (productAmount == null ? BigDecimal.ZERO : productAmount)
				.add(orderDeliveryFee == null ? BigDecimal.ZERO
						: orderDeliveryFee);
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public BigDecimal getOrderDeliveryFee() {
		return orderDeliveryFee;
	}

	public void setOrderDeliveryFee(BigDecimal orderDeliveryFee) {
		this.orderDeliveryFee = orderDeliveryFee;
	}

	public BigDecimal getOrderWeight() {
		return orderWeight;
	}

	public void setOrderWeight(BigDecimal orderWeight) {
		this.orderWeight = orderWeight;
	}

	public String getRecommenderCode() {
		return recommenderCode;
	}

	public void setRecommenderCode(String recommenderCode) {
		this.recommenderCode = recommenderCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public List<PaymentMethod> getPaymentMethodList() {
		return paymentMethodList;
	}

	public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Long getCouponUseId() {
		return couponUseId;
	}

	public void setCouponUseId(Long couponUseId) {
		this.couponUseId = couponUseId;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public BigDecimal getOrderToPayAmount() {
		return orderToPayAmount;
	}

	public void setOrderToPayAmount(BigDecimal orderToPayAmount) {
		this.orderToPayAmount = orderToPayAmount;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public List<TradeOrder> getChildOrders() {
		return childOrders;
	}

	public void setChildOrders(List<TradeOrder> childOrders) {
		this.childOrders = childOrders;
	}

	public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
