package com.juxiao.xchat.api.controller.item;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.event.DutyManager;
import com.juxiao.xchat.manager.common.item.GiftMessageManager;
import com.juxiao.xchat.manager.common.item.mq.BigGiftMessage;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.event.DutyType;
import com.juxiao.xchat.service.api.event.WeekStarGiftService;
import com.juxiao.xchat.service.api.item.mq.GiftDrawMessage;
import com.juxiao.xchat.service.api.user.UserGiftPurseService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftMQReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private DutyManager dutyManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private GiftMessageManager giftMessageManager;
    @Autowired
    private UserGiftPurseService userGiftPurseService;

    @Autowired
    private WeekStarGiftService weekStarGiftService;


    /**
     * 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     *
     * @param text
     */
    @JmsListener(destination = MqDestinationKey.GIFT_QUEUE, concurrency = "2-10")
    public void receiveGift(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收礼物记录 ]接收内容:>空，不作处理");
            return;
        }
        GiftMessage giftMessage = gson.fromJson(text, GiftMessage.class);
        long startTime = System.currentTimeMillis();
        try {
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_gift_status.getKey(), giftMessage.getMessId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }
            giftMessageManager.handleGiftMessage(giftMessage);
            //周星活动
            weekStarGiftActivity(giftMessage.getRecvUid(), giftMessage.getGiftId(), giftMessage.getGiftNum(), giftMessage.getGoldNum());
        } catch (Exception e) {
            logger.error("[ 接收礼物记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收礼物记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }

        try {
            dutyManager.updateDailyFinish(giftMessage.getSendUid(), DutyType.gift_send.getDutyId());
        } catch (Exception e) {
            logger.error("[ 消费礼物消息 ]更新每日任务异常，异常：", text, e);
        }
    }

    /**
     * 周星榜活动
     *
     * @param recvUid 接受者uid
     * @param giftId  礼物ID
     * @param giftNum 礼物数量
     * @param goldNum 金币数量
     */
    private void weekStarGiftActivity(Long recvUid, Integer giftId, Integer giftNum, Long goldNum) {
        logger.info("执行周星榜活动 start -> 接收者uid:{},礼物id:{},礼物数量:{},金币数量:{}", recvUid, giftId, giftNum, goldNum);
        List<WeekStarGiftDTO> weekStarGiftList = weekStarGiftService.getWeekStartGift();
        weekStarGiftList.forEach(item -> {
            if (item.getGiftId().equals(giftId)) {
                redisManager.zincrby(RedisKey.week_star_the_week.getKey(item.getGiftId().toString()), recvUid.toString(), (double) goldNum);
            }
        });
        logger.info("执行周星榜活动 end -> 接收者uid:{},礼物id:{},礼物数量:{},金币数量:{}", recvUid, giftId, giftNum, goldNum);
    }

    @JmsListener(destination = MqDestinationKey.BIG_GIFT_QUEUE, concurrency = "2-10")
    public void receiveBigGift(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收大额礼物记录 ]接收内容:>空，不作处理");
            return;
        }

        BigGiftMessage bigGiftMessage = gson.fromJson(text, BigGiftMessage.class);
        long startTime = System.currentTimeMillis();
        try {
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_big_gift_status.getKey(), bigGiftMessage.getMessId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }
            giftMessageManager.handleBigGiftMessage(bigGiftMessage);
        } catch (Exception e) {
            logger.error("[ 接收大额礼物记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收大额礼物记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }

        try {
            dutyManager.updateDailyFinish(bigGiftMessage.getUid(), DutyType.gift_send.getDutyId());
        } catch (Exception e) {
            logger.error("[ 接收大额礼物记录 ]更新每日任务异常，异常：", text, e);
        }
    }

    @JmsListener(destination = MqDestinationKey.GIFT_DY_DRAW_QUEUE, concurrency = "2-10")
    public void receiveDyDrawMessage(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收捡海螺消息记录 ]接收内容:>空，不作处理");
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            GiftDrawMessage message = gson.fromJson(text, GiftDrawMessage.class);
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }
            userGiftPurseService.handleDyDrawMessage(message);
        } catch (Exception e) {
            logger.error("[ 接收捡海螺消息记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收捡海螺消息记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }
    }

    @JmsListener(destination = MqDestinationKey.GIFT_TRYDRAW_QUEUE, concurrency = "2-10")
    public void receiveTryDrawMessage(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收捡海螺消息记录 ]接收内容:>空，不作处理");
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            GiftDrawMessage message = gson.fromJson(text, GiftDrawMessage.class);
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }
            userGiftPurseService.handleTryDrawMessage(message);
        } catch (Exception e) {
            logger.error("[ 接收捡海螺消息记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收捡海螺消息记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }
    }

    @JmsListener(destination = MqDestinationKey.GIFT_DRAW_QUEUE, concurrency = "2-10")
    public void receiveDrawMessage(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收捡海螺消息记录 ]接收内容:>空，不作处理");
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            GiftDrawMessage message = gson.fromJson(text, GiftDrawMessage.class);
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_gift_draw_message_status.getKey(), message.getMessageId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }
            userGiftPurseService.handleDrawMessage(message);
        } catch (Exception e) {
            logger.error("[ 接收捡海螺消息记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收捡海螺消息记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }
    }


//    @JmsListener(destination = MqDestinationKey.SEND_MYSTIC_GIFT_QUEUE)
//    public void receiveMysticGift(String text) {
//        if (StringUtils.isBlank(text)) {
//            logger.warn("[接收神秘礼物消息] 接收内容:>空, 不出来");
//            return;
//        }
//        long startTime = System.currentTimeMillis();
//        try {
//            MysticGiftMessage message = gson.fromJson(text, MysticGiftMessage.class);
//            String result = redisManager.hget(RedisKey.mq_mystic_gift_status.getKey(), message.getMessId());
//            if (StringUtils.isBlank(result)) {
//                return;
//            }
//            giftMessageManager.handleMysticMessage(message);
//        } catch (Exception e) {
//
//        } finally {
//            logger.info("[ 接收房间消息记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
//        }
//    }

    /**
     * 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     *
     * @param text
     */
    @JmsListener(destination = MqDestinationKey.CALL_QUEUE, concurrency = "2-10")
    public void receiveCall(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 打call记录 ]接收内容:>空，不作处理");
            return;
        }
        GiftMessage giftMessage = gson.fromJson(text, GiftMessage.class);
        long startTime = System.currentTimeMillis();
        try {
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_call_status.getKey(), giftMessage.getMessId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }

            giftMessageManager.handleCallMessage(giftMessage);
        } catch (Exception e) {
            logger.error("[ 打call记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 打call记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
     *
     * @param text
     */
    @JmsListener(destination = MqDestinationKey.GIFT_PROP_QUEUE, concurrency = "2-10")
    public void receiveGiftProp(String text) {
        if (StringUtils.isBlank(text)) {
            logger.warn("[ 接收活动礼物记录 ]接收内容:>空，不作处理");
            return;
        }
        GiftMessage giftMessage = gson.fromJson(text, GiftMessage.class);
        long startTime = System.currentTimeMillis();
        try {
            // 判断该消息是否已经消费过
            String messStatus = redisManager.hget(RedisKey.mq_gift_prop_status.getKey(), giftMessage.getMessId());
            if (StringUtils.isBlank(messStatus)) {
                return;
            }

            giftMessageManager.handleGiftPropMessage(giftMessage);
        } catch (Exception e) {
            logger.error("[ 接收活动礼物记录 ]接收到异常消息，内容:>{}，异常：", text, e);
        } finally {
            logger.info("[ 接收活动礼物记录 ]接收内容:>{}，耗时:>{}", text, (System.currentTimeMillis() - startTime));
        }
    }

}
