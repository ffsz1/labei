package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.PacketConstant;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.user.UserPacketDao;
import com.juxiao.xchat.dao.user.UserPacketRecordDao;
import com.juxiao.xchat.dao.user.domain.UserPacketDO;
import com.juxiao.xchat.dao.user.domain.UserPacketRecordDO;
import com.juxiao.xchat.dao.user.dto.UserPacketDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.StatPacketActivityManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import com.juxiao.xchat.service.api.user.UserPacketService;
import com.juxiao.xchat.service.api.user.vo.UserPacketRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @class: UserPacketServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
@Service
public class UserPacketServiceImpl implements UserPacketService {
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private UserPacketDao packetDao;
    @Autowired
    private UserPacketRecordDao packetRecordDao;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private StatPacketActivityManager packetActivityManager;

    @Override
    public UserPacketRecordVO getFirstPacket(Long uid) {
        UserPacketDTO packetDto = packetDao.getUserPacket(uid);
        if (packetDto != null) {
            return new UserPacketRecordVO();
        }

        Date date = new Date();
        UserPacketDO userPacket = new UserPacketDO();
        userPacket.setUid(uid);
        userPacket.setPacketNum(PacketConstant.fistrtPacketNum);
        userPacket.setHistPacketNum(PacketConstant.fistrtPacketNum);
        userPacket.setCreateTime(date);
        userPacket.setFirstGetTime(date);
        packetDao.save(userPacket);

        packetActivityManager.saveFirstStatPacketActivity(uid);


        //3.生成一条用户红包记录
        UserPacketRecordDO packetRecordDo = new UserPacketRecordDO();
        packetRecordDo.setPacketId(UUIDUtils.get());
        packetRecordDo.setPacketNum(PacketConstant.fistrtPacketNum);
        packetRecordDo.setUid(uid);
        packetRecordDo.setObjId(null);
        packetRecordDo.setType(UserPacketRecordTypeEnum.PACKET_FOR_NEW.getValue());
        packetRecordDo.setHasUnpack(false);
        packetRecordDo.setCreateTime(new Date());
        packetRecordDo.setSrcUid(null);
        packetRecordDao.save(packetRecordDo);

        UserPacketRecordVO packetRecordVo = new UserPacketRecordVO();
        packetRecordVo.setUid(userPacket.getUid());
        packetRecordVo.setType(UserPacketRecordTypeEnum.PACKET_FOR_NEW.getValue());
        packetRecordVo.setPacketNum(userPacket.getPacketNum());
        packetRecordVo.setNeedAlert(true);
        packetRecordVo.setPacketName("新人");

        this.sendPush(packetRecordVo, DefMsgType.Packet, DefMsgType.PacketFirst);
        return null;
    }

    @Async
    void sendPush(UserPacketRecordVO packetRecordVo, int first, int second) {
        List<String> toAccids = Lists.newArrayList(String.valueOf(packetRecordVo.getUid()));
        NeteaseBatchMsgBO batchMsgBo = new NeteaseBatchMsgBO();
        batchMsgBo.setFromAccid(systemConf.getSecretaryUid());
        batchMsgBo.setToAccids(toAccids);
        batchMsgBo.setType(100);
        batchMsgBo.setPushcontent("您获得了一个红包，点击查看~");
        batchMsgBo.setBody(new Body(first, second, packetRecordVo));
        batchMsgBo.setPayload(new Payload());
        neteaseMsgManager.sendBatchMsg(batchMsgBo);
    }
}
