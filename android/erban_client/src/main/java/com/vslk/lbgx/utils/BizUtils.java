package com.vslk.lbgx.utils;

import android.content.Context;

import com.tongdaxing.erban.R;

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
}
