package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserPacketDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;

/**
 * @class: UserPacketManager.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface UserPacketManager {

    /**
     * 增加用户的红包余额
     *
     * @param uid
     * @param packetNum
     * @param type
     * @param shareId
     * @param srcUid
     */
    void addUserPacket(Long uid, double packetNum, UserPacketRecordTypeEnum type, String shareId, Long srcUid);

    /**
     * @param uid
     * @return
     */
    UserPacketDTO getUserPacket(Long uid);

    /**
     * @param uid
     * @return
     */
    int getUserBonusLevel(Long uid);
}
