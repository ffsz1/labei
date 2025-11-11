package com.juxiao.xchat.service.api.room.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.RoomBackgroundDao;
import com.juxiao.xchat.dao.room.dto.RoomBackgroundResultDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.room.RoomBackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @class: RoomBackgroundServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/7/17
 */
@Service
public class RoomBackgroundServiceImpl implements RoomBackgroundService {
    @Autowired
    private Gson gson;
    @Autowired
    private RoomBackgroundDao backgroundDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public List<RoomBackgroundResultDTO> listEffectiveBackground() {
        Map<String, String> result = redisManager.hgetAll(RedisKey.room_background.getKey());
        List<RoomBackgroundResultDTO> list;
        if (result != null && result.size() > 0) {
            Iterator<String> iterator = result.keySet().iterator();
            String value;
            list = Lists.newArrayList();
            try {
                while (iterator.hasNext()) {
                    value = result.get(iterator.next());
                    list.add(gson.fromJson(value, RoomBackgroundResultDTO.class));
                }

                Collections.sort(list);
                return list;
            } catch (Exception e) {
                redisManager.del(RedisKey.room_background.getKey());
            }
        }

        list = backgroundDao.listEffectiveBackground();
        if (list != null && list.size() > 0) {
            for (RoomBackgroundResultDTO resultDto : list) {
                redisManager.hset(RedisKey.room_background.getKey(), String.valueOf(resultDto.getId()), gson.toJson(resultDto));
            }
        }

        return list;
    }
}
