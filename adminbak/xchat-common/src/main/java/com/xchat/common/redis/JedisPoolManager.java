package com.xchat.common.redis;

import com.xchat.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

/**
 * @author liuguofu
 */
public class JedisPoolManager {

    private static final Logger log = LoggerFactory.getLogger(JedisPoolManager.class);

    private static final ThreadLocal<JedisPool> CURRENT_JEDIS_POOL = new ThreadLocal<JedisPool>();

    private List<JedisPool> jedisPools;
    private String password;

    public Jedis getJedis() {
        for (int i = 0, size = jedisPools.size(); i < size; i++) {
            JedisPool jedisPool = jedisPools.get(i);
            Jedis jedis = null;
            try {

                jedis = jedisPool.getResource();
                if (jedis.isConnected()) {
                    CURRENT_JEDIS_POOL.set(jedisPool);
                    return jedis;
                } else {
                    jedisPool.returnBrokenResource(jedis);
                    log.error("Get jedis connection from pool but not connected.");
                }
            } catch (JedisConnectionException e) {
                log.error("Get jedis connection from pool list index:{}", i, e);
                if (jedis != null) {
                    log.warn("Return broken resource:" + jedis);
                    jedisPool.returnBrokenResource(jedis);
                }
            }
        }
        return null;
    }

    public void returnJedis(Jedis jedis) {
        JedisPool jedisPool = CURRENT_JEDIS_POOL.get();
        CURRENT_JEDIS_POOL.remove();
        if (jedisPool != null) {
            jedisPool.returnResource(jedis);

        }

    }

    public void setJedisPools(List<JedisPool> jedisPools) {
        this.jedisPools = jedisPools;
    }

    public List<JedisPool> getJedisPools() {
        return jedisPools;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
