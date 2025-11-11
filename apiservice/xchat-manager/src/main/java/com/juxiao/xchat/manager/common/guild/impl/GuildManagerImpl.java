package com.juxiao.xchat.manager.common.guild.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.GuildDao;
import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.guild.GuildManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 20:29
 * @作者： carl
 */
@Slf4j
@Service
public class GuildManagerImpl implements GuildManager {
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private GuildDao guildDao;

    @Override
    public GuildDTO getGuild(Long id) {
        String jsdata = redisManager.hget(RedisKey.guild.getKey(), String.valueOf(id));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, GuildDTO.class);
        }

        GuildDTO guildDO = guildDao.getDTOById(id);
        if (guildDO == null) {
            return null;
        }

        redisManager.hset(RedisKey.guild.getKey(), String.valueOf(id), gson.toJson(guildDO));

        return guildDO;
    }

    @Override
    public List<GuildDO> getGuildValidList() {
        String jsdata = redisManager.get(RedisKey.guild_list.getKey());
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, new TypeToken<List<GuildDO>>(){}.getType());
        }

        List<GuildDO> list = guildDao.getValidList();
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_list.getKey(),  gson.toJson(list), 5, TimeUnit.MINUTES);
        }

        return list;
    }
}
