package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by HP on 2016/10/27.
 */
public class TradeOrderConfirm implements Serializable {
    private String merchant;//商家名称
    private List<MerchantTradeOrder> merchantOrder;
    private BigDecimal merchantOrderAmount;
    private BigDecimal merchantOrderDeliveryFee;
    private int merchantType;
    private int orderNum;//包裹数
    private int pmNum;//商品总数
    private List<String> productPicUrlList;

    public List<String> getProductPicUrlList() {
        return productPicUrlList;
    }

    public void setProductPicUrlList(List<String> productPicUrlList) {
        this.productPicUrlList = productPicUrlList;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public List<MerchantTradeOrder> getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(List<MerchantTradeOrder> merchantOrder) {
        this.merchantOrder = merchantOrder;
    }

    public BigDecimal getMerchantOrderAmount() {
        return merchantOrderAmount;
    }

    public void setMerchantOrderAmount(BigDecimal merchantOrderAmount) {
        this.merchantOrderAmount = merchantOrderAmount;
    }

    public BigDecimal getMerchantOrderDeliveryFee() {
        return merchantOrderDeliveryFee;
    }

    public void setMerchantOrderDeliveryFee(BigDecimal merchantOrderDeliveryFee) {
        this.merchantOrderDeliveryFee = merchantOrderDeliveryFee;
    }

    public int getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(int merchantType) {
        this.merchantType = merchantType;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getPmNum() {
        return pmNum;
    }

    public void setPmNum(int pmNum) {
        this.pmNum = pmNum;
    }
}
