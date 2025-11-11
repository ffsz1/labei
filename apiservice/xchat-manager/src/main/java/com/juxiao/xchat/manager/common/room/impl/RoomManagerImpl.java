package com.juxiao.xchat.manager.common.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.RoomConfDao;
import com.juxiao.xchat.dao.room.RoomDao;
import com.juxiao.xchat.dao.room.RoomOpenHistDao;
import com.juxiao.xchat.dao.room.domain.RoomDO;
import com.juxiao.xchat.dao.room.domain.RoomOnlineNum;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomOpenHistDTO;
import com.juxiao.xchat.dao.room.enumeration.RoomOpenHistCloseType;
import com.juxiao.xchat.dao.room.enumeration.RoomOptStatus;
import com.juxiao.xchat.dao.room.enumeration.RoomUserAtt;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 房间管理操作实现
 *
 * @class: RoomManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@Service
public class RoomManagerImpl implements RoomManager {
    private static final Logger logger = LoggerFactory.getLogger(RoomManagerImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private RoomConfDao roomConfDao;

    @Autowired
    private RoomOpenHistDao roomOpenHistDao;

    @Autowired
    private RedisManager redisManager;

    @Override
    public RoomDTO getRoom(Long roomId) {
        String roomUid = redisManager.hget(RedisKey.room_room_id.getKey(), String.valueOf(roomId));
        if (StringUtils.isNotBlank(roomUid)) {
            try {
                return this.getUserRoom(Long.valueOf(roomUid));
            } catch (Exception e) {
                redisManager.hdel(RedisKey.room_room_id.getKey(), String.valueOf(roomId));
            }
        }

        RoomDTO roomDto = roomDao.getRoom(roomId);
        if (roomDto != null) {
            redisManager.hset(RedisKey.room_room_id.getKey(), String.valueOf(roomId), String.valueOf(roomDto.getUid()));
        }
        return roomDto;
    }

    @Override
    public RoomDTO getUserRoom(Long uid) {
        if (uid == null) {
            return null;
        }

        String roomStr = redisManager.hget(RedisKey.room.getKey(), String.valueOf(uid));
        if (!StringUtils.isBlank(roomStr)) {
            RoomDTO roomDto = gson.fromJson(roomStr, RoomDTO.class);
            if(roomDto.getFaceType() == null){
                return getDbRoomDTO(uid);
            }
            if (roomDto.getRoomId() != null) {
                return roomDto;
            }
        }
        return getDbRoomDTO(uid);
    }

    private RoomDTO getDbRoomDTO(Long uid){
        RoomDTO roomDto = roomDao.getUserRoom(uid);
        if (roomDto != null) {
            // RoomConfDTO roomConfDto = roomConfDao.getRoomConf(uid);
            roomDto.setGiftDrawEnable(1);
            roomDto.setFaceType(roomDto.getFaceType());
            redisManager.hset(RedisKey.room.getKey(), String.valueOf(uid), gson.toJson(roomDto));
        }
        return roomDto;
    }

    /**
     * 更新房间信息--数据库和缓存中都更新
     *
     * @param roomDo 房间信息
     */
    @Override
    public RoomDTO updateRoomInfo(RoomDO roomDo) {
        if (roomDo == null) {
            return null;
        }

        if (roomDo.getUid() == null) {
            return null;
        }

        roomDao.update(roomDo);

        RoomDTO roomDto = roomDao.getUserRoom(roomDo.getUid());
        RoomConfDTO roomConfDto = roomConfDao.getRoomConf(roomDo.getUid());
        roomDto.setGiftDrawEnable(roomConfDto == null || roomConfDto.getRoomType() == null ? 2 : roomConfDto.getRoomType().intValue());
        redisManager.hset(RedisKey.room.getKey(), roomDto.getUid().toString(), gson.toJson(roomDto));
        return roomDto;
    }

    @Override
    public RoomDTO close(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO roomDTO = getUserRoom(uid);
        if (roomDTO == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        roomDTO.setValid(false);
        roomDTO.setOperatorStatus(RoomOptStatus.out.getStatus());
        saveRoomOpenHist(roomDTO);
        closeRoom(roomDTO);
        return roomDTO;
    }

    @Override
    public int getRobotNum(Long uid) {
        String robotCount = redisManager.hget(RedisKey.robot_num.getKey(), uid.toString());
        if (StringUtils.isBlank(robotCount)) {
            Integer num = jdbcTemplate.queryForObject("select COUNT(1) from room_robot_group r INNER JOIN users u on r.group_no = u.erban_no where u.uid = ?", Integer.class, uid);
            redisManager.hset(RedisKey.robot_num.getKey(), uid.toString(), num.toString());
            return (num < 0) ? 0 : num;
        }

        try {
            return Integer.valueOf(robotCount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public Integer getNeedAddNum(Long uid, Integer onlineNum) {
        // 获取房间人数配置
        String num = redisManager.hget(RedisKey.room_online_num.getKey(), uid.toString());
        if (StringUtils.isEmpty(num)) {
            return 0;
        }

        RoomOnlineNum roomOnlineNum = gson.fromJson(num, RoomOnlineNum.class);
        if (roomOnlineNum.getType().intValue() == 1) {
            return roomOnlineNum.getFactor();
        }

        if (roomOnlineNum.getType().intValue() == 2 && onlineNum != null) {
            return roomOnlineNum.getFactor() * onlineNum / 100;
        }
        return 0;
    }

    @Override
    public RoomVo convertRoomToVo(RoomDTO room) {
        if ((room.getTagId() == null || room.getTagId() <= 0 || room.getTagId() == 8) && StringUtils.isBlank(room.getRoomTag())) {
            logger.error("tagNull convertRoomToVo>:{}",gson.toJson(room));
        }

        RoomVo roomVo = new RoomVo();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setUid(room.getUid());
        roomVo.setMeetingName(room.getMeetingName());
        roomVo.setType(room.getType());
        roomVo.setOperatorStatus(room.getOperatorStatus());
        roomVo.setBackPic(room.getBackPic() == null ? "" : room.getBackPic());
        roomVo.setBadge("".equals(room.getBadge()) ? null : room.getBadge());
        roomVo.setValid(room.getValid());
        roomVo.setRoomDesc(room.getRoomDesc());
        roomVo.setRoomNotice(room.getRoomNotice());
        roomVo.setTitle(room.getTitle());
        roomVo.setOpenTime(room.getOpenTime());
        roomVo.setOfficeUser(new Byte("1"));
        // roomVo.setIsExceptionClose(room.getIsExceptionClose());
        // roomVo.setExceptionCloseTime(room.getExceptionCloseTime());
        roomVo.setRoomPwd(room.getRoomPwd());
        roomVo.setOfficialRoom(room.getOfficialRoom());
        roomVo.setRoomTag(room.getRoomTag());
        roomVo.setTagId(room.getTagId());
        roomVo.setTagPict(room.getTagPict());
        roomVo.setIsPermitRoom(room.getIsPermitRoom());
        roomVo.setFactor(getNeedAddNum(room.getUid(), room.getOnlineNum()));
        roomVo.setOnlineNum(room.getOnlineNum());
        // roomVo.setAvatar(room.getAvatar());
        Byte bytes = room.getAbChannelType();
        if (bytes != null) {
            roomVo.setAbChannelType(bytes);
            return roomVo;
        }
        roomVo.setAbChannelType((RoomUserAtt.A.getAtt()));
        return null;
    }

    /**
     * 保存房间开播时长
     *
     * @param room 房间信息
     */
    private void saveRoomOpenHist(RoomDTO room) {
        logger.info("将当前房间的开播时长写入数据库中，roomUid:" + room.getUid());
        Date date = new Date();
        Date openTime = room.getOpenTime();
        RoomOpenHistDTO roomOpenHist = new RoomOpenHistDTO();
        roomOpenHist.setHistId(UUIDUtils.get());
        roomOpenHist.setMeetingName(room.getMeetingName());
        roomOpenHist.setRoomId(room.getUid());
        roomOpenHist.setUid(room.getUid());
        roomOpenHist.setType(room.getType());
        roomOpenHist.setCloseType(RoomOpenHistCloseType.NORMAL.getType());
        roomOpenHist.setCloseTime(date);
        roomOpenHist.setDura((double) (date.getTime() - openTime.getTime()));
        roomOpenHistDao.insert(roomOpenHist);
    }

    /**
     * 关闭房间--清理缓存,清理云信记录
     *
     * @param roomDTO
     */
    private void closeRoom(RoomDTO roomDTO) {
        // 关闭房间
        // 更新房间信息到数据库和缓存
        RoomDO roomDo = new RoomDO();
        BeanUtils.copyProperties(roomDTO, roomDo);
        updateRoomInfo(roomDo);
        // 通知云信清空聊天室的有序队列
        // netEaseManager.queueDrop(roomDTO.getRoomId());
        // 删除房间的麦序列表信息
        delRoomMicByUid(roomDTO.getUid());
        // 清理缓存
        redisManager.hdel(RedisKey.room_running.getKey(), roomDTO.getUid().toString());
        // 通知云信设置聊天室状态为关闭
        // netEaseManager.toggleRoomStatus(roomDTO.getRoomId(), roomDTO.getUid().toString(), false);
    }

    private void delRoomMicByUid(Long uid) {
        if (uid == null) {
            return;
        }
        redisManager.del(RedisKey.room_mic_list.getKey(uid.toString()));
    }
}
