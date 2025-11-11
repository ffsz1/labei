package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.HomeHotManualRecommDao;
import com.juxiao.xchat.dao.room.domain.HomeHotManualRecommDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomResultDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.UserPurseHotRoomManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.api.user.UserPurseHotRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @class: UserPurseHotRoomServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/7/12
 */
@Service
public class UserPurseHotRoomServiceImpl implements UserPurseHotRoomService {
    @Autowired
    private HomeHotManualRecommDao homeHotManualRecommDao;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private UserPurseHotRoomManager userPurseHotRoomManager;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;


    @Override
    public List<UserPurseHotRoomResultDTO> listHotRoomRecord(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        List<String> recordIds = userPurseHotRoomManager.listUserPurseHotRoomRecordId(uid);
        if (recordIds == null || recordIds.size() == 0) {
            return Lists.newArrayList();
        }

        List<UserPurseHotRoomResultDTO> list = Lists.newArrayList();
        for (String recordId : recordIds) {
            list.add(userPurseHotRoomManager.getHotRoomResult(recordId));
        }
        return list;
    }

    @Override
    public void purse(Long uid, Long erbanNo, String date, String hour) throws WebServiceException {
        if (uid == null || erbanNo == null || date == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO erbanUser = usersManager.getUserByErbanNo(erbanNo);
        if (erbanUser == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomDTO room = roomManager.getUserRoom(erbanUser.getUid());
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        Date startTime;
        try {
            startTime = DateFormatUtils.YYYY_MM_DD_HH_MM.str2Date(date + " " + hour);
        } catch (ParseException e) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        Date now = new Date();
        if (now.getTime() > startTime.getTime()) {
            throw new WebServiceException(WebServiceCode.DATE_EXISTS);
        }

        Date endTime = DateTimeUtils.addHours(startTime, 1);
        int userHotRecommCount = homeHotManualRecommDao.countUserHotRecomm(erbanUser.getUid(), startTime, endTime);
        if (userHotRecommCount > 0) {
            throw new WebServiceException(WebServiceCode.HOT_EXISTS);
        }

        int goldCost = 1000;
        userPurseManager.updateReduceGold(uid, goldCost, true,"购买推荐位", null);

        userPurseHotRoomManager.save(uid, erbanNo, goldCost, startTime, endTime, now);

        HomeHotManualRecommDO recommDo = new HomeHotManualRecommDO();
        recommDo.setUid(erbanUser.getUid());
        recommDo.setSeqNo(1);
        recommDo.setStatus(Byte.parseByte("1"));
        recommDo.setStartValidTime(startTime);
        recommDo.setEndValidTime(endTime);
        recommDo.setCreateTime(now);
        recommDo.setType(0);
        homeHotManualRecommDao.save(recommDo);
        asyncNetEaseTrigger.sendMsg(String.valueOf(uid),"恭喜您成功购买" + DateFormatUtils.YYYY_MM_DD_HH_MM.date2Str(startTime) + "-" + DateFormatUtils.YYYY_MM_DD_HH_MM.date2Str(endTime) + "的热门推荐位，您购买的房间【" + erbanNo + "】会在对应时段自动上推荐，请知悉。");
    }
}
