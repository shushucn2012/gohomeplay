package com.park61.teacherhelper.common.tool;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.park61.teacherhelper.common.set.Log;

/**
 * 日期工具类
 */
public class DateTool {

    public static final String DATE_TIME_FORMAT_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_PATTERN_2 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT_PATTERN_3 = "yyyyMMdd";
    public static final String DATE_TIME_FORMAT_PATTERN_4 = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_PATTERN_5 = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT_PATTERN_6 = "HH:mm";
    public static final String DATE_TIME_FORMAT_PATTERN_7 = "yyyy/MM/dd";
    public static final String DATE_TIME_FORMAT_PATTERN_8 = "MM.dd";

    /**
     * 把时间戳转成时间字符串
     */
    public static String L2SEndDay(String timestamp) {
        if (timestamp == null || timestamp.equals(""))
            return "";
        Long tsp = Long.parseLong(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_4);
        return sdf.format(new Date(tsp));
    }

    /**
     * 以"yyyyMMdd"形式返回当前系统时间
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
        return simpledateformat.format(new Date()).toString();
    }

    /**
     * 以"yyyyMMdd"形式返回时间
     *
     * @return
     */
    public static String getTheDateStrP3(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_3);
        return simpledateformat.format(date).toString();
    }

    public static String getTheDateSt(Date date, String patten) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(patten);
        return simpledateformat.format(date).toString();
    }

    /**
     * 以"yyyy/MM/dd"形式返回时间
     */
    public static String getTheDateStrP7(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_7);
        return simpledateformat.format(date).toString();
    }

    /**
     * 以"yyyy-MM-dd"形式返回时间
     */
    public static String getTheDateStrP4(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_4);
        return simpledateformat.format(date).toString();
    }

    /**
     * 以"MM.dd"形式返回时间
     */
    public static String getTheDateStrP8(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN_8);
        return simpledateformat.format(date).toString();
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate2(long date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate
     * @return date
     */
    public static Date parseDateStr(String strDate) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //加上时间
        Date date = null;
        //必须捕获异常
        try {
            date = sDateFormat.parse(strDate);
        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate
     * @return date
     */
    public static Date parseDateStr2(String strDate) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd"); //加上时间
        Date date = null;
        //必须捕获异常
        try {
            date = sDateFormat.parse(strDate);
        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    public static Date parseDateStr(String strDate, String partten) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(partten); //加上时间
        Date date = null;
        //必须捕获异常
        try {
            date = sDateFormat.parse(strDate);
        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    /**
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static long[] formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return new long[]{days, hours, minutes, seconds};
    }

    /**
     * 获取当前年份
     */
    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

    //得到本月的第一天
    public static String getMonthFirstDay(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat firstDay = new SimpleDateFormat("yyyy/MM/dd");
        return firstDay.format(calendar.getTime());
    }

    //得到本月的最后一天
    public static String getMonthLastDay(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat lastDay = new SimpleDateFormat("yyyy/MM/dd");
        return lastDay.format(calendar.getTime());
    }

    /**
     * 判断时间是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    public static boolean isToday(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
    }
}
