package com.park61.teacherhelper.common.tool;

import java.text.DecimalFormat;

import android.text.TextUtils;

/**
 * 金融类相关通用方法
 * @author super
 */
public class FinancialUtils {
	public static DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * 规格化金额 0.00
	 */
	public static String sumFomat(Double data) {
		try {
			return new java.text.DecimalFormat("0.00").format(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "0.00";
		}
	}
	
	/**
	 * 规格化金额 
	 * @param data 需要格式化的字符串
	 */
	public static String strDbFomat(String data) {
		double dataDb = 0.00;
		try {
			dataDb = Double.parseDouble(data);
			return df.format(dataDb);
		} catch (Exception e) {
			e.printStackTrace();
			return "0.00";
		}
	}

	/**
	 * 将long类型分转化为规格化的String类型元
	 */
	public static String fenToYuan(long fen) {
		if (fen == 0) {
			return "0.00";
		}
		return df.format(Double.valueOf(fen + "") / 100);
	}

	/**
	 * 将String类型元转化为 long类型分
	 */
	public static long yuanToFen(String yuan) {
		// return (long) (Double.valueOf(yuan) * 100);
		if (TextUtils.isEmpty(yuan)) {
			return 0;
		}
		return Long.valueOf(toNormalNumber(yuan).replace(".", ""));
	}
	
	/**
	 * 规格化金额 0.000
	 */
	public static String sumFomatBig(Double data) {
		try {
			return new java.text.DecimalFormat("0.000").format(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "0.000";
		}
	}

	/**
	 * 将String类型任意格式（单位元）转化为格式化的金额（0.00形式）,不会产生近似舍入问题
	 */
	public static String toNormalNumber(String numString) {
		if (numString.equals("") || numString == null || numString.equals(".")) {
			return "0.00";
		} else {
			try {
				numString = sumFomatBig(Double.valueOf(numString));
			} catch (NumberFormatException e) {
				return "0.00";
			}
			int i = numString.indexOf(".");
			int j = numString.length();
			if (i != -1) {
				if (i == 0) {
					if (j == (i + 2)) {
						return "0" + numString + "0";
					} else {
						return "0" + numString.substring(0, 3);
					}
				} else {
					if (j == (i + 2)) {
						return numString + "0";
					} else if (j == (i + 1)) {
						return numString + "00";
					} else {
						return numString.substring(0, i + 3);
					}
				}
			} else {
				return numString + ".00";
			}

		}
	}
}
