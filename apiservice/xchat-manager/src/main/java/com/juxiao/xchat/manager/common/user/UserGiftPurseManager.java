package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.user.dto.UserGiftPurseDTO;
import com.juxiao.xchat.manager.common.item.mq.RoomMessage;

import java.util.Map;

/**
 * @class: UserGiftPurseManager.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface UserGiftPurseManager {

    /**
     * 为用户礼品包更新
     *
     * @param uid
     * @param giftId
     * @param countNum
     */
    void updateUserGiftPurse(Long uid, Integer giftId, Integer countNum) throws WebServiceException;

    /**
     * @param sendUid
     * @param gift
     * @param giftNum
     * @param giftPrice
     * @return
     */
    Map<String, Long> reduceGiftPurseCache(Long sendUid, GiftDTO gift, Integer giftNum, Long giftPrice) throws WebServiceException;


    /**
     * 添加用户礼物钱包
     * @param uid
     * @param giftId
     * @param countNum
     * @return
     * @throws WebServiceException
     */
    public Map<String, Long> addGiftPurseCache(Long uid, Integer giftId, Integer countNum) throws WebServiceException;

    /**
     * 添加打call钱包缓存
     * 减少金币 增加捡海螺次数
     * @param sendUid
     * @param gift
     * @param giftNum
     * @param giftPrice
     * @param conchNum
     * @return
     */
    Map<String, Long> addCallPurseCache(Long sendUid, GiftDTO gift, Integer giftNum, Long giftPrice, Long conchNum) throws WebServiceException;


    /**
     * @class: UserGiftPurseManager.java
     * @author: chenjunsheng
     * @date 2018/6/15
     */
    UserGiftPurseDTO getUserGiftPurse(Long uid, Integer giftId);

    /**
     * 处理捡海螺MQ信息
     *
     * @param roomMessage
     */
    void handleMQRoomMessage(RoomMessage roomMessage);

    /**
     * 减少活动礼物背包缓存

     * @param uid       用户Id
     * @param gift      活动礼物
     * @param giftNum   礼物数量
     * @return
     */
    Map<String, Long> reduceGiftPropPurseCache(Long uid, GiftDTO gift, Integer giftNum) throws WebServiceException;

}
