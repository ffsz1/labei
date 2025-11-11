package com.erban.main.service.mq;

import com.erban.main.message.BigGiftMessage;
import com.erban.main.service.gift.GiftSendService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 */
@Component
public class BigGiftMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(BigGiftMessageListener.class);


    @Autowired
    private JedisService jedisService;
    @Autowired
    private GiftSendService giftSendService;


    @Override
    public void onMessage(Message message) {
        /** 判断消息类型 */
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                BigGiftMessage bigGiftMessage = (BigGiftMessage) objectMessage.getObject();
                logger.info("onMessage giftMessage: {}", bigGiftMessage.toString());
                // 判断该消息是否已经消费过
                String messStatus = jedisService.hget(RedisKey.mq_big_gift_status.getKey(), bigGiftMessage.getMessId());
                if (BlankUtil.isBlank(messStatus)) {
                    return;
                }
                giftSendService.handleBigGiftMessage(bigGiftMessage);
            } catch (JMSException e) {
                logger.error("message queue handle error, ", e);
            } catch (Exception e) {
                logger.error("message queue handle error", e);
            }
        }
    }


}
