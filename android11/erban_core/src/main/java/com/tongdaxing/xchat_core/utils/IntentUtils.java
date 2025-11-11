package com.tongdaxing.xchat_core.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Creator： Chanry
 * Date：2016/3/21
 * Time: 21:09
 * <p/>
 * Description:
 */
public class IntentUtils {

    public static Intent getBackIntent(Context context) {
        return new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setClassName(
                        context,
                        "com.yy.mobile.ui.splash.SplashActivity");
    }

    public static PendingIntent getBackPendingIntent(Context context) {
        return PendingIntent
                .getActivity(context, 0, getBackIntent(context), PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static Intent toWirelessSettingsIntent(Context context) {
        Intent intent = new Intent();

        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            ComponentName component = new ComponentName(
                    "com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
}
