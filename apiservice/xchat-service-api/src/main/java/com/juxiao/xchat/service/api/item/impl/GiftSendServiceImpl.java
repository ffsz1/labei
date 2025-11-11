package com.juxiao.xchat.service.api.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.SendGiftType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.constant.GiftType;
import com.juxiao.xchat.manager.common.item.mq.BigGiftMessage;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;
import com.juxiao.xchat.manager.common.item.vo.GiftSendVO;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomCharmManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomPkVoteManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.item.GiftSendService;
import com.juxiao.xchat.service.api.user.UserGiftPurseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GiftSendServiceImpl implements GiftSendService {
    private static final Logger logger = LoggerFactory.getLogger(GiftSendServiceImpl.class);

    private List<Integer> GIFT_NUM_OPTION = Lists.newArrayList(1, 10, 38, 66, 99, 188, 520, 1314);
    @Autowired
    private SystemConf systemConf;

    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private GiftManager giftManager;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomPkVoteManager pkvoteManager;
    @Autowired
    private UserGiftPurseManager giftPurseManager;
    @Autowired
    private Gson gson;

    @Autowired
    private RoomCharmManager roomCharmManager;

    @Autowired
    private UserGiftPurseService userGiftPurseService;


    @Override
    public WebServiceMessage sendGiftInPrivateChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum,
                                                   byte sendType) throws WebServiceException {
        return sendGiftToOne(sendUid, recvUid, null, giftId, giftNum, sendType, null, "");
    }

    @Override
    public WebServiceMessage sendGiftInRoomChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum,
                                                byte sendType, String appVersion) throws WebServiceException {
        RoomDTO room = roomManager.getUserRoom(roomUid);
        if (room == null) {
            return WebServiceMessage.failure(WebServiceCode.ROOM_NOT_EXIST);
        }

        return sendGiftToOne(sendUid, recvUid, room, giftId, giftNum, sendType, room.getType(), "");
    }

    @Override
    public WebServiceMessage sendExpressGift(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum,
                                             byte sendType, String expressMessage) throws WebServiceException {
        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        return sendGiftToOne(sendUid, recvUid, roomDto, giftId, giftNum, sendType, null, expressMessage);
    }

    /**
     * 送礼物给某个用户
     *
     * @param sendUid        赠送UID
     * @param recvUid        接收UID
     * @param roomDto        房间
     * @param giftId         礼物ID
     * @param giftNum        礼物数量
     * @param sendType       发送类型
     * @param roomType       房间类型
     * @param expressMessage 表白信息
     * @return
     * @throws WebServiceException
     */
    private WebServiceMessage sendGiftToOne(long sendUid, long recvUid, RoomDTO roomDto, int giftId, int giftNum,
                                            byte sendType, Byte roomType, String expressMessage) throws WebServiceException {
        UsersDTO sendUser = usersManager.getUser(sendUid);
        if (sendUser == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO recvUser = usersManager.getUser(recvUid);
        if (recvUser == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        GiftDTO giftDto = giftManager.getValidGiftById(giftId);
        if (giftDto == null) {
            throw new WebServiceException(WebServiceCode.GIFT_DOWN_OR_NOT_EXISTS);
        }

        if (!Boolean.TRUE.equals(giftDto.getIsCanGive())) {
            throw new WebServiceException(WebServiceCode.GIFT_CANNOT_GIVE);
        }

        if (roomDto != null) { // 房间送礼要区分类型 限制海螺礼物送相亲房和相亲礼物送非相亲房
            // 相亲礼物 非相亲房间
            if (giftDto.getGiftType() == GiftType.XIANGQIN && (!roomDto.getTagId().equals(systemConf.getXiangQinTagId()))) {
                throw new WebServiceException(WebServiceCode.XQ_GIFT_TYPE_ERROR);
            }

            // 海螺礼物 相亲房
            if (giftDto.getGiftType() == GiftType.DRAW && roomDto.getTagId().equals(systemConf.getXiangQinTagId())) {
                throw new WebServiceException(WebServiceCode.HL_GIFT_TYPE_ERROR);
            }
        }

        // todo 海角添加数量限制
        // if (!GIFT_NUM_OPTION.contains(giftNum)) {
        //    throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        // }

        // 添加礼物抽奖, 先扣减用户的拥有礼物, 如果还有剩余要送的礼物再按原来逻辑处理
        // 两个要在同一个方法判断，都通过了才能执行方法
        Long giftPrice = giftDto.getGoldPrice();
        // 减去拥有礼物的数量
        Map<String, Long> num = giftPurseManager.reduceGiftPurseCache(sendUid, giftDto, giftNum, giftPrice);

        GiftMessage message;
        // 礼物金币总数
        Long goldNum = Math.abs(giftPrice * giftNum);

        Double diamondNum, ownerDiamondNum = 0.0;
        if (roomDto != null && roomDto.getGiftDrawEnable() != null && roomDto.getGiftDrawEnable() == 1) {
            diamondNum = Math.abs(goldNum * systemConf.getDrawAkira()); // 礼物钻石总数
        } else {
            diamondNum = Math.abs(goldNum * systemConf.getAppAkira()); // 礼物钻石总数
        }
        ownerDiamondNum = Math.abs(goldNum * systemConf.getRoomOwnerAkira()); // 牌照房房主可额外获得

        // 发送礼物消息到MQ处理（加钻石、写DB记录）
        message = buildGiftMessage(sendUid, recvUid, roomDto == null ? null : roomDto.getUid(), giftId, giftNum,
                goldNum, num.get("afterGoldNum"), num.get("useGiftPurseNum"), diamondNum, ownerDiamondNum, 0L, sendType, roomType, null);
        message.setExpressMessage(expressMessage);

        // 缓存消息的消费状态，便于队列消息做幂等处理
        redisManager.hset(RedisKey.mq_gift_status.getKey(), message.getMessId(), gson.toJson(message));
        // 发送MQ消息
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_QUEUE, gson.toJson(message));

        if (roomDto != null) {
            if (SendGiftType.roomToperson == sendType || SendGiftType.room == sendType) {
                try {
                    logger.info("[ 魅力值统计 ] sendUid:{},recvUid:{},goldNum:{}", roomDto.getUid(), recvUid, goldNum);
                    roomCharmManager.saveRoomCharm(roomDto.getUid(), recvUid, goldNum.intValue());
                } catch (Exception e) {
                    logger.error("[ 魅力值统计 ]统计异常，roomUid:>{}, uid:>{}, goldNum:>{}:", message.getRoomUid(),
                            message.getRecvUid(), message.getGoldNum(), e);
                }
            }
        }

        if (SendGiftType.roomToperson == sendType || SendGiftType.room == sendType) {
            try {
                pkvoteManager.goldVote(roomDto == null ? null : roomDto.getUid(), recvUid, goldNum);
            } catch (Exception e) {
            }
        }

//        if (roomDto != null){
//            userGiftPurseService.sendGiftOneRoom(roomDto, sendUser, recvUser,giftDto,giftNum);
//        }

        redisManager.incr(RedisKey.user_send_gift_sum.getKey(String.valueOf(sendUid)), goldNum);

        return WebServiceMessage.success(buildGiftSendVoForOne(sendUid, recvUid, giftId, giftNum, num.get(
                "afterGiftPurseNum"), num.get("afterGoldNum"), null, giftDto, roomDto));
    }

    /**
     * 送礼物给房间内所有麦序上的用户
     *
     * @param sendUid
     * @param recvUids
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @return
     * @throws WebServiceException
     */
    @Override
    @TargetDataSource
    public WebServiceMessage sendGiftToAll(long sendUid, Long[] recvUids, Long roomUid, int giftId, int giftNum,
                                           String appVersion) throws WebServiceException {
        logger.info("sendGiftToAll param==>>> sendUid:{},recvUid:{},roomUid:{},giftId:{},giftNum:{}", sendUid,
                recvUids, roomUid, giftId, giftNum);
        // 获取当前房间的信息，若不存在则返回
        RoomDTO room = roomManager.getUserRoom(roomUid);
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        GiftDTO gift = giftManager.getValidGiftById(giftId);
        if (gift == null) {
            throw new WebServiceException(WebServiceCode.GIFT_DOWN_OR_NOT_EXISTS);
        }

        if (!Boolean.TRUE.equals(gift.getIsCanGive())) {
            throw new WebServiceException(WebServiceCode.GIFT_CANNOT_GIVE);
        }

        // 相亲礼物 非相亲房间
        if (gift.getGiftType() == GiftType.XIANGQIN && (!room.getTagId().equals(systemConf.getXiangQinTagId()))) {
            throw new WebServiceException(WebServiceCode.XQ_GIFT_TYPE_ERROR);
        }

        // 海螺礼物 相亲房
        if (gift.getGiftType() == GiftType.DRAW && room.getTagId().equals(systemConf.getXiangQinTagId())) {
            throw new WebServiceException(WebServiceCode.HL_GIFT_TYPE_ERROR);
        }

        // if (!GIFT_NUM_OPTION.contains(giftNum)) {
        //     throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        // }

        // 添加礼物抽奖，先扣减用户的拥有礼物，如果还有剩余要送的礼物再按原来逻辑处理
        // 两个要在同一个方法判断，都通过了才能执行方法
        Long giftPrice = gift.getGoldPrice();
        Map<String, Long> num = giftPurseManager.reduceGiftPurseCache(sendUid, gift, giftNum * recvUids.length,
                giftPrice);// 减去拥有礼物的数量
        Long everyGoldNum = Math.abs(giftPrice * giftNum);                     // 礼物金币总数，相对于每一次发送者
        // Long totalGoldNum = Math.abs(everyGoldNum * recvUids.length);          // 礼物金币总数，
        // 礼物钻石总数，相对于每一个接收者
        Double diamondNum;
        if (room.getGiftDrawEnable() != null && room.getGiftDrawEnable() == 1) {
            diamondNum = Math.abs(everyGoldNum * systemConf.getDrawAkira()); // 礼物钻石总数
        } else {
            diamondNum = Math.abs(everyGoldNum * systemConf.getAppAkira()); // 礼物钻石总数
        }
        Double ownerDiamondNum = Math.abs(everyGoldNum * systemConf.getRoomOwnerAkira()); // 牌照房房主可额外获得

        GiftMessage message;
        // 循环发送礼物消息到MQ处理（加钻石、写DB记录）
        for (int i = 0; i < recvUids.length; i++) {
            if (i == 0) {// 使用的金币和礼物都在一个里面处理，理论上没问题，但是如果这个报错会导致数据库和缓存不一致
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, everyGoldNum, num.get(
                        "afterGoldNum"), num.get("useGiftPurseNum"), diamondNum, ownerDiamondNum, 0L, SendGiftType.roomToperson,
                        room.getType(), null);
            } else {
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, everyGoldNum, 0L, 0L,
                        diamondNum, ownerDiamondNum, 0L, SendGiftType.roomToperson, room.getType(), null);
            }
            // 缓存消息的消费状态，便于队列消息做幂等处理
            redisManager.hset(RedisKey.mq_gift_status.getKey(), message.getMessId(), gson.toJson(message));
            // 这里报错会有定时器处理
            activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_QUEUE, gson.toJson(message));

            try {
                pkvoteManager.goldVote(roomUid, recvUids[i], everyGoldNum);
            } catch (Exception e) {
            }
        }

        try {
            logger.info("[ 魅力值统计 ] sendUid:{},recvUid:{},goldNum:{}", roomUid, recvUids, everyGoldNum);
            roomCharmManager.saveRoomCharm(roomUid, recvUids, everyGoldNum.intValue());
        } catch (Exception e) {
            logger.error("[ 魅力值统计 ]统计异常，roomUid:>{}, uid:>{}, goldNum:>{}:", roomUid, recvUids, everyGoldNum, e);
        }
        redisManager.incr(RedisKey.user_send_gift_sum.getKey(String.valueOf(sendUid)), everyGoldNum * recvUids.length);

        return WebServiceMessage.success(buildGiftSendVoForAll(sendUid, recvUids, giftId, giftNum, num.get(
                "afterGiftPurseNum"), num.get("afterGoldNum"), gift, room));
    }

    /**
     * 构建礼物消息
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param goldNum
     * @param diamondNum
     * @param sendType   送礼物对象类型 1轻聊房间、竞拍房间给主播直接刷礼物，2私聊送个人礼物,3坑位房中，给坑位中的人刷礼物
     * @param roomType   房间类型竞拍房1，悬赏房2
     * @return
     */
    private GiftMessage buildGiftMessage(Long sendUid, Long recvUid, Long roomUid, Integer giftId, Integer giftNum,
                                         Long goldNum, Long afterGoldNum, Long useGiftPurseNum, Double diamondNum, Double ownerDiamondNum,
                                         Long conchNum, Byte sendType, Byte roomType, Integer beforeGiftId) {
        GiftMessage message = new GiftMessage();
        message.setSendUid(sendUid);
        message.setRecvUid(recvUid);
        message.setRoomUid(roomUid);
        message.setGoldNum(goldNum);
        message.setConchNum(conchNum);
        message.setAfterGoldNum(afterGoldNum);
        message.setUseGiftPurseNum(useGiftPurseNum);
        message.setDiamondNum(diamondNum);
        message.setOwnerDiamondNum(ownerDiamondNum);
        message.setGiftId(giftId);
        message.setGiftNum(giftNum);
        message.setRoomType(roomType);
        message.setSendType(sendType);
        message.setMessId(UUIDUtils.get());
        message.setMessTime(System.currentTimeMillis());
        message.setBeforeGiftId(beforeGiftId);
        return message;
    }

    private GiftSendVO buildGiftSendVoForOne(long sendUid, long recvUid, int giftId, int giftNum,
                                             Long userGiftPurseNum, Long afterGoldNum, Long conchNum, GiftDTO gift,
                                             RoomDTO roomDTO) {
        GiftSendVO giftSendVo = new GiftSendVO();
        UsersDTO sendUser = usersManager.getUser(sendUid);
        if (sendUser != null) {
            giftSendVo.setNick(sendUser.getNick());
            giftSendVo.setAvatar(sendUser.getAvatar());
        } else {
            giftSendVo.setNick(systemConf.getDefaultNick());
            giftSendVo.setAvatar(systemConf.getDefaultHead());
        }
        UsersDTO recvUser = usersManager.getUser(recvUid);
        if (recvUser != null) {
            giftSendVo.setTargetAvatar(recvUser.getAvatar());
            giftSendVo.setTargetNick(recvUser.getNick());
        } else {
            giftSendVo.setTargetNick(systemConf.getDefaultNick());
            giftSendVo.setTargetAvatar(systemConf.getDefaultHead());
        }
        giftSendVo.setUid(sendUid);
        giftSendVo.setTargetUid(recvUid);
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        giftSendVo.setExperLevel(levelManager.getUserExperienceLevelSeq(sendUid));
        giftSendVo.setUserGiftPurseNum(userGiftPurseNum);
        giftSendVo.setUseGiftPurseGold(afterGoldNum);
        giftSendVo.setConchNum(conchNum);
        if (roomDTO != null) {
            giftSendVo.setRoomUid(roomDTO.getUid());
        }
        giftSendVo.setGiftPic(gift.getPicUrl());
        giftSendVo.setGiftName(gift.getGiftName());
        giftSendVo.setGoldPrice(gift.getGoldPrice());
        return giftSendVo;
    }

    private GiftSendVO buildGiftSendVoForAll(long sendUid, Long[] recvUids, int giftId, int giftNum,
                                             Long userGiftPurseNum, Long afterGoldNum, GiftDTO gift, RoomDTO roomDTO) {
        GiftSendVO giftSendVo = new GiftSendVO();
        UsersDTO users = usersManager.getUser(sendUid);
        if (users != null) {
            giftSendVo.setNick(users.getNick());
            giftSendVo.setAvatar(users.getAvatar());
        } else {
            giftSendVo.setTargetNick(systemConf.getDefaultNick());
            giftSendVo.setTargetAvatar(systemConf.getDefaultHead());
        }
        giftSendVo.setUid(sendUid);
        giftSendVo.setTargetUids(Arrays.asList(recvUids));
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        giftSendVo.setExperLevel(levelManager.getUserExperienceLevelSeq(sendUid));
        giftSendVo.setUserGiftPurseNum(userGiftPurseNum);
        giftSendVo.setUseGiftPurseGold(afterGoldNum);
        if (roomDTO != null) {
            giftSendVo.setRoomUid(roomDTO.getUid());
        }
        giftSendVo.setGiftPic(gift.getPicUrl());
        giftSendVo.setGiftName(gift.getGiftName());
        giftSendVo.setGoldPrice(gift.getGoldPrice());
        return giftSendVo;
    }

    @Override
    @TargetDataSource
    public void sendMsgAllRoom(Long uid, Long targetUid, Long roomUid, int giftId, int giftNum, Long[] targetSize) throws WebServiceException {
        GiftDTO gift = giftManager.getValidGiftById(giftId);
        Long giftPrice = gift.getGoldPrice();
        Long totalGoldNum = Math.abs(giftPrice * giftNum * (targetSize == null ? 1 : targetSize.length));          //
        // 礼物金币总数
        if (totalGoldNum < 999) {
            return;
        }
        // 发送礼物消息到MQ处理
        BigGiftMessage message = new BigGiftMessage();
        message.setMessId(UUIDUtils.get());
        message.setMessTime(System.currentTimeMillis());
        message.setUid(uid);
        message.setTargetUid(targetUid);
        message.setRoomUid(roomUid);
        message.setGiftId(giftId);
        message.setGiftNum(giftNum);
        message.setTargetSize(targetSize);
        // 缓存消息的消费状态，便于队列消息做幂等处理
        redisManager.hset(RedisKey.mq_big_gift_status.getKey(), message.getMessId(), gson.toJson(message));
        activeMqManager.sendQueueMessage(MqDestinationKey.BIG_GIFT_QUEUE, gson.toJson(message));
    }

    /**
     * 房间送礼物给用户进行打call
     *
     * @param sendUid    uid
     * @param recvUid    targetUid
     * @param roomUid    roomUid
     * @param giftId     giftId
     * @param giftNum    giftNum
     * @param sendType   type
     * @param appVersion appVersion
     * @return WebServiceMessage
     */
    @Override
    public WebServiceMessage callForUserWithSendGift(long sendUid, long recvUid, Long roomUid, int giftId,
                                                     int giftNum, byte sendType, String appVersion) throws WebServiceException {
        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            return WebServiceMessage.failure(WebServiceCode.ROOM_NOT_EXIST);
        }

        UsersDTO usersDTO = usersManager.getUser(recvUid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        GiftDTO giftDto = giftManager.getValidGiftById(giftId);
        if (giftDto == null) {
            throw new WebServiceException(WebServiceCode.GIFT_DOWN_OR_NOT_EXISTS);
        }

        if (giftDto.getGiftType() != GiftType.CALL) {
            throw new WebServiceException(WebServiceCode.GIFT_NOT_CALL_);
        }

        if (!Boolean.TRUE.equals(giftDto.getIsCanGive())) {
            throw new WebServiceException(WebServiceCode.GIFT_CANNOT_GIVE);
        }

        if (!GIFT_NUM_OPTION.contains(giftNum)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        Long giftPrice = giftDto.getGoldPrice();
        long conchNum = giftPrice / 20;    // 除以金币获取捡海螺次数

        // 更新缓存
        Map<String, Long> num = giftPurseManager.addCallPurseCache(sendUid, giftDto, giftNum, giftPrice, conchNum);

        // 礼物金币总数
        Long goldNum = Math.abs(giftPrice * giftNum);
        GiftMessage message;
        // 发送礼物消息到MQ处理
        message = buildGiftMessage(sendUid, recvUid, roomDto == null ? null : roomDto.getUid(),
                giftId, giftNum, goldNum, null, null, 0.0, 0.0, conchNum, sendType, roomDto.getType(), null);

        // 缓存消息的消费状态，便于队列消息做幂等处理
        redisManager.hset(RedisKey.mq_call_status.getKey(), message.getMessId(), gson.toJson(message));
        // 发送MQ消息
        activeMqManager.sendQueueMessage(MqDestinationKey.CALL_QUEUE, gson.toJson(message));

        return WebServiceMessage.success(buildGiftSendVoForOne(sendUid, recvUid, giftId, giftNum, null, giftPrice,
                num.get("afterConchNum"), giftDto, roomDto));
    }


    /******************************************************************** 发送活动礼物 start
     *  ********************************************************************/

    @Override
    public WebServiceMessage sendGiftPropInPrivateChat(long sendUid, long recvUid, Long roomUid, int giftId,
                                                       int giftNum, byte sendType) throws WebServiceException {
        return sendGiftPropToOne(sendUid, recvUid, null, giftId, giftNum, sendType, null, "");
    }

    @Override
    public WebServiceMessage sendGiftPropInRoomChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum
            , byte sendType, String appVersion) throws WebServiceException {
        RoomDTO room = roomManager.getUserRoom(roomUid);
        if (room == null) {
            return WebServiceMessage.failure(WebServiceCode.ROOM_NOT_EXIST);
        }

        return sendGiftPropToOne(sendUid, recvUid, room, giftId, giftNum, sendType, room.getType(), "");
    }

    /**
     * 送礼物给某个用户
     *
     * @return
     * @throws WebServiceException
     */
    private WebServiceMessage sendGiftPropToOne(long sendUid, long recvUid, RoomDTO roomDto, int giftId, int giftNum,
                                                byte sendType, Byte roomType, String expressMessage) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUser(recvUid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        GiftDTO giftDto = giftManager.getValidGiftById(giftId);
        if (giftDto == null) {
            throw new WebServiceException(WebServiceCode.GIFT_DOWN_OR_NOT_EXISTS);
        }

        if (!Boolean.TRUE.equals(giftDto.getIsCanGive())) {
            throw new WebServiceException(WebServiceCode.GIFT_CANNOT_GIVE);
        }

        // 更新缓存
        Map<String, Long> num = giftPurseManager.reduceGiftPropPurseCache(sendUid, giftDto, giftNum);

        // 减少扣减用户的拥有活动礼物
        GiftMessage message;

        // 发送活动礼物消息到MQ处理
        message = buildGiftMessage(sendUid, recvUid, roomDto == null ? null : roomDto.getUid(), giftId, giftNum, 0L,
                0L, num.get("useGiftPurseNum"), 0.0, 0.0, 0L, sendType, roomType, null);
        message.setExpressMessage(expressMessage);

        // 缓存消息的消费状态，便于队列消息做幂等处理
        redisManager.hset(RedisKey.mq_gift_prop_status.getKey(), message.getMessId(), gson.toJson(message));
        // 发送MQ消息
        activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_PROP_QUEUE, gson.toJson(message));

        return WebServiceMessage.success(buildGiftSendVoForOne(sendUid, recvUid, giftId, giftNum, num.get(
                "afterGiftPurseNum"), 0L, null, giftDto, roomDto));
    }


    /**
     * 送活动礼物给房间内所有麦序上的用户
     *
     * @param sendUid
     * @param recvUids
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @return
     * @throws WebServiceException
     */
    @Override
    @TargetDataSource
    public WebServiceMessage sendGiftPropToAll(long sendUid, Long[] recvUids, Long roomUid, int giftId, int giftNum,
                                               String appVersion) throws WebServiceException {
        logger.info("sendGiftPropToAll param==>>> sendUid:{},recvUid:{},roomUid:{},giftId:{},giftNum:{}", sendUid,
                recvUids, roomUid, giftId, giftNum);
        // 获取当前房间的信息，若不存在则返回
        RoomDTO room = roomManager.getUserRoom(roomUid);
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        GiftDTO gift = giftManager.getValidGiftById(giftId);
        if (gift == null) {
            throw new WebServiceException(WebServiceCode.GIFT_DOWN_OR_NOT_EXISTS);
        }

        if (!Boolean.TRUE.equals(gift.getIsCanGive())) {
            throw new WebServiceException(WebServiceCode.GIFT_CANNOT_GIVE);
        }

//        if (!GIFT_NUM_OPTION.contains(giftNum)) {
//            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
//        }

        // 更新缓存
        Map<String, Long> num = giftPurseManager.reduceGiftPropPurseCache(sendUid, gift, giftNum * recvUids.length);

        // 减少扣减用户的拥有活动礼物
        GiftMessage message;

        // 循环发送礼物消息到MQ处理
        for (int i = 0; i < recvUids.length; i++) {
            if (i == 0) {// 使用的金币和礼物都在一个里面处理，理论上没问题，但是如果这个报错会导致数据库和缓存不一致
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, 0L, 0L, num.get(
                        "useGiftPurseNum"), 0.0, 0.0, 0L, SendGiftType.roomToperson, room.getType(), null);
            } else {
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, 0L, 0L, 0L, 0.0, 0.0, 0L,
                        SendGiftType.roomToperson, room.getType(), null);
            }
            // 缓存消息的消费状态，便于队列消息做幂等处理
            redisManager.hset(RedisKey.mq_gift_prop_status.getKey(), message.getMessId(), gson.toJson(message));
            // 这里报错会有定时器处理
            activeMqManager.sendQueueMessage(MqDestinationKey.GIFT_PROP_QUEUE, gson.toJson(message));
        }

        return WebServiceMessage.success(buildGiftSendVoForAll(sendUid, recvUids, giftId, giftNum, num.get(
                "afterGiftPurseNum"), 0L, gift, room));
    }


    /****************************************************** 发送活动礼物 end
     *  ******************************************************/
}
