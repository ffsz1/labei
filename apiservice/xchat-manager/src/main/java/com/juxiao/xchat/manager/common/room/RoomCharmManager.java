package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.base.web.WebServiceException;

/**
 * 房间魅力值
 */
public interface RoomCharmManager {

    /**
     * 保存房间魅力值，并发送房间自定义消息
     *
     * @param roomUid
     * @param uid
     * @param charmValue
     */
    void saveRoomCharm(Long roomUid, Long uid, Integer charmValue);

    /**
     * 保存房间魅力值，并发送房间自定义消息
     *
     * @param roomUid
     * @param uid
     * @param charmValue
     */
    void saveRoomCharm(Long roomUid, Long[] uid, Integer charmValue);

    /**
     * 更新麦序信息处理魅力值
     *
     * @param roomUid 房间
     * @param uid     用户
     * @param type    用户操作：1，上麦；2，下麦
     */
    void updateMicHandler(Long roomUid, Long uid, Byte type);

    /**
     * 清零魅力值
     *
     * @param roomUid 房间
     * @param uid     用户
     */
    void clearMicCharm(Long roomUid, Long uid) throws WebServiceException;

    /**
     * 延迟5s发送用户魅力值
     *
     * @param roomUid 房主UID
     * @param uid     用户ID
     */
    void sendDelayRoomCharm(Long roomUid, Long uid);

    /**
     * 往房间发送用户变化数据
     *
     * @param roomUid
     */
    void sendRoomAddCharm(Long roomUid);

    /**
     * 发送全部麦上用户魅力值信息
     *
     * @param roomUid 房主
     * @param uid     如果为空，则发送单人
     */
    void sendRoomAllCharm(Long roomUid, Long uid);

    /**
     * 清空房间魅力值
     *
     * @param roomUid
     * @param uid
     */
    void deleteRoomCharm(Long roomUid, Long uid);


    /**
     * 发送用户魅力值
     *
     * @param roomUid 房主UID
     * @param uid     用户ID
     */
    void sendRoomCharm(Long roomUid, Long uid);
}