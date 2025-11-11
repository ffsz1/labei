package com.xchat.common.redis;


import redis.clients.jedis.Jedis;

/**
 * https://github.com/beyondfengyu/DistributedLock
 * 使用redis实现分布式锁（推荐）
 *
 * @author beyond
 * @version 1.0
 * @date 2016/12/1
 */
public class JedisLock {

    private static final String LOCK_KEY = "jedis_lock";
    private static final int RETRY_TIME  = 10 * 1000;//等待锁的时间
    private static final int EXPIRE_TIME = 60 * 1000;//锁超时的时间
    private boolean locked;
    private long lockValue;
    private Jedis jedis;

    public JedisLock(){

    }

    public JedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public synchronized boolean lock(){
        return lock(this.jedis);
    }

    public synchronized void unlock(){
        unlock(this.jedis);
    }

    public synchronized boolean lock(Jedis jedis){
        int retryTime = RETRY_TIME;
        try {
            while (retryTime > 0) {
                lockValue = System.nanoTime();
                if ("OK".equalsIgnoreCase(jedis.set(LOCK_KEY, String.valueOf(lockValue), "NX", "PX", EXPIRE_TIME))) {
                    locked = true;
                    return locked;
                }
                retryTime -= 100;
                Thread.sleep(100);
            }
        } catch (Exception e) { }
        return false;
    }

    public synchronized void unlock(Jedis jedis){
        if(locked) {
            String currLockVal = jedis.get(LOCK_KEY);
            if(currLockVal!=null && Long.valueOf(currLockVal) == lockValue){
                jedis.del(LOCK_KEY);
                locked = false;
            }
        }
    }

}
