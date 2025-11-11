package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.StatPacketActivityDao;
import com.juxiao.xchat.dao.user.StatPacketRegisterDao;
import com.juxiao.xchat.dao.user.UserPacketDao;
import com.juxiao.xchat.dao.user.UserPacketRecordDao;
import com.juxiao.xchat.dao.user.dto.*;
import com.juxiao.xchat.manager.common.user.StatPacketActivityManager;
import com.juxiao.xchat.manager.common.user.UserConfigureManager;
import com.juxiao.xchat.manager.common.user.UserPacketManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.StatPacketActivityService;
import com.juxiao.xchat.service.api.user.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @class: StatPacketActivityServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@Service
public class StatPacketActivityServiceImpl implements StatPacketActivityService {
    @Autowired
    private StatPacketActivityDao packetActivityDao;
    @Autowired
    private StatPacketRegisterDao packetRegisterDao;
    @Autowired
    private UserPacketDao userPacketDao;
    @Autowired
    private UserPacketRecordDao userPacketRecordDao;
    @Autowired
    private StatPacketActivityManager activityManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserConfigureManager configureManager;
    @Autowired
    private UserPacketManager userPacketManager;


    @Override
    public StatPacketActivityVO getUserStatPacketDetail(Long uid) throws WebServiceException {
        if (uid == null || uid <= 0L) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserPacketDTO userPacket = userPacketManager.getUserPacket(uid);
        StatPacketActivityVO activityVo = new StatPacketActivityVO();
        StatPacketActivityDTO packetActivityDto = activityManager.getUserPacketActivity(uid);

        activityVo.setUid(uid);
        activityVo.setPacketNum(userPacket.getPacketNum());
        activityVo.setShareCount(packetActivityDto == null ? 0 : packetActivityDto.getShareCount());
        activityVo.setPacketCount(packetActivityDto == null ? 0 : packetActivityDto.getPacketCount());
        activityVo.setRegisterCout(packetActivityDto == null ? 0 : packetActivityDto.getRegisterCout());
        activityVo.setChargeBonus(packetActivityDto == null ? 0 : packetActivityDto.getChargeBonus());
        activityVo.setTodayRegisterCount(packetActivityDto == null ? 0 : packetActivityDto.getTodayRegisterCount());
        return activityVo;
    }

    @Override
    public StatPacketActivityParentVO listPacketActivityRank(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UserPacketDTO userPacketDto = userPacketManager.getUserPacket(uid);
        StatPacketActRankDTO merankVo = new StatPacketActRankDTO();
        merankVo.setUid(uid);
        merankVo.setPacketNum(userPacketDto.getPacketNum());
        merankVo.setSeqNo(userPacketDao.countStatPacketRankSeqNo(userPacketDto.getHistPacketNum()));// 改成历史金额的排行，下面列表是显示历史金额
        UsersDTO userDto = usersManager.getUser(uid);
        if (userDto != null) {
            merankVo.setUid(uid);
            merankVo.setNick(userDto.getNick());
            merankVo.setAvatar(userDto.getAvatar());
        }

        List<StatPacketActRankDTO> list = userPacketDao.listPacketActivityRank();
        StatPacketActivityParentVO parentVo = new StatPacketActivityParentVO();
        parentVo.setMe(merankVo);
        parentVo.setRankList(list);
        return parentVo;
    }

    @Override
    public StatPacketInviteDetailParentVO getInviteDetail(Long uid) {
        StatPacketActivityDTO packetActivityDto = activityManager.getUserPacketActivity(uid);
        UserPacketDTO userPacketDto = userPacketManager.getUserPacket(uid);
        StatPacketInviteDetailVO inviteDetail = new StatPacketInviteDetailVO();
        inviteDetail.setUid(uid);
        inviteDetail.setTotalRegisterCount(packetActivityDto.getRegisterCout());
        inviteDetail.setPacketNum(userPacketDto.getPacketNum());
        inviteDetail.setTodayRegisterCount(packetActivityDto.getTodayRegisterCount());
        inviteDetail.setDirectlyNum(packetActivityDto.getRegisterCout());

        //是否有上级分成权限
        UserConfigureDTO configureDto = configureManager.getUserConfigure(uid);
        if (configureDto == null || configureDto.getSuperiorBouns() == null) {
            inviteDetail.setSuperiorBouns(Byte.parseByte("0"));
            inviteDetail.setLowerNum(0);
        } else {
            Integer lowerNum = packetRegisterDao.countRegisterLowerNum(uid);
            inviteDetail.setLowerNum(lowerNum == null ? 0 : lowerNum);
            inviteDetail.setSuperiorBouns(configureDto.getSuperiorBouns());
            inviteDetail.setTotalRegisterCount(inviteDetail.getDirectlyNum() + inviteDetail.getLowerNum());
        }

        List<UserPacketInviteRecordDTO> list = userPacketRecordDao.listPacketInviteRegister(uid);
        return new StatPacketInviteDetailParentVO(inviteDetail, list);
    }

    @Override
    public List<StatPacketActivityDTO> listSomeInviteDetail(String uidList) throws WebServiceException {
        if (StringUtils.isBlank(uidList)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        List<String> oldList = Arrays.asList(uidList.split(","));
        if (oldList == null || oldList.size() == 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        List<Long> uids = new ArrayList<>(oldList.size());
        try {
            for (String uid : oldList) {
                uids.add(Long.valueOf(uid));
            }
        } catch (NumberFormatException e) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        List<StatPacketActivityDTO> list = packetActivityDao.listUsersPacketActivity(uids);
        return list;
    }

    @Override
    public List<UserPacketRegTeamDTO> listUserTeam(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        return packetRegisterDao.listUserTeam(uid);
    }

    @Override
    public StatPacketBounsDetailParentVO getBounsDetail(Long uid) throws WebServiceException {
        if (uid == null || uid == 0L) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        StatPacketActivityDTO packetActivityDto = activityManager.getUserPacketActivity(uid);

        StatPacketBounsDetailParentVO parentVo = new StatPacketBounsDetailParentVO();

        List<StatPacketBounsDTO> list = userPacketDao.listInviteBonus(uid);
        parentVo.setTodayBouns(packetActivityDto.getTodayChargeBonus() == null ? 0d : packetActivityDto.getTodayChargeBonus());
        parentVo.setTotalBouns(packetActivityDto.getChargeBonus() == null ? 0d : packetActivityDto.getChargeBonus());
        parentVo.setBounsList(list);
        return parentVo;
    }
}
