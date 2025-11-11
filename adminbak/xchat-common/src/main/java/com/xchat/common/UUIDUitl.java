package com.xchat.common;

import java.util.UUID;

/**
 * Created by liuguofu on 2017/4/28.
 */
public class UUIDUitl {
    public static String get(){
       return UUID.randomUUID().toString().replace("-","");
    }
}
