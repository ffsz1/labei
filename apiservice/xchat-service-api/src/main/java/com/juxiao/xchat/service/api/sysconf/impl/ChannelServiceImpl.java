package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.ChannelDAO;
import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.conf.AuditConfig;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/10/22 16:50
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelDAO channelDAO;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;
    @Autowired
    private AuditConfig auditConfig;

    @Override
    public ChannelDTO getByName(ChannelEnum channel) {
        String result = redisManager.hget(RedisKey.channel_audit.getKey(), channel.toString());
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, ChannelDTO.class);
        }
        List<ChannelDTO> channelList = channelDAO.getByChannelName(channel.toString());
        if (channelList == null || channelList.isEmpty()) {
            return null;
        }
        redisManager.hset(RedisKey.channel_audit.getKey(), channel.toString(), gson.toJson(channelList.get(0)));
        return channelList.get(0);
    }

    @Override
    public List<Long> listByChannelId(Integer channel) {
        String result = redisManager.hget(RedisKey.channel_audit_room.getKey(), channel.toString());
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, new TypeToken<List<Long>>(){}.getType());
        }
        List<Long> uidList = channelDAO.listByChannel(channel.toString());
        if (uidList == null) {
            return Lists.newArrayList();
        }
        redisManager.hset(RedisKey.channel_audit_room.getKey(), channel.toString(), gson.toJson(uidList));
        return uidList;
    }

    @Override
    public ChannelAuditVO checkAudit(ChannelEnum channel, String appVersion, Long uid) {
        ChannelAuditVO vo = new ChannelAuditVO();
        vo.setAudit(false);
        //
        ChannelDTO channelDTO = getByName(channel);
        if (channelDTO == null) {
            return vo;
        }
        if (!channelDTO.getAuditVersion().equals(appVersion)) {
            return vo;
        }
        if (!Boolean.TRUE.equals(channelDTO.getAuditOption())) {
            return vo;
        }
        long time = System.currentTimeMillis();
        if (channelDTO.getBeginTime() != null && channelDTO.getBeginTime().getTime() < time &&
                channelDTO.getEndTime() != null && channelDTO.getEndTime().getTime() > time) {
            // 在审核期间内
            if (auditConfig.getAuditUidList().contains(uid)) {
                // 审核账号
                vo.setAudit(true);
                return vo;
            }
            int level = levelManager.getUserExperienceLevelSeq(uid);
            if (channelDTO.getLeftLevel() != null && level > channelDTO.getLeftLevel()) {
                // 该用户等级大于vivo的审核等级
                return vo;
            } else {
                vo.setAudit(true);
                return vo;
            }
        }
        return vo;
    }

    /**
     * 根据渠道ID 查询Icon列表
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> listIconByChannelId(Integer id) {
        String result = redisManager.hget(RedisKey.channel_audit_icon.getKey(), id.toString());
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, new TypeToken<List<Long>>(){}.getType());
        }
        List<Integer> iconList = channelDAO.listIconByChannelId(id.toString());
        if (iconList == null) {
            return Lists.newArrayList();
        }
        redisManager.hset(RedisKey.channel_audit_icon.getKey(), id.toString(), gson.toJson(iconList));
        return iconList;
    }

    /**
     * 根据渠道ID 查询Banner列表
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> listBannerByChannelId(Integer id) {
        String result = redisManager.hget(RedisKey.channel_audit_banner.getKey(), id.toString());
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, new TypeToken<List<Long>>(){}.getType());
        }
        List<Integer> bannerList = channelDAO.listBannerByChannelId(id.toString());
        if (bannerList == null) {
            return Lists.newArrayList();
        }
        redisManager.hset(RedisKey.channel_audit_banner.getKey(), id.toString(), gson.toJson(bannerList));
        return bannerList;
    }
}
