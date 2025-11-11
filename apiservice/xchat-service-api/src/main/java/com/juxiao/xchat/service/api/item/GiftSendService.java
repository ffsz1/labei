package com.juxiao.xchat.service.api.item;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;

public interface GiftSendService {

    /**
     * 私聊时给对方送礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendGiftInPrivateChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType) throws WebServiceException;

    /**
     * 房间内给对方送礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendGiftInRoomChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType, String appVersion) throws WebServiceException;

    /**
     * 发送表白礼物
     * @param sendUid 发送用户
     * @param recvUid 接收用户
     * @param roomUid 房间ID
     * @param giftId 礼物ID
     * @param giftNum 礼物数量
     * @param sendType 发送类型--4表白
     * @param expressMessage 表白留言
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendExpressGift(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType, String expressMessage) throws WebServiceException;

    WebServiceMessage sendGiftToAll(long sendUid, Long[] recvUids, Long roomUid, int giftId, int giftNum, String appVersion) throws WebServiceException;

    void sendMsgAllRoom(Long uid, Long targetUid, Long roomUid, int giftId, int giftNum, Long[] targetSize) throws WebServiceException;

    WebServiceMessage callForUserWithSendGift(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType, String appVersion) throws WebServiceException;


    /**
     * 私聊时给对方送活动礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendGiftPropInPrivateChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType) throws WebServiceException;

    /**
     * 房间内给对方送活动礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendGiftPropInRoomChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum, byte sendType, String appVersion) throws WebServiceException;

    /**
     * 私聊时给对方送活动礼物
     * @param sendUid
     * @param recvUids
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param appVersion
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage sendGiftPropToAll(long sendUid, Long[] recvUids, Long roomUid, int giftId, int giftNum, String appVersion) throws WebServiceException;

}
