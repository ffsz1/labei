package com.juxiao.xchat.manager.mq;

import org.springframework.stereotype.Service;

/**
 * @class: ActiveMqManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Service
public interface ActiveMqManager {

    /**
     * @param destination
     * @param message
     */
    void sendQueueMessage(String destination, final String message);

    void sendDelayQueueMessage(String mcoinPkQueue, String toJSONString, long endTime);

}
