package com.juxiao.xchat.api.controller.charge;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.event.DutyManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserShareRecordManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.event.DutyType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ChargeMQReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DutyManager dutyManager;

    @Autowired
    private UserDrawManager userDrawManager;

    @Autowired
    private McoinMissionManager missionManager;

    @Autowired
    private UserShareRecordManager shareRecordManager;

    @JmsListener(destination = MqDestinationKey.CHARGE_QUEUE)
    public void receiveCharge(String message) {
        if (StringUtils.isBlank(message)) {
            logger.warn("[ 消费用户充值信息 ]接收内容:>空,不作处理");
            return;
        }
        JSONObject object = JSONObject.parseObject(message);
        try {
            dutyManager.updateDailyFinish(object.getLongValue("uid"), DutyType.charge.getDutyId());
        } catch (Exception e) {
            logger.error("[ 消费用户充值信息 ] 处理每日任务异常：", e);
        }

        int amount = object.getIntValue("amount");
        long uid = object.getLongValue("uid");
        String chargeRecordProdId = object.getString("chargeRecordProdId");

        if (StringUtils.isNotBlank(chargeRecordProdId)) {
            logger.error("[ 用户充值分成 ] 不保存：");
            try {
                shareRecordManager.saveUserBonusRecord(object.getLongValue("uid"), null, chargeRecordProdId, amount);
            } catch (Exception e) {
                logger.error("[ 用户充值分成 ] 保存分成记录错误：", e);
            }
        } else {
            logger.error("[ 用户充值分成 ] 传入参数不满足，不进行保存，chargeRecordProdId:>{}, amount:>{}", chargeRecordProdId, amount);
        }


        try {
            missionManager.finish(uid, 8);
        } catch (WebServiceException e) {
            logger.error("[ 消费用户充值信息 ] 完成日常任务处理异常：", e);
        }

    }

}
