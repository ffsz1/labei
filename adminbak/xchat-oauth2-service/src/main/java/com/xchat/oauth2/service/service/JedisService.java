package com.xchat.oauth2.service.service;

import com.google.common.collect.Lists;
import com.xchat.common.redis.JedisLock;
import com.xchat.common.redis.JedisPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liuguofu
 * on 10/29/14.
 */
@Service
public class JedisService {

    private static final Logger LOG = LoggerFactory.getLogger(JedisService.class);

    @Autowired
    @Qualifier("readJedisPoolManager")
    private JedisPoolManager readJedisPoolManager;

    @Autowired
    @Qualifier("writeJedisPoolManager")
    private JedisPoolManager writeJedisPoolManager;

    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;


    private JedisLock jedisLock;

    private String lockKey;

    private int expireMsecs = 5 * 1000;

    private int timeoutMsecs = 5 * 1000;

    private volatile boolean locked = false;

    public String getLockKey() {
        return lockKey;
    }


    public synchronized boolean lock(String lockKeys, int timeoutMsecss, int expireMsecss) throws InterruptedException {
        this.lockKey = lockKeys + "_lock";
        this.timeoutMsecs = timeoutMsecss;
        this.expireMsecs = expireMsecss;
        int timeout = timeoutMsecs;
        Jedis rjedis = readJedisPoolManager.getJedis();
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (rjedis.setnx(lockKey, expiresStr) == 1) {
                // lock acquired
                locked = true;
                return true;
            }
            String currentValueStr = rjedis.get(lockKey); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired
                String oldValueStr = rjedis.getSet(lockKey, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受

                    //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
            timeout = DEFAULT_ACQUIRY_RESOLUTION_MILLIS;

            /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);

        }
        return false;
    }

    public synchronized void unlock(String lockKeys) {
        this.lockKey = lockKeys + "_lock";
        Jedis rjedis = readJedisPoolManager.getJedis();
        if (locked) {
            rjedis.del(lockKey);
            locked = false;
        }
    }


    public byte[] readByte(String key) throws RedisDataAccessException {
        byte[] ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            byte[] jedisKey = key.getBytes();
            byte[] content = rjedis.get(jedisKey);
            if (content != null && content.length > 0) {
                ret = content;
            }
        } catch (Exception e) {
            LOG.error("read from jedis error. key:{} msg:{}", key, e);
            throw new RedisDataAccessException("redis read error.", e);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    /**
     * 写入redis，并指定失效时间点
     *
     * @param key
     * @param content  数据
     * @param deadLine 失效时间点
     */
    public void write(String key, String content, Date deadLine) {
        Jedis wjedis = null;
        long now = System.currentTimeMillis();
        long dead = deadLine.getTime();
        int expireTime = (int) (dead - now) / (1000 * 60);//转换为分钟
        if (expireTime <= 0) {
            LOG.warn("request ignored .Date:{} msg:{}", new Object[]{deadLine, " invalid deadLine:The deadLine must be one minute later than currentTime "});
            return;
        } else {
            try {
                wjedis = writeJedisPoolManager.getJedis();
                byte[] data = null;
                if (content != null) {
                    data = content.getBytes();
                }
                byte[] jedisKey = key.getBytes();
                wjedis.setex(jedisKey, expireTime, data);
            } catch (Exception e) {
//                LOG.error("write to jedis error. key:{} data:{} msg:{}", key, content, e);
                throw new RedisDataAccessException("redis read error.", e);
            } finally {
                if (wjedis != null) {
                    writeJedisPoolManager.returnJedis(wjedis);
                }
            }
        }
    }

    public void write(String key, byte[] content, int expireTime) throws RedisDataAccessException {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            byte[] data = content;
            byte[] jedisKey = key.getBytes();
            wjedis.setex(jedisKey, expireTime, data);
        } catch (Exception e) {
//            LOG.error("write to jedis error. key:{} data:{} msg:{}", key, content, e);
            throw new RedisDataAccessException("Failed to write key " + key, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    /**
     * 写入redis，并指定失效时间点
     *
     * @param key
     * @param content    数据
     * @param expireTime 失效时长(秒)
     */
    public void write(String key, String content, int expireTime) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            byte[] data = null;
            if (content != null) {
                data = content.getBytes();
            }
            byte[] jedisKey = key.getBytes();

            wjedis.setex(jedisKey, expireTime, data);
        } catch (Exception e) {
//            LOG.error("write to jedis error. key:{} data:{} msg:{}", key, content, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void set(String key, String content) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.set(key, content);
        } catch (Exception e) {
            LOG.error("set to jedis error. key:{} data:{} msg:{}", key, content, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void set(String key, String content, int expire) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.set(key, content);
            if (expire >= 0) {
                wjedis.expire(key, expire);
            }
        } catch (Exception e) {
            LOG.error("set to jedis error. key:{} data:{} msg:{}", key, content, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void del(String key) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.del(key);
        } catch (Exception e) {
            LOG.error("delete from jedis error. key:{}", key);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public String get(String key) {
        String ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.get(key);

        } catch (Exception e) {
            LOG.error("get from jedis error. key:{}", key);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public String read(String key) {
        String ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.get(key);
        } catch (Exception e) {
            LOG.error("hget from jedis error. key:{} field:{}", key);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public Long incr(String key) {
        Long wet = null;
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wet = wjedis.incr(key);
        } catch (Exception e) {
            LOG.error("incr from jedis error. key:{}", key);
        } finally {
            if (wjedis != null) {
                readJedisPoolManager.returnJedis(wjedis);
            }
        }
        return wet;
    }

    public Long incrBy(String key, long inet) {
        Long ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.incrBy(key, inet);
        } catch (Exception e) {
            LOG.error("incrBy from jedis error. key:{} inet:{}", key, inet);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public Long incr(String key, int time) {
        Long wet = null;
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wet = wjedis.incr(key);
            wjedis.expire(key, time);
        } catch (Exception e) {
            LOG.error("incr from jedis error. key:{}", key);
        } finally {
            if (wjedis != null) {
                readJedisPoolManager.returnJedis(wjedis);
            }
        }
        return wet;
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param seconds 过期时间 秒
     * @return
     */
    public Long expire(String key, int seconds) {
        Long ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.expire(key, seconds);
        } catch (Exception e) {
            LOG.error("expire from jedis error. key:{} seconds:{}", key, seconds);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public long lpush(String key, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.lpush(key, value);
        } catch (Exception e) {
            LOG.error("lpush from jedis error. key:{}  value:{}", key, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return 0L;
    }

    public long rpush(String key, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.rpush(key, value);
        } catch (Exception e) {
            LOG.error("rpush from jedis error. key:{}  value:{}", key, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return 0L;
    }

//    public long lpushx(String key, String... values) {
//        Jedis wjedis = null;
//        try {
//            wjedis = writeJedisPoolManager.getJedis();
//            return wjedis.lpushx(key, values);
//        } catch (Exception e) {
//            LOG.error("lpushx from jedis error. key:{}  value:{}", key, values);
//        } finally {
//            if (wjedis != null) {
//                writeJedisPoolManager.returnJedis(wjedis);
//            }
//        }
//        return 0L;
//    }

    public List<String> lrange(String key, int start, int end) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.lrange(key, start, end);
        } catch (Exception e) {
            LOG.error("lrange from jedis error. key:{}  start:{}  end:{}", key, start, end);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return Lists.newArrayList();
    }

    public String ltrim(String key, int start, int end) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.ltrim(key, start, end);
        } catch (Exception e) {
            LOG.error("ltrim from jedis error. key:{}  start:{}  end:{}", key, start, end);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return "";
    }

    public void rpop(String key) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.rpop(key);
        } catch (Exception e) {
            LOG.error("rpop from jedis error. key:{}  value:{}", key);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public long size(String key) {
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            return rjedis.llen(key);
        } catch (Exception e) {
            LOG.error("hget from jedis error. key=" + key, e);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return 0;
    }

    public String hget(String key, String field) {
        String ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.hget(key, field);
        } catch (Exception e) {
            LOG.error("hget from jedis error. key=" + key + "field=" + field, e);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public void hset(String key, String field, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hset(key, field, value);
        } catch (Exception e) {
            LOG.error("hset from jedis error. key:{} field:{} value:{}", key, field, value, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public List<String> hmread(String key, String... field) {
        List<String> ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.hmget(key, field);
        } catch (Exception e) {
            LOG.error("hmread from jedis error. key=" + key + "field=" + field, e);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.hgetAll(key);
        } catch (Exception e) {
            LOG.error("hget from jedis error. key:{}", key);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    /**
     * 获取hash的字段数量
     * @param key
     * @return
     */
    public long hlen(String key) {
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            return rjedis.hlen(key);
        } catch (Exception e) {
            LOG.error("hget from jedis error. key=" + key, e);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return 0;
    }

    public Set<String> hgetAllKeysByKey(String key) {
        Set<String> ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.hkeys(key);
        } catch (Exception e) {
            LOG.error("hreadAllByKey from jedis error. key:{}", key);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public Map<String, String> hgetAllBykey(String key) {
        Map<String, String> ret = null;
        Jedis rjedis = null;
        try {
            rjedis = readJedisPoolManager.getJedis();
            ret = rjedis.hgetAll(key);
        } catch (Exception e) {
            LOG.error("hreadAllByKey from jedis error. key:{}", key);
        } finally {
            if (rjedis != null) {
                readJedisPoolManager.returnJedis(rjedis);
            }
        }
        return ret;
    }

    public void hwrite(String key, String field, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hset(key, field, value);
        } catch (Exception e) {
            LOG.error("hwrite from jedis error. key:{} field:{} value:{}", key, field, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hdelete(String key, String field, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hdel(key, field);
        } catch (Exception e) {
            LOG.error("hdelete from jedis error. key:{} field:{} value:{}", key, field, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hdel(String key, String field) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hdel(key, field);
        } catch (Exception e) {
            LOG.error("hdel from jedis error. key:{} field:{} value:{}", key, field);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hincr(String key, String field) {
        hincrBy(key, field, 1L);
    }

    public void hincrBy(String key, String field, Long value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hincrBy(key, field, value);
        } catch (Exception e) {
            LOG.error("hincrBy from jedis error. key:{} field:{} value:{}", key, field, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hdeleteKey(String key) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.del(key);
        } catch (Exception e) {
            LOG.error("delete from jedis error. key:{} field:{} value:{}", key);
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hwrite(String key, Map<String, String> value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hmset(key, value);
        } catch (Exception e) {
            LOG.error("hwrite from jedis error. key:{} value:{}", key, value);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void disableCache(String key) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.expire(key, 0);
        } catch (Exception e) {
            LOG.error("disableCache error. key:{} msg:{}", key, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void remove(String key) throws RedisDataAccessException {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.del(key);
        } catch (Exception e) {
            LOG.error("remove error. key:{} msg:{}", key, e);
            throw new RedisDataAccessException("Failed to remove key " + key, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

    public void hincrbyfloat(String key, String property, double value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            wjedis.hincrByFloat(key, property, value);
        } catch (Exception e) {
            LOG.error("hincrbyfloat from jedis error. key:=" + key + "&value=" + value, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }


    public String lockWithTimeout(String locaName,
                                  long acquireTimeout, long timeout) {
        Jedis conn = null;
        String retIdentifier = null;
        String lockKey = null;
        try {
            // 获取连接
            conn = writeJedisPoolManager.getJedis();
            // 随机生成一个value
            String identifier = UUID.randomUUID().toString();
            // 锁名，即key值
            lockKey = "lock:" + locaName;
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (int) (timeout / 1000);

            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (conn.setnx(lockKey, identifier) == 1) {
                    conn.expire(lockKey, lockExpire);
                    // 返回value值，用于释放锁时间确认
                    retIdentifier = identifier;
                    return retIdentifier;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (conn.ttl(lockKey) == -1) {
                    conn.expire(lockKey, lockExpire);
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            LOG.info("lock name: {}", lockKey);
            if (conn != null) {
                conn.close();
            }
        }
        return retIdentifier;
    }

    /**
     * 释放锁
     *
     * @param lockName   锁的key
     * @param identifier 释放锁的标识
     * @return
     */
    public boolean releaseLock(String lockName, String identifier) {
        Jedis conn = null;
        String lockKey = "lock:" + lockName;
        boolean retFlag = false;
        try {
            conn = writeJedisPoolManager.getJedis();
            while (true) {
                // 监视lock，准备开始事务
                conn.watch(lockKey);
                // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                if (identifier.equals(conn.get(lockKey))) {
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
//            LOG.info("锁名称：" + lockKey);
            if (conn != null) {
                conn.close();
            }
        }
        return retFlag;
    }

    /**
     * zset 添加元素
     * @param key
     * @param value
     * @param source
     */
    public Double zincrby(String key, String value, double source) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.zincrby(key, source, value);
        } catch (Exception e) {
            LOG.error("zincrby from jedis error. key:=" + key + "&value=" + value, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return null;
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            LOG.error("zrangeWithScores from jedis error. key:=" + key + "&start=" + start + "end=" + end, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return null;
    }

    /**
     *
     * @param key
     * @param start
     * @param end
     */
    public Set<Tuple> zrangeWithScores(String key, long start, long end){
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            LOG.error("zrangeWithScores from jedis error. key:=" + key + "&start=" + start + "end=" + end, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return null;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Long zrem(String key, String... value){
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.zrem(key, value);
        } catch (Exception e) {
            LOG.error("zrem from jedis error. key:=" + key + "&value=" + value, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return null;
    }

    /**
     * 获取排序的source
     * @param key
     * @param value
     * @return
     */
    public Double zscore(String key, String value) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            return wjedis.zscore(key, value);
        } catch (Exception e) {
            LOG.error("zscore from jedis error. key:=" + key + "&value=" + value, e);
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
        return null;
    }
    
    /**
           * 删除指定前缀的key
     * @param key
     */
    public void delKeys(String keyStr) {
        Jedis wjedis = null;
        try {
            wjedis = writeJedisPoolManager.getJedis();
            Set<String> keys =wjedis.keys(keyStr+"*");
            for(String key:keys) {
            	 wjedis.del(key);
            }
        } catch (Exception e) {
            LOG.error("delete from jedis error. keys:{}", keyStr+"*");
        } finally {
            if (wjedis != null) {
                writeJedisPoolManager.returnJedis(wjedis);
            }
        }
    }

}
