package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class PmPrice implements Serializable {

	private static final long serialVersionUID = 3834359026318996986L;
	private Long pmInfoId;
	private BigDecimal marketPrice;// 市场价
	private BigDecimal currentUnifyPrice;// 当前统一售价
	private Integer promType;// 价格促销类型 0-不促销
	private BigDecimal promPrice;// 促销价格
	private String promStartTime;// 促销开始时间
	private String promEndTime;// 促销结束时间
	private BigDecimal fightGroupPrice;
	private Long fightGroupDetailId;
	private int fightGroupStockNum;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BigDecimal getFightGroupPrice() {
		return fightGroupPrice;
	}

	public void setFightGroupPrice(BigDecimal fightGroupPrice) {
		this.fightGroupPrice = fightGroupPrice;
	}

	public Long getFightGroupDetailId() {
		return fightGroupDetailId;
	}

	public void setFightGroupDetailId(Long fightGroupDetailId) {
		this.fightGroupDetailId = fightGroupDetailId;
	}

	public int getFightGroupStockNum() {
		return fightGroupStockNum;
	}

	public void setFightGroupStockNum(int fightGroupStockNum) {
		this.fightGroupStockNum = fightGroupStockNum;
	}

	public Long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(Long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getCurrentUnifyPrice() {
		return currentUnifyPrice;
	}

	public void setCurrentUnifyPrice(BigDecimal currentUnifyPrice) {
		this.currentUnifyPrice = currentUnifyPrice;
	}

	public Integer getPromType() {
		return promType;
	}

	public void setPromType(Integer promType) {
		this.promType = promType;
	}

	public BigDecimal getPromPrice() {
		return promPrice;
	}

	public void setPromPrice(BigDecimal promPrice) {
		this.promPrice = promPrice;
	}

	public String getPromStartTime() {
		return promStartTime;
	}

	public void setPromStartTime(String promStartTime) {
		this.promStartTime = promStartTime;
	}

	public String getPromEndTime() {
		return promEndTime;
	}

	public void setPromEndTime(String promEndTime) {
		this.promEndTime = promEndTime;
	}

}
