package com.tongdaxing.xchat_core;

import android.content.Context;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.TicketInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.pref.ObjectPref;
import com.tongdaxing.xchat_framework.util.util.pref.SettingsPref;

/**
 * Created by zhouxiangfeng on 2017/5/19.
 */

public class PreferencesUtils {

    private static AccountInfo accountInfo;
    private static TicketInfo ticketInfo;

    private static final String KEY_TICKET_INFO = "TicketInfo";
    private static final String KEY_FRIST_SEED_GOLD = "FristSendGold";
    private static final String KEY_FRIST_QQ = "FristQQ";
    private static final String KEY_FRIST_USER = "FristUser";
    private static final String KEY_LOGIN_INFO = "LoginInfo";
    private static final String KEY_ACCOUNT_INFO = "AccountInfo";
    private static final String KEY_NOTI_TOGGLE = "NotiToggle";
    private static final String KEY_NOTI_CONFIG = "StatusBarNotificationConfig";


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
        if (null == accountInfo) {
            accountInfo = (AccountInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_ACCOUNT_INFO);
        }
        return accountInfo;
    }

    public static TicketInfo readTicketInfo() {
        if (null == ticketInfo) {
            return (TicketInfo) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_TICKET_INFO);
        }
        return ticketInfo;
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

    public static boolean readFristSendGold() {
        return SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).getBoolean(KEY_FRIST_SEED_GOLD, true);
    }

    public static void setFristSendGold(boolean isOpen) {
        SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).putBoolean(KEY_FRIST_SEED_GOLD, isOpen);
    }
    public static boolean readFristQQ() {
        return SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).getBoolean(KEY_FRIST_QQ, true);
    }

    public static void setFristQQ(boolean isOpen) {
        SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).putBoolean(KEY_FRIST_QQ, isOpen);
    }
    public static boolean readFristUser() {
        return SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).getBoolean(KEY_FRIST_USER, true);
    }

    public static void setFristUser(boolean isOpen) {
        SettingsPref.instance(BasicConfig.INSTANCE.getAppContext()).putBoolean(KEY_FRIST_USER, isOpen);
    }



    public static void saveStatusBarNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).put(KEY_NOTI_CONFIG, notificationConfig);
    }

    public static StatusBarNotificationConfig readStatusBarNotificationConfig() {
        return (StatusBarNotificationConfig) ObjectPref.instance(BasicConfig.INSTANCE.getAppContext()).readObject(KEY_NOTI_CONFIG);
    }

    public static void clear() {
        accountInfo = null;
        ticketInfo = null;
        saveCurrentAccountInfo(new AccountInfo());
        saveTicketInfo(new TicketInfo());
        saveLoginInfo(new LoginInfo("", ""));
    }
}
