package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;

public class PmInfo implements Serializable {

	private static final long serialVersionUID = -8039745893875612414L;
	private Long id;// 商品id
	private Long productId;// 产品id
	//private Product product;// 产品信息
	private Long merchantId;// 商家id
	private Integer canSale;// 是否可销 0-不可售 1-可售
	private Integer canShow;// 是否可见 0-不可见 1-可见
	private Integer saleType;// 销售类型

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/*public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}*/

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getCanSale() {
		return canSale;
	}

	public void setCanSale(Integer canSale) {
		this.canSale = canSale;
	}

	public Integer getCanShow() {
		return canShow;
	}

	public void setCanShow(Integer canShow) {
		this.canShow = canShow;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

}
