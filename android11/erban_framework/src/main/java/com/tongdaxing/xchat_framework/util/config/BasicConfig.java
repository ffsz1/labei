package com.tongdaxing.xchat_framework.util.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tongdaxing.xchat_framework.BuildConfig;
import com.tongdaxing.xchat_framework.util.cache.CacheClientFactory;
import com.tongdaxing.xchat_framework.util.util.file.StorageUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;


/**
 * Created by xujiexing on 14-6-12.
 */
public enum BasicConfig {
    INSTANCE;

    private Context mContext;
    private boolean isDebuggable;
    private boolean isTestMode;
    private File mLogDir;
    private File mRoot;
    private File mConfigDir;
    private File mCacheDir;
    private File mVoiceDir;
    private String channel;
    // only used by unit test
    public boolean isTestMode() {
        return isTestMode;
    }

    public void setIsTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }


    private boolean isDebugMode(Context context) {
        boolean debuggable = false;
        ApplicationInfo appInfo = null;
        PackageManager packMgmr = context.getPackageManager();
        try {
            appInfo = packMgmr.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            MLog.error(this, e);
        }
        if (appInfo != null) {
            debuggable = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
        }
        MLog.verbose(this, "isDebugMode debuggable = %b", debuggable);
        return debuggable;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    /**
     * @return Application context
     */
    public Context getAppContext() {
        return mContext;
    }

    public void setAppContext(Context context) {
        mContext = context;
    }

    public boolean isDebuggable() {
        return isDebuggable;
    }

    public void setDebuggable(boolean debuggable) {
        isDebuggable = debuggable;
    }

    public void registerPrivateCacheClient(String uid) {
        CacheClientFactory.registerPrivate(uid);
    }

    public void removePrivateCacheClient() {
        CacheClientFactory.removePrivate();
    }

    public File getRootDir() {
        return this.mRoot;
    }

    public File getExternalRootDir(String rootDir) {
        File f = StorageUtils.getOwnCacheDirectory(mContext, rootDir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    public void setRootDir(String rootDir) {
        File f = StorageUtils.getCacheDirectory(mContext, rootDir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }
        this.mRoot = f;
    }

    public File getConfigDir() {
        return mConfigDir;
    }

    /**
     * 设置config的目录
     *
     * @param dir
     */
    public void setConfigDir(String dir) {
        try {
            mConfigDir = StorageUtils.getCacheDirectory(mContext, dir);
            if (!mConfigDir.exists()) {
                if (!mConfigDir.mkdirs()) {
                    MLog.error(this, "Can't create config dir " + mConfigDir);
                    return;
                }
            }
        } catch (Exception e) {
            MLog.error(this, "Set config dir error", e);
        }
    }

    public File getLogDir() {
        return mLogDir;
    }

    /**
     * 设置log的目录
     *
     * @param dir
     */
    public void setLogDir(String dir) {
        try {
            mLogDir = StorageUtils.getCacheDirectory(mContext, dir);
            if (!mLogDir.exists()) {
                if (!mLogDir.mkdirs()) {
                    MLog.error(this, "Can't create log dir " + mLogDir);
                    return;
                }
            }
        } catch (Exception e) {
            MLog.error(this, "Set log dir error", e);
        }
    }

    public File getCacheDir() {
        return mCacheDir;
    }

    public void setCacheDir(String dir) {
        try {
            mCacheDir = StorageUtils.getCacheDirectory(mContext, dir);
            if (!mCacheDir.exists()) {
                if (!mCacheDir.mkdirs()) {
                    MLog.error(this, "Can't create log dir " + mCacheDir);
                    return;
                }
            }
        } catch (Exception e) {
            MLog.error(this, "Set log dir error", e);
        }
    }

    public void setVoiceDir(String dir) {
        try {
            mVoiceDir = StorageUtils.getCacheDirectory(mContext, dir);
            if (!mVoiceDir.exists()) {
                if (!mVoiceDir.mkdirs()) {
                    MLog.error(this, "Can't create voice dir " + mVoiceDir);
                    return;
                }
            }
        } catch (Exception e) {
            MLog.error(this, "Set log voice error", e);
        }
    }

    public File getVoiceDir() {
        return mVoiceDir;
    }
}
