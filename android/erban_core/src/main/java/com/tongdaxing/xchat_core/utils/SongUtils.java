package com.tongdaxing.xchat_core.utils;


import com.tongdaxing.xchat_framework.util.util.UUIDUtil;

/**
 * Creator: 舒强睿
 * Date:2014/12/4
 * Time:21:21
 * <p/>
 * Description：
 */
public class SongUtils {

    public static final String PREFIX = "third_";

    public static String generateThirdPartyId() {
        return PREFIX.concat(UUIDUtil.getUUID());
    }

    public static boolean isThirdPartyId(String songId) {
        return songId != null && songId.startsWith(PREFIX);
    }
}
