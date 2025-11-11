package com.juxiao.xchat.manager.cache.redis;


import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存通用接口
 *
 * @class: RedisManager.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface RedisManager {
    /**
     * 根据key值和field值获取redis的hash值
     *
     * @param key
     * @param field
     * @return
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    String hget(String key, String field);

    /**
     * 根据key值获取Map
     *
     * @param key
     * @return
     * @author: chenjunsheng
     * @date 2018/6/11
     */
    Map<String, String> hgetAll(String key);

    /**
     * 根据Key的存在与否设置值
     * setnx[SET if Not eXists](如果不存在则设置KEY-VALUE, 相反则不作任何操作)
     *
     * @param lockKey     键名
     * @param lockTimeout 时长
     * @return
     */
    String setnx(String lockKey, int lockTimeout);

    /**
     * 保存value内容到相关的key和field中
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, String value);

    /**
     * 该key进行加值运算（原子）
     *
     * @param key
     * @param delta 增量
     * @return
     */
    long incr(String key, long delta);


    /**
     * 在指定时间内增加该key进行加值运算，过了timeout之后，key直接失效
     *
     * @param key
     * @param timeout
     * @return
     */
    long incrByTime(String key, int timeout);

    /**
     * 在hashmap中，对某个field值进行加值运算
     *
     * @param key
     * @param field
     * @param value
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    Long hincrBy(String key, String field, Long value);


    /**
     * 删除指定field的内容
     *
     * @param key
     * @param field
     */
    void hdel(String key, String field);

    /**
     * 根据key获取redis中的信息
     *
     * @param key redis中的key
     * @return
     */
    String get(String key);

    /**
     * 保存数据到指定的key中
     *
     * @param key   redis中的key
     * @param value 保存的数据
     */
    void set(String key, String value);

    /**
     * 保存数据到指定的key中
     *
     * @param key   redis中的key
     * @param value 保存的数据
     * @param time
     * @param unit
     */
    void set(String key, String value, int time, TimeUnit unit);

    /**
     * 根据key删除信息
     *
     * @param key 需要删除的key
     * @return
     */
    boolean del(String key);

    String lock(String lockKey, int lockTimeout);

    void unlock(String lockKey, String lockValue);

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     */
    void expire(String key, int timeout, TimeUnit unit);

    /**
     * 根据key批量查询
     *
     * @param key  需要查询的key
     * @param keys keyList
     * @return
     */
    List<String> multiGet(String key, List<String> keys);

    /**
     * 添加到list--从列头开始添加
     *
     * @param key
     * @param values
     * @return
     */
    Long leftPushAll(String key, List<String> values);

    /**
     * 获取列表指定下标的元素
     *
     * @param key
     * @param index
     * @return
     */
    String lIndex(String key, long index);

    /**
     * 获取列表中的数据
     *
     * @param key   key
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    List<String> range(String key, int start, int end);

    /**
     * 获取列表的长度
     *
     * @param key
     * @return
     */
    long size(String key);

    /**
     * 从队列左侧添加数据
     *
     * @param key
     * @param value
     * @return
     */
    long lpush(String key, String value);

    /**
     * 从队列右侧添加数据
     *
     * @param key
     * @param value
     * @return
     */
    long rightPush(String key, String value);

    /**
     * 清理队列,清理[start, end] 以外的元素
     *
     * @param key   队列的key
     * @param start 开始位置
     * @param end   结束位置
     */
    void trim(String key, int start, int end);

    /**
     * 查询key
     *
     * @param key
     * @return
     */
    Set<String> keys(String key);

    /**
     * 删除制定的key
     *
     * @param keys
     */
    void delete(Set<String> keys);

    Boolean zadd(String key, String field, double score);

    /**
     * redis zadd命令
     *
     * @param key
     * @param score
     * @param value
     */
    void zadd(String key, double score, String value);

    Long zcount(String key, double minScore, double maxScore);

    Set<String> zrangeByScore(String key, double minScore, double maxScore, int offset, int count);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。
     *
     * @param key
     * @param fields
     * @return
     */
    List<String> hMget(String key, List<String> fields);

    /**
     * 查看分数
     *
     * @param key
     * @param object
     * @return
     */
    Double zscore(String key, String object);

    /**
     * 查看玩家的排名
     *
     * @param key
     * @param object
     * @return
     */
    Long zrevrank(String key, String object);

    /**
     * 增减玩家分数
     *
     * @param key
     * @param object
     * @param score
     * @return
     */
    Double zincrby(String key, String object, Double score);

    /**
     * 在zset中获取对象
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> reverseZsetRange(String key, int start, int end);

    /**
     * 获取范围分数内的数据
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    Set<String> zrangeByScore(String key, double minScore, double maxScore);

    /**
     * redis zrem命令
     *
     * @param key
     * @param value
     * @return
     */
    boolean zrem(String key, String value);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    Set<String> zrevRangeByScore(String key, double max, double min);

    /**
     * 选择数据库
     *
     * @param index
     * @return
     */
    RedisManager select(int index);

    /**
     * 等同redis中的zincrby命令
     *
     * @param key
     * @param increment
     * @param member
     * @return
     */
    Double zincrby(String key, double increment, String member);

    /**
     * @param key
     * @param min
     * @param max
     */
    void zremRangeByScore(String key, double min, double max);

    /**
     * @param key
     * @param start
     * @param stop
     * @return
     */
    Set<ZSetOperations.TypedTuple<String>> zrevrangeWithScores(String key, long start, long stop);


    /**
     * 递增
     *
     * @param key
     * @return
     */
    Long incr(String key);


}
