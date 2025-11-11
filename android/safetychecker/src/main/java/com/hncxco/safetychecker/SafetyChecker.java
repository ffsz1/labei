package com.hncxco.safetychecker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.hncxco.safetychecker.bean.SafetyCheckResultBean;

import java.util.List;

/**
 * 安全检测器
 */
public class SafetyChecker {
    private volatile static SafetyChecker checker;

    static {
        System.loadLibrary("ndk-safetychecker");
    }

    private SafetyChecker() {
    }

    public static SafetyChecker getInstance() {
        if (checker == null) {
            synchronized (SafetyChecker.class) {
                if (checker == null) {
                    checker = new SafetyChecker();
                }
            }
        }
        return checker;
    }

    /**
     * 检查
     */
    public SafetyCheckResultBean check(Context context) {
        SafetyCheckResultBean safetyCheckResult = new SafetyCheckResultBean();
        try {
            checkInstalledApplications(context, safetyCheckResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            checkException(safetyCheckResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int jniXposedCheckStatus = checkHook() ? 1 : 0;
            if (jniXposedCheckStatus == 1) {
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getXposedCheckResult().setJniInstalledCheckStatus(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return safetyCheckResult;
    }

    /**
     * 检查手机是否安装非法程序
     */
    private void checkInstalledApplications(Context context, SafetyCheckResultBean safetyCheckResult) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.loadLabel(packageManager).toString().contains("八门神器")) {
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getBamenCheckResult().setJavaInstalledCheckStatus(1);
                safetyCheckResult.getBamenCheckResult().setInstallAppName(applicationInfo.loadLabel(context.getPackageManager()).toString());
                return;
            } else if ("de.robv.android.xposed.installer".equals(applicationInfo.packageName)
                    || "io.va.exposed".equals(applicationInfo.packageName)
                    || "me.weishu.exp".equals(applicationInfo.packageName)) {//手机有安装Xposed
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getXposedCheckResult().setJavaInstalledCheckStatus(1);
                safetyCheckResult.getXposedCheckResult().setInstallAppName(applicationInfo.loadLabel(context.getPackageManager()).toString());
                return;
            } else if ("com.lishu.net.LishuNet".equals(applicationInfo.packageName)) {//手机有安装LishuNet
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getLishuNetCheckResult().setJavaInstalledCheckStatus(1);
                safetyCheckResult.getLishuNetCheckResult().setInstallAppName(applicationInfo.loadLabel(context.getPackageManager()).toString());
                return;
            } else if ("cn.mm.gk".equals(applicationInfo.packageName) || "com.zhangkongapp.joke.bamenshenqi".equals(applicationInfo.packageName)) {//手机有安装八门神器
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getBamenCheckResult().setJavaInstalledCheckStatus(1);
                safetyCheckResult.getBamenCheckResult().setInstallAppName(applicationInfo.loadLabel(context.getPackageManager()).toString());
                return;
            } else if ("catch_.me.if_.you_.can_".equals(applicationInfo.packageName)
                    || "catch_.me1.if_.you_.can_".equals(applicationInfo.packageName)) {//手机有安装gameGuardian
                safetyCheckResult.setCheckStatus(1);
                safetyCheckResult.getGameGuardianCheckResult().setJavaInstalledCheckStatus(1);
                safetyCheckResult.getGameGuardianCheckResult().setInstallAppName(applicationInfo.loadLabel(context.getPackageManager()).toString());
                return;
            }
        }
    }

    /**
     * 检查Exception
     */
    private void checkException(SafetyCheckResultBean safetyCheckResult) {
        try {
            throw new Exception("xxx");
        } catch (Exception e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement != null
                        && ("de.robv.android.xposed.XposedBridge".equals(stackTraceElement.getClassName())
                        || "handleHookedMethod".equals(stackTraceElement.getMethodName()))) {//存在Xposed
                    safetyCheckResult.setCheckStatus(1);
                    safetyCheckResult.getXposedCheckResult().setJavaExceptionCheckStatus(1);
                    break;
                }
            }
            if (safetyCheckResult.getXposedCheckResult().getJavaExceptionCheckStatus() == 0) {//防止getStackTrace被hook，用message查一次
                String message = e.getMessage();
                if (message != null && message.contains("de.robv.android.xposed.XposedBridge")) {
                    safetyCheckResult.setCheckStatus(1);
                    safetyCheckResult.getXposedCheckResult().setJavaExceptionCheckStatus(1);
                }
            }
        }
    }

    /**
     * jni层检测hook（暂时只检查是否有安装xposed）
     */
    public native boolean checkHook();
}