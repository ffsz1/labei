package com.juxiao.xchat.api.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.manager.common.mcoin.McoinPkManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class McoinPkMQReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private McoinPkManager mcoinPkManager;

    @JmsListener(destination = MqDestinationKey.MCOIN_PK_QUEUE)
    public void receiveCharge(String message) {
        JSONObject object = JSONObject.parseObject(message);
        int item = object.getIntValue("item");
        Long endTime = object.getLongValue("endTime");
        StringBuilder sb = new StringBuilder();
        sb.append("现在时间");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sb.append(",第");
        sb.append(item);
        sb.append("期开奖");
        logger.info(sb.toString());

        try {
            mcoinPkManager.lottery(item,endTime);
        } catch (Exception e) {
            logger.error("[ 点点币Pk活动第{}期开奖 ] 开奖活动处理异常：",object.getIntValue("item"), e);
        }
    }
}
