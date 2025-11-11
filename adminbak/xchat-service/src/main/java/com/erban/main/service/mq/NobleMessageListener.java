package com.erban.main.service.mq;

import com.erban.main.message.NobleMessage;
import com.erban.main.service.noble.NobleMessageService;
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
 * 处理贵族开通和续费的消息
 */
@Component
public class NobleMessageListener implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(NobleMessageListener.class);

    @Autowired
    private JedisService jedisService;
    @Autowired
    private NobleMessageService nobleMessageService;


    @Override
    public void onMessage(Message message) {
        /** 判断消息类型 */
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                NobleMessage nobleMessage = (NobleMessage) objectMessage.getObject();
                logger.info("receive nobleMessage: {}", nobleMessage.toString());
                // 判断该消息是否已经消费过
                String messStatus = jedisService.hget(RedisKey.mq_noble_status.getKey(), nobleMessage.getMessId());
                if (BlankUtil.isBlank(messStatus)) {
                    return;
                }
                nobleMessageService.handleNobleMessage(nobleMessage);
            } catch (JMSException e) {
                logger.error("nobleMessage queue handle error, ", e);
            } catch (Exception e) {
                logger.error("nobleMessage queue handle error", e);
            }
        }
    }


}
