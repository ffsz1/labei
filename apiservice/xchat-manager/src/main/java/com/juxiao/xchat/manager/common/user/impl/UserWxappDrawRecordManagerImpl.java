package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.UserWxappDrawRecordDao;
import com.juxiao.xchat.dao.user.dto.UserTodayShareDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserWxappDrawRecordManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWxappDrawRecordManagerImpl implements UserWxappDrawRecordManager {
    @Autowired
    private Gson gson;
    @Autowired
    private UserWxappDrawRecordDao wxappDrawRecordDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;

    @Override
    public List<UserTodayShareDTO> listTodayShareUser(Long uid) {
        String array = redisManager.hget(RedisKey.wxapp_share_day_list.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(array)) {
            try {
                return gson.fromJson(array, new TypeToken<List<UserTodayShareDTO>>() {
                }.getType());
            } catch (Exception e) {
                redisManager.hdel(RedisKey.wxapp_share_day_list.getKey(), String.valueOf(uid));
            }
        }

        List<UserTodayShareDTO> list = wxappDrawRecordDao.listTodayShareUser(uid);
        if (list == null || list.size() == 0) {
            return list;
        }

        UsersDTO usersDto;
        for (UserTodayShareDTO shareDto : list) {
            usersDto = usersManager.getUser(shareDto.getUid());
            if (usersDto == null) {
                continue;
            }
            shareDto.setAvater(usersDto.getAvatar());
            shareDto.setNewcomer(true);
            shareDto.setFreeDrawCount(1);
        }

        redisManager.hset(RedisKey.wxapp_share_day_list.getKey(), String.valueOf(uid), gson.toJson(list));
        return list;
    }
}
