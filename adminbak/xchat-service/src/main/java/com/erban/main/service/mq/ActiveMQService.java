package com.erban.main.service.mq;

import com.erban.main.message.BigGiftMessage;
import com.erban.main.message.GiftMessage;
import com.erban.main.message.RoomMessage;
import com.erban.main.service.base.BaseService;
import com.erban.main.message.NobleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class ActiveMQService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQService.class);

    @Autowired
    @Qualifier( value = "giftJmsTemplate")
    private JmsTemplate giftJmsTemplate;
    @Autowired
    @Qualifier( value = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    /**
     * 送礼物消息，发送到MQ
     *
     * @param giftMessage
     */
    public void sendGiftMessage(final GiftMessage giftMessage) {
        logger.info("sendGiftMessage gift message: {}", giftMessage);
        giftJmsTemplate.send("gift-queue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(giftMessage);
            }
        });
    }

    /**
     * 送礼物消息，发送到MQ
     *
     * @param message
     */
    public void sendBigGiftMessage(final BigGiftMessage message) {
        logger.info("sendBigGiftMessage gift message: {}", message);
        giftJmsTemplate.send("big-gift-queue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(message);
            }
        });
    }

    /**
     * 送房间公屏消息，发送到MQ
     *
     * @param roomMessage
     */
    public void sendRoomMessage(final RoomMessage roomMessage) {
        logger.info("sendRoomMessage roomMessage: {}", roomMessage);
        giftJmsTemplate.send("room-message-queue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(roomMessage);
            }
        });
    }

    /**
     * 送贵族开通或续费消息，发送到MQ
     *
     * @param nobleMessage
     */
    public void sendNobleMessage(final NobleMessage nobleMessage) {
        logger.info("sendNobleMessage noble message: {}", nobleMessage);
        giftJmsTemplate.send("noble-queue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(nobleMessage);
            }
        });
    }
}
