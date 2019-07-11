package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算订单明细V1版
 * 
 * @author wangfei
 * @date 2016年3月6日
 * 
 */
public class TradeOrderItem implements Serializable {

	private static final long serialVersionUID = 1066433284960521278L;
	// 商品信息
	private ProductMerchant productMerchant;
	// 商品购买数量
	private Integer orderItemNum;
	// 商品总金额
	private BigDecimal orderItemAmount;

	public ProductMerchant getProductMerchant() {
		return productMerchant;
	}

	public void setProductMerchant(ProductMerchant productMerchant) {
		this.productMerchant = productMerchant;
	}

	public Integer getOrderItemNum() {
		return orderItemNum;
	}

	public void setOrderItemNum(Integer orderItemNum) {
		this.orderItemNum = orderItemNum;
	}

	public BigDecimal getOrderItemAmount() {
		return orderItemAmount;
	}

	public void setOrderItemAmount(BigDecimal orderItemAmount) {
		this.orderItemAmount = orderItemAmount;
	}

}
