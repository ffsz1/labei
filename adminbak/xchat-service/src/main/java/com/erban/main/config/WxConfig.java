package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
@Order(-1)
@Lazy(false)
public class WxConfig {
    public static String appId;
    public static String appSecret;
    public static String mchId;
    public static String token;
    public static String returnUrl;
    public static Boolean isCreateMenu;
    public static String key;
    public static String getCodeUrl;
    public static String modelMsgId;
    public static String guide;
    public static String home;
    public static String payUrl;
    public static String giveGold;


    @Value("${APP_ID}")
    public void setAppId(String appId) {
        WxConfig.appId = appId;
    }

    @Value("${APP_SECRET}")
    public void setAppSecret(String appSecret) {
        WxConfig.appSecret = appSecret;
    }

    @Value("${MCH_ID}")
    public void setMchId(String mchId) {
        WxConfig.mchId = mchId;
    }

    @Value("${TOKEN}")
    public void setToken(String token) {
        WxConfig.token = token;
    }

    @Value("${RETURN_URL}")
    public void setReturnUrl(String returnUrl) {
        WxConfig.returnUrl = returnUrl;
    }

    @Value("${isCreateMenu}")
    public void setIsCreateMenu(Boolean isCreateMenu) {
        WxConfig.isCreateMenu = isCreateMenu;
    }

    @Value("${KEY}")
    public void setKey(String key) {
        WxConfig.key = key;
    }

    @Value("${GET_CODE_URL}")
    public void setGetCodeUrl(String getCodeUrl) {
        WxConfig.getCodeUrl = getCodeUrl;
    }

    @Value("${MODEL_MSG_ID}")
    public void setModelMsgId(String modelMsgId) {
        WxConfig.modelMsgId = modelMsgId;
    }

    @Value("${GUIDE}")
    public void setGuide(String guide) {
        WxConfig.guide = guide;
    }

    @Value("${HOME}")
    public void setHome(String home) {
        WxConfig.home = home;
    }

    @Value("${PAY_URL}")
    public void setPayUrl(String payUrl) {
        WxConfig.payUrl = payUrl;
    }

    @Value("${GIVE_GOLD}")
    public void setGiveGold(String giveGold) {
        WxConfig.giveGold = giveGold;
    }
}
