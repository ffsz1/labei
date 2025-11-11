package com.juxiao.xchat.service.task.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.ListUtil;
import com.juxiao.xchat.dao.event.OneDayRoomRecvSumDAO;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.dto.UserSoundDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import com.juxiao.xchat.service.task.user.UsersTaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UsersTaskServiceImpl implements UsersTaskService {
    private static final Logger logger = LoggerFactory.getLogger(UsersTaskServiceImpl.class);
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private Gson gson;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private OneDayRoomRecvSumDAO oneDayRoomRecvSumDAO;

    @Override
    public void saveOppositeSex(String gender, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> map = taskDao.findOppositeSex(gender, startDate, endDate);
            if (map != null && map.size() > 0) {
                Integer levelExer;
                Integer levelChar;
                Integer robotNum;
                Integer erbanNo;
                List<Map<String, Object>> result = Lists.newArrayList();
                for (Map<String, Object> m : map) {
                    levelExer = levelManager.getUserExperienceLevelSeq(Long.valueOf(m.get("uid").toString()));
                    levelChar = levelManager.getUserCharmLevelSeq(Long.valueOf(m.get("uid").toString()));
                    robotNum = roomManager.getRobotNum(Long.valueOf(m.get("uid").toString()));
                    erbanNo = Integer.valueOf(m.get("erbanNo").toString());
                    if ((levelChar >= 2 || levelExer >= 2) && robotNum == 0 && erbanNo > 10000) {// 2级以上，没有机器人，非特殊账号
                        result.add(m);
                    }
                }
                redisManager.hset(RedisKey.opposite_sex.getKey(), gender, gson.toJson(result));
            }
        } catch (Exception e) {
            logger.error("[ 缓存偶遇列表失败 ]", e);
        }
    }

    @Override
    public void refreshSoundPool() {
        List<UserSoundDTO> list = usersDao.listUserBySound();

        //性别、年龄
        Long time = System.currentTimeMillis() / 1000L;
        redisManager.del(RedisKey.user_sound_gender_age_pool.getKey());
        redisManager.del(RedisKey.user_sound_gender_age_pool.getKey("1"));
        redisManager.del(RedisKey.user_sound_gender_age_pool.getKey("2"));
        list.stream().forEach(dto -> {
            int age = dto.getBirth() == null ? 0 : (int) ((time - dto.getBirth().getTime()) / (12 * 30 * 24 * 3600L));
            age = age < 0 ? 0 : age;
            String str = gson.toJson(dto);

            redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey(), str, age);
            if (dto.getGender().intValue() == 1) {
                redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey("1"), str, age);
            } else if (dto.getGender().intValue() == 2) {
                redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey("2"), str, age);
            }
        });
    }
}
