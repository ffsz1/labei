package com.erban.main.service.user;

import com.erban.main.model.UserPurse;
import com.erban.main.mybatismapper.UserPurseMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.common.JedisLockService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户钱包服务，统一用户的充值、扣钱操作
 */
@Service
public class UserPurseUpdateService extends BaseService {
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseMapperMgr userPurseMapperMgr;
    @Autowired
    private JedisLockService jedisLockService;


    /**
     * 扣减DB钱包的普通金币，成功返回1，否则返回0
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int reduceChargeGoldNumFromDB(Long uid, Long goldNum) {
        return userPurseMapperMgr.updateMinusChargeGold(uid, goldNum);
    }

    public int reduceNobleGoldNumFromDB(Long uid, Long goldNum) {
        return userPurseMapperMgr.updateMinusNobleGold(uid, goldNum);
    }

    /**
     * 扣减DB钱包的金币，成功返回1，否则返回0
     * 先扣减贵族金币，贵族金币不足时，再扣减普通金币
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int reduceGoldNumFromDB(Long uid, Long goldNum) {
        UserPurse userPurse = userPurseService.getUserPurseFromDb(uid);
        if (userPurse.getNobleGoldNum() == 0) {
            return reduceChargeGoldNumFromDB(uid, goldNum);
        } else if (userPurse.getNobleGoldNum() >= goldNum) {
            return reduceNobleGoldNumFromDB(uid, goldNum);
        } else {
            return userPurseMapperMgr.updateMinusGold(uid, goldNum, goldNum - userPurse.getNobleGoldNum()
                    , userPurse.getNobleGoldNum());
        }
    }

    /**
     * 增加DB钱包的钻石，成功返回1，否则返回0
     *
     * @param uid
     * @param diamoudNum
     * @return
     */
    public int addDiamondNumFromDB(Long uid, Double diamoudNum) {
        return userPurseMapperMgr.updateAddDiamond(uid, diamoudNum);
    }

    /**
     * 增加DB钱包的金币，成功返回1，否则返回0
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int addChargeGoldNumFromDB(Long uid, Long goldNum) {
        return userPurseMapperMgr.updateAddChargeGold(uid, goldNum);
    }

    /**
     * 增加DB钱包的贵族金币，成功返回1，否则返回0
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int addNobleGoldNumFromDB(Long uid, Long goldNum) {
        return userPurseMapperMgr.updateAddNobleGold(uid, goldNum);
    }

    /**
     * 充值金币统一入口 ，成功返回用户的钱包信息（分布式下加分布式锁）
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public UserPurse addGoldFromCache(Long uid, Long goldNum) {
        String lockVal = null;
        UserPurse userPurse = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return null;
            }
            userPurse = userPurseService.getPurseByUid(uid);
            userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() + goldNum);
            userPurseService.saveUserPurseCache(userPurse);
            return userPurse;
        } catch (Exception e) {
            throw e;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    public UserPurse addGoldDbAndCache(Long uid, Long goldNum) {
        UserPurse userPurse = addGoldFromCache(uid, goldNum);
        addChargeGoldNumFromDB(uid, goldNum);
        return userPurse;
    }

    /**
     * 增加钻石，更新DB和缓存
     *
     * @param uid
     * @param diamoudNum
     * @return
     */
    public int addDiamondDbAndCache(Long uid, Double diamoudNum) {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            int result = addDiamondNumFromDB(uid, diamoudNum);
            if (result != 1) {
                return 500;
            }
            userPurse.setDiamondNum(userPurse.getDiamondNum() + diamoudNum);
            userPurseService.saveUserPurseCache(userPurse);
        } catch (Exception e) {
            logger.error("addDiamondDbAndCache uid:" + uid + ", diamoudNum: " + diamoudNum, e);
            return 503;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        return 200;
    }

    /**
     * 增加充值金币
     *
     * @param uid
     * @param goldNum
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public int addChargeGoldDbAndCache(Long uid, Long goldNum) throws Exception {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            // 增加DB的贵族金币
            int result = addChargeGoldNumFromDB(uid, goldNum);
            if (result != 1) {
                return 500;
            }
            // 增加缓存贵族金币
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() + goldNum);
            userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
            userPurse.setIsFirstCharge(false);
            userPurseService.saveUserPurseCache(userPurse);
        } catch (Exception e) {
            logger.error("addChargeGoldDbAndCache uid:" + uid + ", goldNum: " + goldNum, e);
            throw new Exception("addChargeGoldDbAndCache error , uid:" + uid + ", goldNum: " + goldNum, e);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        return 200;
    }

    /**
     * 增加充值金币
     *
     * @param uid
     * @param goldNum
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public UserPurse addChargeGoldDbAndCache2(Long uid, Long goldNum) throws Exception {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return null;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            logger.info("Before user purse gold is : " + userPurse.getGoldNum());
            // 增加DB的贵族金币
            int result = addChargeGoldNumFromDB(uid, goldNum);
            if (result != 1) {
                return null;
            }
            // 增加缓存贵族金币
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() + goldNum);
            userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
            userPurse.setIsFirstCharge(false);//不再是首次充值
            userPurseService.saveUserPurseCache(userPurse);
            logger.info("After user purse gold is : " + userPurse.getGoldNum());
            return userPurse;
        } catch (Exception e) {
            logger.error("addChargeGoldDbAndCache uid:" + uid + ", goldNum: " + goldNum, e);
            throw new Exception("addChargeGoldDbAndCache error , uid:" + uid + ", goldNum: " + goldNum, e);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    /**
     * 增加贵族金币
     *
     * @param uid
     * @param goldNum
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public int addNobleGoldDbAndCache(Long uid, Long goldNum) throws Exception {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            // 增加DB的贵族金币
            int result = addNobleGoldNumFromDB(uid, goldNum);
            if (result != 1) {
                return 500;
            }
            // 增加缓存贵族金币
            userPurse.setNobleGoldNum(userPurse.getNobleGoldNum() + goldNum);
            userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
            userPurseService.saveUserPurseCache(userPurse);
        } catch (Exception e) {
            logger.error("addNobleGoldDbAndCache uid:" + uid + ", goldNum: " + goldNum, e);
            throw new Exception("addNobleGoldDbAndCache error , uid:" + uid + ", goldNum: " + goldNum, e);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        return 200;
    }

    /**
     * 减少普通金币
     *
     * @param uid
     * @param goldNum
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public int reduceChargeGoldDbAndCache(Long uid, Long goldNum) throws Exception {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            // 判断余额是否足够
            if (userPurse.getChargeGoldNum() - goldNum < 0 || userPurse.getGoldNum() - goldNum < 0) {
                return 403;
            }
            // 减少DB的金币
            int result = reduceGoldNumFromDB(uid, goldNum);
            if (result != 1) {
                return 403;
            }
            // 减少cache金币
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() - goldNum);
            userPurse.setGoldNum(userPurse.getGoldNum() - goldNum);
            userPurseService.saveUserPurseCache(userPurse);
        } catch (Exception e) {
            logger.error("reduceGoldDbAndCache uid:" + uid + ", goldNum: " + goldNum, e);
            throw new Exception("reduceGoldDbAndCache error , uid:" + uid + ", goldNum: " + goldNum, e);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        return 200;
    }

    /**
     * 扣减Cache钱包的金币，成功返回用户的钱包信息（分布式下加分布式锁）
     * 先扣减贵族金币，贵族金币不足时再扣减普通金币
     * 余额不足时不允许扣减
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int reduceGoldFromCache(Long uid, Long goldNum) {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long balance = userPurse.getGoldNum() - goldNum;
            // 判断余额是否足够
            if (userPurse.getGoldNum() - goldNum < 0) {
                return 403;
            }
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() - goldNum);
            userPurse.setGoldNum(balance);
            userPurseService.saveUserPurseCache(userPurse);
            return 200;
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid + ", goldNum: " + goldNum, e);
            return 500;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    /**
     * 扣减Cache钱包的充值金币（分布式下加分布式锁）
     * 余额不足时不允许扣减
     *
     * @param uid
     * @param goldNum
     * @return
     */
    public int reduceChargeGoldFromCache(Long uid, Long goldNum) {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long balance = userPurse.getChargeGoldNum() - goldNum;
            // 判断余额是否足够
            if (balance < 0) {
                return 403;
            }
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() - goldNum);
            userPurse.setGoldNum(userPurse.getGoldNum() - goldNum);
            userPurseService.saveUserPurseCache(userPurse);
            return 200;
        } catch (Exception e) {
            logger.error("reduceChargeGoldFromCache uid:" + uid + ", goldNum: " + goldNum, e);
            return 500;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }

    /**
     * 兑换钻石为金币
     *
     * @param uid
     * @param diamonNum
     * @return
     */
    public int exchageDiamondToChargeGold(UserPurse userPurse, Long uid, Double diamonNum, Long goldNum) {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (BlankUtil.isBlank(lockVal)) {
                return 503;
            }
            //更新数据库
            int result = userPurseMapperMgr.exchageDiamondToChargeGold(uid, diamonNum, goldNum);
            if (result != 1) {
                return 403;
            }
            //更新缓存
            userPurseService.saveUserPurseCache(userPurse);
            return 200;
        } catch (Exception e) {
            logger.error("exchageDiamondToChargeGold uid:" + uid + ", goldNum: " + uid, e);
            return 500;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
    }
}
