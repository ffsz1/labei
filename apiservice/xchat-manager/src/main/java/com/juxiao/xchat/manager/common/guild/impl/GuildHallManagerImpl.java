package com.juxiao.xchat.manager.common.guild.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.GuildHallDao;
import com.juxiao.xchat.dao.guild.GuildHallMemberDao;
import com.juxiao.xchat.dao.guild.domain.GuildHallDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.guild.GuildHallManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 18:38
 * @作者： carl
 */
@Slf4j
@Service
public class GuildHallManagerImpl implements GuildHallManager {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private GuildHallDao guildHallDao;

    @Autowired
    private Gson gson;

    @Autowired
    private GuildHallMemberDao guildHallMemberDao;


    @Override
    public GuildHallDTO getHall(Long id) {
        String jsHall = redisManager.hget(RedisKey.guild_hall.getKey(), String.valueOf(id));
        if (StringUtils.isNotBlank(jsHall)) {
            return gson.fromJson(jsHall, GuildHallDTO.class);
        }

        GuildHallDTO guildHallDTO = guildHallDao.getDTOById(id);
        if (guildHallDTO != null) {
            redisManager.hset(RedisKey.guild_hall.getKey(), String.valueOf(id), gson.toJson(guildHallDTO));
        }

        return guildHallDTO;
    }

    @Override
    public List<GuildHallDTO> getHallListOrderByGoldDesc(Long guildId, Date date) {
        String jsHall = redisManager.get(RedisKey.guild_hall_list.getKey(String.valueOf(guildId)));
        if (StringUtils.isNotBlank(jsHall)) {
            return gson.fromJson(jsHall,new TypeToken<List<GuildHallDTO>>(){}.getType());
        }

        List<GuildHallDTO> list = guildHallDao.findByGuildIdOrderByGoldDesc(guildId, date);
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_hall_list.getKey(String.valueOf(guildId)), gson.toJson(list), 5, TimeUnit.MINUTES);
        }

        return list;
    }

    @Override
    public GuildHallDTO getHallByMemberUid(Long uid) throws WebServiceException {
        String jsHall = redisManager.hget(RedisKey.guild_member_in_hall.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(jsHall)) {
            return gson.fromJson(jsHall, GuildHallDTO.class);
        }

        GuildHallDTO guildHallDTO;
        // 检查是否成员
        GuildHallMemberDTO memberDTO = guildHallMemberDao.getGuildMemberInfo(uid);
        if (memberDTO != null) {
            guildHallDTO = this.getHall(memberDTO.getHallId());
            if (guildHallDTO != null) {
                redisManager.hset(RedisKey.guild_member_in_hall.getKey(), String.valueOf(uid), gson.toJson(guildHallDTO));
            }

            return guildHallDTO;
        }

//        // 检查是否厅主
//        guildHallDTO = guildHallDao.getDTOByHallUid(uid);
//        if (guildHallDTO != null) {
//            redisManager.hset(RedisKey.guild_member_in_hall.getKey(), String.valueOf(uid), gson.toJson(guildHallDTO));
//        }

        return null;
    }
}
