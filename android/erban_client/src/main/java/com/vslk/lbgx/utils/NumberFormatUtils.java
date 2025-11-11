package com.vslk.lbgx.utils;

import com.tongdaxing.xchat_framework.util.util.StringUtils;

public class NumberFormatUtils {
    /**
     * 格式化double，如果没小数不显示小数点，有小数正常显示小数
     */
    public static String formatDoubleDecimalPoint(double doubleNumber) {
        int intNumber = (int) doubleNumber;
        if (intNumber == doubleNumber) {
            return String.valueOf(intNumber);
        } else {
            return String.valueOf(doubleNumber);
        }
    }


    public static String hideMiddleExtend(String number) {
        int startCount = number.length() - 6;
        StringBuilder sbBuilder = new StringBuilder("*");
        for (int i = 1; i < startCount; i++) {
            sbBuilder.append("*");
        }
        return StringUtils.overlay(number, sbBuilder.toString(), 3, number.length() - 3);
    }
}
