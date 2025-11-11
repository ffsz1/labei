package com.juxiao.xchat.manager.cache.redis.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存通用实现类
 *
 * @class: RedisManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@Service
public class RedisManagerImpl implements RedisManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ThreadLocal<Integer> database = new ThreadLocal<>();

    /**
     * @see com.juxiao.xchat.manager.cache.redis.RedisManager#hget(String, String)
     */
    @Override
    public String hget(String key, String field) {
        Integer dbindex = database.get();
        if (dbindex == null) {
            BoundHashOperations<String, String, String> operation = redisTemplate.boundHashOps(key);
            return operation.get(field);
        }

        database.remove();
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.select(dbindex);
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value = connection.hGet(serializer.serialize(key), serializer.serialize(field));
            return serializer.deserialize(value);
        });
    }

    @Override
    public String setnx(String lockKey, int lockTimeout) {
        try {
                //锁时间
                Long lock_timeout = currtTimeForRedis() + lockTimeout + 1;
                if (redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                        //定义序列化方式
                        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                        byte[] value = serializer.serialize(lock_timeout.toString());
                        boolean flag = redisConnection.setNX(lockKey.getBytes(), value);
                        return flag;
                    }
                })) {
                    //设置超时时间，释放内存
                    redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                    return lock_timeout + "";
                } else {
                    //获取redis里面的时间
                    String result = redisTemplate.opsForValue().get(lockKey);
                    Long currt_lock_timeout_str = result == null ? null : Long.parseLong(result);
                    //锁已经失效
                    if (currt_lock_timeout_str != null && currt_lock_timeout_str < System.currentTimeMillis()) {
                        //判断是否为空，不为空时，说明已经失效，如果被其他线程设置了值，则第二个条件判断无法执行
                        //获取上一个锁到期时间，并设置现在的锁到期时间
                        Long old_lock_timeout_Str = Long.valueOf(redisTemplate.opsForValue().getAndSet(lockKey, lock_timeout.toString()));
                        if (old_lock_timeout_Str != null && old_lock_timeout_Str.equals(currt_lock_timeout_str)) {
                            //设置超时间，释放内存
                            redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                            //返回加锁时间
                            return lock_timeout + "";
                        }
                    }
                }
        } catch (Exception e) {
            logger.error("[ redis锁报错 ] , lockKey:" + lockKey + ", waitTime:" + RETRY_TIME + ", timeout:" + lockTimeout, e);
        }
        return null;
    }

    /**
     * 根据key值获取Map
     *
     * @param key
     * @return
     * @author: chenjunsheng
     * @date 2018/6/11
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        Integer dbindex = database.get();
        if (dbindex == null) {
            BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(key);
            return operations.entries();
        }

        database.remove();
        return redisTemplate.execute((RedisCallback<Map<String, String>>) connection -> {
            connection.select(dbindex);

            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            Map<byte[], byte[]> map = connection.hGetAll(serializer.serialize(key));
            if (map.size() == 0) {
                return Maps.newHashMap();
            }

            Set<Map.Entry<byte[], byte[]>> entries = map.entrySet();
            if (entries.size() == 0) {
                return Maps.newHashMap();
            }

            Map<String, String> result = Maps.newHashMap();
            Iterator<Map.Entry<byte[], byte[]>> iterator = entries.iterator();
            Map.Entry<byte[], byte[]> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (entry == null) {
                    continue;
                }
                result.put(serializer.deserialize(entry.getKey()), serializer.deserialize(entry.getValue()));
            }

            return result;
        });
    }

    /**
     * @see com.juxiao.xchat.manager.cache.redis.RedisManager#hset(String, String, String)
     */
    @Override
    public void hset(String key, String field, String value) {
        BoundHashOperations<String, String, String> operation = redisTemplate.boundHashOps(key);
        operation.put(field, value);
    }

    @Override
    public long incr(String key, long delta) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.addAndGet(delta);
    }

    @Override
    public long incrByTime(String key, int timeout) {
        long result = redisTemplate.boundValueOps(key).increment(1);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 在hashmap中，对某个field值进行加值运算
     *
     * @param key
     * @param field
     * @param value
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    @Override
    public Long hincrBy(String key, String field, Long value) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.increment(key, field, value);
    }

    @Override
    public void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, int time, TimeUnit unit) {
        BoundValueOperations<String, String> operations = redisTemplate.boundValueOps(key);
        operations.set(value, time, unit);
    }

    @Override
    public boolean del(String key) {
        return redisTemplate.delete(key);
    }

    // 等待锁的时间
    private static final int RETRY_TIME = 5000;

    @Override
    public String lock(String lockKey, int lockTimeout) {
        int retryTime = RETRY_TIME;
        try {
            while (retryTime > 0) {
                // 锁时间
                Long lock_timeout = currtTimeForRedis() + lockTimeout + 1;
                if (redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                        //定义序列化方式
                        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                        byte[] value = serializer.serialize(lock_timeout.toString());
                        boolean flag = redisConnection.setNX(lockKey.getBytes(), value);
                        return flag;
                    }
                })) {
                    // 设置超时时间, 释放内存
                    redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                    return lock_timeout + "";
                } else {
                    //获取redis里面的时间
                    String result = redisTemplate.opsForValue().get(lockKey);
                    Long currt_lock_timeout_str = result == null ? null : Long.parseLong(result);
                    //锁已经失效
                    if (currt_lock_timeout_str != null && currt_lock_timeout_str < System.currentTimeMillis()) {
                        //判断是否为空，不为空时，说明已经失效，如果被其他线程设置了值，则第二个条件判断无法执行
                        //获取上一个锁到期时间，并设置现在的锁到期时间
                        Long old_lock_timeout_Str = Long.valueOf(redisTemplate.opsForValue().getAndSet(lockKey, lock_timeout.toString()));
                        if (old_lock_timeout_Str != null && old_lock_timeout_Str.equals(currt_lock_timeout_str)) {
                            //设置超时间，释放内存
                            redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                            //返回加锁时间
                            return lock_timeout + "";
                        }
                    }
                }
                retryTime -= 100;
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error("[ redis锁报错 ] , lockKey:" + lockKey + ", waitTime:" + RETRY_TIME + ", timeout:" + lockTimeout, e);
        }
        return null;
    }

    @Override
    public void unlock(String lockKey, String lockValue) {
        //获取redis中设置的时间
        String result = redisTemplate.opsForValue().get(lockKey);
        //如果是加锁者，则删除锁， 如果不是，则等待自动过期，重新竞争加锁
        if (StringUtils.isNotEmpty(lockValue) && lockValue.equals(result)) {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public void expire(String key, int timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public long currtTimeForRedis() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }

    @Override
    public List<String> multiGet(String key, List<String> keys) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.multiGet(key, keys);
    }

    @Override
    public Long leftPushAll(String key, List<String> values) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.leftPushAll(key, values);
    }

    @Override
    public String lIndex(String key, long index) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.index(key, index);
    }

    @Override
    public List<String> range(String key, int start, int end) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, start, end);
    }

    @Override
    public long size(String key) {
        //
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public long lpush(String key, String value) {
        //
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public long rightPush(String key, String value) {

        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public void trim(String key, int start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Set<String> keys(String key) {
        if (StringUtils.isBlank(key)) {
            return Sets.newConcurrentHashSet();
        }
        return redisTemplate.keys(key);
    }

    @Override
    public void delete(Set<String> keys) {
        //
        redisTemplate.delete(keys);
    }

    @Override
    public Boolean zadd(String key, String field, double score){
        return redisTemplate.boundZSetOps(key).add(field, score);
    }

    @Override
    public Long zcount(String key, double minScore, double maxScore){
        return redisTemplate.boundZSetOps(key).count(minScore, maxScore);
    }

    @Override
    public Set<String> zrangeByScore(String key, double minScore, double maxScore, int offset, int count){
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore, offset, count);
    }

    @Override
    public List<String> hMget(String key, List<String> fields) {
        BoundHashOperations<String, String, String> operations = redisTemplate.boundHashOps(key);
        return operations.multiGet(fields);
    }

    @Override
    public Double zscore(String key, String object) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        return operations.score(object);
    }

    @Override
    public Long zrevrank(String key, String object) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        return operations.reverseRank(object);
    }

    @Override
    public Double zincrby(String key, String object, Double score) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        return operations.incrementScore(object.toString(), score);
    }

    @Override
    public Set<String> reverseZsetRange(String key, int start, int end) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        return operations.reverseRange(start, end);
    }

    @Override
    public void zadd(String key, double score, String value) {
        BoundZSetOperations<String, String> zsetOps = redisTemplate.boundZSetOps(key);
        zsetOps.add(value, score);
    }

    @Override
    public Set<String> zrangeByScore(String key, double minScore, double maxScore) {
        Integer dbindex = database.get();
        if (dbindex == null) {
            return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
        }

        database.remove();
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.select(dbindex);
            Set<byte[]> set = connection.zRangeByScore(serializer.serialize(key), minScore, maxScore);
            Set<String> result = Sets.newHashSet();
            for (byte[] item : set) {
                result.add(serializer.deserialize(item));
            }
            return result;
        });

    }

    @Override
    public boolean zrem(String key, String value) {
        BoundZSetOperations<String, String> zsetOps = redisTemplate.boundZSetOps(key);
        long result = zsetOps.remove(value);
        return result == 1;
    }

    @Override
    public Set<String> zrevRangeByScore(String key, double max, double min) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        return operations.reverseRangeByScore(min, max);
    }

    @Override
    public RedisManager select(int index) {
        database.set(0);
        return this;
    }

    @Override
    public Double zincrby(String key, double increment, String member) {
        BoundZSetOperations<String, String> zsetOps = redisTemplate.boundZSetOps(key);
        return zsetOps.incrementScore(member, increment);
    }

    @Override
    public void zremRangeByScore(String key, double min, double max) {
        BoundZSetOperations<String, String> operations = redisTemplate.boundZSetOps(key);
        operations.removeRangeByScore(min, max);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zrevrangeWithScores(String key, long start, long stop) {
        BoundZSetOperations<String, String> zsetOps = redisTemplate.boundZSetOps(key);
        return zsetOps.reverseRangeWithScores(start, stop);
    }


    /**
     * 递增
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        return increment;
    }
}
