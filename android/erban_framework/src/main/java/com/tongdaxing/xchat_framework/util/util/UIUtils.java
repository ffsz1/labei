package com.tongdaxing.xchat_framework.util.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by lijun on 2015/1/15.
 */
public class UIUtils {

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = listView.getLastVisiblePosition();

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
//            return listView.getAdapter().getView(pos, null, listView);
            return null;
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static void setPopupWindowTouchModal(PopupWindow popupWindow,
                                                boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        }
        catch (Exception e) {
            MLog.error("UIUtils", e);
        }
    }

    @TargetApi(17)
    public static boolean checkActivityValid(Activity activity) {
        try {
            if (null == activity) {
                return false;
            }

            if (activity.isFinishing()) {
                MLog.warn(activity, "activity is finishing");
                return false;
            }

            if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
                MLog.warn(activity, "activity is isDestroyed");
                return false;
            }
        } catch (Exception e) {
            MLog.error(UIUtils.class, e);
        }
        return true;
    }

    public static boolean isTopActivity(Activity activity) {
        if (null == activity) {
            return false;
        }

        ActivityManager manager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (null != runningTasks) {
            cmpNameTemp = (runningTasks.get(0).topActivity).toString();
        }

        if (null == cmpNameTemp) {
            return false;
        }

        return cmpNameTemp.contains(((Object) activity).getClass().getSimpleName());
    }

    public static void setOverflowShowingAlways(Context context) {
        try {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            MLog.error(UIUtils.class, e);
        }
    }

    @TargetApi(19)
    public static void enableTranslucentStatus(Activity activity) {
        setTranslucentStatus(activity, true);
    }

    @TargetApi(19)
    public static void disableTranslucentStatus(Activity activity) {
        setTranslucentStatus(activity, false);
    }

    private static void setTranslucentStatus(Activity activity, boolean enable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (null == activity) {
            return;
        }

        Window window = activity.getWindow();
        if (enable) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(19)
    public static void enableTranslucentNavigation(Activity activity) {
        setTranslucentNavigation(activity, true);
    }

    @TargetApi(19)
    public static void disableTranslucentNavigation(Activity activity) {
        setTranslucentNavigation(activity, false);
    }

    private static void setTranslucentNavigation(Activity activity, boolean enable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (null == activity) {
            return;
        }

        Window window = activity.getWindow();
        if (enable) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
