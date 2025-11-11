package com.tongdaxing.xchat_framework.data;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;

/**
 * Function:
 * Author: Edward on 2019/6/13
 */
public class BaseConstants {
    /**
     * 最新APK文件路径
     */
    public static final String NEWEST_APK_FILE_URL = "https://pic.hnyueqiang.com/formal.apk";
    /**
     * 汇潮支付APP ID
     */
    public static final String HUI_CHAO_PAY_APP_ID = "197041S190614003";
    private static final long devRoomId = 2;//测试公聊
    private static final long formalRoomId = 4;//线上
    private static final long checkDevRoomId = 1;//线下审核
    private static final long checkRoomId = 3;//线上审核
    /**
     * 1分钟发送一次
     */
    public static final long SEND_STATISTICS_INTERVAL = 60 * 1000;

    public static long getRoomId(boolean audit) {
        if (!audit) {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                return devRoomId;
            } else {
                return formalRoomId;
            }
        } else {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                return checkDevRoomId;
            } else {
                return checkRoomId;
            }
        }
    }
}
