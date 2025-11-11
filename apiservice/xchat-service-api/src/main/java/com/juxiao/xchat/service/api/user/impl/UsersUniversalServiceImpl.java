package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.user.UsersUniversalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户提现白名单
 * @author BigCat
 */
@Service
public class UsersUniversalServiceImpl implements UsersUniversalService {

    @Autowired
    private RedisManager redisManager;


    /**
     * 检测用户是否在提现白名单中
     *
     * @param uid uid
     * @return boolean
     */
    @Override
    public boolean checkUsersWithdrawWhitelist(Long uid) {
        String cacheResult = redisManager.hget(RedisKey.users_withdraw_whitelist.getKey(),uid.toString());
        return StringUtils.isNotBlank(cacheResult);
    }
}
