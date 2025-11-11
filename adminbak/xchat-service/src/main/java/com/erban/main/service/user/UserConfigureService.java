package com.erban.main.service.user;

import com.erban.main.model.UserConfigure;
import com.erban.main.service.base.CacheBaseService;
import com.xchat.common.redis.RedisKey;
import org.springframework.stereotype.Service;

@Service
public class UserConfigureService extends CacheBaseService<UserConfigure, UserConfigure> {

    @Override
    public UserConfigure getOneByJedisId(String jedisId) {
        return getOne(RedisKey.user_configure.getKey(), jedisId, "select * from user_configure where uid = ? ", jedisId);
    }

    @Override
    public UserConfigure entityToCache(UserConfigure entity) {
        return entity;
    }

}
