package com.park61.teacherhelper.common.set;

/**
 * Log调试
 *
 * @author super
 */
public final class Log {

    private static boolean mDebug = true;

    /**
     * 打开或关闭log
     *
     * @param debug
     */
    public static void init(final boolean debug) {
        mDebug = debug;
    }

    public static void out(String msg) {
        if (mDebug) {
            android.util.Log.e("qjw", msg);
        }
    }

    public static void out(String msg, int n) {
        if (mDebug) {
            for (int i = 0; i < n; i++) {
                android.util.Log.e("qjw", msg.substring(msg.length()/n*i, msg.length()/n*(i+1)));
            }
        }
    }

    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void outMore(String msg) {
        String TAG = "qjw";
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void e(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void w(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.w(tag, msg, tr);
        }
    }

    public static void d(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void v(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.v(tag, msg, tr);
        }
    }

    public static void i(final String tag, final String msg) {
        if (mDebug) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(final String tag, final String msg, final Throwable tr) {
        if (mDebug) {
            android.util.Log.i(tag, msg, tr);
        }
    }
}
