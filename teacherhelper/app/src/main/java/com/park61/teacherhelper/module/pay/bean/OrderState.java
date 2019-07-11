package com.park61.teacherhelper.module.pay.bean;

public class OrderState {
	/* 订单状态------------------------------------------------ */
	/** 1 已下单:货款未全收 */
	public static final int ORDER_STATUS_ORDERED_WAITING_FOR_PAYMENT = 1;
	/** 2 已下单:货款已全收 */
	public static final int ORDER_STATUS_ORDERED_PAYED = 2;
	/** 3 已转DO */
	public static final int ORDER_STATUS_TRUNED_TO_DO = 3;
	/** 4 已出库（货在途） */
	public static final int ORDER_STATUS_OUT_OF_WH = 4;
	/** 5 货物用户已收货 */
	public static final int ORDER_STATUS_CUSTOM_RECEIVED = 5;
	/** 6 送货失败（其它） */
	public static final int ORDER_STATUS_DELIVER_FAILED_OTHER = 6;
	/** 7 要求退货 */
	public static final int ORDER_STATUS_TO_RETURN = 7;
	/** 8 退货中 */
	public static final int ORDER_STATUS_RETURNING = 8;
	/** 9 退货完成 */
	public static final int ORDER_STATUS_RETURNED = 9;
	/** 10 订单取消 */
	public static final int ORDER_STATUS_ORDER_CANCELED = 10;
	/** 11 订单完成 */
	public static final int ORDER_STATUS_ORDER_FINISHED = 11;
}
