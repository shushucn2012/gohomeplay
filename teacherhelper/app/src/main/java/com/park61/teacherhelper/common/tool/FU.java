package com.park61.teacherhelper.common.tool;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.park61.teacherhelper.common.set.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 金额数字工具类 简写
 *
 * @author shushucn2012
 */
public class FU {

    public static String RENMINBI_UNIT = "¥";

    /**
     * 规格化金额 0.00
     *
     * @param data 需要格式化的字符串
     */
    public static String strDbFmt(String data) {
        DecimalFormat df = new DecimalFormat("0.00");
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
     * 规格化金额 0.00
     *
     * @param data 需要格式化的字符串
     */
    public static String strBFmt(BigDecimal data) {
        DecimalFormat df = new DecimalFormat("0.00");
        double dataDb = 0.00;
        try {
            dataDb = Double.parseDouble(data + "");
            return df.format(dataDb);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * 规格化金额 0.00
     *
     * @param data 需要格式化的字符串
     */
    public static String strDbFmt(double data) {
        DecimalFormat df = new DecimalFormat("0.00");
        double dataDb = 0.00;
        try {
            dataDb = Double.parseDouble(data + "");
            return df.format(dataDb);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * 规格化金额 0.00
     *
     * @param data 需要格式化的字符串
     */
    public static String strDbFmt(float data) {
        DecimalFormat df = new DecimalFormat("0.00");
        double dataDb = 0.00;
        try {
            dataDb = Double.parseDouble(data + "");
            return df.format(dataDb);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static double paseDb(String dbStr) {
        Log.out("dbStr========================" + dbStr);
        double mDb = 0.00;
        try {
            mDb = Double.parseDouble(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.out("mDb========================" + mDb);
        return mDb;
    }

    public static int paseInt(String intStr) {
        int mInt = 0;
        try {
            mInt = Integer.parseInt(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mInt;
    }

    public static long paseLong(String intStr) {
        long mlong = 0;
        try {
            mlong = Long.parseLong(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.out("mlong======" + mlong);
        return mlong;
    }

    public static boolean isNumEmpty(String data) {
        if ("0.00".equals(strDbFmt(data)))
            return true;
        return false;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param d1 加数
     * @param d2 加数
     * @return 两个参数的和
     */
    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 金额为0转成免费
     */
    public static String zeroToMF(String myprice) {
        String price = strDbFmt(myprice);
        String priceStrShow = "";
        if ("0.00".equals(price)) {
            priceStrShow = "免费";
        } else {
            priceStrShow = "￥" + price;
        }
        return priceStrShow;
    }

    /**
     * 金额格式化double类型
     */
    public static SpannableString formatPrice(Double old_price) {
        if (old_price == null) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtSmall(new_price);
    }

    /**
     * 金额格式化String类型
     */
    public static SpannableString formatPrice(String old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtSmall(new_price);
    }

    /**
     * 金额格式化BigDecimal类型
     */
    public static SpannableString formatPrice(BigDecimal old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strBFmt(old_price);
        return fmtSmall(new_price);
    }

    /**
     * 金额格式化BigDecimal类型
     */
    public static SpannableString formatPrice(Float old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtSmall(new_price);
    }

    /**
     * 金额格式化double类型
     */
    public static SpannableString formatBigPrice(Double old_price) {
        if (old_price == null) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtBig(new_price);
    }

    /**
     * 金额格式化String类型
     */
    public static SpannableString formatBigPrice(String old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtBig(new_price);
    }

    /**
     * 金额格式化BigDecimal类型
     */
    public static SpannableString formatBigPrice(BigDecimal old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strBFmt(old_price);
        return fmtBig(new_price);
    }

    /**
     * 金额格式化Float类型
     */
    public static SpannableString formatBigPrice(Float old_price) {
        if (old_price == null || "".equals(old_price)) {
            return getNullSpan();
        }
        String new_price = strDbFmt(old_price);
        return fmtBig(new_price);
    }

    /**
     * 返回空金额sp
     */
    public static SpannableString getNullSpan() {
        String nullPrice = "￥";
        SpannableString spNull = new SpannableString(nullPrice);
        spNull.setSpan(new AbsoluteSizeSpan(10, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spNull;
    }

    /**
     * 返回大字体格式化后sp
     */
    public static SpannableString fmtBig(String newPrice) {
        String[] strArr = newPrice.split("\\.");
        String priceNum = strArr[0];
        String priceStrWhole = "";
        boolean isHasPoint = false;
        if (!"00".equals(strArr[1])) {//小数位不为空或0时，加上小数第一位
            priceNum = priceNum + "." + strArr[1].substring(0, 2);
            isHasPoint = true;
        }
        priceStrWhole = "￥" + priceNum;
        SpannableString sp = new SpannableString(priceStrWhole);
        sp.setSpan(new AbsoluteSizeSpan(13, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isHasPoint) {
            sp.setSpan(new AbsoluteSizeSpan(13, true), priceStrWhole.length() - 2,
                    priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    /**
     * 返回小字体格式化后sp
     */
    public static SpannableString fmtSmall(String newPrice) {
        String[] strArr = newPrice.split("\\.");
        String priceNum = strArr[0];
        String priceStrWhole = "";
        boolean isHasPoint = false;
        if (!"00".equals(strArr[1])) {//小数位不为空或0时，加上小数第一位
            priceNum = priceNum + "." + strArr[1].substring(0, 2);
            isHasPoint = true;
        }
        priceStrWhole = "￥" + priceNum;
        SpannableString sp = new SpannableString(priceStrWhole);
        sp.setSpan(new AbsoluteSizeSpan(12, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isHasPoint) {
            sp.setSpan(new AbsoluteSizeSpan(12, true), priceStrWhole.length() - 2,
                    priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    /**
     * 格式化金额 ￥变小
     *
     * @param size                变小的大小
     * @param needSmallAfterPoint 是否需要变小小数
     */
    public static SpannableString fmtSmallFrontUnit(String newPrice, int size, boolean needSmallAfterPoint) {
        String[] strArr = newPrice.split("\\.");
        String priceNum = strArr[0];
        String priceStrWhole = "";
        boolean isHasPoint = false;
        if (!"00".equals(strArr[1])) {//小数位不为空或0时，加上小数第一位
            priceNum = priceNum + "." + strArr[1].substring(0, 2);
            isHasPoint = true;
        }
        priceStrWhole = FU.RENMINBI_UNIT + priceNum;
        SpannableString sp = new SpannableString(priceStrWhole);
        sp.setSpan(new AbsoluteSizeSpan(size, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isHasPoint && needSmallAfterPoint) {
            sp.setSpan(new AbsoluteSizeSpan(size, true), priceStrWhole.length() - 2,
                    priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    public static String formatPaiseNum(int num) {
        String numStr = num + "";
        if (numStr.length() >= 5) {
            return numStr.substring(0, numStr.length() - 4) + "万";
        }
        return numStr;
    }

    public static String addDownLoadNumStr(String nowNum) {
        String result = nowNum;
        try {
            int mInt = Integer.parseInt(nowNum);
            result = (mInt + 1) + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 时间数字小于10前面补0
     *
     * @param num
     * @return
     */
    public static String addFrontZero(int num) {
        return num < 10 ? "0" + num : num + "";
    }

    /**
     * 时间数字小于10前面补0
     *
     * @param num
     * @return
     */
    public static String addFrontZero(long num) {
        return num < 10 ? "0" + num : num + "";
    }


}
