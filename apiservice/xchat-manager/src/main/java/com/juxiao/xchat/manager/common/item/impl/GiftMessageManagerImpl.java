package com.juxiao.xchat.manager.common.item.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.SendGiftType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.bill.BillGiftGiveDao;
import com.juxiao.xchat.dao.bill.BillGiftUseDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillGiftGiveDO;
import com.juxiao.xchat.dao.bill.domain.BillGiftUseDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.item.GiftSendRecordDao;
import com.juxiao.xchat.dao.item.domain.GiftSendRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.mcoin.UserMcoinPurseDao;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.UserGiftPurseDao;
import com.juxiao.xchat.dao.user.UserGiftWallDao;
import com.juxiao.xchat.dao.user.UserPurseDao;
import com.juxiao.xchat.dao.user.domain.UserGiftPurseDO;
import com.juxiao.xchat.dao.user.domain.UserGiftWallDO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.GiftMessageManager;
import com.juxiao.xchat.manager.common.item.mq.BigGiftMessage;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;
import com.juxiao.xchat.manager.common.item.vo.GiftSendVO;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.ExpressWallService;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class GiftMessageManagerImpl implements GiftMessageManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BillGiftUseDao giftUseDao;

    @Resource
    private BillGiftGiveDao giftGiveDao;

    @Resource
    private GiftSendRecordDao giftSendRecordDao;

    @Resource
    private BillRecordDao recordDao;

    @Resource
    private UserPurseDao userPurseDao;

    @Resource
    private UserGiftPurseDao giftPurseDao;

    @Resource
    private UserGiftWallDao userGiftWallDao;

    @Resource
    private GiftManager giftManager;

    @Resource
    private ImRoomManager imroomManager;

    @Resource
    private LevelManager levelManager;

    @Resource
    private RedisManager redisManager;

    @Resource
    private RoomManager roomManager;

    @Resource
    private UsersManager usersManager;

    @Resource
    private UserPurseManager userPurseManager;

    @Resource
    private ExpressWallService expressWallService;

    @Resource
    private UserMcoinPurseDao mcoinPurseDao;

    @Resource
    private McoinMissionManager missionManager;

    @Override
    public void handleGiftMessage(GiftMessage message) throws Exception {
        if (message.getAfterGoldNum() > 0) {
            // 更新送礼物用户的钱包，减金币
            userPurseDao.updateGoldCost(message.getSendUid(), message.getAfterGoldNum());
        }

        if (message.getUseGiftPurseNum().intValue() > 0) {// 要减的礼物数量
            // 更新送礼物用户的拥有礼物
            UserGiftPurseDO giftPurseDo = new UserGiftPurseDO();
            giftPurseDo.setUid(message.getSendUid());
            giftPurseDo.setGiftId(message.getGiftId());
            giftPurseDo.setCountNum(message.getUseGiftPurseNum().intValue());
            giftPurseDao.updateReduceCountNum(giftPurseDo);

            BillGiftUseDO giftDo = new BillGiftUseDO();
            giftDo.setUid(message.getSendUid());
            giftDo.setGiftId(message.getGiftId());
            giftDo.setGiftNum(message.getUseGiftPurseNum().intValue());
            giftDo.setCreateTime(new Date());
            giftUseDao.save(giftDo);

            // FIXME: 兼容管理后台的查询，管理后台修改之后，删除
            BillRecordDO billRecord = new BillRecordDO();
            billRecord.setBillId(UUIDUtils.get());
            billRecord.setUid(message.getSendUid());
            billRecord.setObjId(message.getGiftId().toString());
            billRecord.setObjType(BillRecordType.useGift);
            billRecord.setGiftId(message.getGiftId());
            billRecord.setGiftNum(message.getUseGiftPurseNum().intValue());
            billRecord.setCreateTime(new Date());
            recordDao.save(billRecord);
        }

        // 更新收礼物用户的钱包缓存信息，加钻石
        UserPurseDTO purseDto = userPurseManager.getUserPurse(message.getRecvUid());
        if (purseDto != null) {
            userPurseManager.updateAddDiamond(message.getRecvUid(), message.getDiamondNum(), false);
        }
        RoomDTO roomDto = roomManager.getUserRoom(message.getRoomUid());
        if (roomDto != null && 1 == roomDto.getIsPermitRoom().intValue()) {
            UsersDTO roomOwner = usersManager.getUser(message.getRoomUid());
            UserPurseDTO roomOwnerPurse = userPurseManager.getUserPurse(roomOwner.getUid());
            Double roomDiamondBefore = roomOwnerPurse.getDiamondNum();// 房主增加前钻石数量
            userPurseManager.updateAddDiamond(message.getRoomUid(), message.getOwnerDiamondNum(), false);
            logger.info("[ 增加钻石 ] uid:>{} , 增加前:>{}，增加后:>{}", roomOwner.getUid(),
                    roomDiamondBefore, roomDiamondBefore + message.getOwnerDiamondNum());
            logger.info("", message.getGoldNum());
        }

        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_gift_status.getKey(), message.getMessId());
        //送礼物者增加用户等级经验
        logger.info("送礼物者增加用户等级经验+收礼物者增加用户等级魅力：goldNum:{}", message.getGoldNum());
        redisManager.hincrBy(RedisKey.user_level_exper.getKey(), message.getSendUid().toString(), message.getGoldNum());
        //收礼物者增加用户等级魅力
        redisManager.hincrBy(RedisKey.user_level_charm.getKey(), message.getRecvUid().toString(), message.getGoldNum());

        // 插入送礼物记录
        GiftSendRecordDO sendRecordDo = new GiftSendRecordDO();
        sendRecordDo.setGiftId(message.getGiftId());
        sendRecordDo.setReciveUid(message.getRecvUid());
        sendRecordDo.setRoomUid(message.getRoomUid());
        sendRecordDo.setRoomType(message.getRoomType());
        sendRecordDo.setGiftNum(message.getGiftNum());
        sendRecordDo.setSendEnv(message.getSendType());
        sendRecordDo.setUid(message.getSendUid());
        sendRecordDo.setTotalGoldNum(message.getGoldNum());
        sendRecordDo.setTotalDiamondNum(message.getDiamondNum());
        sendRecordDo.setCreateTime(new Date());
        giftSendRecordDao.save(sendRecordDo);

        BillGiftGiveDO giftDo = new BillGiftGiveDO();
        giftDo.setRoomUid(message.getRoomUid());
        giftDo.setGiverUid(message.getSendUid());
        giftDo.setReceiverUid(message.getRecvUid());
        giftDo.setRecordId(sendRecordDo.getSendRecordId());
        giftDo.setGiftId(message.getGiftId());
        giftDo.setGiftNum(message.getGiftNum());
        giftDo.setGoldCost(message.getGoldNum().intValue());
        giftDo.setDiamondAmount(message.getDiamondNum());
        giftDo.setCreateTime(new Date());
        giftGiveDao.save(giftDo);

        // FIXME: 兼容管理后台的查询
        this.buildBillRecord(message, message.getSendUid(), message.getRecvUid(),
                String.valueOf(sendRecordDo.getSendRecordId()), BillRecordType.giftPay);
        this.buildBillRecord(message, message.getRecvUid(), message.getSendUid(),
                String.valueOf(sendRecordDo.getSendRecordId()), BillRecordType.giftIncome);

        if (message.getSendType() == SendGiftType.express) {
            // 添加表白记录
            expressWallService.save(message);
        }

        try {
            int userGiftWallCount = userGiftWallDao.countUserGiftWall(message.getRecvUid(), message.getGiftId());
            UserGiftWallDO wallDo = new UserGiftWallDO();
            if (userGiftWallCount == 0) {
                wallDo.setUid(message.getRecvUid());
                wallDo.setGiftId(message.getGiftId());
                wallDo.setReciveCount(message.getGiftNum());
                wallDo.setCreateTime(new Date());
                userGiftWallDao.save(wallDo);
            } else {
                wallDo.setGiftId(message.getGiftId());
                wallDo.setUid(message.getRecvUid());
                wallDo.setReciveCount(message.getGiftNum());
                userGiftWallDao.updateGiftWallReciveCount(wallDo);
            }
        } catch (Exception e) {
            logger.warn("[ 记录礼物墙失败 ] recvUid=" + message.getRecvUid() + ", giftId=" + message.getGiftId() + ", " +
                    "giftNum=" + message.getGiftNum());
        }

        try {
            missionManager.finish(message.getSendUid(), 9);
        } catch (Exception e) {
            logger.error("[ 每日任务 ] 完成送礼异常:", message, e);
        }
    }

    @Override
    public void handleBigGiftMessage(BigGiftMessage message) throws Exception {
        GiftDTO gift = giftManager.getValidGiftById(message.getGiftId());
        Long totalGoldNum = Math.abs(gift.getGoldPrice() * message.getGiftNum() * (message.getTargetSize() == null ?
                1 : message.getTargetSize().length));
        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_big_gift_status.getKey(), message.getMessId());
        if (totalGoldNum < 30000) {
            return;
        }

        UsersDTO users = usersManager.getUser(message.getUid());
        UsersDTO roomUser = usersManager.getUser(message.getRoomUid());
        UsersDTO targerUsers = null;
        if (message.getTargetUid() != null) {
            targerUsers = usersManager.getUser(message.getTargetUid());
        }

        GiftSendVO sendGiftVo = new GiftSendVO();
        int expLevel = levelManager.getUserExperienceLevelSeq(users.getUid());
        sendGiftVo.setUid(message.getUid());
        sendGiftVo.setGiftId(message.getGiftId());
        sendGiftVo.setAvatar(users.getAvatar());
        sendGiftVo.setNick(users.getNick());
        sendGiftVo.setGiftNum(message.getGiftNum());
        if (targerUsers == null) {
            sendGiftVo.setTargetUid(0L);
            sendGiftVo.setTargetAvatar("");
            sendGiftVo.setTargetNick("全麦");
        } else {
            sendGiftVo.setTargetUid(targerUsers.getUid());
            sendGiftVo.setTargetAvatar(targerUsers.getAvatar());
            sendGiftVo.setTargetNick(targerUsers.getNick());
        }
        sendGiftVo.setRoomUid(message.getRoomUid());// 跳转房间的用户id
        sendGiftVo.setUserNo(roomUser.getErbanNo());// 跳转房间的用户号
        sendGiftVo.setExperLevel(expLevel);

        Attach attach = new Attach();
        attach.setFirst(DefMsgType.GiftServiceSend);
        attach.setSecond(DefMsgType.GiftServiceSend);
        attach.setData(sendGiftVo);

        JSONObject object = new JSONObject();
        object.put("custom", attach);

        List<Long> interceptRoomList = Lists.newArrayList();
        RoomDTO roomDto = roomManager.getUserRoom(message.getRoomUid());
        if (roomDto != null) {
            interceptRoomList.add(roomDto.getRoomId());
        }

        imroomManager.pushAllRoomMsg(object, interceptRoomList);
    }

    @Override
    public void handleCallMessage(GiftMessage message) throws Exception {
        if (message.getGoldNum() > 0 && message.getConchNum() > 0) {//getGoldNum 要减的金币数量 要减的海螺次数
            // 更新打call 用户sql的钱包，减金币 加次数
            userPurseDao.updateGoldCostConchAmount(message.getSendUid(), message.getGoldNum(), message.getConchNum());

            // 插入打call记录
            GiftSendRecordDO sendRecordDo = new GiftSendRecordDO();
            sendRecordDo.setGiftId(message.getGiftId());
            sendRecordDo.setReciveUid(message.getRecvUid());
            sendRecordDo.setRoomUid(message.getRoomUid());
            sendRecordDo.setRoomType(message.getRoomType());
            sendRecordDo.setGiftNum(message.getGiftNum());
            sendRecordDo.setSendEnv(message.getSendType());
            sendRecordDo.setUid(message.getSendUid());
            sendRecordDo.setTotalGoldNum(message.getGoldNum());
            sendRecordDo.setTotalDiamondNum(message.getDiamondNum());
            sendRecordDo.setCreateTime(new Date());
            giftSendRecordDao.saveCall(sendRecordDo);

            BillGiftGiveDO giftDo = new BillGiftGiveDO();
            giftDo.setRoomUid(message.getRoomUid());
            giftDo.setGiverUid(message.getSendUid());
            giftDo.setReceiverUid(message.getRecvUid());
            giftDo.setRecordId(sendRecordDo.getSendRecordId());
            giftDo.setGiftId(message.getGiftId());
            giftDo.setGiftNum(message.getGiftNum());
            giftDo.setGoldCost(message.getGoldNum().intValue());
            giftDo.setDiamondAmount(message.getDiamondNum());
            giftDo.setCreateTime(new Date());
            giftGiveDao.saveCallBill(giftDo);
        } else {
            logger.error("[ 打call记录 ] 参数异常, uid:>{}, goldNum:>{}, conchNum:>{}", message.getSendUid(),
                    message.getGoldNum(), message.getConchNum());
        }

        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_call_status.getKey(), message.getMessId());
    }

    @Override
    public void handleGiftPropMessage(GiftMessage message) throws Exception {
        // todo 1.0.1版本有bug 几个版本过后修改代码块 挪到执行完sql的后面 可参考 handleGiftMessage
        // 删除该标识，表示消息已经消费过
        redisManager.hdel(RedisKey.mq_gift_prop_status.getKey(), message.getMessId());

        if (message.getUseGiftPurseNum().intValue() > 0) {// 要减的礼物数量
            // BillGiftUseDO giftDo = new BillGiftUseDO();
            // giftDo.setUid(message.getSendUid());
            // giftDo.setGiftId(message.getGiftId());
            // giftDo.setGiftNum(message.getUseGiftPurseNum().intValue());
            // giftDo.setCreateTime(new Date());
            // giftUseDao.save(giftDo);
            //
            // // FIXME: 兼容管理后台的查询，管理后台修改之后，删除
            // BillRecordDO billRecord = new BillRecordDO();
            // billRecord.setBillId(UUIDUtils.get());
            // billRecord.setUid(message.getSendUid());
            // billRecord.setObjId(message.getGiftId().toString());
            // billRecord.setObjType(BillRecordType.useGift);
            // billRecord.setGiftId(message.getGiftId());
            // billRecord.setGiftNum(message.getUseGiftPurseNum().intValue());
            // billRecord.setCreateTime(new Date());
            // recordDao.save(billRecord);

            // 插入送礼物记录
            GiftSendRecordDO sendRecordDo = new GiftSendRecordDO();
            sendRecordDo.setGiftId(message.getGiftId());
            sendRecordDo.setReciveUid(message.getRecvUid());
            sendRecordDo.setRoomUid(message.getRoomUid());
            sendRecordDo.setRoomType(message.getRoomType());
            sendRecordDo.setGiftNum(message.getGiftNum());
            sendRecordDo.setSendEnv(message.getSendType());
            sendRecordDo.setUid(message.getSendUid());
            sendRecordDo.setTotalGoldNum(message.getGoldNum());
            sendRecordDo.setTotalDiamondNum(message.getDiamondNum());
            sendRecordDo.setCreateTime(new Date());
            giftSendRecordDao.saveProp(sendRecordDo);

            // todo 1.0.1版本有bug 几个版本过后修改代码块 挪到插入记录前面 可参考 handleGiftMessage
            // 更新送礼物用户的拥有礼物
            UserGiftPurseDO giftPurseDo = new UserGiftPurseDO();
            giftPurseDo.setUid(message.getSendUid());
            giftPurseDo.setGiftId(message.getGiftId());
            giftPurseDo.setCountNum(message.getUseGiftPurseNum().intValue());
            giftPurseDao.updateReduceCountNum(giftPurseDo);
        }

        // BillGiftGiveDO giftDo = new BillGiftGiveDO();
        // giftDo.setRoomUid(message.getRoomUid());
        // giftDo.setGiverUid(message.getSendUid());
        // giftDo.setReceiverUid(message.getRecvUid());
        // giftDo.setRecordId(sendRecordDo.getSendRecordId());
        // giftDo.setGiftId(message.getGiftId());
        // giftDo.setGiftNum(message.getGiftNum());
        // giftDo.setGoldCost(message.getGoldNum().intValue());
        // giftDo.setDiamondAmount(message.getDiamondNum());
        // giftDo.setCreateTime(new Date());
        // giftGiveDao.save(giftDo);

        // try {
        //    int userGiftWallCount = userGiftWallDao.countUserGiftWall(message.getRecvUid(), message.getGiftId());
        //    UserGiftWallDO wallDo = new UserGiftWallDO();
        //    if (userGiftWallCount == 0) {
        //        wallDo.setUid(message.getRecvUid());
        //        wallDo.setGiftId(message.getGiftId());
        //        wallDo.setReciveCount(message.getGiftNum());
        //        wallDo.setCreateTime(new Date());
        //        userGiftWallDao.save(wallDo);
        //    } else {
        //        wallDo.setGiftId(message.getGiftId());
        //        wallDo.setUid(message.getRecvUid());
        //        wallDo.setReciveCount(message.getGiftNum());
        //        userGiftWallDao.updateGiftWallReciveCount(wallDo);
        //    }
        // } catch (Exception e) {
        //    logger.warn("[ 记录礼物墙失败 ] recvUid=" + message.getRecvUid() + ", giftId=" + message.getGiftId() + ",
        //    giftNum=" + message.getGiftNum());
        // }

        // try {
        //    missionManager.finish(message.getSendUid(), 9);
        // } catch (Exception e) {
        //    logger.error("[ 每日任务 ] 完成送礼异常:", message, e);
        // }
    }

    @Deprecated
    private void buildBillRecord(GiftMessage giftMessage, Long uid, Long targetUid, String objId,
                                 BillRecordType billType) {
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(UUIDUtils.get());
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
        recordDao.save(billRecord);
    }
}
