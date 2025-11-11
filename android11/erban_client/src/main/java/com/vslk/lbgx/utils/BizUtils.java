package com.vslk.lbgx.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.common.permission.EasyPermissions;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;

public class BizUtils {
    public static int getGenderIcon(int gender) {
        int icon = R.drawable.ic_gender_all;
        switch (gender) {
            case 0:
                icon = R.drawable.ic_gender_all;
                break;
            case 1:
                icon = R.drawable.ic_gender_man;
                break;
            case 2:
                icon = R.drawable.ic_gender_girl;
                break;
        }
        return icon;
    }

    public static int getResource(Context context, String imageName) {
        Context ctx = context;
        int resId = context.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        if (resId == 0) {
            resId = R.drawable.lv0;
        }
        return resId;
    }

    public static boolean hasPermission(Context context, int resString, String... mPerms) {
        if (EasyPermissions.hasPermissions(context, mPerms)) {
           return true;
        } else {
            EasyPermissions.requestPermissions(context, context.getString(resString), 120, mPerms);
            return false;
        }
    }

    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

}
