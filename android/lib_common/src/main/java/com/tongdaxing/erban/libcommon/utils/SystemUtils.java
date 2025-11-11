package com.tongdaxing.erban.libcommon.utils;

import android.content.Context;
import android.os.Build;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/16
 */
public class SystemUtils {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取是否nettype字段
     *
     * @return
     */
    public static int getNetworkType(Context context) {
        if (NetworkUtils.getNetworkType(context) == NetworkUtils.NET_WIFI) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * 获取运营商字段
     *
     * @return
     */
    public static int getIspType(Context context) {
        String isp = NetworkUtils.getOperator(context);
        int ispType = 4;
        if (isp.equals(NetworkUtils.ChinaOperator.CMCC)) {
            ispType = 1;
        } else if (isp.equals(NetworkUtils.ChinaOperator.UNICOM)) {
            ispType = 2;
        } else if (isp.equals(NetworkUtils.ChinaOperator.CTL)) {
            ispType = 3;
        }

        return ispType;
    }
}
