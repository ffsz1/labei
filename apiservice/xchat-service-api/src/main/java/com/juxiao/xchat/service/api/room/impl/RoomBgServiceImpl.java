package com.juxiao.xchat.service.api.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.RoomBgDAO;
import com.juxiao.xchat.dao.room.domain.RoomBgDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.service.api.room.RoomBgService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 房间背景实现
 * @Date: 2018/10/9 13:53
 */
@Service
public class RoomBgServiceImpl implements RoomBgService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoomBgDAO roomBgDAO;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public RoomBgDO getById(Integer id, Long roomId) {
        if (id == null) {
            return null;
        }
        String result = redisManager.hget(RedisKey.room_background.getKey(), id.toString());
        RoomBgDO bgDO;
        if (StringUtils.isBlank(result)) {
            bgDO = roomBgDAO.getById(id);
            if (bgDO != null) {
                List<Integer> tagList = roomBgDAO.listTagByBgId(bgDO.getId());
                List<Long> uidList = roomBgDAO.listUidByBgId(bgDO.getId());
                bgDO.setTagIds(tagList);
                bgDO.setUids(uidList);
                redisManager.hset(RedisKey.room_background.getKey(), id.toString(), gson.toJson(bgDO));
            }
        } else {
            bgDO = gson.fromJson(result, RoomBgDO.class);
        }
        if (bgDO != null) {
            RoomDTO roomDTO = roomManager.getRoom(roomId);
            if (roomDTO == null) {
                return null;
            }
            logger.info("[房间背景]  bgType:{},uid:{},tagId:{},bgDO:{}", bgDO.getType(), roomDTO.getUid(), roomDTO.getTagId(), bgDO);
            if (bgDO.getType() == 1) {
                // 所有人可以用
                return bgDO;
            }
            if (bgDO.getType().equals(3) && bgDO.getUids() != null) {
                // 指定用户可以用
                for(Long uid : bgDO.getUids()) {
                    if (uid.equals(roomDTO.getUid())) {
                        return bgDO;
                    }
                }
            }
            if (bgDO.getType().equals(2) && bgDO.getTagIds() != null) {
                // 指定标签可以用
                for(Integer tagId : bgDO.getTagIds()) {
                    if (tagId.equals(roomDTO.getTagId())) {
                        return bgDO;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public RoomBgDO getById(String id, Long roomId) {
        if (id != null && StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
            return getById(Integer.valueOf(id), roomId);
        }
        return null;
    }

    @Override
    public List<RoomBgDO> listByRoomId(Long roomId) {
        return roomBgDAO.listByRoomId(roomId);
    }
}
