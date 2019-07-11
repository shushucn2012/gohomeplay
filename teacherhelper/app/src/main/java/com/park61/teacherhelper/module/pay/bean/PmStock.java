package com.park61.teacherhelper.module.pay.bean;

import java.io.Serializable;

/**
 * 仓库实际库存<br>
 * 暂不考虑虚拟库存，后期再引入
 * 
 * @author wangfei
 * @date 2016年3月7日
 * 
 */
public class PmStock implements Serializable {

	private static final long serialVersionUID = 2080564319102531024L;
	private Long pmInfoId;// 商品id
	private Long warehouseId;// 仓库id
	private Long realStockNum;// 真实库存
	private Long realFrozenStockNum;// 真实冻结库存

	public Long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(Long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public void setRealStockNum(Long realStockNum) {
		this.realStockNum = realStockNum;
	}

	public void setRealFrozenStockNum(Long realFrozenStockNum) {
		this.realFrozenStockNum = realFrozenStockNum;
	}

	/**
	 * 第一版：可用库存=真实库存-真实冻结库存
	 */
	public Long getAvailableStockNum() {
		long rs = realStockNum == null ? 0L : realStockNum;
		long rfs = realFrozenStockNum == null ? 0L : realFrozenStockNum;
		long as = rs - rfs;
		if (as > 0) {
			return as;
		} else {
			return 0L;
		}
	}
}
