package com.erban.main.service.gift;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.GiftMapper;
import com.erban.main.mybatismapper.UserGiftPurseMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.SysConfService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserGiftPurseService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.GiftSendVo;
import com.erban.main.vo.GiftVo;
import com.erban.main.vo.SendGiftVo;
import com.erban.main.vo.UserPurseVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.FileUploadRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.core.util.StringUtils;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class GiftService extends BaseService{
    @Autowired
    private UserGiftPurseMapper userGiftPurseMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private GiftMapper giftMapper;
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private GiftSendRecordService giftSendRecordService;
    @Autowired
    private UserGiftPurseService userGiftPurseService;

    private static String defaultNick="拉贝新人";
    private static String defaultAvatar="http://res.91fb.com/default_head.png";
    private static Byte giftValid=new Byte("1");


    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public BusiResult sendGift(Long uid, Integer giftId, Long targetUid, int type) throws Exception {
        // 获取礼物
        Gift gift = getGiftById(giftId);
        if (!checkGiftValid(gift)) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        Room room = roomService.getRoomByUid(targetUid);
        Long goldPrice = gift.getGoldPrice();
        if(goldPrice<=0){
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        Double diamondNum = goldPrice * SystemConfig.appAkira;
        if(diamondNum<=0){
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        if (uid.equals(targetUid)) {
            String indentifier = jedisService.lockWithTimeout(uid.longValue() + "gold", 5000, 1000);
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            if (goldNum < goldPrice) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            boolean sendResult = userPurseService.updateGoldBySendGift(uid, goldPrice);
            if (!sendResult) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            userPurseService.updateDiamondByOrderIncome(targetUid, diamondNum);
//            billRecordService.insertBillRecordBySendGift(uid, uid, null, Constant.BillType.giftPay, null, -goldPrice, null, giftNum, giftId);
//            billRecordService.insertBillRecordBySendGift(uid, uid, null, Constant.BillType.giftIncome, diamondNum, null, null, giftNum, giftId);
//            giftSendRecordService.insertGiftInfo(uid, targetUid, type, giftId, giftNum);
            Users users = usersService.getUsersByUid(uid);
            SendGiftVo sendGiftVo = new SendGiftVo();
            sendGiftVo.setUid(uid);
            sendGiftVo.setGiftId(gift.getGiftId());
            sendGiftVo.setTargetUid(targetUid);
            sendGiftVo.setAvatar(users.getAvatar());
            sendGiftVo.setNick(users.getNick());
            sendGiftVo.setTargetAvatar(users.getAvatar());
            sendGiftVo.setTargetUid(users.getUid());
            sendGiftVo.setTargetNick(users.getNick());
//            sendGiftVo.setGiftNum(giftNum);
            sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(), uid.toString(), Constant.DefMsgType.Gift,
                    Constant.DefMsgType.GiftSend, sendGiftVo);
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
            BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
            busiResult.setData(sendGiftVo);
            return busiResult;
        } else {
            String indentifier = jedisService.lockWithTimeout(uid.longValue() + "gold", 5000, 1000);
            logger.info("正在送礼物，用户id：" + uid + "礼物id:" + giftId+"&targetUid="+targetUid);
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            if (goldNum < goldPrice) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurse2 = userPurseService.updateGoldBySendGiftCache(userPurse, goldPrice);
            if (userPurse2 == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
//            userPurseService.sendSysMsgByModifyGold(userPurse2);
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
//            logger.info("发送消息队列：" +uid+giftId+ targetUid+ diamondNum+ goldPrice+ Constant.ActiveMq.send_gift+ type+ giftNum);
//            sendMessage(uid, giftId, targetUid, diamondNum, goldPrice, Constant.ActiveMq.send_gift, type, giftNum);
            if (type == Constant.SendGiftType.person) {
                return new BusiResult(BusiStatus.SUCCESS);
            }
            Users users = usersService.getUsersByUid(uid);
            Users targetUsers = usersService.getUsersByUid(targetUid);
            SendGiftVo sendGiftVo = new SendGiftVo();
            sendGiftVo.setUid(uid);
            sendGiftVo.setGiftId(gift.getGiftId());
            sendGiftVo.setTargetUid(targetUid);
            if(users!=null){
                sendGiftVo.setAvatar(users.getAvatar());
                sendGiftVo.setNick(users.getNick());
            }else{

            }
            if(targetUsers!=null){
                sendGiftVo.setTargetNick(targetUsers.getNick());
                sendGiftVo.setTargetAvatar(targetUsers.getAvatar());
            }
            sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(), uid.toString(), Constant.DefMsgType.Gift,
                    Constant.DefMsgType.GiftSend, sendGiftVo);
            BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
            busiResult.setData(sendGiftVo);
            return busiResult;
        }
    }


    @Deprecated
    public BusiResult sendGiftV2(Long uid, Integer giftId, Long targetUid,Long roomUid, Integer giftNum,int type) throws Exception {
        long start = System.currentTimeMillis();
        // 获取礼物
        Gift gift = getGiftById(giftId);
        if (!checkGiftValid(gift)) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        if(giftNum<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Long giftPrice=gift.getGoldPrice();
        Long totalGoldPrice = giftPrice*giftNum;
        Double diamondNum = totalGoldPrice * SystemConfig.appAkira;

        if(diamondNum<=0||totalGoldPrice<=0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        long end1 = System.currentTimeMillis();

        String lockVal = null;
        UserPurse userPurseAfter = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10*1000);
            if (BlankUtil.isBlank(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }

            //#################################加锁start#################################
            logger.info("正在送礼物，用户id：" + uid + "礼物id:" + giftId+"&targetUid="+targetUid+"&roomUid="+roomUid);
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            // 判断余额是否足够
            if (goldNum < totalGoldPrice) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, totalGoldPrice);
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid + ", goldNum: " + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        long end2 = System.currentTimeMillis();

        if(Constant.SendGiftType.person==type){//一对一刷礼物
            sendMessage(uid, giftId, targetUid,null, null,diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
        }else if(Constant.SendGiftType.room==type||Constant.SendGiftType.roomToperson==type){
            Room room = roomService.getRoomByUid(roomUid);
            sendChatRoomMsgBySendGift(uid,targetUid,room.getRoomId(),giftId,giftNum);

            if (uid.equals(targetUid)) {//自己给自己刷礼物
                userPurseService.updateDiamondByOrderIncome(targetUid, diamondNum);
                GiftSendRecord giftSendRecord =giftSendRecordService.insertGiftInfo(uid, targetUid,roomUid,room.getType(), type, giftId, giftNum,totalGoldPrice,diamondNum);
                String objId= giftSendRecord.getSendRecordId().toString();
                billRecordService.insertBillRecordBySendGift(uid, targetUid, roomUid,objId,Constant.BillType.giftPay, null, -totalGoldPrice, null, giftNum, giftId);
                billRecordService.insertBillRecordBySendGift(targetUid,uid , roomUid,objId,Constant.BillType.giftIncome, diamondNum, null, null, giftNum, giftId);
                sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
            } else {
                logger.info("发送消息队列：" +uid+giftId+ targetUid+ diamondNum+ totalGoldPrice+ Constant.ActiveMq.send_gift+ type+ giftNum);
                sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
            }
        }
        long end3 = System.currentTimeMillis();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        UserPurseVo userPurseVo=userPurseService.getUserPurseVo(userPurseAfter);
        busiResult.setData(userPurseVo);
        long end4 = System.currentTimeMillis();

        logger.info("end1-start={}, end2-end1={}, end3-end2={}, end4-end3={}", end1-start, end2-end1, end3-end2, end4-end3);
        return busiResult;

    }
    @Deprecated
    public BusiResult sendGiftToWholeMicro(Long uid, Integer giftId,  Long [] targetUids, Long roomUid, int giftNum) throws Exception{
        if(targetUids==null||targetUids.length<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if(giftNum<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        List<Long> targetUidsList=Lists.newArrayList();
        for(int i=0;i<targetUids.length;i++){
            if(uid.equals(targetUids[i])){
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            if(targetUids[i]==null||targetUids[i]==0){
                continue;
            }
            targetUidsList.add(targetUids[i]);
        }
        int targetUidsLength=targetUidsList.size();
        // 获取礼物
        Gift gift = getGiftById(giftId);
        if (!checkGiftValid(gift)) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        Long giftPrice=gift.getGoldPrice();
        Long totalGoldPrice = giftPrice*giftNum*targetUidsLength;
        Long perGoldPrice=giftPrice*giftNum;
        Double diamondNum = perGoldPrice * SystemConfig.appAkira;

        if(diamondNum<=0||totalGoldPrice<=0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //#################################加锁start#################################
        String lockVal = null;
        UserPurse userPurseAfter = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10*1000);
            if (BlankUtil.isBlank(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            if (goldNum < totalGoldPrice) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, totalGoldPrice);
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid + ", goldNum: " + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        Room room = roomService.getRoomByUid(roomUid);
        sendChatRoomMsgBySendWholeMicroGift(uid,targetUidsList,room.getRoomId(),giftId,giftNum);
        for(int i=0;i<targetUidsList.size();i++){
            Long targetUid=targetUidsList.get(i);
            logger.info("全麦送礼物发送消息队列：" +uid+giftId+ targetUid+ diamondNum+ perGoldPrice+ Constant.ActiveMq.send_gift+ giftNum);
            sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, Constant.SendGiftType.roomToperson, giftNum);
        }
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        UserPurseVo userPurseVo=userPurseService.getUserPurseVo(userPurseAfter);
        busiResult.setData(userPurseVo);
        return busiResult;
    }



    @Transactional(rollbackFor = Exception.class)
    public BusiResult<GiftSendVo> sendGiftV3(Long uid, Integer giftId, Long targetUid, Long roomUid, Integer giftNum, int type) throws Exception {
        // 获取礼物
        Gift gift = getGiftById(giftId);
        if (!checkGiftValid(gift)) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        if(giftNum<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        Long giftPrice=gift.getGoldPrice();
        Long totalGoldPrice = giftPrice*giftNum;
        Double diamondNum = totalGoldPrice * SystemConfig.appAkira;

        if(diamondNum<=0||totalGoldPrice<=0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //#################################加锁start#################################
        String indentifier = jedisService.lockWithTimeout(uid.longValue() + "gold", 5000, 1000);
        logger.info("正在送礼物，用户id：" + uid + "礼物id:" + giftId+"&targetUid="+targetUid+"&roomUid="+roomUid);
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        Long goldNum = userPurse.getGoldNum();
        if (goldNum < totalGoldPrice) {
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
        }
        // TODO 若其它地方同时更新钱包，存在并发问题
        UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, totalGoldPrice);
        if (userPurseAfter == null) {
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
        }
        jedisService.releaseLock(uid.longValue() + "gold", indentifier);
        //#################################结束锁end#################################

        if(Constant.SendGiftType.person==type){//一对一刷礼物
            sendMessage(uid, giftId, targetUid,null, null,diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
        }else if(Constant.SendGiftType.room==type||Constant.SendGiftType.roomToperson==type){

            Room room = roomService.getRoomByUid(roomUid);
            if (uid.equals(targetUid)) {//自己给自己刷礼物

                userPurseService.updateDiamondByOrderIncome(targetUid, diamondNum);
                GiftSendRecord giftSendRecord =giftSendRecordService.insertGiftInfo(uid, targetUid,roomUid,room.getType(), type, giftId, giftNum,totalGoldPrice,diamondNum);
                String objId= giftSendRecord.getSendRecordId().toString();
                billRecordService.insertBillRecordBySendGift(uid, targetUid, roomUid,objId,Constant.BillType.giftPay, null, -totalGoldPrice, null, giftNum, giftId);
                billRecordService.insertBillRecordBySendGift(targetUid,uid , roomUid,objId,Constant.BillType.giftIncome, diamondNum, null, null, giftNum, giftId);
                sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
            } else {
                logger.info("发送消息队列：" +uid+giftId+ targetUid+ diamondNum+ totalGoldPrice+ Constant.ActiveMq.send_gift+ type+ giftNum);
                sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, type, giftNum);
            }
        }
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        GiftSendVo giftSendVo=new GiftSendVo();
        String[] uidsArray={uid.toString(),targetUid.toString()};
        Map<Long,Users> usersMap=usersService.getUsersMapBatch(uidsArray);
        Users user=usersMap.get(uid);
        Users targetUser=usersMap.get(targetUid);
        giftSendVo.setUid(uid);
        giftSendVo.setTargetUid(targetUid);

        if(user!=null){
            giftSendVo.setNick(user.getNick());
            giftSendVo.setAvatar(user.getAvatar());
        }else{
            giftSendVo.setNick(defaultNick);
            giftSendVo.setAvatar(defaultAvatar);
        }
        if(targetUser!=null){
            giftSendVo.setTargetAvatar(targetUser.getAvatar());
            giftSendVo.setTargetNick(targetUser.getNick());
        }else{
            giftSendVo.setTargetNick(defaultNick);
            giftSendVo.setTargetAvatar(defaultAvatar);
        }
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        busiResult.setData(giftSendVo);
        return busiResult;

    }
    public BusiResult<GiftSendVo> sendGiftToWholeMicroV3(Long uid, Integer giftId,  Long [] targetUids, Long roomUid, int giftNum) throws Exception{
        if(targetUids==null||targetUids.length<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if(giftNum<1){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        List<Long> targetUidsList=Lists.newArrayList();
        for(int i=0;i<targetUids.length;i++){
            if(uid.equals(targetUids[i])){
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            if(targetUids[i]==null||targetUids[i]==0){
                continue;
            }
            targetUidsList.add(targetUids[i]);
        }
        int targetUidsLength=targetUidsList.size();
        // 获取礼物
        Gift gift = getGiftById(giftId);
        if (!checkGiftValid(gift)) {
            return new BusiResult(BusiStatus.GIFTDOWNORNOTEXISTS);
        }
        Long giftPrice=gift.getGoldPrice();
        Long totalGoldPrice = giftPrice*giftNum*targetUidsLength;
        Long perGoldPrice=giftPrice*giftNum;
        Double diamondNum = perGoldPrice * SystemConfig.appAkira;

        if(diamondNum<=0||totalGoldPrice<=0){
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        //#################################加锁start#################################
        String indentifier = jedisService.lockWithTimeout(uid.longValue() + "gold", 5000, 1000);
        UserPurse userPurse = userPurseService.getPurseByUid(uid);
        Long goldNum = userPurse.getGoldNum();
        if (goldNum < totalGoldPrice) {
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
        }
        UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, totalGoldPrice);
        if (userPurseAfter == null) {
            jedisService.releaseLock(uid.longValue() + "gold", indentifier);
            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
        }
        jedisService.releaseLock(uid.longValue() + "gold", indentifier);
        //#################################结束锁end#################################
        Room room = roomService.getRoomByUid(roomUid);
        for(int i=0;i<targetUidsList.size();i++){
            Long targetUid=targetUidsList.get(i);
            logger.info("全麦送礼物发送消息队列：" +uid+giftId+ targetUid+ diamondNum+ perGoldPrice+ Constant.ActiveMq.send_gift+ giftNum);
            sendMessage(uid, giftId, targetUid,roomUid,room.getType(), diamondNum, giftPrice, Constant.ActiveMq.send_gift, Constant.SendGiftType.roomToperson, giftNum);
        }
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        GiftSendVo giftSendVo=new GiftSendVo();
        Users users=usersService.getUsersByUid(uid);
        giftSendVo.setUid(users.getUid());
        if(users!=null){
            giftSendVo.setNick(users.getNick());
            giftSendVo.setAvatar(users.getAvatar());
        }else{
            giftSendVo.setNick(defaultNick);
            giftSendVo.setAvatar(defaultAvatar);
        }
        giftSendVo.setTargetUids(targetUidsList);
        giftSendVo.setGiftId(giftId);
        giftSendVo.setGiftNum(giftNum);
        busiResult.setData(giftSendVo);
        return busiResult;
    }



    private void sendChatRoomMsgBySendGift(Long uid,Long targetUid,Long roomId,Integer giftId,int giftNum) throws Exception{
        String[] uidArray={uid.toString(),targetUid.toString()};
        Map<Long,Users> usersMap=usersService.getUsersMapBatch(uidArray);
        Users users = usersMap.get(uid);
        Users targetUsers = usersMap.get(targetUid);

        SendGiftVo sendGiftVo = new SendGiftVo();
        sendGiftVo.setUid(uid);
        sendGiftVo.setGiftId(giftId);
        sendGiftVo.setTargetUid(targetUid);

        if(users!=null){
            sendGiftVo.setAvatar(users.getAvatar());
            sendGiftVo.setNick(users.getNick());
        }else{
            sendGiftVo.setAvatar(defaultAvatar);
            sendGiftVo.setNick(defaultNick);
        }

        if(targetUsers!=null){
            sendGiftVo.setTargetNick(targetUsers.getNick());
            sendGiftVo.setTargetAvatar(targetUsers.getAvatar());
        }else{
            sendGiftVo.setTargetNick(defaultNick);
            sendGiftVo.setTargetAvatar(defaultAvatar);
        }

        sendGiftVo.setGiftNum(giftNum);
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId, uid.toString(), Constant.DefMsgType.Gift,
                Constant.DefMsgType.GiftSend, sendGiftVo);

    }

    private void sendChatRoomMsgBySendWholeMicroGift(Long uid,List<Long> targetUids,Long roomId,Integer giftId,int giftNum) throws Exception{
        Users users = usersService.getUsersByUid(uid);
        SendGiftVo sendGiftVo = new SendGiftVo();
        sendGiftVo.setUid(uid);
        sendGiftVo.setGiftId(giftId);
        sendGiftVo.setTargetUids(targetUids);
        if(users!=null){
            sendGiftVo.setAvatar(users.getAvatar());
            sendGiftVo.setNick(users.getNick());
        }else{
            sendGiftVo.setAvatar(defaultAvatar);
            sendGiftVo.setNick(defaultNick);
        }
        sendGiftVo.setGiftNum(giftNum);
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId, uid.toString(), Constant.DefMsgType.GiftWhole,
                Constant.DefMsgType.GiftSendWholeMicro, sendGiftVo);

    }



    public BusiResult<List<GiftVo>> getGiftListVo() throws Exception {
        List<GiftVo> giftVoList = queryGiftListVo();
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(giftVoList);
        return busiResult;
    }

    public BusiResult<Map<String, Object>> getGiftListVoV2(Integer uid, Integer type) throws Exception {
        List<GiftVo> giftVoList = queryGiftListVo();
        List<GiftVo> list = Lists.newArrayList();
        if (type != null && type == 1) {
            // 过滤表白礼物-- 四个???
            int maxCount = 4;
            for (GiftVo vo : giftVoList) {
                if (list.size() >= maxCount) {
                    break;
                }
                if (vo.getExpressGift()) {
                    list.add(vo);
                }
            }
            giftVoList = list;
        }
        Map<String, Object> giftData = Maps.newHashMap();
        SysConf sysConf = sysConfService.getSysConfById(Constant.SysConfId.cur_gift_version);//获取礼物版本
        giftData.put("giftVersion", sysConf.getConfigValue());
        if(uid!=null){
            List<UserGiftPurse> userGiftPurseList = userGiftPurseService.getUserList(uid);
            if(userGiftPurseList!=null){
                for(UserGiftPurse userGiftPurse:userGiftPurseList){
                    for(GiftVo giftVo:giftVoList){
                        if(userGiftPurse.getGiftId()==giftVo.getGiftId()){
                            giftVo.setUserGiftPurseNum(userGiftPurse.getCountNum());
                        }
                    }
                }
            }
        }
        giftData.put("gift", giftVoList);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(giftData);
        return busiResult;
    }

    public void refreshGiftVersion(String giftVersion){
        List<Gift> giftList = Lists.newArrayList();
        GiftExample giftExample = new GiftExample();
        giftExample.createCriteria().andGiftStatusEqualTo(new Byte("1"));
        giftExample.setOrderByClause(" seq_no asc");
        giftList = giftMapper.selectByExample(giftExample);
        jedisService.hdeleteKey(RedisKey.gift.getKey());
        batchSaveGiftCache(giftList);
        sysConfService.refreshSysConf(Constant.SysConfId.cur_gift_version, giftVersion);
    }

    private List<GiftVo> queryGiftListVo() {
        List<Gift> giftList = queryGiftList();
        List<GiftVo> giftVoList = Lists.newArrayList();
        for (Gift gift : giftList) {
            GiftVo giftVo = new GiftVo();
            giftVo.setGiftId(gift.getGiftId());
            giftVo.setGiftName(gift.getGiftName());
            giftVo.setGoldPrice(gift.getGoldPrice());
            giftVo.setGiftUrl(gift.getPicUrl());
            giftVo.setNobleId(gift.getNobleId());
            giftVo.setNobleName(gift.getNobleName());
            giftVo.setIsNobleGift(gift.getIsNobleGift());
            giftVo.setSeqNo(gift.getSeqNo());
            giftVo.setHasGifPic(gift.getHasGifPic());
            giftVo.setGifUrl(gift.getGifUrl());
            giftVo.setGiftType(gift.getGiftType());

            giftVo.setHasVggPic(gift.getHasVggPic());
            giftVo.setVggUrl(gift.getVggUrl());
            giftVo.setHasLatest(gift.getIsLatest());
            giftVo.setHasTimeLimit(gift.getIsTimeLimit());
            giftVo.setHasEffect(gift.getHasEffect());

            giftVo.setExpressGift(gift.getIsExpressGift());
            giftVoList.add(giftVo);
        }
        Collections.sort(giftVoList);
        return giftVoList;
    }

    private List<Gift> queryGiftList() {
        Map<String, String> giftKeyMap = jedisService.hgetAllBykey(RedisKey.gift.getKey());
        List<Gift> giftList = Lists.newArrayList();
        if (giftKeyMap == null || giftKeyMap.size() == 0) {
            GiftExample giftExample = new GiftExample();
            giftExample.createCriteria().andGiftStatusEqualTo(new Byte("1"));
            giftExample.setOrderByClause(" seq_no asc");
            giftList = giftMapper.selectByExample(giftExample);
            batchSaveGiftCache(giftList);
        } else {
            Iterator<Map.Entry<String, String>> it = giftKeyMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    Gift gift = gson.fromJson(value, Gift.class);
                    giftList.add(gift);
                }
            }
        }
        return giftList;
    }

    private void batchSaveGiftCache(List<Gift> giftList) {
        if (CollectionUtils.isEmpty(giftList)) {
            return;
        }
        for (Gift gift : giftList) {
            saveGiftCache(gift);
        }
    }

    private void saveGiftCache(Gift gift) {
        jedisService.hwrite(RedisKey.gift.getKey(), gift.getGiftId().toString(), gson.toJson(gift));
    }

    public Gift getGiftById(Integer giftId) {
        String giftStr = jedisService.hget(RedisKey.gift_all.getKey(), giftId.toString());
        List<Gift> giftList = null;
        Gift gift=null;
        if (StringUtils.isEmpty(giftStr)) {
            GiftExample giftExample = new GiftExample();
            giftExample.createCriteria().andGiftIdEqualTo(giftId);
            giftList = giftMapper.selectByExample(giftExample);

            if (CollectionUtils.isEmpty(giftList)){
                return null;
            } else {
                gift=giftList.get(0);
//                saveGiftCache(giftList.get(0));
                return gift;
            }
        }
        gift = gson.fromJson(giftStr, Gift.class);
        return gift;
    }
    private boolean checkGiftValid(Gift gift){
        if(gift==null){
            return false;
        }
        if(gift.getGiftStatus().equals(giftValid)){
            return true;
        }else{
            return false;
        }

    }

    private void sendMessage(final Long uid, final Integer giftId, final Long targetUid,final Long roomUid,final Byte roomType, final Double diamondNum,
                             final Long goldNum, final String title, final int type, final Integer giftNum) {
        jmsTemplate.send("spring-queue", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                mapMessage.setObject("uid", uid);
                mapMessage.setObject("targetUid", targetUid);
                mapMessage.setObject("roomUid", roomUid);
                mapMessage.setObject("roomType",roomType);
                mapMessage.setObject("giftId", giftId);
                mapMessage.setObject("diamondNum", diamondNum);
                mapMessage.setObject("goldNum", goldNum);
                mapMessage.setObject("title", title);
                mapMessage.setObject("type", type);
                mapMessage.setObject("giftNum", giftNum);
                return mapMessage;
            }
        });
    }


    public BusiResult updateGift(FileUploadRet fileUploadRet, Integer giftId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        GiftExample example = new GiftExample();
        example.createCriteria().andGiftIdEqualTo(giftId);
        List<Gift> gifts = giftMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(gifts)) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        Gift gift = gifts.get(0);
        gift.setPicUrl(fileUploadRet.getUrl());
        giftMapper.updateByPrimaryKey(gift);
        jedisService.hdeleteKey(RedisKey.gift.getKey());
        queryGiftList();
        return busiResult;
    }

    /**
     * 获取礼物的信息，礼物必须处在有效状态, CACHE -> DB -> CACHE
     *
     * @param giftId
     * @return
     */
    public Gift getValidGiftById(Integer giftId) {
        String giftStr = jedisService.hget(RedisKey.gift.getKey(), giftId.toString());
        if (BlankUtil.isBlank(giftStr)) {
            Gift gift = giftMapper.selectByPrimaryKey(giftId);
            if (gift == null || gift.getGiftStatus() == null || gift.getGiftStatus()!=1) {
                return null;
            }
            jedisService.hwrite(RedisKey.gift.getKey(), giftId.toString(), gson.toJson(gift));
            return gift;
        }
        return gson.fromJson(giftStr, Gift.class);
    }

    public void updateUserGiftPurse(Long uid, Integer giftId) {
        UserGiftPurse userGiftPurse = userGiftPurseService.getOneByJedisId(uid.toString(), giftId.toString());
        if (userGiftPurse == null) {
            userGiftPurse = new UserGiftPurse();
            userGiftPurse.setUid(uid);
            userGiftPurse.setGiftId(giftId);
            userGiftPurse.setCountNum(1);
            userGiftPurse.setCreateTime(new Date());
            userGiftPurseMapper.insertSelective(userGiftPurse);
        } else {
            userGiftPurse.setCountNum(userGiftPurse.getCountNum() + 1);
            userGiftPurseMapper.updateByPrimaryKeySelective(userGiftPurse);
            jedisService.hset(RedisKey.user_gift_purse.getKey(), uid.toString() + giftId.toString(), gson.toJson(userGiftPurse));
        }
    }

}
