package com.tongdaxing.xchat_framework.http_image.http;


import com.tongdaxing.xchat_framework.util.util.log.MLog;

/**
 * Created by zhongyongsheng on 14-4-4.
 */
public class HttpLog {

    private static final String TAG = "HttpLog";

    public static boolean isDebug() {
        return true;
    }

    public static void v(String format, Object... args) {
        MLog.verbose(TAG, format, args);
    }

    public static void d(String format, Object... args) {
        MLog.debug(TAG, format, args);
    }

    public static void e(String format, Object... args) {
        MLog.error(TAG, format, args);
    }

    public static void e(Throwable tr, String format, Object... args) {
        MLog.error(TAG, format, tr, args);
    }

    static String format(String format, Object... args) {
        try {
            return String.format(format, args);
        } catch (Exception e) {
            return "";
        }
    }

}
