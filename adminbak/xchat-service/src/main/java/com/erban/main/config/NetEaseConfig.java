package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
public class NetEaseConfig {
    public static String appKey;
    public static String appSecret;
    public static String smsAppKey;
    public static String smsAppSecret;

    @Value("${netEaseAppKey}")
    public void setAppKey(String appKey) {
        NetEaseConfig.appKey = appKey;
    }

    @Value("${netEaseAppSecret}")
    public static void setAppSecret(String appSecret) {
        NetEaseConfig.appSecret = appSecret;
    }

    @Value("${netEaseSmsAppKey}")
    public void setSmsAppKey(String smsAppKey) {
        NetEaseConfig.smsAppKey = smsAppKey;
    }

    @Value("${netEaseSmsAppSecret}")
    public static void setSmsAppSecret(String smsAppSecret) {
        NetEaseConfig.smsAppSecret = smsAppSecret;
    }
}
