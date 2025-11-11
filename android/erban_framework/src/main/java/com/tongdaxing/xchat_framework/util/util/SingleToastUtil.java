package com.tongdaxing.xchat_framework.util.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.widget.Toast;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.view.toast.ToastCompat;

/**
 * Created by qinbo on 2014/8/12.
 */
public class SingleToastUtil {
    private static String oldMsg;
    protected static ToastCompat toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static Handler handler;

    public static void showToast(String s) {
        showToast(null, s, Toast.LENGTH_LONG);
    }

    public static void showShortToast(String s) {
        showToast(null, s, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String s) {
        showToast(context, s, Toast.LENGTH_LONG);
    }

    private static void doShowToast(Context context, String s, int length) {
        if (context == null || StringUtils.isEmpty(s))
            return;
        if (toast == null) {
            toast = ToastCompat.makeText(context, s, length);
            toast.setGravity(Gravity.CENTER,0,0);
            try {
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            oneTime = SystemClock.uptimeMillis();
        } else {
            twoTime = SystemClock.uptimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > length) {
                    //Android 7.1 机型上报告的由Toast引起的BadTokenException错误？
                    //https://segmentfault.com/q/1010000012340763
                    try {//android.widget.Toast$TN.handleShow(Toast.java:465) 的问题
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                try {
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context mContext, final String s, final int length) {
        // 内存泄露的解决
        final Context context = BasicConfig.INSTANCE.getAppContext();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            synchronized (SingleToastUtil.class) {
                if (null == handler) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    doShowToast(context, s, length);
                }
            });
        } else {
            doShowToast(context, s, length);
        }
    }

    public static void showToast(Context context, int resId, int length) {
        showToast(context, context.getString(resId), length);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_LONG);
    }

    public static void replaceToast(Context context, String str) {
        if (toast == null) {
            showToast(context, str, Toast.LENGTH_LONG);
        } else {
            oldMsg = str;
            toast.setText(str);
        }
    }
}