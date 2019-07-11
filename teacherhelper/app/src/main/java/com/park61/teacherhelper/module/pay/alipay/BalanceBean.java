package com.park61.teacherhelper.module.pay.alipay;

import java.io.Serializable;
import java.math.BigDecimal;

public class BalanceBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5276528134825807904L;

	private Long id;// bigint(20)主键id

	private Long userId;// 用户ID

	private BigDecimal availableAmount;// 可用金额

	private BigDecimal freezingAmount;// 冻结金额
	private BigDecimal giftAmount;//赠送余额
	private BigDecimal outHave;
	private BigDecimal rechargeAmount;//充值余额

	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}

	public BigDecimal getOutHave() {
		return outHave;
	}

	public void setOutHave(BigDecimal outHave) {
		this.outHave = outHave;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getFreezingAmount() {
		return freezingAmount;
	}

	public void setFreezingAmount(BigDecimal freezingAmount) {
		this.freezingAmount = freezingAmount;
	}

}
