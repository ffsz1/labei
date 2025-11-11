package com.xchat.common.utils;

import java.util.UUID;

/**
 * @author laizhilong
 * @Title:
 * @Package com.xchat.common.utils
 * @date 2018/9/4
 * @time 20:04
 */
public class UUIDUtils {

    /**
     * 生成唯一标识
     *
     * @return
     */
    public static String get() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
