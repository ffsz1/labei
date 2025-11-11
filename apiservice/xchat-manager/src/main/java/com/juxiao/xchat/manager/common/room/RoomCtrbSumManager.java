package com.juxiao.xchat.manager.common.room;

/**
 * @class: RoomCtrbSumManager.java
 * @author: chenjunsheng
 * @date 2018/6/20
 */
public interface RoomCtrbSumManager {


    /**
     * @param uid
     * @param ctrbUid
     * @param sumGold
     */
    void updateRoomCtrbSum(Long uid, Long ctrbUid, Long sumGold);

    /**
     * @param uid
     * @param flowSumTotal
     */
    void updateRoomTotalCtrbSum(Long uid, Long flowSumTotal);
}
