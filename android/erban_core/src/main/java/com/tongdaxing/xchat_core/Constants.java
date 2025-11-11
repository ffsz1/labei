package com.tongdaxing.xchat_core;

import java.io.File;

/**
 * <p> 常量集合 </p>
 * Created by Administrator on 2017/11/9.
 */
public class Constants {
    public static final  String WX_APPID = "wxfdccf7c83108ee43";

    public static final String LIMIT_USER_SEND_PIC = "sendPicLeftLevel";
    public static final String BASE_CONFIG_ALI_PAY_SWITCH = "aliPaySwitch";

    public static final int MESSAGE_COUNT_LOCAL_LIMIT = 1500;
    public static final String IS_OPEN_ANIMATION_EFFECT = "isOpenAnimationEffect";
    public static final int START_BIRTH = 1970;

    public static final int END_BIRTH = 2019;

    public static final int MATCH_SIZE = 35;
    //官方客服
    public static final int OFFICIAL = 90000000;
    /**
     * 礼物占位符图标宽度和高度
     */
    public static final int GIFT_PLACEHOLDER_ICON_SIZE = 20;
    /**
     *
     */
    public static final String SEND_TO = " 送给 ";
    /**
     *
     */
    public static final String ALL_MIC_SEND = " 全麦送出 ";
    /**
     * 礼物占位符
     */
    public static final String GIFT_PLACEHOLDER = "gift_placeholder";
    /**
     * 等级占位符
     */
    public static final String LEVEL_PLACEHOLDER = "level_placeholder";
    /**
     * 标签占位符
     */
    public static final String LABEL_PLACEHOLDER = "label_placeholder";
    public static final int WX_PLATFORM_CODE = 1;
    public static final int QQ_PLATFORM_CODE = 2;

    public static final String ERBAN_DIR_NAME = "com.hncx.xxm";
    public static final String nimAppKey = "a09fff8642460fecd9fdb899bcba114c";
    public static final String nimAppSecret = "6c979ac40f7c";
//    public static final String nimAppKey = "3d01bdbf3a1383fb0d30777bf37af82f";
//    public static final String nimAppSecret = "9f4dfa42f868";
    /**
     * 百度统计
     */
    public static final String BAIDU_APPKEY = "e1afec7406";

    public static final String LOG_DIR = ERBAN_DIR_NAME + File.separator + "logs";
    public static final String CONFIG_DIR = ERBAN_DIR_NAME + File.separator + "config";
    public static final String VOICE_DIR = ERBAN_DIR_NAME + File.separator + "voice";
    public static final String CACHE_DIR = ERBAN_DIR_NAME + File.separator + "cache";
    public static final String HTTP_CACHE_DIR = ERBAN_DIR_NAME + File.separator + "http";
    public static final String IMAGE_CACHE_DIR = ERBAN_DIR_NAME + File.separator + "image";


    public static final String KEY_MAIN_POSITION = "key_main_position";

    public static final int RESULT_OK = 200;

    public static final int PAGE_START = 1;
    public static final int PAGE_START_ZERO = 0;
    public static final int PAGE_SIZE = 10;
    public static final int PAGE_HOME_HOT_SIZE = 25;
    public static final int BILL_PAGE_SIZE = 50;

    public static final String HOME_TAB_INFO = "home_tab_info";
    public static final String KEY_USER_INFO = "key_user_info";

    public static final String KEY_HOME_HOT_LIST = "key_home_hot_list";
    public static final String KEY_HOME_NO_HOT_LIST = "key_home_no_hot_list";

    public static final int FAN_MAIN_PAGE_TYPE = 100;
    public static final int FAN_NO_MAIN_PAGE_TYPE = 101;
    public static final String KEY_PAGE_TYPE = "page_type";
    public static final String KEY_MAIN_TAB_LIST = "main_tab_list";

    public static final String KEY_POSITION = "position";

    public static final String CHARGE_WX_JOINPAY = "WEIXIN_APP";
    public static final String CHARGE_WX_JOINPAY3 = "WEIXIN_APP3";
    public static final String CHARGE_WX = "wx";
    public static final String CHARGE_ALIPAY = "alipay";

    public static final int PAGE_TYPE_AV_ROOM_ACTIVITY = 100;
    public static final int PAGE_TYPE_USER_INFO_ACTIVITY = 101;
    public static final java.lang.String KEY_ROOM_IS_SHOW_ONLINE = "is_show_online";
    public static final String KEY_ROOM_INFO = "key_room_info";

    /**
     * 房间相关Key设置
     */
    public static final String ROOM_UPDATE_KEY_POSTION = "micPosition";
    public static final String ROOM_UPDATE_KEY_UID = "micUid";
    public static final String ROOM_UPDATE_KEY_GENDER = "gender";

    public static final String KEY_CHAT_ROOM_INFO_ROOM = "roomInfo";
    public static final String KEY_CHAT_ROOM_INFO_MIC = "micQueue";

    public static final String ROOM_UID = "ROOM_UID";
    public static final String ROOM_TYPE = "ROOM_TYPE";

    public static final String TYPE = "TYPE";

    //大礼物
    public static final int SUPER_GIFT_SIZE = 999;

    public static boolean isNeedJoin = false;

}
