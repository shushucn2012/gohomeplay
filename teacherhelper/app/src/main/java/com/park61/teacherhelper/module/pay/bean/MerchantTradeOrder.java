package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by HP on 2016/10/27.
 */
public class MerchantTradeOrder implements Serializable {
    private BigDecimal couponAmount;
    private BigDecimal discountAmount;
    private int merchantId;//商家id
    private int merchantType;
    private BigDecimal orderDeliveryFee;
    private int orderItemNum;
    private int orderWeight;
    private BigDecimal productAmount;
    private List<TradeOrderItem> orderItemList;
    private String isFightGroup;//true:拼团

    public String getIsFightGroup() {
        return isFightGroup;
    }

    public void setIsFightGroup(String isFightGroup) {
        this.isFightGroup = isFightGroup;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(int merchantType) {
        this.merchantType = merchantType;
    }

    public BigDecimal getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public void setOrderDeliveryFee(BigDecimal orderDeliveryFee) {
        this.orderDeliveryFee = orderDeliveryFee;
    }

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public int getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(int orderWeight) {
        this.orderWeight = orderWeight;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public List<TradeOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TradeOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
