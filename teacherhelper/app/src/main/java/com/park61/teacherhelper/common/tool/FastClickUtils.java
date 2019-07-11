package com.park61.teacherhelper.common.tool;

/**
 * 判断是否快速点击工具类
 * Created by shubei on 2018/5/7.
 */

public class FastClickUtils {

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    /**
     * 是否快速点击
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
