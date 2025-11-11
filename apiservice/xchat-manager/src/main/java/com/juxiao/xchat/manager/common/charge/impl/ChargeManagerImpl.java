package com.juxiao.xchat.manager.common.charge.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.charge.ChargeRecordDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.ChargeManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeManagerImpl implements ChargeManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ChargeRecordDao recordDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void sumUserCharge(Long uid, Integer amount) {
        String redisKey = RedisKey.user_charge.getKey("lock");
        String lockVal = redisManager.lock(redisKey, 10000);
        if (StringUtils.isBlank(lockVal)) {
            return;
        }
        try {
            long chargeSum = redisManager.hincrBy(RedisKey.user_charge.getKey(), uid.toString(), (long)amount);

            // 第一次进入缓存，重新在数据库中进行统计
            if (chargeSum <= amount) {
                int chargeSuccessSum = recordDao.sumUserChargeSuccess(uid);
                redisManager.hset(RedisKey.user_charge.getKey(), uid.toString(), String.valueOf(chargeSuccessSum));
            }

            /*String chargeExp = redisManager.hget(RedisKey.user_charge_exp.getKey(), String.valueOf(uid));
            if (StringUtils.isNotBlank(chargeExp)) {
                redisManager.hincrBy(RedisKey.user_charge_exp.getKey(), String.valueOf(uid), amount);
            }*/
        } catch (Exception e) {
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }
}
