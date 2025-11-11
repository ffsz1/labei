package com.juxiao.xchat.manager.common.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.PacketConstant;
import com.juxiao.xchat.dao.user.StatPacketRegisterDao;
import com.juxiao.xchat.dao.user.dto.StatPacketRegisterDTO;
import com.juxiao.xchat.dao.user.dto.UserConfigureDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserConfigureManager;
import com.juxiao.xchat.manager.common.user.UserPacketManager;
import com.juxiao.xchat.manager.common.user.UserShareRecordManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserShareRecordManagerImpl implements UserShareRecordManager {
    private static DecimalFormat doubleFormat = new DecimalFormat("0.00");
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private StatPacketRegisterDao packetRegisterDao;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private UserConfigureManager configureManager;
    @Autowired
    private UserPacketManager userPacketManager;

    @Override
    public void saveUserBonusRecord(Long uid, Long superiorBonusUid, String chargeRecordProdId, Integer chargeAmount) {
        //查询是否是他人邀请注册
        StatPacketRegisterDTO packetRegisterDto;
        if (superiorBonusUid == null) {
            packetRegisterDto = packetRegisterDao.getShareRegister(uid);
        } else {
            packetRegisterDto = packetRegisterDao.getShareRegister(superiorBonusUid);
        }

        //没有被邀请记录，不做处理
        if (packetRegisterDto == null) {
            return;
        }

        Long gainBonusUid = packetRegisterDto.getUid();
        if (gainBonusUid == null) {
            return;
        }

        UserConfigureDTO userConfigure = configureManager.getUserConfigure(gainBonusUid);
        Integer bonusLevel;
        //是否有上级分成权限
        if (userConfigure == null || userConfigure.getSuperiorBouns() == null || userConfigure.getSuperiorBouns().intValue() <= 0) {
            if (superiorBonusUid != null) {
                return;
            }
            bonusLevel = 1;
        } else {
            bonusLevel = userPacketManager.getUserBonusLevel(gainBonusUid);
        }

        Double bonusRate;
        if (bonusLevel == 1) {
            bonusRate = superiorBonusUid == null ? PacketConstant.bonusRate1 : PacketConstant.superiorBonusRate1;
        } else if (bonusLevel == 2) {
            bonusRate = superiorBonusUid == null ? PacketConstant.bonusRate2 : PacketConstant.superiorBonusRate2;
        } else if (bonusLevel == 3) {
            bonusRate = superiorBonusUid == null ? PacketConstant.bonusRate3 : PacketConstant.superiorBonusRate3;
        } else {
            bonusRate = superiorBonusUid == null ? PacketConstant.bonusRate4 : PacketConstant.superiorBonusRate4;
        }

        double packetNumBonus = Double.valueOf(doubleFormat.format(chargeAmount / 100 * bonusRate));
        if (packetNumBonus <= 0.00) {
            return;
        }


        userPacketManager.addUserPacket(gainBonusUid, packetNumBonus, superiorBonusUid == null ? UserPacketRecordTypeEnum.PACKET_FOR_PROFITS:UserPacketRecordTypeEnum.PACKET_FOR_SUPERIOR, chargeRecordProdId, uid);

        Map<String, Object> data = new HashMap<>();
        data.put("packetNum", packetNumBonus);
        data.put("uid", gainBonusUid);
        data.put("createTime", new Date());
        data.put("type", superiorBonusUid == null ? UserPacketRecordTypeEnum.PACKET_FOR_PROFITS.getValue():UserPacketRecordTypeEnum.PACKET_FOR_SUPERIOR.getValue());
        data.put("needAlert", true);
        data.put("packetName", "分成");
        this.sendPush(gainBonusUid, data);
        // 添加上一级邀请人分成
        if (superiorBonusUid == null) {
            this.saveUserBonusRecord(uid, gainBonusUid, chargeRecordProdId, chargeAmount);
        }

    }

    @Async
    void sendPush(Long uid, Map<String, Object> data) {
        List<String> toAccids = Lists.newArrayList();
        toAccids.add(String.valueOf(uid));
        NeteaseBatchMsgBO batchMsgBo = new NeteaseBatchMsgBO();
        batchMsgBo.setFromAccid(systemConf.getSecretaryUid());
        batchMsgBo.setToAccids(toAccids);
        batchMsgBo.setType(100);
        batchMsgBo.setPushcontent("您获得了一个红包，点击查看");
        batchMsgBo.setBody(new Body(DefMsgType.Packet, DefMsgType.PacketBouns, data));
        batchMsgBo.setPayload(new Payload());
        neteaseMsgManager.sendBatchMsg(batchMsgBo);
    }
}
