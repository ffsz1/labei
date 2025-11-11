package com.erban.main.service.common;

import com.xchat.common.redis.JedisPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class JedisLockService {

    private static final Logger logger = LoggerFactory.getLogger(JedisLockService.class);

    @Autowired
    @Qualifier("writeJedisPoolManager")
    private JedisPoolManager writeJedisPoolManager;


    private static final String LOCK_KEY = "jedis_lock";
    private static final int RETRY_TIME = 5 * 1000;  //等待锁的时间
    private static final int EXPIRE_TIME = 5 * 1000; //锁超时的时间


    public String lock() {
        return lock(LOCK_KEY);
    }

    public String lock(String lockKey) {
        return lock(lockKey, RETRY_TIME);
    }

    public String lock(String lockKey, int waitTime) {
        return lock(lockKey, waitTime, EXPIRE_TIME);
    }

    public String lock(String lockKey, int waitTime, int timeout) {
        Jedis jedis = null;
        try {
            jedis = writeJedisPoolManager.getJedis();
            return lock(jedis, lockKey, waitTime, timeout);
        } catch (Exception e) {
            logger.error("lock error", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String lock(Jedis jedis, String lockKey, int waitTime, int timeout) {
        int retryTime = waitTime;
        try {
            long lockValue = 0;
            while (retryTime > 0) {
                lockValue = System.nanoTime();
                if ("OK".equalsIgnoreCase(jedis.set(lockKey, String.valueOf(lockValue), "NX", "PX", timeout))) {
                    return lockValue + "";
                }
                retryTime -= 100;
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error("lock error, lockKey:"+lockKey+", waitTime:"+waitTime+", timeout:"+timeout, e);
        }
        return null;
    }

    public void unlock(String lockKey, String lockVal) {
        Jedis jedis = null;
        try {
            jedis = writeJedisPoolManager.getJedis();
            unlock(jedis, lockKey, lockVal);
        } catch (Exception e) {
            logger.error("unlock error, lockKey:" + lockKey + ", lockVal:" + lockVal, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void unlock(Jedis jedis, String lockKey, String lockVal) {
        String currLockVal = jedis.get(lockKey);
        if (currLockVal != null && currLockVal.equals(lockVal)) {
            jedis.del(lockKey);
        }
    }
}
