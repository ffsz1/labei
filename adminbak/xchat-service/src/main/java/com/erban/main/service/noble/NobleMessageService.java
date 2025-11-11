package com.erban.main.service.noble;

import com.alibaba.fastjson.JSONObject;
import com.erban.main.config.SystemConfig;
import com.erban.main.message.NobleMessage;
import com.erban.main.model.*;
import com.erban.main.param.neteasepush.Body;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.google.common.collect.Lists;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NobleMessageService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(NobleMessageService.class);

    @Autowired
    private NobleRightService nobleRightService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private NobleRecordService nobleRecordService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private NobleHelperService nobleHelperService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private NobleUserInRoomService nobleUserInRoomService;

    public void handleNobleMessage(NobleMessage message) throws Exception {
        String messId = message.getMessId();
        // 消息去重，判断消息是否被消费过
        if (nobleRecordService.isExistMessId(messId)) {
            jedisService.hdel(RedisKey.mq_noble_status.getKey(), messId);
            logger.warn("NobleMessage has consumed, message: " + message.toString());
            return;
        }

        switch (message.getOptType()) {
            case Constant.NobleOptType.open:
                openNobleBuss(message);
                break;
            case Constant.NobleOptType.renew:
                renewNobleBuss(message);
                break;
        }
        // 删除该标识，表示消息已经消费过
        jedisService.hdel(RedisKey.mq_noble_status.getKey(), messId);
    }

    /**
     * 处理贵族开通的业务
     *
     * @param message
     * @throws Exception
     */
    private void openNobleBuss(NobleMessage message) throws Exception {
        Long uid = message.getUid();
        Long roomUid = message.getRoomUid();
        Integer nobleId = message.getNobleId();
        String nobleName = message.getNobleName();

        NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);

        if(!message.getPayType().equals((byte)3)) {
            // 减少普通金币
            int result = userPurseUpdateService.reduceChargeGoldDbAndCache(uid, nobleRight.getOpenGold());
            logger.info("openNobleBuss reduceChargeGoldDbAndCache, result:{}", result);
        }

        // 初始化用户的贵族信息
        NobleUsers nobleUsers = null;
        if (nobleUsersService.isExistNoble(uid)) {
            // 已存在用户贵族信息时，重置为默认值
            nobleUsers = nobleUsersService.resetNobleUsers(uid, nobleRight);
        } else {
            nobleUsers = nobleUsersService.addNobleUsers(uid, nobleRight);
        }
        // 升级时需要去掉原来的缓存
        nobleUsersService.updateNobleUserCache(uid);
        logger.info("openNobleBuss init nobleUsers, {}", nobleUsers);

        // 更新用户的贵族标识
        Users users = usersService.getUsersByUid(uid);
        users.setNobleId(message.getNobleId());
        users.setNobleName(message.getNobleName());
        users.setUpdateTime(new Date());
        usersService.updateUser(users);
        logger.info("openNobleBuss updateUser, uid:{}, nobleId:{}, nobleName:{}", uid, nobleId, nobleName);

        // 返还贵族金币
        int result = userPurseUpdateService.addNobleGoldDbAndCache(uid, nobleRight.getOpenReturn());
        logger.info("openNobleBuss addNobleGoldNumFromDB, result:{}", result);

        // 房主获得分成
        if (roomUid != null && !uid.equals(roomUid)) {
            Long goldNum = (nobleRight.getOpenGold() - nobleRight.getOpenReturn()) / 2;
            try {
                result = userPurseUpdateService.addChargeGoldDbAndCache(roomUid, goldNum);
                logger.info("openNobleBuss add room ower gold, result:{}, roomUid:{}", result, roomUid);
                billRecordService.insertBillRecord(roomUid, roomUid, null, Constant.BillType.roomNoble
                        , null, goldNum, null);
                nobleHelperService.sendNobleRoomIncomeMess(roomUid, users, nobleRight, goldNum);
                logger.info("openNobleBuss sendNobleRoomIncomeMess, roomUid:{}", roomUid);
                nobleUserInRoomService.saveNobleUserInRoomCache(uid, roomUid);
                logger.info("openNobleBuss saveNobleUserInRoomCache, uid:{},roomUid:{}", uid, roomUid);
            } catch (Exception e) {
                logger.error("addChargeGoldDbAndCache error, roomUid:" + roomUid + ", goldNum:" + goldNum, e);
            }
        }

        // 插入开通或续费贵族的记录
        NobleRecord nobleRecord = buildNobleRecord(message);
        result = nobleRecordService.addNobleRecord(nobleRecord);
        logger.info("openNobleBuss addNobleRecord, uid:{}, nobleId:{} result:{}", uid, nobleId, result);
        // 发送小秘书开通提醒
        nobleHelperService.sendNobleOpenMess(uid, nobleName);
        logger.info("openNobleBuss sendNobleOpenMess, uid:{}, nobleName:{}", uid, nobleName);

        if(roomUid != null) {
            // 房间内发送开通通知
            result = sendRoomMessage(users, nobleRight, roomUid, Constant.NobleOptType.open);
            logger.info("openNobleBuss sendRoomMessage, uid:{}, result:{}", uid, result);
        }
        // 发送全站广播通知
        if (nobleRight.getOpenNotice().equals((byte)2)) {
            result = sendBroadCastMessage(users, nobleRight, Constant.NobleOptType.open, roomUid);
            logger.info("openNobleBuss sendBroadCastMessage, uid:{}, result:{}", uid, result);
        }
    }

    /**
     * 处理贵族续费业务
     *
     * @param message
     */
    private void renewNobleBuss(NobleMessage message) throws Exception {
        Long uid = message.getUid();
        Long roomUid = message.getRoomUid();
        Integer nobleId = message.getNobleId();

        NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);

        if(!message.getPayType().equals((byte)3)) {
            // 减少普通金币
            int result = userPurseUpdateService.reduceChargeGoldDbAndCache(uid, nobleRight.getOpenGold());
            logger.info("openNobleBuss reduceChargeGoldDbAndCache, result:{}", result);
        }

        // 初始化用户的贵族信息
        NobleUsers nobleUsers = nobleUsersService.getValidNobleUserFromDb(uid);
        nobleUsers.setRenewTime(new Date());
        nobleUsers.setRecomCount((byte)(nobleUsers.getRecomCount() + nobleRight.getRecomRoom()));
        int result = nobleUsersService.updateRenewNobleUsers(uid, nobleRight.getRecomRoom()
                , DateTimeUtil.getNextDay(nobleUsers.getExpire(), 30));
        logger.info("renewNobleBuss init nobleUsers, result:{}", result);

        // 返还贵族金币
        result = userPurseUpdateService.addNobleGoldDbAndCache(uid, nobleRight.getRenewReturn());
        logger.info("renewNobleBuss addNobleGoldNumFromDB, result:{}", result);

        // 插入续费贵族的记录
        NobleRecord nobleRecord = buildNobleRecord(message);
        result = nobleRecordService.addNobleRecord(nobleRecord);
        // 需要去掉原来的缓存
        nobleUsersService.updateNobleUserCache(uid);
        logger.info("renewNobleBuss addNobleRecord, uid:{}, nobleId:{} result:{}", uid, nobleId, result);

        // 发送小秘书续费提醒
        nobleHelperService.sendNobleRenewMess(uid, nobleUsers.getNobleName());
        logger.info("openNobleBuss sendNobleRenewMess, uid:{}, nobleName:{}", uid, nobleUsers.getNobleName());

        Users users = usersService.getUsersByUid(uid);
        if(roomUid != null && users != null) {
            // 房间内发送开通通知
            result = sendRoomMessage(users, nobleRight, roomUid, Constant.NobleOptType.renew);
            logger.info("renewNobleBuss sendRoomMessage, uid:{}, result:{}", uid, result);
        }
        // 发送全站广播通知
        if (nobleRight.getOpenNotice().equals((byte)2)) {
            result = sendBroadCastMessage(users, nobleRight, Constant.NobleOptType.renew, roomUid);
            logger.info("renewNobleBuss sendBroadCastMessage, uid:{}, result:{}", uid, result);
        }
    }

    private NobleRecord buildNobleRecord(NobleMessage message) {
        NobleRecord record = new NobleRecord();
        record.setRecordId(UUIDUitl.get());
        record.setUid(message.getUid());
        record.setRoomUid(message.getRoomUid());
        record.setNobleId(message.getNobleId());
        record.setNobleName(message.getNobleName());
        record.setOptType(message.getOptType());
        record.setPayType(message.getPayType());
        record.setGoldNum(message.getPayGold());
        record.setMoney(message.getMoney());
        record.setMessId(message.getMessId());
        record.setCreateTime(new Date());
        return record;
    }

    /**
     * 发送房间消息
     *
     * @param users
     * @param nobleRight
     * @param roomUid
     * @param optType
     * @return
     * @throws Exception
     */
    public int sendRoomMessage(Users users, NobleRight nobleRight, Long roomUid, Byte optType) throws Exception {
        Room room = roomService.getRoomByUid(roomUid);
        Attach attach = buildAttach(users, nobleRight, optType, roomUid);
        return erBanNetEaseService.sendChatRoomMsg(room.getRoomId(), UUIDUitl.get(), roomUid.toString()
                , Constant.DefineProtocol.CUSTOM_MESS_DEFINE, gson.toJson(attach)).getCode();
    }

    /**
     * 发送广播消息
     *
     * @param users
     * @param nobleRight
     * @param optType    开通或续费，1：开通，2：续费
     * @return
     */
    public int sendBroadCastMessage(Users users, NobleRight nobleRight, Byte optType, Long roomUid) {
        Attach attach = buildAttach(users, nobleRight, optType, roomUid);
        return sendSysMsgService.broadCastMsg(SystemConfig.secretaryUid, gson.toJson(attach));
    }


    private Attach buildAttach(Users users, NobleRight nobleRight, Byte optType, Long roomUid) {
        Attach attach = new Attach();
        attach.setFirst(Constant.DefineProtocol.CUSTOM_MESS_HEAD_NOBLE);
        if (optType.equals(Constant.NobleOptType.open)) {
            attach.setSecond(Constant.DefineProtocol.CUSTOM_MESS_SUB_OPENNOBLE);
        } else {
            attach.setSecond(Constant.DefineProtocol.CUSTOM_MESS_SUB_RENEWNOBLE);
        }
        JSONObject jsonObject = new JSONObject();
        if (roomUid != null) {
            Room room = roomService.getRoomByUid(roomUid);
            Users users1 = usersService.getUsersByUid(roomUid);
            if (room != null && users1 != null) {
                jsonObject.put("roomErbanNo", users1.getErbanNo()); // 房主拉贝号
                jsonObject.put("roomTitle", room.getTitle());       // 房间标题
            }
        }
        jsonObject.put("type", optType);
        jsonObject.put("uid", users.getUid());
        jsonObject.put("nick", users.getNick());
        jsonObject.put("avatar", users.getAvatar());
        jsonObject.put("nobleInfo", nobleRight);
        attach.setData(jsonObject);
        return attach;
    }


    public static void main(String[] args) {
        Byte dd = 3;
        if (dd.equals((byte)3)) {
            System.out.println((byte)3);
        }
    }
}
