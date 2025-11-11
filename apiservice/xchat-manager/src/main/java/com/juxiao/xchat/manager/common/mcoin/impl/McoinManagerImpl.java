package com.juxiao.xchat.manager.common.mcoin.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.McoinLevelRecordDao;
import com.juxiao.xchat.dao.mcoin.UserMcoinPurseDao;
import com.juxiao.xchat.dao.mcoin.domain.McoinLevelRecordDO;
import com.juxiao.xchat.dao.mcoin.domain.UserMcoinPurseDO;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class McoinManagerImpl implements McoinManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private McoinLevelRecordDao mcoinLevelRecordDao;
    @Autowired
    private UserMcoinPurseDao mcoinPurseDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    private Map<Integer, Integer> levelMcoin;

    public McoinManagerImpl() {
        this.levelMcoin = Maps.newTreeMap();
        this.levelMcoin.put(6, 100);
        this.levelMcoin.put(11, 200);
        this.levelMcoin.put(21, 500);
    }

    @Override
    public UserMcoinPurseDTO getUserMcoinPurse(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        String json = redisManager.hget(RedisKey.mcoin_user_purse.getKey(), uid.toString());
        if (StringUtils.isNotBlank(json)) {
            try {
                return gson.fromJson(json, UserMcoinPurseDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.mcoin_user_purse.getKey(), uid.toString());
            }
        }

        UserMcoinPurseDTO mcoinPurseDto = mcoinPurseDao.getUserMcoinPurse(uid);
        if (mcoinPurseDto == null) {
            Date now = new Date();
            UserMcoinPurseDO purseDo = new UserMcoinPurseDO(uid, 0, (byte) 1, now, now);
            mcoinPurseDao.save(purseDo);
            mcoinPurseDto = new UserMcoinPurseDTO();
            BeanUtils.copyProperties(purseDo, mcoinPurseDto);
        }

        redisManager.hset(RedisKey.mcoin_user_purse.getKey(), uid.toString(), gson.toJson(mcoinPurseDto));
        return mcoinPurseDto;
    }

    @Override
    public void updateAddMcoin(Long uid, int mcoinAmount) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String redisKey = RedisKey.mcoin_user_purse_lock.getKey(uid.toString());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            UserMcoinPurseDTO purseDto = this.getUserMcoinPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            int mcoinBefore = purseDto.getMcoinNum();
            purseDto.setMcoinNum(mcoinBefore + mcoinAmount);
            logger.info("[ 用户点点币余额 ]uid:>{},增加前:>{},增加后:>{}", uid, mcoinBefore, purseDto.getMcoinNum());
            mcoinPurseDao.updateMcoinAmount(uid, mcoinAmount);
            redisManager.hset(RedisKey.mcoin_user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Override
    public void updateReduceMcoin(Long uid, int mcoinCost) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String redisKey = RedisKey.mcoin_user_purse_lock.getKey(uid.toString());
        String lockVal = redisManager.lock(redisKey, 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserMcoinPurseDTO purseDto = this.getUserMcoinPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            // 判断余额是否足够
            int mcoinBefore = purseDto.getMcoinNum();
            if (mcoinBefore < mcoinCost) {
                throw new WebServiceException(WebServiceCode.MCOIN_PURSE_NOT_ENOUGH);
            }
            purseDto.setMcoinNum(mcoinBefore - mcoinCost);
            mcoinPurseDao.updateMcoinCost(uid, mcoinCost);
            redisManager.hset(RedisKey.mcoin_user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 用户点点币余额 ]uid:>{},减少前:>{},减少后:>{}", uid, mcoinBefore, purseDto.getMcoinNum());
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Async
    @Override
    public void updateAddLevelMcoin(Long uid, Integer charmLevel) {
        if (uid == null || charmLevel == null || charmLevel < 0) {
            return;
        }

        String redisKey = RedisKey.mcoin_user_level_lock.getKey(uid.toString());
        String lockVal = redisManager.lock(redisKey, 10 * 1000);
        if (StringUtils.isBlank(lockVal)) {
            return;
        }


        try {
            for (Map.Entry<Integer, Integer> entry : this.levelMcoin.entrySet()) {
                if (charmLevel < entry.getKey()) {
                    break;
                }

                int levelCount = mcoinLevelRecordDao.countLevelMcoinRecord(uid, entry.getKey());
                if (levelCount > 0) {
                    continue;
                }

                mcoinLevelRecordDao.save(new McoinLevelRecordDO(uid, entry.getKey(), entry.getValue()));
                this.updateAddMcoin(uid, entry.getValue());
            }

        } catch (Exception e) {
            logger.error("[ 魅力等级 ] 保存魅力等级异常，uid:>{}, charmLevel:>{}", uid, charmLevel, e);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Override
    public void sendMessageToUser(Long uid, McoinMissionDTO missionDto) throws WebServiceException {
        StringBuilder msg = new StringBuilder();
        msg.append("恭喜你成功邀请");
        msg.append(missionDto.getMissionName());
        msg.append("注册，获得邀请奖励");
        msg.append(missionDto.getMcoinAmount());
        msg.append("点点币！已发送至你的点点币钱包。");
        // 发送消息给用户
        asyncNetEaseTrigger.sendMsg(String.valueOf(uid),msg.toString());
    }

    /**
     * 减少缓存中点点币
     *
     * @param uid      sendUid
     * @param afterGoldNum afterGoldNum
     * @param pushMsg      pushMsg
     */
    @Override
    public void reduceGoldCache(Long uid, Long afterGoldNum, boolean pushMsg) throws WebServiceException{
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.mcoin_user_purse_lock.getKey(String.valueOf(uid)), 10 * 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserMcoinPurseDTO purseDto = this.getUserMcoinPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            long balance = purseDto.getMcoinNum() - afterGoldNum;
            // 判断余额是否足够
            if (balance < 0) {
                throw new WebServiceException(WebServiceCode.POINT_PURSE_MONEY_NOT_ENOUGH);
            }
            long goldBefore = purseDto.getMcoinNum();
            purseDto.setMcoinNum(purseDto.getMcoinNum() - afterGoldNum.intValue());
            redisManager.hset(RedisKey.mcoin_user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少缓存点点币 ] uid:>{},减少前:>{},减少后:>{}", uid, goldBefore, purseDto.getMcoinNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        }  finally {
            redisManager.unlock(RedisKey.mcoin_user_purse_lock.getKey(uid.toString()), lockVal);
        }
    }

    private void pushMsg(UserMcoinPurseDTO purseDto) {
    }

    public static void main(String[] args) {
        boolean flag = true;
        if(flag){
            System.out.println(1);
        }else{
            System.out.println(2);

        }
    }
}
