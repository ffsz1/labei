package com.juxiao.xchat.manager.common.draw.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.bill.BillGiftDrawDao;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首次捡海螺
 */
@Service("FirstGiftDrawManager")
public class FirstGiftDrawManagerImpl extends BaseGiftDrawManager implements GiftDrawManager {
    @Autowired
    private BillGiftDrawDao giftDrawDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public boolean check(Long uid, RoomConfDTO roomConfDto, int totalDrawNum, boolean isXq, boolean isHd) {
        return false;
//        String drawCountStr = redisManager.hget(RedisKey.user_gift_first.getKey(), uid.toString());
//        int drawCount;
//        if (StringUtils.isBlank(drawCountStr)) {
//            drawCount = giftDrawDao.countUserDraw(uid);
//            redisManager.hset(RedisKey.user_gift_first.getKey(), uid.toString(), String.valueOf(drawCount + 1));
//            return drawCount == 0;
//        }
//
//        try {
//            drawCount = Integer.valueOf(drawCountStr);
//        } catch (Exception e) {
//            drawCount = 0;
//        }
//        return drawCount == 0;
    }

    @Override
    double[] getDrawRates() {
        return giftDrawConf.getFirstDrawRates();
    }

    @Override
    int[] getDrawGifts() {
        return giftDrawConf.getDrawGifts();
    }

    @Override
    String getType() {
        return "首次概率";
    }
}
