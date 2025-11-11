package com.tongdaxing.xchat_framework.util.util;

import java.util.UUID;

/**
 * Created by lijun on 2014/12/3.
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
