package com.juxiao.xchat.manager.common.sysconf;

/**
 * @class: AppSignKeyManager.java
 * @author: chenjunsheng
 * @date 2018/7/16
 */
public interface AppSignKeyManager {

    /**
     * 根据客户端和客户端版本获取加密key值
     *
     * @param os
     * @param appVersion
     * @return
     */
    String getAppSignKey(String os, String appVersion);
}
