package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;

public class ProductMerchant implements Serializable {

	private static final long serialVersionUID = 8302600043690077582L;
	/** 商品基本信息 */
	private PmInfo pmInfo;
	/** 商品价格信息 */
	private PmPrice pmPrice;
	/** 商品库存 */
	private PmStock pmStock;

	public PmInfo getPmInfo() {
		return pmInfo;
	}

	public void setPmInfo(PmInfo pmInfo) {
		this.pmInfo = pmInfo;
	}

	public PmPrice getPmPrice() {
		return pmPrice;
	}

	public void setPmPrice(PmPrice pmPrice) {
		this.pmPrice = pmPrice;
	}

	public PmStock getPmStock() {
		return pmStock;
	}

	public void setPmStock(PmStock pmStock) {
		this.pmStock = pmStock;
	}

}
