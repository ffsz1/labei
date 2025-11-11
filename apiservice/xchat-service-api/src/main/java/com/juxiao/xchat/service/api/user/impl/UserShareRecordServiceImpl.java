package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.StatPacketRegisterDao;
import com.juxiao.xchat.dao.user.UserShareRecordDao;
import com.juxiao.xchat.dao.user.domain.UserShareRecordDO;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;
import com.juxiao.xchat.dao.user.dto.StatPacketRegisterDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import com.juxiao.xchat.dao.user.query.TodayShareRecordQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.StatPacketActivityManager;
import com.juxiao.xchat.manager.common.user.UserPacketManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.service.api.user.UserShareRecordService;
import com.juxiao.xchat.service.api.user.vo.UserPacketRecordVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


/**
 * @class: UserShareRecordServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Service
public class UserShareRecordServiceImpl implements UserShareRecordService {
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.00");
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private UserShareRecordDao shareRecordDao;
    @Autowired
    private UserPacketManager userPacketManager;
    @Autowired
    private StatPacketActivityManager packetActivityManager;
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UsersManager usersManager;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private StatPacketRegisterDao packetRegisterDao;



    @Override
    public UserPacketRecordVO saveShareRecord(Long uid, String shareType, Integer sharePageId, Long targetUid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        //1.获取红包金额（每天第一次分享才有红包）
        int todayShareCount = shareRecordDao.countTodayShareRecord(new TodayShareRecordQuery(uid));
        if (todayShareCount > 0) {
            return new UserPacketRecordVO();
        }

        Date date = new Date();
        String shareId = UUIDUtils.get();
        //2.生成一条分享记录
        UserShareRecordDO shareRecord = new UserShareRecordDO();
        shareRecord.setShareId(shareId);
        shareRecord.setUid(uid);
        shareRecord.setShareId(UUIDUtils.get());
        shareRecord.setSharePageId(sharePageId);
        shareRecord.setShareType(new Byte(shareType));
        shareRecord.setTargetUid(targetUid);
        shareRecord.setCreateTime(date);
        shareRecordDao.save(shareRecord);


        double sharePacketNum = Double.valueOf(DOUBLE_FORMAT.format(this.getSharePacketNum(uid)));
        userPacketManager.addUserPacket(uid, sharePacketNum, UserPacketRecordTypeEnum.PACKET_FOR_SHARE, shareId, null);
        UserPacketRecordVO recordVo = new UserPacketRecordVO();
        recordVo.setUid(uid);
        recordVo.setPacketNum(sharePacketNum);
        recordVo.setCreateTime(date);
        recordVo.setType(UserPacketRecordTypeEnum.PACKET_FOR_INVITE.getValue());
        recordVo.setNeedAlert(true);
        recordVo.setPacketName("分享");

        this.sendPush(recordVo, DefMsgType.Packet, DefMsgType.PacketShare);
//        //4.是否分享的是抽奖页面
//        if(UserDrawService.sharePageId==sharePageId){
//            userDrawService.genUserDrawChanceByShare(uid,shareId);
//        }
        return recordVo;
    }

    @Override
    public UserPacketRecordVO saveUserShareRegisterRecord(Long uid, Long targetUid) {
        //1.获取用户参与活动的红包统计数据
        StatPacketActivityDTO activityDto = packetActivityManager.getUserPacketActivity(uid);
        double packetNum;
        //2.生成随机红包金额
        if (activityDto.getRegisterCout() <= 7) {
            packetNum = RandomUtils.randomRegionDouble(1.00, 3.00);
        } else if (activityDto.getRegisterCout() <= 20){
            packetNum = RandomUtils.randomRegionDouble(0.50, 1.50);
        } else {
            packetNum = 0.01;
        }

        if (packetNum <= 0.00) {
            return new UserPacketRecordVO();
        }

        UserPacketRecordVO recordVo = new UserPacketRecordVO();
        //3.更新相关统计表
        userPacketManager.addUserPacket(uid, packetNum, UserPacketRecordTypeEnum.PACKET_FOR_INVITE, String.valueOf(targetUid), targetUid);
        recordVo.setPacketNum(packetNum);
        recordVo.setCreateTime(new Date());
        recordVo.setUid(uid);
        recordVo.setType(UserPacketRecordTypeEnum.PACKET_FOR_INVITE.getValue());
        recordVo.setNeedAlert(true);
        recordVo.setPacketName("邀请");

        //4.发送系统消息
        this.sendPush(recordVo, DefMsgType.Packet, DefMsgType.PacketShare);
        return recordVo;
    }

    @Override
    public UserPacketRecordVO saveUserShareRegisterRecord(Long uid, String shareCode) throws WebServiceException {
        if (StringUtils.isBlank(shareCode)) {
            return null;
        }

        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (!StringUtils.isNumeric(shareCode)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        Long shareErbanNo = Long.valueOf(shareCode);
        String lockVal = redisManager.lock(RedisKey.user_reg_share_lock.getKey(String.valueOf(uid)), 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            StatPacketRegisterDTO registerDto = packetRegisterDao.getShareRegister(uid);
            if (registerDto != null) {
                return null;
            }

            AccountDTO accountDto = accountDao.getAccount(uid);
            if (accountDto == null) {
                throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
            }


            if (shareErbanNo.equals(accountDto.getErbanNo())) {
                throw new WebServiceException(WebServiceCode.USER_SHARE_CODE_BIND_SELF);
            }

            UsersDTO usersDto = usersManager.getUserByErbanNo(shareErbanNo);
            if (usersDto == null) {
                throw new WebServiceException(WebServiceCode.USER_SHARE_CODE_NOT_EXITS);
            }

            return this.saveUserShareRegisterRecord(usersDto.getUid(), uid);
        } finally {
            redisManager.unlock(RedisKey.user_reg_share_lock.getKey(), lockVal);
        }

    }


    /**
     * 分享红包,前七次分享0.5到1.0，后面分享0.1到0.4
     *
     * @param uid
     * @return
     */
    private double getSharePacketNum(Long uid) {
        int shareCount = shareRecordDao.countUserShareRecord(uid);
        if (shareCount <= 7) {
            return RandomUtils.randomRegionDouble(0.50, 1.0);
        }
        return RandomUtils.randomRegionDouble(0.10, 0.40);
    }

    @Async
    void sendPush(UserPacketRecordVO recordVo, int first, int second) {
        // 3.发送系统消息给用户
        List<String> toAccids = Lists.newArrayList(String.valueOf(recordVo.getUid()));
        NeteaseBatchMsgBO batchMsgBo = new NeteaseBatchMsgBO();
        batchMsgBo.setFromAccid(systemConf.getSecretaryUid());
        batchMsgBo.setToAccids(toAccids);
        batchMsgBo.setType(100);
        batchMsgBo.setPushcontent("您获得了一个红包，点击查看");
        batchMsgBo.setBody(new Body(first, second, recordVo));
        batchMsgBo.setPayload(new Payload());
        neteaseMsgManager.sendBatchMsg(batchMsgBo);
    }
}
