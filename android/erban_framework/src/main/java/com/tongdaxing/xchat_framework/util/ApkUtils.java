package com.tongdaxing.xchat_framework.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Function:
 * Author: Edward on 2019/6/4
 */
public class ApkUtils {
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
