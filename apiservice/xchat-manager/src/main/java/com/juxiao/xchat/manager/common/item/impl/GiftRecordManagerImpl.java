package com.juxiao.xchat.manager.common.item.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.item.GiftSendRecordDao;
import com.juxiao.xchat.dao.item.dto.UserTotalGoldDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftRecordManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 礼物收发记录公共操作类
 *
 * @class: GiftSendRecordManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@Service
public class GiftRecordManagerImpl implements GiftRecordManager {
    @Autowired
    private GiftSendRecordDao giftRecordDao;

    @Autowired
    private RedisManager redisManager;

    /**
     * 获取用户经验值
     *
     * @param uid
     * @return
     */
    @Override
    public long getUserExerpence(Long uid) {
        String exerpence = redisManager.hget(RedisKey.user_level_exper.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(exerpence)) {
            return Long.valueOf(exerpence);
        }

        //从数据库获取
        UserTotalGoldDTO totalGoldDto = giftRecordDao.getUserSendTotalGold(uid);
        if (totalGoldDto != null && totalGoldDto.getTotalGoldNum() != null) {
            redisManager.hincrBy(RedisKey.user_level_exper.getKey(), uid.toString(), totalGoldDto.getTotalGoldNum());
            return totalGoldDto.getTotalGoldNum();
        }

        redisManager.hincrBy(RedisKey.user_level_exper.getKey(), uid.toString(), 0L);
        return 0;
    }

    /**
     * 获取魅力值
     *
     * @param receivedUid
     * @return
     */
    @Override
    public long getUserCharm(Long receivedUid) {
        String charmValue = redisManager.hget(RedisKey.user_level_charm.getKey(), receivedUid.toString());
        if (StringUtils.isNotBlank(charmValue) && StringUtils.isNumeric(charmValue)) {
            return Long.valueOf(charmValue);
        }

        //从数据库获取
        UserTotalGoldDTO totalGoldDto = giftRecordDao.getUserReceiveTotalGold(receivedUid);
        if (totalGoldDto != null && totalGoldDto.getTotalGoldNum() != null) {
            redisManager.hincrBy(RedisKey.user_level_charm.getKey(), receivedUid.toString(), totalGoldDto.getTotalGoldNum());//魅力值
            return totalGoldDto.getTotalGoldNum();
        }

        redisManager.hincrBy(RedisKey.user_level_charm.getKey(), receivedUid.toString(), 0L);
        return Long.valueOf(0);
    }
}
