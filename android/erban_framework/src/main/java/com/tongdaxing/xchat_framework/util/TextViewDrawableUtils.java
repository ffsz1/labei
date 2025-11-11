package com.tongdaxing.xchat_framework.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class TextViewDrawableUtils {

    public static Drawable getCompoundDrawables(Context context, int imgId) {
        Drawable drawable = context.getResources().getDrawable(imgId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static void setCompoundDrawablesTop(Context context, TextView tvCountry, int res) {
        Drawable drawable = TextViewDrawableUtils.getCompoundDrawables(context, res);
        tvCountry.setCompoundDrawablesRelative(null, drawable, null, null);
    }


    public static void setNationalFlag(Context context, String nationalFlagStr, TextView tvCountry, int arrowEndRes) {
        Drawable arrowEnd = getCompoundDrawables(context, arrowEndRes);
        try {
            int res = context.getResources().getIdentifier(nationalFlagStr, "mipmap", context.getPackageName());
            if (res != 0) {
                Drawable drawable = TextViewDrawableUtils.getCompoundDrawables(context, res);
                tvCountry.setCompoundDrawablesRelative(drawable, null, arrowEnd, null);
            } else {
                tvCountry.setCompoundDrawablesRelative(null, null, arrowEnd, null);
            }
        } catch (Exception e) {
            tvCountry.setCompoundDrawablesRelative(null, null, arrowEnd, null);
        }
    }
}
