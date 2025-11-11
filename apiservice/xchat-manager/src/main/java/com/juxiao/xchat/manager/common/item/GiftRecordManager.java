package com.juxiao.xchat.manager.common.item;

/**
 * @class: GiftRecordManager.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface GiftRecordManager {

    /**
     * 获取用户经验值
     *
     * @param uid
     * @return
     */
    long getUserExerpence(Long uid);

    /**
     * 获取魅力值
     *
     * @param receivedUid
     * @return
     */
    long getUserCharm(Long receivedUid);
}
