package com.tongdaxing.xchat_framework.util.util;

import android.app.ActivityManager;
import android.content.Context;

import com.tongdaxing.xchat_framework.util.util.codec.MD5Utils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.util.List;

/**
 * 进程相关的工具类
 * @author:wsq
 * @time:Dec 16, 2014 4:52:28 PM
 */
public final class ProcessUtil {

    /**
     * 获取进程相关的文件名，除了主进程，其他进程文件会改变为fileName_mb5(进程名).hashCode()
     * @param context
     * @param fileName
     * @return
     */
    public static String getFileNameBindProcess(Context context, String fileName) {
        try{
            if(!isMainProcess(context)){
                fileName = fileName+"_"+ MD5Utils.getMD5String(getCurProcessName(context)).hashCode();
            }
        }catch(Exception e){
//			fileName += "_unknown";
            MLog.error(ProcessUtil.class, "fileName[%s] instead of it,exception on getFileNameBindProcess: %s ", fileName, e);
        }
        return fileName;
    }

    /**
     * 获取当前进程名称
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        if(context==null){
            return null;
        }
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 判断当前进程是否android的主进程
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context){
        if(context==null){
            return false;
        }
        String curProcessName = getCurProcessName(context);
        String processName = context.getApplicationInfo().processName;
        return processName.equals(curProcessName);
    }

    /**
     * app是否处于后台
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        try{
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(packageName)) {
                    return (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND);
                }
            }

            return false;
        }catch(Exception e){
            MLog.error(ProcessUtil.class, "isBackground exceptioon: %s",e);
            return false;
        }
    }
}

