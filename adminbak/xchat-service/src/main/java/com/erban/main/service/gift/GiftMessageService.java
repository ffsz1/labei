package com.erban.main.service.gift;

import com.erban.main.message.GiftMessage;
import com.erban.main.model.BillRecord;
import com.erban.main.model.GiftSendRecord;
import com.erban.main.model.UserPurse;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.statis.StatRoomCtrbSumService;
import com.erban.main.service.statis.StatRoomCtrbSumTotalService;
import com.erban.main.service.user.ExpressWallService;
import com.erban.main.service.user.UserGiftWallService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GiftMessageService extends BaseService {

    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private GiftSendRecordService giftSendRecordService;
    @Autowired
    private StatRoomCtrbSumService statRoomCtrbSumService;
    @Autowired
    private StatRoomCtrbSumTotalService statRoomCtrbSumTotalService;
    @Autowired
    private UserGiftWallService userGiftWallService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private ExpressWallService expressWallService;

    public void handleGiftMessage(GiftMessage giftMessage) throws Exception {
        Long sendUid = giftMessage.getSendUid();
        Long recvUid = giftMessage.getRecvUid();
        Long roomUid = giftMessage.getRoomUid();
        Long goldNum = giftMessage.getGoldNum();
        Long afterGoldNum = giftMessage.getAfterGoldNum();
        Long useGiftPurseNum = giftMessage.getUseGiftPurseNum();
        Integer giftId = giftMessage.getGiftId();
        Integer giftNum = giftMessage.getGiftNum();
        Byte sendType = giftMessage.getSendType();
        String expressMessage = giftMessage.getExpressMessage();

        if(afterGoldNum.intValue()>0){
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(sendUid, afterGoldNum);
            if(result != 1){
                UserPurse mysqlPurse = userPurseService.getUserPurseFromDb(sendUid);
                UserPurse cachePurse = userPurseService.getUserPurseCache(sendUid);
                logger.error("reduceGoldNumFromDB error, sendUid=" + sendUid + ", recvUid=" + recvUid + ", roomUid="
                        + roomUid + ", giftId=" + giftId + ", giftNum=" + giftNum
                        + ", cacheGold="+(cachePurse==null?"":cachePurse.getGoldNum()) +", mysqlGold="+mysqlPurse.getGoldNum());
            }
        }
        if(useGiftPurseNum.intValue()>0){
            // 更新送礼物用户的拥有礼物
            int result = jdbcTemplate.update("update user_gift_purse set count_num = count_num - ? where uid = ? and gift_id = ? and count_num - ? >=0", useGiftPurseNum, sendUid, giftId, useGiftPurseNum);
            if(result != 1){
                logger.error("更新送礼物用户的拥有礼物失败");
            }
            BillRecord billRecord = new BillRecord();
            billRecord.setBillId(UUIDUitl.get());
            billRecord.setUid(sendUid);
            billRecord.setObjId(giftId.toString());
            billRecord.setObjType(Constant.BillType.useGift);
            billRecord.setGiftId(giftId);
            billRecord.setGiftNum(useGiftPurseNum.intValue());
            billRecord.setCreateTime(new Date());
            billRecordService.insertBillRecord(billRecord);
        }
        // 更新收礼物用户的钱包，加钻石
        userPurseUpdateService.addDiamondNumFromDB(recvUid, giftMessage.getDiamondNum());
        // 更新收礼物用户的钱包缓存信息
        UserPurse userPurse = userPurseService.getPurseByUid(recvUid);
        if (userPurse != null) {
            userPurse.setDiamondNum(userPurse.getDiamondNum()+giftMessage.getDiamondNum());
            userPurseService.saveUserPurseCache(userPurse);
        }
        // 删除该标识，表示消息已经消费过
        jedisService.hdel(RedisKey.mq_gift_status.getKey(), giftMessage.getMessId());
        //送礼物者增加用户等级经验
        logger.info("送礼物者增加用户等级经验+收礼物者增加用户等级魅力：goldNum:{}",goldNum);
        jedisService.hincrBy(RedisKey.user_level_exper.getKey(), sendUid.toString(),goldNum);
        //收礼物者增加用户等级魅力
        jedisService.hincrBy(RedisKey.user_level_charm.getKey(), recvUid.toString(),goldNum);

        // 插入送礼物记录
        GiftSendRecord giftSendRecord = buildGiftSendRecord(giftMessage);
        giftSendRecord = giftSendRecordService.insertGiftSendRecord(giftSendRecord);

        String objId = giftSendRecord.getSendRecordId().toString();
        BillRecord billRecord = null;
        // 插入账单记录表（发送礼物用户）
        billRecord = buildBillRecord(giftMessage, sendUid, recvUid, objId, Constant.BillType.giftPay);
        billRecordService.insertBillRecord(billRecord);
        // 插入账单记录表（接收礼物用户）
        billRecord = buildBillRecord(giftMessage, recvUid, sendUid, objId, Constant.BillType.giftIncome);
        billRecordService.insertBillRecord(billRecord);

        // 房间内送礼物时，更新房间内礼物统计排行榜
        if (sendType == Constant.SendGiftType.room || sendType == Constant.SendGiftType.roomToperson) {
            statRoomCtrbSumService.addAndUpdateStatRoomCtrbSumList(roomUid, sendUid, goldNum);
            statRoomCtrbSumTotalService.addAndUpdateStatRoomCtrbSumTotalList(roomUid, goldNum);
        }
        if (sendType == Constant.SendGiftType.express) {
            // 添加表白记录
            expressWallService.save(sendUid, recvUid, giftId, giftNum, goldNum, expressMessage);
        }
        try{
            //更新个人礼物墙
            userGiftWallService.updateGiftWallCount(recvUid, giftId, giftNum);
        }catch (Exception e){// 并发情况会报错，之后改善
            logger.error("updateGiftWallCount error, recvUid=" + recvUid + ", giftId=" + giftId + ", giftNum=" + giftNum);
        }

    }

    private GiftSendRecord buildGiftSendRecord(GiftMessage giftMessage) {
        GiftSendRecord giftSendRecord = new GiftSendRecord();
        giftSendRecord.setGiftId(giftMessage.getGiftId());
        giftSendRecord.setReciveUid(giftMessage.getRecvUid());
        giftSendRecord.setRoomUid(giftMessage.getRoomUid());
        giftSendRecord.setRoomType(giftMessage.getRoomType());
        giftSendRecord.setGiftNum(giftMessage.getGiftNum());
        giftSendRecord.setSendEnv(giftMessage.getSendType());
        giftSendRecord.setUid(giftMessage.getSendUid());
        giftSendRecord.setTotalGoldNum(giftMessage.getGoldNum());
        giftSendRecord.setTotalDiamondNum(giftMessage.getDiamondNum());
        giftSendRecord.setCreateTime(new Date());
        return giftSendRecord;
    }

    private BillRecord buildBillRecord(GiftMessage giftMessage, Long uid, Long targetUid, String objId, Byte billType) {
        Date date = new Date();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(targetUid);
        billRecord.setRoomUid(giftMessage.getRoomUid());
        billRecord.setObjId(objId);
        billRecord.setObjType(billType);
        billRecord.setGoldNum(giftMessage.getGoldNum());
        billRecord.setDiamondNum(giftMessage.getDiamondNum());
        billRecord.setMoney(null);
        billRecord.setCreateTime(date);
        billRecord.setGiftId(giftMessage.getGiftId());
        billRecord.setGiftNum(giftMessage.getGiftNum());
        return billRecord;
    }
}
