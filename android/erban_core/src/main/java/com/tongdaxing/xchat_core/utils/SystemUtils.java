/**
 * 
 */
package com.tongdaxing.xchat_core.utils;

import android.content.Context;
import android.os.Build;
import android.os.Looper;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.reflect.Method;

/**
 * @author daixiang
 *
 */
public final class SystemUtils {

    public static final String TAG = "SystemUtils";

	public static boolean isMainThread() {
		
		return Looper.getMainLooper() == Looper.myLooper();
	}

	/**
	 * MediaSessionLegacyHelper is a static singleton that is lazily instantiated and keeps a
	 * reference to the context it's given the first time MediaSessionLegacyHelper.getHelper()
	 * is called.
	 * This leak was introduced in android-5.0.1_r1 and fixed in Android 5.1.0_r1 by calling
	 * context.getApplicationContext().
	 * Fix: https://github.com/android/platform_frameworks_base/commit/9b5257c9c99c4cb541d8e8e78fb04f008b1a9091
	 *
	 * Hack: to fix this, you could call MediaSessionLegacyHelper.getHelper() early in
	 * Application.onCreate() and pass it the application context.
	 * https://github.com/square/leakcanary/blob/master/leakcanary-android/src/main/java/com/squareup/leakcanary/AndroidExcludedRefs.java
	 */
	public static void fixMediaSessionLegacyHelper() {
		try {
			if (Build.VERSION.SDK_INT != 21) {
                MLog.info(TAG, "fixMediaSessionLegacyHelper api:" + Build.VERSION.SDK_INT);
				return;
			}
			Class cls = Class.forName("android.media.session.MediaSessionLegacyHelper");
			if (cls == null) {
				return;
			}
			Method method = cls.getMethod("getHelper", Context.class);
			method.invoke(null, BasicConfig.INSTANCE.getAppContext());
            MLog.info(TAG, "fixMediaSessionLegacyHelper done!");
		} catch (Throwable throwable) {
			MLog.error(TAG, "fixMediaSessionLegacyHelper error! " + throwable);
		}
	}

}
