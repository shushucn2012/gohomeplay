package com.park61.teacherhelper.common.tool;

import android.content.Context;

/**
 * 设备（屏幕）属性
 * @author super
 */
public class DevAttr {

	/**
	 * 得到设备屏幕的宽度
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 得到设备屏幕的高度
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 得到设备的密度
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 把dp转换为像素
	 */
	public static int dip2px(Context context, float px) {
		float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

}
