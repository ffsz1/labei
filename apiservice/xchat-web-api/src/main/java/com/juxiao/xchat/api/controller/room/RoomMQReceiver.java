package com.juxiao.xchat.api.controller.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.manager.common.room.RoomCharmManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomMQReceiver {
    @Autowired
    private RoomCharmManager roomCharmManager;

    /**
     * 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     *
     * @param text
     */
    @JmsListener(destination = MqDestinationKey.ROOM_CHARM_SEND_MSG)
    public void receiveRoomCharmMsg(String text) {
        log.info("[ 房间魅力值 ]消费MQ信息:>{}", text);
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            JSONObject object = JSON.parseObject(text);
            if (object.containsKey("uid")) {
                roomCharmManager.sendRoomAllCharm(object.getLongValue("roomUid"), object.getLongValue("uid"));
            } else {
                roomCharmManager.sendRoomAddCharm(object.getLongValue("roomUid"));
            }
        } catch (Exception e) {
            log.error("[房间魅力值] 消息MQ出现异常信息:{}" , e);
        }


    }
}
