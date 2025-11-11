package com.erban.main;

import redis.clients.jedis.Jedis;

/**
 * Created by liuguofu on 2017/7/25.
 */
public class RedisTest {
    public static void main(String[] args) {
        try {
//            String host = "r-m5e12ed48316a234.redis.rds.aliyuncs.com";//控制台显示访问地址
            String host = "115.28.95.16";//控制台显示访问地址
            int port = 6379;
            Jedis jedis = new Jedis(host, port);
//鉴权信息
            jedis.auth("erbanRediscav7248866");//password
            String key = "erban_auditing_iosversion";
            String value = "1.0.0";
//select db默认为0
//            jedis.select(1);
//set一个key
            jedis.set(key, value);
            System.out.println("Set Key " + key + " Value: " + value);
//get 设置进去的key
            String getvalue = jedis.get(key);
            System.out.println("Get Key " + key + " ReturnValue: " + getvalue);
            jedis.quit();
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
