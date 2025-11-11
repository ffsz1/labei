package com.tongdaxing.xchat_core;

import android.content.Context;
import android.text.TextUtils;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.TicketInfo;
import com.tongdaxing.xchat_core.gift.GiftListInfo;
import com.tongdaxing.xchat_core.initial.InitInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.pref.ObjectPref;
import com.tongdaxing.xchat_framework.util.util.pref.SettingsPref;

/**
 * Created by zhouxiangfeng on 2017/5/6.
 */

public class DemoCache {
    private static StatusBarNotificationConfig notificationConfig;
    private static final String KEY_TICKET_INFO = "TicketInfo";
    private static final String KEY_ACCOUNT_INFO = "AccountInfo";
    private static final String KEY_LOGIN_INFO = "LoginInfo";
    private static final String KEY_NOTI_TOGGLE = "NotiToggle";
    private static final String KEY_NOTI_CONFIG = "StatusBarNotificationConfig";
    private static final String KEY_GIFT_LIST_INFO = "GiftListInfo";
    private static final String KEY_MYSTERY_GIFT_LIST_INFO = "MysteryGiftListInfo";
    private static final String KEY_FACE_LIST_INFO = "FaceListInfo";
    private static final String KEY_INIT_DATE = "InitInfo";
    private static final String KEY_INIT_DATE_SAVE_TIME = "InitInfoSavingTime";
    private static final String KEY_INIT_DATE_SPLASH_PICTURE = "InitInfoSplashPicture";

    public static String readSplashPicture() {
        return SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).getString(KEY_INIT_DATE_SPLASH_PICTURE);
    }

    public static void saveSplashPicture(String path) {
        SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).putString(KEY_INIT_DATE_SPLASH_PICTURE, path);
    }

    public static Long readInitInfoSavingTime() {
        return (Long) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).get(KEY_INIT_DATE_SAVE_TIME, System.currentTimeMillis());
    }

    public static void saveInitInfoSavingTime(Long savingTime) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).put(KEY_INIT_DATE_SAVE_TIME, savingTime);
    }

    public static InitInfo readInitInfo() {
        return (InitInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_INIT_DATE);
    }

    public static void saveInitInfo(InitInfo initInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_INIT_DATE, initInfo);
    }

    public static String readFaceList() {
        return (String) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).get(KEY_FACE_LIST_INFO, null);
    }

    public static void saveFaceList(String encrypt) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).put(KEY_FACE_LIST_INFO, encrypt);
    }

    public static void saveGiftList(GiftListInfo giftListInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_GIFT_LIST_INFO, giftListInfo);
    }

    public static GiftListInfo readGiftList() {
        return (GiftListInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_GIFT_LIST_INFO);
    }

    public static void saveMysteryGiftList(GiftListInfo giftListInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_MYSTERY_GIFT_LIST_INFO, giftListInfo);
    }

    public static GiftListInfo readMysteryGiftList() {
        return (GiftListInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_MYSTERY_GIFT_LIST_INFO);
    }

    public static void saveTicketInfo(TicketInfo ticketInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_TICKET_INFO, ticketInfo);
    }

    public static void saveCurrentAccountInfo(AccountInfo accountInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_ACCOUNT_INFO, accountInfo);
    }

    public static void saveLoginInfo(LoginInfo loginInfo) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).saveObject(KEY_LOGIN_INFO, loginInfo);
    }

    public static AccountInfo readCurrentAccountInfo() {
        return (AccountInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_ACCOUNT_INFO);
    }

    public static TicketInfo readTicketInfo() {
        return (TicketInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_TICKET_INFO);
    }

    public static LoginInfo readLoginInfo(Context context) {
        return (LoginInfo) ObjectPref.instance(context).readObject(KEY_LOGIN_INFO);
    }

    public static boolean readNotificationToggle() {
        return SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).getBoolean(KEY_NOTI_TOGGLE, true);
    }

    public static void setNotificationToggle(boolean isOpen) {
        SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).putBoolean(KEY_NOTI_TOGGLE, isOpen);
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        DemoCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static void saveStatusBarNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).put(KEY_NOTI_CONFIG, notificationConfig);
    }

    public static StatusBarNotificationConfig readStatusBarNotificationConfig() {
        return (StatusBarNotificationConfig) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_NOTI_CONFIG);
    }

    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        LoginInfo loginInfo = DemoCache.readLoginInfo(BasicConfig.INSTANCE.getAppContext());
        // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
        if (null != loginInfo) {
            if (!TextUtils.isEmpty(loginInfo.getAccount()) && !TextUtils.isEmpty(loginInfo.getToken())) {
                return loginInfo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
