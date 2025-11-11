package com.juxiao.xchat.manager.common.user.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.UserConfigureDao;
import com.juxiao.xchat.dao.user.dto.UserConfigureDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserConfigureManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: UserConfigureManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Service
public class UserConfigureManagerImpl implements UserConfigureManager {
    @Autowired
    private UserConfigureDao configureDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    /**
     * @param uid
     * @return
     */
    @Override
    public UserConfigureDTO getUserConfigure(Long uid) {
        String confStr = redisManager.hget(RedisKey.user_configure.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(confStr)) {
            return gson.fromJson(confStr, UserConfigureDTO.class);
        }

        UserConfigureDTO configureDto = configureDao.getUserConfigure(uid);
        if (configureDto != null) {
            redisManager.hset(RedisKey.user_configure.getKey(), String.valueOf(uid), gson.toJson(configureDto));
        }
        return configureDto;
    }
}
