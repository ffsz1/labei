package com.erban.main.service.noble;

import com.erban.main.message.NobleMessage;
import com.erban.main.model.NobleRight;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.mq.ActiveMQService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoblePayService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(NoblePayService.class);

    @Autowired
    private NobleRightService nobleRightService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private ActiveMQService activeMQService;
    @Autowired
    private BillRecordService billRecordService;

    /**
     * 通过金币开通贵族
     *
     * @param uid
     * @param clientIp
     * @param nobleRight
     * @return
     */
    public BusiResult openNobleByGold(Long uid, Long roomUid, NobleRight nobleRight, String clientIp) throws Exception {
        // 扣减用户钱包的金币（扣减充值的金币数和总金币数）
        int result = userPurseUpdateService.reduceChargeGoldDbAndCache(uid, nobleRight.getOpenGold());
        if (result != 200) {
            logger.info("openNobleByGold fail=============>>>>{}, clientIp:{}", result, clientIp);
            switch (result) {
                case 503:
                    return new BusiResult(BusiStatus.SERVERBUSY);
                case 500:
                    return new BusiResult(BusiStatus.SERVERERROR);
                case 403:
                    return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
        }
        // 处理开通或者续费贵族
        handlOpenOrRenewNoble(uid, roomUid, Constant.BillType.openNoble, Constant.NobleOptType.open
                , Constant.NoblePayType.gold, nobleRight);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 通过金币续费贵族
     *
     * @param uid
     * @param clientIp
     * @param nobleRight
     * @return
     */
    public BusiResult renewNobleByGold(Long uid, Long roomUid, NobleRight nobleRight, String clientIp) throws Exception {
        // 扣减用户钱包的金币（扣减充值的金币数和总金币数）
        int result = userPurseUpdateService.reduceChargeGoldDbAndCache(uid, nobleRight.getRenewGold());
        if (result != 200) {
            logger.info("renewNobleByGold fail=============>>>>{}, clientIp:{}", result, clientIp);
            switch (result) {
                case 503:
                    return new BusiResult(BusiStatus.SERVERBUSY);
                case 500:
                    return new BusiResult(BusiStatus.SERVERERROR);
                case 403:
                    return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
        }
        // 处理开通或者续费贵族
        handlOpenOrRenewNoble(uid, roomUid, Constant.BillType.renewNoble, Constant.NobleOptType.renew
                , Constant.NoblePayType.gold, nobleRight);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     *  处理开通或者续费贵族
     *
     * @param uid
     * @param roomUid
     * @param billType
     * @param optType
     * @param payType
     * @param nobleRight
     */
    public void handlOpenOrRenewNoble(Long uid, Long roomUid, Byte billType, Byte optType, Byte payType, NobleRight nobleRight) {
        // 插入账单记录
        billRecordService.insertBillRecord(uid, uid, null, billType, null
                , -nobleRight.getRenewGold(), null);
        // 发送贵族开通消息，由MQ消费进行后续的初始化
        sendNobleMessage(uid, roomUid, optType, payType, null, nobleRight);
    }

    public void sendNobleMessage(Long uid, Long roomUid, Byte optType, Byte payType, Long money, NobleRight nobleRight){
        // 构建开通贵族的队列消息
        NobleMessage message = buildNobleMessage(uid, roomUid, optType, payType, money, nobleRight);
        // 缓存消息的消费状态，便于队列消息做幂等处理
        jedisService.hset(RedisKey.mq_noble_status.getKey(), message.getMessId(), gson.toJson(message));
        // 发送开通消息到MQ进行后续操作
        activeMQService.sendNobleMessage(message);
    }

    public NobleMessage buildNobleMessage(Long uid, Long roomUid, Byte optType, Byte payType, Long money, NobleRight nobleRight) {
        NobleMessage message = new NobleMessage();
        message.setMessId(UUIDUitl.get());
        message.setMessTime(new Date().getTime());
        message.setUid(uid);
        message.setMoney(money);
        message.setRoomUid(roomUid);
        message.setOptType(optType);
        message.setPayType(payType);
        message.setNobleId(nobleRight.getId());
        message.setNobleName(nobleRight.getName());
        if (optType.equals(Constant.NobleOptType.open)) { // 开通
            message.setPayGold(nobleRight.getOpenGold());
            message.setNobleGold(nobleRight.getOpenReturn());
        } else { // 续费
            message.setPayGold(nobleRight.getRenewGold());
            message.setNobleGold(nobleRight.getRenewReturn());
        }
        return message;
    }


    public NobleRight getNobleRightById(Integer nobleId){
       return nobleRightService.getNobleRight(nobleId);
    }

    public static void main(String[] args) {
        Byte ss = 1;
        byte dd = 1;

        if (ss == dd) {
            System.out.println(3432);
        }
        if (dd == ss) {
            System.out.println(4444);
        }

        if (ss.equals(dd)) {
            System.out.println(56555);
        }
    }
}
