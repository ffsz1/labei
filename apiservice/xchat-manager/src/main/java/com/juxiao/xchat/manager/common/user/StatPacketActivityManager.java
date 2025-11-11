package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;

/**
 * @class: StatPacketActivityManager.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface StatPacketActivityManager {

    /**
     * @param uid
     */
    void saveFirstStatPacketActivity(Long uid);

    /**
     * 分享红包时更新
     *
     * @param uid
     */
    void updateSharePacketAcitivty(Long uid);

    /**
     * @param uid
     * @param srcUid
     */
    void updateRegisterPacketActivity(Long uid, Long srcUid);

    /**
     * @param uid
     * @param chargeBouns
     */
    void updateBounsPacketActivity(Long uid, double chargeBouns);

    /**
     * @param uid
     * @return
     */
    StatPacketActivityDTO getUserPacketActivity(Long uid);
}
