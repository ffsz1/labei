package com.juxiao.xchat.manager.common.draw.impl;

import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseGiftDrawManager implements GiftDrawManager {
    @Autowired
    protected GiftDrawConf giftDrawConf;
    @Autowired
    protected GiftManager giftManager;

    @Autowired
    private SystemConf systemConf;

    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private DingtalkChatbotManager chatbotManager;

    @Override
    public int draw(Long uid, Long roomUid, int totalDrawNum, boolean isXq, boolean isHd) {
        // 每个线程有自己的Random对象
        double randomNumber = RandomUtils.threadLocalRandomDouble();
        int length = this.getDrawRates().length;
        int giftId = giftDrawConf.getDefaultGiftId();
        for (int i = 0; i < length; i++) {
            if (randomNumber <= getDrawRates()[i]) {
                giftId = getDrawGifts()[i];
                break;
            }
        }

        if (giftId <= 0) {
            return giftDrawConf.getDefaultGiftId();
        }
        log.info("[ 捡海螺 ] uid:>{}, 类型:{}, 砸出礼物:>{}, 随机数:>{}, 概率:>{}", uid, this.getType(), giftId, randomNumber, this.getDrawRates());
        if (giftId == giftDrawConf.getFullGiftId()) {
            dingLog(uid, giftId, randomNumber);
        }
        return giftId;
    }

    /**
     * 获取捡海螺礼物的概率配置
     *
     * @return
     */
    abstract double[] getDrawRates();

    /**
     * 获取捡海螺礼物的礼物
     *
     * @return
     */
    abstract int[] getDrawGifts();

    protected int[] getGiftPrices(){
        return giftDrawConf.getGiftPrices();
    }

    abstract String getType();

    protected int draw(long uid, int[] drawGifts, double[] drawRates, String type) {
        // 每个线程有自己的Random对象
        double randomNumber = RandomUtils.threadLocalRandomDouble();
        int length = drawRates.length;
        int giftId = giftDrawConf.getDefaultGiftId();
        for (int i = 0; i < length; i++) {
            if (randomNumber <= drawRates[i]) {
                giftId = drawGifts[i];
                break;
            }
        }

        // 检查礼物是否有效
        GiftDTO gift = giftManager.getValidGiftById(giftId);
        // 礼物失效
        if (gift == null) {
            return giftDrawConf.getDefaultGiftId();
        }
        log.info("[ 捡海螺 ] uid:>{} , 类型:{} , 砸出礼物:>{} , 随机数:>{} , 概率:>{}", uid, type, giftId, randomNumber, drawRates);
        if (giftId == giftDrawConf.getFullGiftId()) {
            dingLog(uid, giftId, randomNumber);
        }
        return giftId;
    }

    /**
     * 日志输出
     *
     * @param uid    uid
     * @param giftId giftId
     */
    private void dingLog(Long uid, int giftId, Double randomNumber) {
        log.info("[ 捡海螺 ] {}通过【{}】方式砸中礼物，礼物ID:>{}", uid, this.getType(), giftId);
        if (SystemConf.ENV_PROD.equalsIgnoreCase(systemConf.getEnv())) {
            String text = String.format("[ 捡海螺预警 ] 【%s】%s通过【%s】方式砸中礼物,随机数:【%s】礼物ID:>%s", systemConf.getEnvName(), uid, this.getType(), randomNumber, giftId);
            chatbotManager.send(dingTalkConf.getDevelopChatbot(), new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false));
        }
    }
}
