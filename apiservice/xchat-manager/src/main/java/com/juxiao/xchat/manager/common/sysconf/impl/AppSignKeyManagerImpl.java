package com.juxiao.xchat.manager.common.sysconf.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.AppSecretKeyDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.AppSignKeyManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppSignKeyManagerImpl implements AppSignKeyManager {
    @Autowired
    private AppSecretKeyDao signKeyDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public String getAppSignKey(String os, String appVersion) {
        String signKey = redisManager.hget(RedisKey.app_sign_key.getKey(), os + "_" + appVersion);
        if (StringUtils.isNotBlank(signKey)) {
            return signKey;
        }

        signKey = signKeyDao.getAppSignKey(os, appVersion);
        if (StringUtils.isNotBlank(signKey)) {
            redisManager.hset(RedisKey.app_sign_key.getKey(), os + "_" + appVersion, signKey);
        }
        return signKey;
    }
}
