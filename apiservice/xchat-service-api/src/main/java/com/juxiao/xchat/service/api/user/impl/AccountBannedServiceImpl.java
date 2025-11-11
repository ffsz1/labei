package com.juxiao.xchat.service.api.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.domain.AccountBannedDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.user.AccountBannedService;
import com.juxiao.xchat.service.api.user.vo.AccountBannedVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountBannedServiceImpl implements AccountBannedService {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public AccountBannedVO checkBanned(Long uid) {
        String value = redisManager.hget(RedisKey.account_banned_record.getKey(), String.valueOf(uid));
        if (StringUtils.isEmpty(value)) {
            // 缓存中没有禁言记录...
            return new AccountBannedVO(false, false, false, false);
        }
        AccountBannedDO accountBanned = gson.fromJson(value, AccountBannedDO.class);
        if (accountBanned.getBannedEndTime().getTime() > new Date().getTime()) {
            // 禁言中...
            AccountBannedVO accountBannedVO;
            String status = accountBanned.getBannedType();
            switch (status) {
                case "0":
                    accountBannedVO = new AccountBannedVO(true, false, false, false);
                    break;
                case "1":
                    accountBannedVO = new AccountBannedVO(false, true, false, false);
                    break;
                case "2":
                    accountBannedVO = new AccountBannedVO(false, false, true, false);
                    break;
                case "3":
                    accountBannedVO = new AccountBannedVO(false, false, false, true);
                    break;
                default:
                    accountBannedVO = new AccountBannedVO(false, false, false, false);
            }
            return accountBannedVO;
        }
        // 已经过了禁言时间...
        redisManager.hdel(RedisKey.account_banned_record.getKey(), String.valueOf(uid));
        return new AccountBannedVO(false, false, false, false);
    }
}
