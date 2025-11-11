package com.juxiao.xchat.manager.common.room.impl;

import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomVsDao;
import com.juxiao.xchat.dao.room.RoomVsTeamDao;
import com.juxiao.xchat.dao.room.RoomVsTeamUserDao;
import com.juxiao.xchat.dao.room.domain.RoomVsDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomVsResultDTO;
import com.juxiao.xchat.dao.room.enumeration.RoomVsScoreType;
import com.juxiao.xchat.dao.room.enumeration.RoomVsStatus;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomVsManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.Custom;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：房间PK
 *
 * @创建时间： 2020/10/28 17:09
 * @作者： carl
 */
@Slf4j
@Service
public class RoomVsManagerImpl implements RoomVsManager {

    @Autowired
    private RoomVsDao roomVsDao;

    @Autowired
    private RoomVsTeamDao roomVsTeamDao;

    @Autowired
    private RoomVsTeamUserDao roomVsTeamUserDao;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private ImRoomManager imRoomManager;

    @Autowired
    private RedisManager redisManager;

    /**
     * 停止房间PK（当前逻辑只支持）
     * @param endUser 停止用户（可为空，例如倒计时到期）
     * @param roomVsId PK对应的主键
     * @param targetStatus 停止的目标状态
     */
    @Override
    public void endRoomVs(Long endUser, Long roomVsId, RoomVsStatus targetStatus) throws WebServiceException {
        if (targetStatus.getValue() == RoomVsStatus.ONGOING.getValue())
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);

        // 倒计时结束才允许参数endUser为null
        if (roomVsId == null && targetStatus.getValue() != RoomVsStatus.END_BY_COUNTDOWN.getValue()) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 加锁
        String lock = redisManager.lock(RedisKey.roomvs_lock.getKey(roomVsId.toString()), 5 * 1000);
        if (StringUtils.isBlank(lock)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        try {
            RoomVsDO roomVsDO = roomVsDao.selectByPrimaryKey(roomVsId);
            if (roomVsDO == null) {
                throw new WebServiceException(WebServiceCode.ROOMVS_NOT_EXIST);
            } else if (roomVsDO.getStatus().byteValue() != RoomVsStatus.ONGOING.getValue()) {
                throw new WebServiceException(WebServiceCode.ROOMVS_IS_END);
            }

            Date now = new Date();
            roomVsDO.setUpdateTime(now);
            roomVsDO.setEndTime(now);
            roomVsDO.setEndUser(endUser);
            roomVsDO.setTerminateTime(now);

            // 设置DO对象的胜队属性值
            switch (targetStatus) {
                case END_BY_COUNTDOWN:  //倒计时结束，根据得分判断胜队
                    this.setWinTeamByScore(roomVsDO);
                    break;
                case END_BY_MANAGER:  //管理员提前结束，根据得分判断胜队
                    this.setWinTeamByScore(roomVsDO);
                    break;
                case END_BY_OFFLINE:   //PK用户掉线，自动判定为负方，另一方则为胜方
                    this.setWinTeamByLoser(roomVsDO, endUser);
                    break;
                case END_BY_LEAVEMIC:  //PK用户下麦，自动判定为负方，另一方则为胜方
                    this.setWinTeamByLoser(roomVsDO, endUser);
                    break;
                case END_BY_QUIT:      //PK用户退出房间，自动判定为负方，另一方则为胜方
                    this.setWinTeamByLoser(roomVsDO, endUser);
                    break;
            }

            roomVsDao.updateByPrimaryKey(roomVsDO);

            // 异步发送房间PK结束消息
            this.sendEndVsMsg(roomVsDO);

            // 异步设置各队的得分和队员的得分
            this.updateTeamAndUserScore(roomVsDO);
        }
        finally {
            redisManager.unlock(RedisKey.roomvs_lock.getKey(roomVsId.toString()), lock);
        }
    }

    /**
     * 发送房间PK结束消息
     */
    @Async
    public void sendEndVsMsg(RoomVsDO roomVsDO) {
        RoomVsResultDTO resultDTO = new RoomVsResultDTO();
        resultDTO.setId(roomVsDO.getId());



        RoomDTO roomDto = roomManager.getUserRoom(roomVsDO.getRoomUid());
        ImRoomMessage msginfo = new ImRoomMessage();
        Map<String, Object> data = new HashMap<>(8);
        data.put("roomId", roomDto.getRoomId());
        data.put("timestamps", System.currentTimeMillis());
        data.put("result", resultDTO);
        msginfo.setRoomId(String.valueOf(roomDto.getRoomId()));
        msginfo.setCustom(new Custom(DefMsgType.RoomCharm, DefMsgType.RoomVsStart, data));
        try {
            imRoomManager.pushRoomCustomMsg(msginfo);
        } catch (Exception e) {
            log.error("发送房间PK结束消息异常，消息:>{}，异常信息：", msginfo, e);
        }
    }

    /**
     * 根据得分判断胜队
     */
    private void setWinTeamByScore(RoomVsDO roomVsDO) {
        // 目前只有礼物总价值这一种
        if (roomVsDO.getScoreType().byteValue() == RoomVsScoreType.SCORE_BY_GITF_GOLD_TOTAL.getValue()) {
            // 获取得分进行比较

            // roomVsDO.setWinTeamId();
        }
    }

    /**
     * 根据用户所在的队伍，判定另一队为胜方
     * @param roomVsDO
     * @param loserUid 判定败方用户的uid
     */
    private void setWinTeamByLoser(RoomVsDO roomVsDO, Long loserUid) {
        // 找出所有队伍和PK用户，非loserUid的队伍的index排在前面的为胜队
        // roomVsDO.setWinTeamId();
    }

    @Async
    public void updateTeamAndUserScore(RoomVsDO roomVsDO) {
        // 从缓存中获取到得分更新队伍得分和个人得分

    }
}
