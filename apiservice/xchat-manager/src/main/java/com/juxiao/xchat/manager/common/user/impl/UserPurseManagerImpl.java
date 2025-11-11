package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.UserPurseDao;
import com.juxiao.xchat.dao.user.domain.UserPurseDO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.vo.UserPurseVO;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.Attach;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteasePushBO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

/**
 * 用户钱包通用操作
 *
 * @class: UserPurseManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class UserPurseManagerImpl implements UserPurseManager {
    private static final Logger logger = LoggerFactory.getLogger(UserPurseManagerImpl.class);

    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.00");

    @Autowired
    private UserPurseDao purseDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    /**
     * @see com.juxiao.xchat.manager.common.user.UserPurseManager#save(UserPurseDO)
     */
    @Override
    public void save(UserPurseDO purseDo) {
        purseDao.save(purseDo);
        UserPurseDTO purseDto = new UserPurseDTO();
        BeanUtils.copyProperties(purseDo, purseDto);
        redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(purseDo.getUid()), gson.toJson(purseDto));
    }

    @Override
    public void updateAddGoldReduceDiamond(Long uid, Long goldAmount, Double diamondCost, boolean pushMsg) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            // 判断余额是否足够
            if (purseDto.getDiamondNum() < diamondCost) {
                throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
            }
            int updateCount = purseDao.updateAddGoldReduceDiamond(uid, goldAmount, diamondCost);
            if (updateCount <= 0) {
                throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
            }
            long goldBefore = purseDto.getGoldNum();
            double diamondBfore = purseDto.getDiamondNum();
            purseDto.setChargeGoldNum(purseDto.getChargeGoldNum() + goldAmount);
            purseDto.setGoldNum(purseDto.getGoldNum() + goldAmount);
            purseDto.setDiamondNum(Double.valueOf(DOUBLE_FORMAT.format(purseDto.getDiamondNum() - diamondCost)));
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 钻石兑换金币 ] uid:>{}，兑换前:>钻石={}，金币={}，兑换后:>钻石={}，金币={}", uid, diamondBfore, goldBefore, purseDto.getDiamondNum(), purseDto.getGoldNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：钻石兑换金币", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(String.valueOf(uid)), lockVal);
        }

    }

    @Override
    public void updateAddGold(Long uid, Long goldAmount, boolean isCharge, boolean pushMsg, String type, Map<String, Long> num, String chargeDesc) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            int updateCount = purseDao.updateGoldAmount(uid, goldAmount.longValue());
            if (updateCount <= 0) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            long goldBefore = purseDto.getGoldNum();
            purseDto.setChargeGoldNum(purseDto.getChargeGoldNum() + goldAmount);
            purseDto.setGoldNum(purseDto.getGoldNum() + goldAmount);
            logger.info("[ 增加金币 ] uid:>{}, 增加金币类型:>{} , 增加前:>{},增加后:>{}", uid, type, goldBefore, purseDto.getGoldNum());
            logger.info("[ 用户余额 ] uid:>{}, diamondNum:>{}, goldNum:>{}, 类型：增加金币", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            if (num != null) {
                num.put("addBeforeGoldNum", goldBefore);
                num.put("addAfterGoldNum", purseDto.getGoldNum());
            }

            if (isCharge) {
                purseDto.setIsFirstCharge(false);
            }

            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));

            if (pushMsg) {
                asyncNetEaseTrigger.sendMsg(String.valueOf(uid),chargeDesc);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }

    }

    @Override
    public void updateReduceGold(Long uid, Integer goldCost, boolean pushMsg, String type, Map<String, Long> num) throws WebServiceException {
        if (uid == null && goldCost == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            // 判断余额是否足够
            if (purseDto.getGoldNum() < goldCost) {
                throw new WebServiceException(WebServiceCode.PURSE_MONEY_NOT_ENOUGH);
            }
            int updateCount = purseDao.updateGoldCost(uid, goldCost.longValue());
            if (updateCount <= 0) {
                throw new WebServiceException(WebServiceCode.PURSE_MONEY_NOT_ENOUGH);
            }
            long goldBefore = purseDto.getGoldNum();
            purseDto.setChargeGoldNum(purseDto.getChargeGoldNum() - goldCost);
            purseDto.setGoldNum(purseDto.getGoldNum() - goldCost);

            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少金币 ]uid:>{}, 减少金币类型:>{} , 减少前:>{},减少后:>{}", uid, type, goldBefore, purseDto.getGoldNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：减少金币", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());

            if (num != null) {
                num.put("reduceBeforeGoldNum", goldBefore);
                num.put("reduceAfterGoldNum", purseDto.getGoldNum());
            }

            if (pushMsg) {
                pushMsg(purseDto);
            }
        } catch (WebServiceException e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }


    }

    @Override
    public void reduceGoldCache(Long uid, Long goldCost, boolean pushMsg) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(String.valueOf(uid)), 10 * 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            long balance = purseDto.getGoldNum() - goldCost;
            // 判断余额是否足够
            if (balance < 0) {
                throw new WebServiceException(WebServiceCode.PURSE_MONEY_NOT_ENOUGH);
            }
            long goldBefore = purseDto.getGoldNum();
            purseDto.setChargeGoldNum(purseDto.getChargeGoldNum() - goldCost);
            purseDto.setGoldNum(balance);
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少缓存金币 ] uid:>{},减少前:>{},减少后:>{}", uid, goldBefore, purseDto.getGoldNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：减少缓存金币", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    @Override
    public void updateReduceGoldAddConch(Long uid, Long goldCost, Long conchAmount, boolean pushMsg) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(String.valueOf(uid)), 10 * 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            long balance = purseDto.getGoldNum() - goldCost;
            // 判断余额是否足够
            if (balance < 0) {
                throw new WebServiceException(WebServiceCode.PURSE_MONEY_NOT_ENOUGH);
            }

            // 判断海螺次数是否正确
            if (conchAmount <= 0) {
                logger.info("[ 捡海螺次数异常 ] uid:>{}, conchNum :>{}", uid, conchAmount);
                throw new WebServiceException(WebServiceCode.PURSE_CONCH_NUM_ERROR);
            }

            long goldBefore = purseDto.getGoldNum();
            long conchBefore = purseDto.getConchNum();
            purseDto.setChargeGoldNum(purseDto.getChargeGoldNum() - goldCost);
            purseDto.setGoldNum(balance);
            purseDto.setConchNum(purseDto.getConchNum() + conchAmount);

            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少缓存金币 ] uid:>{},减少前:>{},减少后:>{}", uid, goldBefore, purseDto.getGoldNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：减少缓存金币", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            logger.info("[ 增加海螺次数 ] uid:>{}, 增加前:>{}, 增加后:>{}", uid, conchBefore, purseDto.getConchNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    @Override
    public void updateReduceTryCoin(Long uid, Integer tryCoinCost, boolean pushMsg, String type, Map<String, Long> num) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(String.valueOf(uid)), 10 * 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            long curTrycoinNum = purseDto.getTrycoinNum() - tryCoinCost;
            // 判断海螺次数是否充足
            if (curTrycoinNum < 0) {
                throw new WebServiceException(WebServiceCode.PURSE_CONCH_NOT_ENOUGH);
            }

            Long trycoinBefore = purseDto.getTrycoinNum();
            purseDto.setTrycoinNum(purseDto.getTrycoinNum() - tryCoinCost);
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少体验币次数 ] uid:>{},减少前:>{},减少后:>{} 减少类型:>{}", uid, trycoinBefore, purseDto.getTrycoinNum(), type);

            if (num != null){
                num.put("reduceAfterConchNum", purseDto.getConchNum());
                num.put("reduceAfterTryCoinNum", purseDto.getTrycoinNum());
            }

            if (pushMsg) {
                pushMsg(purseDto);
            }
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    @Override
    public void updateReduceConch(Long uid, Integer conchCost, boolean pushMsg, String type, Map<String, Long> num) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(String.valueOf(uid)), 10 * 1000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            long curConchNum = purseDto.getConchNum() - conchCost;
            // 判断海螺次数是否充足
            if (curConchNum < 0) {
                throw new WebServiceException(WebServiceCode.PURSE_CONCH_NOT_ENOUGH);
            }

            Long conchBefore = purseDto.getConchNum();
            purseDto.setConchNum(purseDto.getConchNum() - conchCost);
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少海螺次数 ] uid:>{},减少前:>{},减少后:>{} 减少类型:>{}", uid, conchBefore, purseDto.getConchNum(), type);

            if (num != null){
                num.put("reduceAfterConchNum", purseDto.getConchNum());
                num.put("reduceAfterTryCoinNum", purseDto.getTrycoinNum());
            }

            if (pushMsg) {
                pushMsg(purseDto);
            }
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    @Override
    public void updateAddDiamond(Long uid, Double diamondAmount, boolean pushMsg) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            int updateCount = purseDao.updateDiamondAmount(uid, diamondAmount);
            if (updateCount <= 0) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            double diamondBefore = purseDto.getDiamondNum();
            purseDto.setDiamondNum(Double.valueOf(DOUBLE_FORMAT.format(purseDto.getDiamondNum() + diamondAmount)));

            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 增加钻石 ] uid:>{} , 增加前:>{}，增加后:>{}", uid, diamondBefore, purseDto.getDiamondNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：增加钻石", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }

    }

    @Override
    public void updateReduceDiamond(Long uid, Double diamondCost, boolean pushMsg) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }

        String lockVal = redisManager.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            UserPurseDTO purseDto = this.getUserPurse(uid);
            if (purseDto == null) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            // 判断余额是否足够
            if (purseDto.getDiamondNum() < diamondCost) {
                throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
            }
            int updateCount = purseDao.updateDiamondCost(uid, diamondCost);
            if (updateCount <= 0) {
                throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
            }
            double diamondBefore = purseDto.getDiamondNum();
            purseDto.setDiamondNum(Double.valueOf(DOUBLE_FORMAT.format(purseDto.getDiamondNum() - diamondCost)));
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            logger.info("[ 减少钻石 ] uid:>{},  减少前:>{}, 减少后:>{}", uid, diamondBefore, purseDto.getDiamondNum());
            logger.info("[ 用户余额 ] uid:>{},diamondNum:>{}, goldNum:>{}, 类型：减少钻石", uid, purseDto.getDiamondNum(), purseDto.getGoldNum());
            if (pushMsg) {
                pushMsg(purseDto);
            }
        } catch (WebServiceException e) {
            throw e;
        } finally {
            redisManager.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }

    }

    /**
     * @see com.juxiao.xchat.manager.common.user.UserPurseManager#getUserPurse(Long)
     */
    @Override
    public UserPurseDTO getUserPurse(Long uid) {
        String purseStr = redisManager.hget(RedisKey.user_purse.getKey(), uid.toString());
        if (!StringUtils.isEmpty(purseStr)) {
            return gson.fromJson(purseStr, UserPurseDTO.class);
        }
        UserPurseDTO purseDto = purseDao.getUserPurse(uid);
        if (purseDto != null) {
            purseDto.setDiamondNum(Double.valueOf(DOUBLE_FORMAT.format(purseDto.getDiamondNum())));
            redisManager.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(purseDto));
            return purseDto;
        }
        UserPurseDO purseDo = new UserPurseDO();
        purseDo.setUid(uid);
        purseDo.setGoldNum(0L);
        purseDo.setConchNum(0L);
        purseDo.setTrycoinNum(0L);
        purseDo.setDiamondNum(0.00);
        purseDo.setNobleGoldNum(0L);
        purseDo.setChargeGoldNum(0L);
        purseDo.setDepositNum(0L);
        purseDo.setIsFirstCharge(true);
        purseDo.setUpdateTime(new Date());
        this.save(purseDo);

        purseDto = new UserPurseDTO();
        BeanUtils.copyProperties(purseDo, purseDto);
        return purseDto;
    }

    @Async
    void pushMsg(UserPurseDTO purseDto) {
        try {
            NeteasePushBO pushBo = new NeteasePushBO();
            UserPurseVO purseVo = new UserPurseVO();
            BeanUtils.copyProperties(purseDto, purseVo);
            pushBo.setFrom(systemConf.getSecretaryUid());
            pushBo.setTo(String.valueOf(purseDto.getUid()));
            pushBo.setMsgtype(0);
            pushBo.setSave(2); // 默认存离线
            pushBo.setAttach(new Attach(DefMsgType.Purse, DefMsgType.PurseGoldMinus, purseVo));
            neteaseMsgManager.sendAttachMsg(pushBo);
        } catch (Exception e) {
            logger.error("[ 发送用户钱包失败 ]", e);
        }
    }
}
