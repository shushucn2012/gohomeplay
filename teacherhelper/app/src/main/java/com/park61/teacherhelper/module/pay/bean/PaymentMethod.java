package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentMethod implements Serializable {

	private static final long serialVersionUID = 8155960183985502804L;
	private Long id;// 主键id
	private String paymentMethodName;// 支付方式名称
	private Integer paymentMethodType;// 1-网上支付 2-余额支付
	private String paymentMethodPicUrl;// 图标地址
	private Integer isDefault;// 是否默认支付方式 0-否 1-是
	private BigDecimal accountBalance;// 账户余额
	private boolean isEnough = true;// 是否足够支付

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	public Integer getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(Integer paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public String getPaymentMethodPicUrl() {
		return paymentMethodPicUrl;
	}

	public void setPaymentMethodPicUrl(String paymentMethodPicUrl) {
		this.paymentMethodPicUrl = paymentMethodPicUrl;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public boolean isEnough() {
		return isEnough;
	}

	public void setEnough(boolean enough) {
		isEnough = enough;
	}

}
