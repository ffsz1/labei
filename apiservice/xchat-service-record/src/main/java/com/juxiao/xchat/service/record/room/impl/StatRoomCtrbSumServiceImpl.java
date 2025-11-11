package com.juxiao.xchat.service.record.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.StatRoomCtrbSumDao;
import com.juxiao.xchat.dao.room.dto.RoomCtrbTopDTO;
import com.juxiao.xchat.dao.room.dto.StatRoomUserCtrbSumDTO;
import com.juxiao.xchat.dao.room.query.RoomContributionQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.event.conf.RankDatetype;
import com.juxiao.xchat.manager.common.event.conf.RoomRankType;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.service.record.room.StatRoomCtrbSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 房间贡献榜服务层
 *
 * @class: StatRoomCtrbSumServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Service
public class StatRoomCtrbSumServiceImpl implements StatRoomCtrbSumService {
    @Autowired
    private StatRoomCtrbSumDao roomCtrbDao;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;


    /**
     * @see com.juxiao.xchat.service.record.room.StatRoomCtrbSumService#listRoomContribution(Long, Integer, Integer)
     */
    @Override
    public List<StatRoomUserCtrbSumDTO> listRoomContribution(Long uid, Integer dataType, Integer type) throws WebServiceException {
        if (uid == null || dataType == null || type == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

//        String field = uid + "_" + dataType + "_" + type;
//        String ctrblistStr = redisManager.hget(RedisKey.room_ctrb_list.getKey(), field);
//        if (StringUtils.isNotBlank(ctrblistStr)) {
//            return gson.fromJson(ctrblistStr, new TypeToken<List<StatRoomUserCtrbSumDTO>>() {
//            }.getType());
//        }

        Date startDate;
        Date endDate;
        if (dataType == RankDatetype.day) {// 日榜
            Date date = new Date();
            startDate = DateTimeUtils.setTime(date, 0, 0, 0);
            endDate = DateTimeUtils.setTime(date, 23, 59, 59);
        } else if (dataType == RankDatetype.week) {// 周榜
            startDate = DateUtils.getCurrentMonday();
            endDate = DateUtils.getCurrentSundayTime(23, 59, 59);
        } else if (dataType == RankDatetype.total) {// 总榜
            startDate = null;
            endDate = null;
        } else {
            throw new WebServiceException("不支持的查询类型");
        }

        RoomContributionQuery query = new RoomContributionQuery(uid, startDate, endDate);
        List<StatRoomUserCtrbSumDTO> list;
        if (type == RoomRankType.wealth) {// 财富榜
            list = roomCtrbDao.listRoomWealthCtrb(query);
        } else if (type == RoomRankType.charm) {// 魅力榜
            list = roomCtrbDao.listRoomCharmCtrb(query);
        } else if (type == RoomRankType.xqWealth) {// 相亲财富榜
            list = roomCtrbDao.listXqRoomWealthCtrb(query);
        } else if (type == RoomRankType.xqCharm) {// 相亲魅力榜
            list = roomCtrbDao.listXqRoomCharmCtrb(query);
        } else {
            throw new WebServiceException("不支持的查询类型");
        }

        int experLevel;
        int charmLevel;
        for (StatRoomUserCtrbSumDTO ctrbSumDto : list) {
            experLevel = levelManager.getUserExperienceLevelSeq(ctrbSumDto.getCtrbUid());
            ctrbSumDto.setExperLevel(experLevel);
            charmLevel = levelManager.getUserCharmLevelSeq(ctrbSumDto.getCtrbUid());
            ctrbSumDto.setCharmLevel(charmLevel);
        }

//        redisManager.hset(RedisKey.room_ctrb_list.getKey(), field, gson.toJson(list));
        return list;
    }

    @Override
    public List<RoomCtrbTopDTO> listRoomCtrbTop(Long roomUid) throws WebServiceException {
        if (roomUid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        Date date = new Date();
        Date startDate = DateTimeUtils.setTime(date, 0, 0, 0);
        Date endDate = DateTimeUtils.setTime(date, 23, 59, 59);
        RoomContributionQuery query = new RoomContributionQuery(roomUid, startDate, endDate);

        List<RoomCtrbTopDTO> list = new ArrayList<>();
        StatRoomUserCtrbSumDTO wealthCtrbSumDto = roomCtrbDao.getRoomWealthCtrbTop(query);
        if (wealthCtrbSumDto != null) {
            //1：财富；2：魅力分栏
            list.add(new RoomCtrbTopDTO((byte) 1, wealthCtrbSumDto.getAvatar()));
        }

        StatRoomUserCtrbSumDTO charmCtrbSumDto = roomCtrbDao.getRoomCharmCtrbTop(query);
        if (charmCtrbSumDto != null) {
            //1：财富；2：魅力分栏
            list.add(new RoomCtrbTopDTO((byte) 2, charmCtrbSumDto.getAvatar()));
        }

        return list;
    }
}
