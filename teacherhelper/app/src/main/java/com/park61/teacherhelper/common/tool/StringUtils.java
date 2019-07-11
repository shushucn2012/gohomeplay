package com.park61.teacherhelper.common.tool;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;

        return toInt(obj.toString(), 0);
    }

    public static double toDouble(String obj) {
        if (obj == null || obj.equals(""))
            return 0.0;
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        if (month.length() == 1)
            month = 0 + month;
        if (day.length() == 1)
            day = 0 + day;
        if (hour.length() == 1)
            hour = 0 + hour;
        if (mins.length() == 1)
            mins = 0 + mins;
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getStr(String str) {
        if (null == str || "null".equals(str) || "NULL".equals(str)) {
            str = "";
        }
        return str;
    }

    /**
     * @param input
     * @return
     */
    public static String escapeHtml(String input) {
        StringBuilder builder = new StringBuilder();
        int pos = 0;
        int len = input.length();
        while (pos < len) {
            int codePoint = Character.codePointAt(input, pos);
            if (codePoint == '"')
                builder.append("&quot;");
            else if (codePoint == '&')
                builder.append("&amp;");
            else if (codePoint == '<')
                builder.append("&lt;");
            else if (codePoint == '>')
                builder.append("&gt;");
            else if (codePoint == '\n')
                builder.append("<br />");
            else if (codePoint >= 0 && codePoint < 160)
                builder.append(Character.toChars(codePoint));
            else
                builder.append("&#").append(codePoint).append(';');
            pos += Character.charCount(codePoint);
        }
        return builder.toString();
    }

    public static String moneyUnit(){
        return "￥";
    }

}