package com.tongdaxing.xchat_framework.util.util;

public class CommonUtils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick(long timeLong) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < timeLong) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }


}
