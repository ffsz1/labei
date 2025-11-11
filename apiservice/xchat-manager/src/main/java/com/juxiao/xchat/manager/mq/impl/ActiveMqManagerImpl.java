package com.juxiao.xchat.manager.mq.impl;

import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;

@Service
public class ActiveMqManagerImpl implements ActiveMqManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private DingtalkChatbotManager dingtalkChatbotManager;
    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager neteaseSmsManager;

    @Async
    @Override
    public void sendQueueMessage(String destination, String message) {
        try {
            logger.info("[ MQ队列信息 ] destination:{},message:{}",destination,message);
            jmsTemplate.convertAndSend(new ActiveMQQueue(destination), message);
        } catch (Exception e) {
            logger.error("[ MQ队列信息 ]发送【{}】队列信息【{}】异常：", destination, message, e);
            try {
                neteaseSmsManager.sendAlarmSms("发送mq报错");
            } catch (Exception e1) {
            }
            if (SystemConf.ENV_PROD.equalsIgnoreCase(systemConf.getEnv())) {
                String text = "[ 服务预警 ] " + systemConf.getEnvName() + "发送mq报错:" + e.getMessage() + "，预警级别：高";
                dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), new DingtalkTextMessageBO(text, dingTalkConf.getProgrammer(), false));
            }

        }
    }

    @Async
    @Override
    public void sendDelayQueueMessage(String destination, String message, long delayTime) {
        ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            Destination destin = session.createQueue(destination);
            producer = session.createProducer(destin);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayTime);
            producer.send(textMessage);
            session.commit();
            logger.info("[ MQ队列信息 ]发送【{}】队列信息【{}】成功", destination, message);
        } catch (Exception e) {
            logger.error("[ MQ队列信息 ]发送【{}】队列信息【{}】异常：", destination, message, e);
            if (session != null) {
                try {
                    session.rollback();
                } catch (JMSException e1) {
                }
            }
            if (SystemConf.ENV_PROD.equalsIgnoreCase(systemConf.getEnv())) {
                String text = "[ 服务预警 ] " + systemConf.getEnvName() + "发送mq报错:" + e.getMessage() + "，预警级别：高";
                dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), new DingtalkTextMessageBO(text, dingTalkConf.getProgrammer(), false));
            }
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
