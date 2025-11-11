package com.juxiao.xchat.service.api.room.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomGameConfigDAO;
import com.juxiao.xchat.dao.room.RoomGameRecordDao;
import com.juxiao.xchat.dao.room.domain.RoomGameRecordDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomGameConfigDTO;
import com.juxiao.xchat.dao.room.dto.RoomGameDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.service.api.room.RoomGameService;
import com.juxiao.xchat.service.api.room.vo.RoomGameRecordVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoomGameServiceImpl implements RoomGameService {
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomGameRecordDao roomGameRecordDao;

    @Autowired
    private RoomGameConfigDAO roomGameConfigDAO;

    @Autowired
    private Gson gson;


    /**
     * 获取游戏状态
     *
     * @param uid
     * @param roomId
     * @return
     * @throws WebServiceException
     */
    @Override
    public RoomGameRecordVO getState(Long uid, Long roomId) throws WebServiceException {
        if (uid == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO roomDTO = roomManager.getRoom(roomId);
        if (roomDTO == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        RoomGameRecordVO roomGameRecordVo = new RoomGameRecordVO();
        String json = redisManager.hget(RedisKey.room_game_config.getKey(), String.valueOf(roomDTO.getUid()));
        if (StringUtils.isNotBlank(json)) {
            RoomGameConfigDTO roomGameConfigDTO = gson.fromJson(json, RoomGameConfigDTO.class);
            if (roomGameConfigDTO != null) {
                if (!DateUtils.betweenStrToDate(roomGameConfigDTO.getStart(), roomGameConfigDTO.getEnd())) {
                    redisManager.hdel(RedisKey.room_game_config.getKey(), String.valueOf(roomDTO.getUid()));
                    roomGameRecordVo.setStatus(-1);
                    roomGameConfigDAO.updateByStatus(roomGameConfigDTO.getId(), 2);
                } else {
                    RoomGameRecordDO roomGameRecord = getRoomGameRecord(uid, roomId);
                    if (roomGameRecord == null) {
                        roomGameRecordVo.setStatus(0);
                    } else {
                        roomGameRecordVo.setStatus(roomGameRecord.getType().intValue());
                        roomGameRecordVo.setResult(roomGameRecord.getResult());
                    }
                }
            } else {
                roomGameRecordVo.setStatus(-1);
            }
        } else {
            roomGameRecordVo.setStatus(-1);
        }
        return roomGameRecordVo;
    }

    private RoomGameRecordDO getRoomGameRecord(Long uid, Long roomId) {
        String result = redisManager.hget(RedisKey.room_game_record.getKey(), uid + "_" + roomId);
        if (StringUtils.isBlank(result)) {
            List<RoomGameRecordDO> list = roomGameRecordDao.listRecord(uid, roomId);
            if (list == null || list.isEmpty()) {
                return null;
            } else {
                RoomGameRecordDO recordDO = list.get(0);
                redisManager.hset(RedisKey.room_game_record.getKey(), uid + "_" + roomId, gson.toJson(recordDO));
                return recordDO;
            }
        }
        return gson.fromJson(result, RoomGameRecordDO.class);
    }

    /**
     * @param uid
     * @param roomId
     * @param type
     * @param result
     * @return
     * @throws WebServiceException
     */
    @Override
    public String choose(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        if (uid == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (type == null || (type != 1 && type != 2)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (type == 2 && StringUtils.isBlank(result)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }

        String json = redisManager.hget(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
        if (StringUtils.isNotBlank(json)) {
            RoomGameConfigDTO roomGameConfigDTO = gson.fromJson(json, RoomGameConfigDTO.class);
            if (roomGameConfigDTO != null) {
                if (!DateUtils.betweenStrToDate(roomGameConfigDTO.getStart(), roomGameConfigDTO.getEnd())) {
                    redisManager.hdel(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
                    roomGameConfigDAO.updateByStatus(roomGameConfigDTO.getId(), 2);
                    throw new WebServiceException(WebServiceCode.ACTIVITY_END);
                }
            } else {
                throw new WebServiceException(WebServiceCode.ACTIVITY_END);
            }
        } else {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }

        RoomGameRecordDO roomGameRecord = getRoomGameRecord(uid, roomId);
        if (roomGameRecord != null) {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }
        if (type == 1) {
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    result = "" + (int) (Math.random() * 6 + 1);
                } else {
                    result += "," + (int) (Math.random() * 6 + 1);
                }
            }
        }
        roomGameRecord = new RoomGameRecordDO();
        roomGameRecord.setUid(uid);
        roomGameRecord.setRoomId(roomId);
        roomGameRecord.setType(type.byteValue());
        roomGameRecord.setResult(result);
        roomGameRecord.setGameStatus(new Byte("0"));
        roomGameRecord.setCreateTime(new Date());
        roomGameRecordDao.insert(roomGameRecord);
        redisManager.hset(RedisKey.room_game_record.getKey(), uid + "_" + roomId, gson.toJson(roomGameRecord));
        return result;
    }

    /**
     * @param uid
     * @param roomId
     * @param type
     * @param result
     * @return
     * @throws WebServiceException
     */
    @Override
    public String confirm(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        if (uid == null || roomId == null || StringUtils.isBlank(result)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (type == null || (type != 1 && type != 2)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        String json = redisManager.hget(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
        if (StringUtils.isNotBlank(json)) {
            RoomGameConfigDTO roomGameConfigDTO = gson.fromJson(json, RoomGameConfigDTO.class);
            if (roomGameConfigDTO != null) {
                if (!DateUtils.betweenStrToDate(roomGameConfigDTO.getStart(), roomGameConfigDTO.getEnd())) {
                    redisManager.hdel(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
                    roomGameConfigDAO.updateByStatus(roomGameConfigDTO.getId(), 2);
                    throw new WebServiceException(WebServiceCode.ACTIVITY_END);
                }
            } else {
                throw new WebServiceException(WebServiceCode.ACTIVITY_END);
            }
        } else {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }
        RoomGameRecordDO roomGameRecord = getRoomGameRecord(uid, roomId);
        if (roomGameRecord == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (roomGameRecord.getType().intValue() != type || !result.equals(roomGameRecord.getResult())) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        roomGameRecord.setGameStatus(new Byte("1"));
        roomGameRecordDao.updateStatus(roomGameRecord);
        redisManager.hdel(RedisKey.room_game_record.getKey(), uid + "_" + roomId);
        return result;
    }

    @Override
    public String cancel(Long uid, Long roomId, Integer type, String result) throws WebServiceException {
        if (uid == null || roomId == null || StringUtils.isBlank(result)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (type == null || (type != 1 && type != 2)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        String json = redisManager.hget(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
        if (StringUtils.isNotBlank(json)) {
            RoomGameConfigDTO roomGameConfigDTO = gson.fromJson(json, RoomGameConfigDTO.class);
            if (roomGameConfigDTO != null) {
                if (!DateUtils.betweenStrToDate(roomGameConfigDTO.getStart(), roomGameConfigDTO.getEnd())) {
                    redisManager.hdel(RedisKey.room_game_config.getKey(), String.valueOf(room.getUid()));
                    roomGameConfigDAO.updateByStatus(roomGameConfigDTO.getId(), 2);
                    throw new WebServiceException(WebServiceCode.ACTIVITY_END);
                }
            } else {
                throw new WebServiceException(WebServiceCode.ACTIVITY_END);
            }
        } else {
            throw new WebServiceException(WebServiceCode.ACTIVITY_END);
        }
        RoomGameRecordDO roomGameRecord = getRoomGameRecord(uid, roomId);
        if (roomGameRecord == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (roomGameRecord.getType().intValue() != type || !result.equals(roomGameRecord.getResult())) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        roomGameRecord.setGameStatus(new Byte("2"));
        roomGameRecordDao.updateStatus(roomGameRecord);
        redisManager.hdel(RedisKey.room_game_record.getKey(), uid + "_" + roomId);
        return result;
    }

    @Override
    public List<RoomGameDTO> getPersonGame(Long uid, Long roomId) throws WebServiceException {
        String userSendSumStr = redisManager.get(RedisKey.user_send_gift_sum.getKey(String.valueOf(uid)));// 用户送礼金币总数

        long userSendSum = userSendSumStr == null ? 0 : Integer.parseInt(userSendSumStr);
        ArrayList<RoomGameDTO> roomGameDTOS = Lists.newArrayList();

        RoomGameDTO gameDTO;
        if (userSendSum >= 10 * 1) {// 1RMB == 10gold  送礼超过6块开启海螺玩法
            gameDTO = new RoomGameDTO(1, 1);
        } else {
            gameDTO = new RoomGameDTO(1, 0);
        }
        roomGameDTOS.add(gameDTO);

        return roomGameDTOS;
    }

}
