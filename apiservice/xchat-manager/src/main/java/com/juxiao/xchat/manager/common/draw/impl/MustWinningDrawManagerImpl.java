package com.juxiao.xchat.manager.common.draw.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import com.juxiao.xchat.dao.user.UsersMiningMustDAO;
import com.juxiao.xchat.dao.user.domain.UsersMiningMustDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.draw.GiftDrawManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: alwyn
 * @Description: 指定用户必出全服
 * @Date: 2018/9/28 18:26
 */
@Slf4j
@Service("MustWinningDrawManager")
public class MustWinningDrawManagerImpl extends BaseGiftDrawManager implements GiftDrawManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private Gson gson;

    @Resource
    private UsersMiningMustDAO miningMustDao;

    @Resource
    private RedisManager redisManager;

    @Override
    public boolean check(Long uid, RoomConfDTO roomConfDto, int totalDrawNum, boolean isXq, boolean isHd) {
        if (uid == null) {
            return false;
        }
        return true;
    }

    @Override
    public int draw(Long uid, Long roomUid, int totalDrawNum, boolean isXq, boolean isHd) {
        if (uid == null) {
            return -1;
        }

        String result = redisManager.hget(RedisKey.users_mining_must.getKey(), uid.toString());
        if (StringUtils.isBlank(result)) {
            log.info("[ 捡海螺 ] 必中礼物，用户【{}】在房间【{}】无必中项:>{}", uid, roomUid, result);
            return -1;
        }

        UsersMiningMustDO mustDo;
        try {
            mustDo = gson.fromJson(result, UsersMiningMustDO.class);
        } catch (Exception e) {
            redisManager.hdel(RedisKey.users_mining_must.getKey(), uid.toString());
            return -1;
        }

        if (mustDo == null) {
            return -1;
        }

        if (!uid.equals(mustDo.getUid())) {
            log.info("[ 捡海螺 ] 必中礼物，用户【{}】在房间【{}】必中配置对应不上:>{}", uid, roomUid, result);
            return -1;
        }

        if (mustDo.getGiftId() != null) {
            redisManager.hdel(RedisKey.users_mining_must.getKey(), uid.toString());
            miningMustDao.updateByStatus(uid);
            this.dingLog(uid, mustDo.getGiftId());
            logger.info("[ 全服必中 ] 用户【{}】在房间【{}】 捡海螺必中:>{}", uid, roomUid, mustDo.getGiftId());
            return mustDo.getGiftId();
        }

        logger.info("[ 全服必中 ] 用户【{}】在房间【{}】 捡海螺必中默认礼物Id:>{}", uid, roomUid, giftDrawConf.getDefaultGiftId());
        return giftDrawConf.getDefaultGiftId();
    }

    @Override
    double[] getDrawRates() {
        return new double[0];
    }

    @Override
    int[] getDrawGifts() {
        return new int[0];
    }

    @Override
    String getType() {
        return null;
    }

    /**
     * 日志输出
     *
     * @param uid    uid
     * @param giftId giftId
     */
    private void dingLog(Long uid, int giftId) {
        logger.info("[ 捡海螺活动 ] {}通过【官方指定用户】方式砸中礼物，礼物ID:>{}", uid, giftId);
    }
}
