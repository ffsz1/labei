package com.erban.main.service.room;

import com.erban.main.dto.RoomBgDTO;
import com.erban.main.model.Room;
import com.erban.main.mybatismapper.RoomBgMapper;
import com.erban.main.util.StringUtils;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-09
 * @time 10:09
 */
@Service
public class RoomBgPicService {

    @Autowired
    private RoomBgMapper roomBgMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private RoomService roomService;
    private Gson gson = new Gson();

    public RoomBgDTO getById(Integer id, Long roomId) {
        if (id == null) {
            return null;
        }
        String result = jedisService.hget(RedisKey.room_background.getKey(), id.toString());
        RoomBgDTO bgDO;
        if (StringUtils.isBlank(result)) {
            bgDO = roomBgMapper.getById(id);
            if (bgDO != null) {
                List<Integer> tagList = roomBgMapper.listTagByBgId(bgDO.getId());
                List<Long> uidList = roomBgMapper.listUidByBgId(bgDO.getId());
                bgDO.setTagIds(tagList);
                bgDO.setUids(uidList);
                jedisService.hset(RedisKey.room_background.getKey(), id.toString(), gson.toJson(bgDO));
            }
        } else {
            bgDO = gson.fromJson(result, RoomBgDTO.class);
        }
        if (bgDO != null) {
            Room roomDTO = roomService.getRoomByRoomId(roomId);
            if (roomDTO == null) {
                return null;
            }
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
}
