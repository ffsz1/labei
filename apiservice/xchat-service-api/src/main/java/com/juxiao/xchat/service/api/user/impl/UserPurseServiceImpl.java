package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.charge.ChargeRecordDao;
import com.juxiao.xchat.dao.room.StatRoomCtrbSumDao;
import com.juxiao.xchat.dao.room.domain.StatRoomFlowDo;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.vo.UserPurseVO;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.api.user.UserPurseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @class: UserPurseServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Service
@Slf4j
public class UserPurseServiceImpl implements UserPurseService {
    @Autowired
    private ChargeRecordDao chargeRecordDao;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private StatRoomCtrbSumDao statRoomSumDao;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Override
    public UserPurseVO getUserPurse(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        UserPurseVO purseVo = new UserPurseVO();
        BeanUtils.copyProperties(purseDto, purseVo);
        return purseVo;
    }

    @Override
    public boolean queryFirst(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // Integer hour = DateTimeUtils.getHour(new Date());
        // 晚上20-次日早上9点需要隐藏
        //if (hour < 9 || hour > 20) {
        // 返回true给客户端表示要隐藏跳链
        // return true;
        //}

        // 9-20时间段充值少于3次再
        // int chargeSuccessCount = chargeRecordDao.countUserChargeSuccess(uid);
        // 判断充值次数少于3次，则不显示,true为隐藏，false为显示
        // return chargeSuccessCount <= 3;
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void houseOwnerShare(String date, Long uid) throws WebServiceException {
        if (date == null || uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        StatRoomFlowDo shareRecord = statRoomSumDao.getRoomOwnerShareRecord(uid, date);
        if (shareRecord == null) {
            StatRoomFlowDo statRoomFlowDo = new StatRoomFlowDo();
            StatRoomFlowDo flowDo = statRoomSumDao.getRoomOwnerFlow(uid, date, date);
            if (flowDo == null) {
                throw new WebServiceException("流水查询失败");
            }
            BeanUtils.copyProperties(flowDo, statRoomFlowDo);

            Double ownerDiamondNum = Math.abs(flowDo.getTotalGoldNum() * systemConf.getRoomOwnerAkira()); // 牌照房房主可额外获得总流水的分成
            statRoomFlowDo.setShareDiamondNum(ownerDiamondNum);
            statRoomFlowDo.setCreateTime(new Date());
            statRoomFlowDo.setStatus(1);

            userPurseManager.updateAddDiamond(uid, ownerDiamondNum, false);
            statRoomSumDao.saveShare(statRoomFlowDo);
            asyncNetEaseTrigger.sendMsg(String.valueOf(uid), "房间流水分成 " + ownerDiamondNum + " 已到账,请查收！");
        } else {
            throw new WebServiceException("佣金已发放");
        }
    }

}
