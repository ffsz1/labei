package com.erban.main.service.gift;

import com.erban.main.config.SystemConfig;
import com.erban.main.message.BigGiftMessage;
import com.erban.main.message.GiftMessage;
import com.erban.main.model.Gift;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.home.CheckExcessService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.mq.ActiveMQService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserGiftPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.GiftSendVo;
import com.erban.main.vo.SendGiftVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GiftSendService extends BaseService {
    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private ActiveMQService activeMQService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserGiftPurseService userGiftPurseService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private CheckExcessService checkExcessService;

    /**
     * 发送表白礼物
     * @param sendUid 发送用户
     * @param recvUid 接收用户
     * @param roomUid 房间ID
     * @param giftId 礼物ID
     * @param giftNum 礼物数量
     * @param sendType 发送类型--4表白
     * @param expressMessage 表白留言
     * @return
     * @throws Exception
     */
    public BusiResult sendExpressGift(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum
            , byte sendType, String expressMessage)throws Exception{
        return  sendGiftToOne(sendUid, recvUid, roomUid, giftId, giftNum, sendType, null,null, expressMessage);
    }

    /**
     * 私聊时给对方送礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws Exception
     */
    public BusiResult sendGiftInPrivateChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum
            , byte sendType) throws Exception {
        return sendGiftToOne(sendUid, recvUid, roomUid, giftId, giftNum, sendType, null,null, "");
    }

    /**
     * 房间内给对方送礼物
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws Exception
     */
    public BusiResult sendGiftInRoomChat(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum
            , byte sendType,String appVersion) throws Exception {
        Room room = roomService.getRoomByUid(roomUid);
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        return sendGiftToOne(sendUid, recvUid, roomUid, giftId, giftNum, sendType, room.getType(),appVersion, "");
    }

    /**
     * 送礼物给某个用户
     *
     * @param sendUid
     * @param recvUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     * @param sendType
     * @return
     * @throws Exception
     */
    public BusiResult sendGiftToOne(long sendUid, long recvUid, Long roomUid, int giftId, int giftNum
            , byte sendType, Byte roomType,String appVersion, String expressMessage) throws Exception {
        logger.info("sendGiftToOne param==>>> sendUid:{},recvUid:{},roomUid:{},giftId:{},giftNum:{},sendType:{},roomType:{},appVersion:{}", "expressMessage: {}",
                sendUid, recvUid, roomUid, giftId, giftNum, sendType, roomType,appVersion, expressMessage);
        Gift gift = giftService.getValidGiftById(giftId);
        if (gift == null) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        // 添加礼物抽奖，先扣减用户的拥有礼物，如果还有剩余要送的礼物再按原来逻辑处理
        // 两个要在同一个方法判断，都通过了才能执行方法
        BusiResult busiResult;
        Long giftPrice = gift.getGoldPrice();
        try{
            busiResult = userGiftPurseService.reduceGiftPurseCache(sendUid, giftId, giftNum, giftPrice);// 减去拥有礼物的数量
        }catch (Exception e){
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        if(busiResult.getCode()!=200){
            return busiResult;
        }
        Long goldNum = Math.abs(giftPrice * giftNum); // 礼物金币总数
        Double diamondNum = Math.abs(goldNum * SystemConfig.appAkira); // 礼物钻石总数

//        // 扣除赠送用户的金币，扣除成功返回200
//        // 多节点部署时可能存在并发问题，需要加上分布式锁
//        int result = userPurseV2Service.reduceGoldFromCache(sendUid, goldNum);
//        if (result != 200) {
//            logger.info("=============>>>>{}", result);
//            switch (result) {
//                case 503:
//                    return new BusiResult(BusiStatus.SERVERBUSY);
//                case 500:
//                    return new BusiResult(BusiStatus.SERVERERROR);
//                case 403:
//                    return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
//            }
//        }
        Map<String, Long> num = (Map<String, Long>)busiResult.getData();
        // 发送礼物消息到MQ处理（加钻石、写DB记录）
        GiftMessage message = buildGiftMessage(sendUid, recvUid, roomUid, giftId, giftNum, goldNum, num.get("afterGoldNum"), num.get("useGiftPurseNum"), diamondNum, sendType, roomType, expressMessage);
        // 缓存消息的消费状态，便于队列消息做幂等处理
        jedisService.hwrite(RedisKey.mq_gift_status.getKey(), message.getMessId(), gson.toJson(message));
        try{// 这里报错会有定时器处理
            activeMQService.sendGiftMessage(message);
        }catch (Exception e){
            checkExcessService.sendSms("10000000");
            logger.error("发送mq报错"+e.getMessage());
        }

        return new BusiResult(BusiStatus.SUCCESS, buildGiftSendVoForOne(sendUid, recvUid, giftId, giftNum, appVersion, num.get("afterGiftPurseNum"), num.get("afterGoldNum")));
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
     * @throws Exception
     */
    public BusiResult sendGiftToAll(long sendUid, Long[] recvUids, Long roomUid, int giftId, int giftNum,String appVersion)
            throws Exception {
        logger.info("sendGiftToAll param==>>> sendUid:{},recvUid:{},roomUid:{},giftId:{},giftNum:{}", sendUid, recvUids
                , roomUid, giftId, giftNum);
        // 获取当前房间的信息，若不存在则返回
        Room room = roomService.getRoomByUid(roomUid);
        if (room == null) {
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }

        Gift gift = giftService.getValidGiftById(giftId);
        if (gift == null) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        // 添加礼物抽奖，先扣减用户的拥有礼物，如果还有剩余要送的礼物再按原来逻辑处理
        // 两个要在同一个方法判断，都通过了才能执行方法
        BusiResult busiResult;
        Long giftPrice = gift.getGoldPrice();
        try{
            busiResult = userGiftPurseService.reduceGiftPurseCache(sendUid, giftId, giftNum * recvUids.length, giftPrice);// 减去拥有礼物的数量
        }catch (Exception e){
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        if(busiResult.getCode()!=200){
            return busiResult;
        }
        Long everyGoldNum = Math.abs(giftPrice * giftNum);                     // 礼物金币总数，相对于每一次发送者
        Long totalGoldNum = Math.abs(everyGoldNum * recvUids.length);          // 礼物金币总数，
        Double diamondNum = Math.abs(everyGoldNum * SystemConfig.appAkira);// 礼物钻石总数，相对于每一个接收者

//        // 扣除赠送用户的金币，扣除成功返回用户的钱包信息200
//        // 多节点部署时可能存在并发问题，需要加上分布式锁
//        int result = userPurseV2Service.reduceGoldFromCache(sendUid, totalGoldNum);
//        if (result != 200) {
//            switch (result) {
//                case 503:
//                    return new BusiResult(BusiStatus.SERVERBUSY);
//                case 500:
//                    return new BusiResult(BusiStatus.SERVERERROR);
//                case 403:
//                    return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
//            }
//        }
        Map<String, Long> num = (Map<String, Long>)busiResult.getData();
        GiftMessage message;
        // 循环发送礼物消息到MQ处理（加钻石、写DB记录）
        for (int i = 0; i < recvUids.length; i++) {
            if(i==0){// 使用的金币和礼物都在一个里面处理，理论上没问题，但是如果这个报错会导致数据库和缓存不一致
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, everyGoldNum, num.get("afterGoldNum"), num.get("useGiftPurseNum"), diamondNum, Constant.SendGiftType.roomToperson, room.getType(), "");
            }else{
                message = buildGiftMessage(sendUid, recvUids[i], roomUid, giftId, giftNum, everyGoldNum, 0L, 0L, diamondNum, Constant.SendGiftType.roomToperson, room.getType(), "");
            }
            // 缓存消息的消费状态，便于队列消息做幂等处理
            jedisService.hwrite(RedisKey.mq_gift_status.getKey(), message.getMessId(), gson.toJson(message));
            try{// 这里报错会有定时器处理
                activeMQService.sendGiftMessage(message);
            }catch (Exception e){
                if(i==0){
                    checkExcessService.sendSms("10000000");
                    logger.error("发送mq报错"+e.getMessage());
                }
            }
        }

        return new BusiResult(BusiStatus.SUCCESS, buildGiftSendVoForAll(sendUid, recvUids, giftId, giftNum,appVersion, num.get("afterGiftPurseNum"), num.get("afterGoldNum")));
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
    private GiftMessage buildGiftMessage(Long sendUid, Long recvUid, Long roomUid, Integer giftId, Integer giftNum
            , Long goldNum, Long afterGoldNum, Long useGiftPurseNum, Double diamondNum, Byte sendType, Byte roomType, String expressMessage) {
        GiftMessage message = new GiftMessage();
        message.setSendUid(sendUid);
        message.setRecvUid(recvUid);
        message.setRoomUid(roomUid);
        message.setGoldNum(goldNum);
        message.setAfterGoldNum(afterGoldNum);
        message.setUseGiftPurseNum(useGiftPurseNum);
        message.setDiamondNum(diamondNum);
        message.setGiftId(giftId);
        message.setGiftNum(giftNum);
        message.setRoomType(roomType);
        message.setSendType(sendType);
        message.setMessId(UUIDUitl.get());
        message.setExpressMessage(expressMessage);
        message.setMessTime(new Date().getTime());
        return message;
    }


    private GiftSendVo buildGiftSendVoForOne(long sendUid, long recvUid, int giftId, int giftNum, String appVersion, Long userGiftPurseNum, Long afterGoldNum) {
        GiftSendVo giftSendVo = new GiftSendVo();
        Users sendUser = usersService.getUsersByUid(sendUid);
        if (sendUser != null) {
            giftSendVo.setNick(sendUser.getNick());
            giftSendVo.setAvatar(sendUser.getAvatar());
        } else {
            giftSendVo.setNick(Constant.DEFAULT_NICK);
            giftSendVo.setAvatar(Constant.DEFAULT_HEAD);
        }
        Users recvUser = usersService.getUsersByUid(recvUid);
        if (recvUser != null) {
            giftSendVo.setTargetAvatar(recvUser.getAvatar());
            giftSendVo.setTargetNick(recvUser.getNick());
        } else {
            giftSendVo.setTargetNick(Constant.DEFAULT_NICK);
            giftSendVo.setTargetAvatar(Constant.DEFAULT_HEAD);
        }
        giftSendVo.setUid(sendUid);
        giftSendVo.setTargetUid(recvUid);
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(sendUid);
        if (levelExerpenceVo != null) {
            giftSendVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
        } else {
            giftSendVo.setExperLevel(0);
        }
        giftSendVo.setUserGiftPurseNum(userGiftPurseNum==null?null:userGiftPurseNum);
        giftSendVo.setUseGiftPurseGold(afterGoldNum==null?null:afterGoldNum);
        return giftSendVo;
    }

    private GiftSendVo buildGiftSendVoForAll(long sendUid, Long[] recvUids, int giftId, int giftNum, String appVersion, Long userGiftPurseNum, Long afterGoldNum) {
        GiftSendVo giftSendVo = new GiftSendVo();
        Users users = usersService.getUsersByUid(sendUid);
        if (users != null) {
            giftSendVo.setNick(users.getNick());
            giftSendVo.setAvatar(users.getAvatar());
        } else {
            giftSendVo.setTargetNick(Constant.DEFAULT_NICK);
            giftSendVo.setTargetAvatar(Constant.DEFAULT_HEAD);
        }
        giftSendVo.setUid(users.getUid());
        giftSendVo.setTargetUids(Arrays.asList(recvUids));
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(sendUid);
        if (levelExerpenceVo != null) {
            giftSendVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
        } else {
            giftSendVo.setExperLevel(0);
        }
        giftSendVo.setUserGiftPurseNum(userGiftPurseNum==null?null:userGiftPurseNum);
        giftSendVo.setUseGiftPurseGold(afterGoldNum==null?null:afterGoldNum);
        return giftSendVo;
    }

    public void sendMsgAllRoom(Long uid, Long targetUid, Long roomUid, int giftId, int giftNum, Long[] targetSize){
        Gift gift = giftService.getValidGiftById(giftId);
        Long giftPrice = gift.getGoldPrice();
        Long totalGoldNum = Math.abs(giftPrice * giftNum * (targetSize==null?1:targetSize.length));          // 礼物金币总数
        if(totalGoldNum<30000){
            return;
        }
        // 发送礼物消息到MQ处理
        BigGiftMessage message = new BigGiftMessage();
        message.setMessId(UUIDUitl.get());
        message.setMessTime(new Date().getTime());
        message.setUid(uid);
        message.setTargetUid(targetUid);
        message.setRoomUid(roomUid);
        message.setGiftId(giftId);
        message.setGiftNum(giftNum);
        message.setTargetSize(targetSize);
        // 缓存消息的消费状态，便于队列消息做幂等处理
        jedisService.hwrite(RedisKey.mq_big_gift_status.getKey(), message.getMessId(), gson.toJson(message));
        try{
            activeMQService.sendBigGiftMessage(message);
        }catch (Exception e){
            checkExcessService.sendSms("10000000");
            logger.error("发送mq报错"+e.getMessage());
        }
    }

    public void handleBigGiftMessage(BigGiftMessage message){
        long start = System.currentTimeMillis();
        Gift gift = giftService.getValidGiftById(message.getGiftId());
        Long giftPrice = gift.getGoldPrice();
        Long totalGoldNum = Math.abs(giftPrice * message.getGiftNum() * (message.getTargetSize()==null?1:message.getTargetSize().length));          // 礼物金币总数
        // 删除该标识，表示消息已经消费过
        jedisService.hdel(RedisKey.mq_big_gift_status.getKey(), message.getMessId());
        if(totalGoldNum<30000){
            return;
        }
        // 获取所有在线房间
        List<Map<String, Object>> roomList = jdbcTemplate.queryForList("SELECT uid ,room_id as roomId from room where valid = 1 and online_num >= 2 and uid <> 500000");
        if(roomList.size()==0){
            return;
        }
        Users users = usersService.getUsersByUid(message.getUid());
        Users targerUsers=null;
        Users roomUser = usersService.getUsersByUid(message.getRoomUid());
        if(message.getTargetUid()!=null){
            targerUsers = usersService.getUsersByUid(message.getTargetUid());
        }
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(users.getUid());
        for (Map<String, Object> room:roomList) {
            if(message.getRoomUid().equals(Long.valueOf(room.get("uid").toString()))){// 自己房间和在线人数少于2的不需要发送
                continue;
            }
            sendBigGift(message, users, targerUsers, roomUser, levelExerpenceVo, Long.valueOf(room.get("roomId").toString()));
        }
        logger.error("大额礼物处理总耗时:" + (System.currentTimeMillis() - start));
    }

    @Async
    public void sendBigGift(BigGiftMessage message, Users users, Users targerUsers, Users roomUser, LevelExerpenceVo levelExerpenceVo, Long roomId){
        try {
            SendGiftVo sendGiftVo = new SendGiftVo();
            sendGiftVo.setUid(message.getUid());
            sendGiftVo.setGiftId(message.getGiftId());
            sendGiftVo.setAvatar(users.getAvatar());
            sendGiftVo.setNick(users.getNick());
            sendGiftVo.setGiftNum(message.getGiftNum());
            if(targerUsers==null){
                sendGiftVo.setTargetUid(0L);
                sendGiftVo.setTargetAvatar("");
                sendGiftVo.setTargetNick("全麦");
            }else {
                sendGiftVo.setTargetUid(targerUsers.getUid());
                sendGiftVo.setTargetAvatar(targerUsers.getAvatar());
                sendGiftVo.setTargetNick(targerUsers.getNick());
            }
            sendGiftVo.setRoomId(message.getRoomUid().intValue());// 跳转房间的用户id
            sendGiftVo.setUserNo(roomUser.getErbanNo());// 跳转房间的用户号
            if(levelExerpenceVo==null){
                sendGiftVo.setExperLevel(0);
            }else{
                sendGiftVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
            }
            sendChatRoomMsgService.sendSendChatRoomMsg(roomId, message.getUid().toString() , Constant.DefMsgType.GiftServiceSend, Constant.DefMsgType.GiftServiceSend, sendGiftVo);
        } catch (Exception e) {
            logger.error("大额礼物处理失败:"+e.getMessage());
        }
    }

}
