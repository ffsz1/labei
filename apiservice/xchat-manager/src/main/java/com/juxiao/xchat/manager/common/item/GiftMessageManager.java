package com.juxiao.xchat.manager.common.item;

import com.juxiao.xchat.manager.common.item.mq.BigGiftMessage;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;

/**
 * @class: GiftMessageService.java
 * @author: chenjunsheng
 * @date 2018/6/20
 */
public interface GiftMessageManager {

    /**
     * 处理MQ信息
     *
     * @param message
     */
    void handleGiftMessage(GiftMessage message) throws Exception;

    /**
     * @param message
     * @throws Exception
     */
    void handleBigGiftMessage(BigGiftMessage message) throws Exception;

    /**
     * 处理打callMQ消息
     *
     * @param message
     */
    void handleCallMessage(GiftMessage message) throws Exception;

    /**
     * 处理MQ活动礼物消息
     *
     * @param message
     */
    void handleGiftPropMessage(GiftMessage message) throws Exception;

}
