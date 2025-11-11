package com.erban.main.service.mq;

import com.erban.main.message.RoomMessage;
import com.erban.main.service.user.UserGiftPurseService;
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
public class RoomMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RoomMessageListener.class);

    @Autowired
    private JedisService jedisService;
    @Autowired
    private UserGiftPurseService userGiftPurseService;

    @Override
    public void onMessage(Message message) {
        /** 判断消息类型 */
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                RoomMessage roomMessage = (RoomMessage) objectMessage.getObject();
                logger.info("onMessage RoomMessage: {}", roomMessage.toString());
                // 判断该消息是否已经消费过
                String messStatus = jedisService.hget(RedisKey.mq_room_message_status.getKey(), roomMessage.getMessId());
                if (BlankUtil.isBlank(messStatus)) {
                    return;
                }
                userGiftPurseService.handleRoomMessage(roomMessage);
            } catch (JMSException e) {
                logger.error("message queue handle error, ", e);
            } catch (Exception e) {
                logger.error("message queue handle error", e);
            }
        }
    }


}
