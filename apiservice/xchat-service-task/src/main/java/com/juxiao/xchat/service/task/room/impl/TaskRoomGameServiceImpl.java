package com.juxiao.xchat.service.task.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.dao.room.dto.RoomGameConfigDTO;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.room.RoomGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-13
 * @time 11:03
 */
@Service
public class TaskRoomGameServiceImpl implements RoomGameService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private Gson gson;

    @Autowired
    private RedisManager redisManager;


    /**
     * 检查姻缘求签状态
     */
    @Override
    public void checkGameStatus() {
        List<RoomGameConfigDTO> gameConfigDTOS = taskDao.findRoomGameConfig();
        if(gameConfigDTOS != null && gameConfigDTOS.size() > 0){
            gameConfigDTOS.forEach(item ->{
                if(DateUtils.betweenStrToDate(item.getStart(),item.getEnd())){
                    redisManager.hset(RedisKey.room_game_config.getKey(),String.valueOf(item.getUid()),gson.toJson(item));
                }else{
                    taskDao.updateByStatus(item.getId(),2);
                    redisManager.hdel(RedisKey.room_game_config.getKey(),String.valueOf(item.getUid()));
                }
            });
        }
    }
}
