package com.tongdaxing.xchat_framework.http_image.util;

import android.content.Context;
import android.os.Build;

import com.tongdaxing.xchat_framework.http_image.http.DefaultRequestParam;
import com.tongdaxing.xchat_framework.http_image.http.RequestParam;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.ChannelUtil;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.VersionUtil;

import java.util.HashMap;
import java.util.Map;

public class CommonParamUtil {

    public static RequestParam fillCommonParam() {
        DefaultRequestParam param = new DefaultRequestParam();
        param.put("os", "android");
        param.put("osVersion", Build.VERSION.RELEASE);
        param.put("appid", "tianya");
        param.put("ispType", String.valueOf(getIspType()));
        param.put("netType", String.valueOf(getNetworkType()));
        param.put("model", getPhoneModel());
        param.put("appVersion", VersionUtil.getLocalName(BasicConfig.INSTANCE.getAppContext()));
        param.put("appCode", VersionUtil.getVersionCode(BasicConfig.INSTANCE.getAppContext()) + "");
        param.put("deviceId", DeviceUuidFactory.getDeviceId(BasicConfig.INSTANCE.getAppContext()));
        param.put("channel", ChannelUtil.getChannel(BasicConfig.INSTANCE.getAppContext()));
        return param;
    }

    public static HashMap<String, String> fillCommonParamMap() {
        HashMap<String, String> param = new HashMap();
        param.put("os", "android");
        param.put("osVersion", Build.VERSION.RELEASE);
        param.put("appid", "tianya");
        param.put("ispType", String.valueOf(getIspType()));
        param.put("netType", String.valueOf(getNetworkType()));
        param.put("model", getPhoneModel());
        param.put("appVersion", VersionUtil.getLocalName(BasicConfig.INSTANCE.getAppContext()));
        param.put("appCode", VersionUtil.getVersionCode(BasicConfig.INSTANCE.getAppContext()) + "");
        param.put("deviceId", DeviceUuidFactory.getDeviceId(BasicConfig.INSTANCE.getAppContext()));
        param.put("channel", ChannelUtil.getChannel(BasicConfig.INSTANCE.getAppContext()));
        return param;
    }

    public static Map<String, String> getDefaultParam(Map<String, String> param) {
        if (param == null) {
            param = new HashMap<>();
        }
        param.put("os", "android");
        param.put("osVersion", Build.VERSION.RELEASE);
        param.put("appid", "tianya");
        param.put("ispType", String.valueOf(getIspType()));
        param.put("netType", String.valueOf(getNetworkType()));
        param.put("model", getPhoneModel());
        param.put("appVersion", VersionUtil.getLocalName(BasicConfig.INSTANCE.getAppContext()));
        param.put("appCode", VersionUtil.getVersionCode(BasicConfig.INSTANCE.getAppContext()) + "");
        param.put("deviceId", DeviceUuidFactory.getDeviceId(BasicConfig.INSTANCE.getAppContext()));
        param.put("channel", ChannelUtil.getChannel(BasicConfig.INSTANCE.getAppContext()));
        return param;
    }

    public static Map<String, String> getDefaultParam() {
        Map<String, String> param = new HashMap<String, String>(10);
        param.put("os", "android");
        param.put("osVersion", Build.VERSION.RELEASE);
        param.put("appid", "tianya");
        param.put("ispType", String.valueOf(getIspType()));
        param.put("netType", String.valueOf(getNetworkType()));
        param.put("model", getPhoneModel());
        param.put("appVersion", VersionUtil.getLocalName(BasicConfig.INSTANCE.getAppContext()));
        param.put("appCode", VersionUtil.getVersionCode(BasicConfig.INSTANCE.getAppContext()) + "");
        param.put("deviceId", DeviceUuidFactory.getDeviceId(BasicConfig.INSTANCE.getAppContext()));
        param.put("channel", ChannelUtil.getChannel(BasicConfig.INSTANCE.getAppContext()));
        return param;
    }

    public static Map<String, String> getDefaultHeaders(Context context) {
        Map<String, String> param = new HashMap<String, String>(10);
        return param;
    }

    public static Map<String, String> getDefaultHeaders() {
        Map<String, String> param = new HashMap<String, String>(10);
        return param;
    }

    /**
     * 获取是否nettype字段
     *
     * @return
     */
    public static int getNetworkType() {
        if (NetworkUtils.getNetworkType(BasicConfig.INSTANCE.getAppContext()) == NetworkUtils.NET_WIFI) {
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
    public static int getIspType() {
        String isp = NetworkUtils.getOperator(BasicConfig.INSTANCE.getAppContext());
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

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
}
