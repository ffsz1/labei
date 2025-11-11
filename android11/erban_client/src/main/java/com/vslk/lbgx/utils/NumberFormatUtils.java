package com.vslk.lbgx.utils;

import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.math.BigDecimal;

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

    public static String getBigDecimal(long charm) {
        String charmStr = " ";
        if (charm < 1000000) {
            charmStr = "" + charm;
        } else {
            double d = (double) charm;
            double num = d / 1000000;//1.将数字转换成以万为单位的数字
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            charmStr = f1 + "百万";
        }
        return charmStr;
    }

    public static String getThousandStr(int count) {
        String charmStr = " ";
        if (count < 10000) {
            charmStr = "" + count;
        } else {
            double d = (double) count;
            double num = d / 10000;//1.将数字转换成以万为单位的数字
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            charmStr = f1 + "w";
        }
        return charmStr;
    }

}
