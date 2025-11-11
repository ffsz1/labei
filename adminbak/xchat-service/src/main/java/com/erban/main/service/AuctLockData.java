package com.erban.main.service;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Created by liuguofu on 2017/7/21.
 */
public class AuctLockData {
    public static HashMap<String,String> auctLockMap= Maps.newHashMap();

    public static void putLockData(String id){
        auctLockMap.put(id,id);
    }
    public static void removeLockData(String id){
        auctLockMap.remove(id);
    }
    public static String getLockData(String id){
        return auctLockMap.get(id);
    }
}
