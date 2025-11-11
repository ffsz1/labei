package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.HomeChannelDAO;
import com.juxiao.xchat.dao.sysconf.domain.HomeChannelDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.sysconf.HomeChannelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: alwyn
 * @Description: 渠道信息实现类
 * @Date: 2018/11/7 16:06
 */
@Service
public class HomeChannelServiceImpl implements HomeChannelService {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private HomeChannelDAO homeChannelDAO;
    @Autowired
    private Gson gson;

    @Override
    public HomeChannelDO getByChannel(String channel) {
        if (StringUtils.isBlank(channel)) {
            return null;
        }
        String result = redisManager.hget(RedisKey.home_channel.getKey(), channel);
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, HomeChannelDO.class);
        }
        HomeChannelDO channelDO = homeChannelDAO.getByChannel(channel);
        if (channelDO != null) {
            redisManager.hset(RedisKey.home_channel.getKey(), channel, gson.toJson(channelDO));
        }
        return channelDO;
    }
}
