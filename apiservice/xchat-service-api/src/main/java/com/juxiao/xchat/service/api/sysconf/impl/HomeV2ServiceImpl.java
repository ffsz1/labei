package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserHotDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.BannerManager;
import com.juxiao.xchat.manager.common.sysconf.IconManager;
import com.juxiao.xchat.service.api.event.RankService;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.HomeV2Service;
import com.juxiao.xchat.service.api.sysconf.IconService;
import com.juxiao.xchat.service.api.sysconf.conf.AuditConfig;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认的首页逻辑
 */
@Service("homeV2Service")
public class HomeV2ServiceImpl extends BaseHomeServiceImpl implements HomeV2Service {
    @Autowired
    private RankService rankService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private IconManager iconManager;

    @Autowired
    private BannerManager bannerManager;

    @Autowired
    private AuditConfig auditConfig;

    @Autowired
    private IconService iconService;

    @Override
    public HomeV2Vo index(String channel, IndexParam indexParam, String clientIp) throws WebServiceException {
        return super.index(indexParam, clientIp);
    }

    @Override
    public List<RoomVo> findRoomByTag(String channel, Long uid, Long tagId, Integer pageNum, Integer pageSize, String os, String app, String appVersion, String clientIp) throws WebServiceException {
        if (tagId == null) {
            return Lists.newArrayList();
        }
        return super.findRoomByTag(uid, tagId, pageNum, pageSize, os, app, appVersion, clientIp, channel);
    }

    @Override
    public List<RoomVo> getHomeHotRoom(String channel, Long uid, String appVersion, Integer pageNum, Integer pageSize, String appid) {
        return super.getHomeHotRoom(uid, appVersion, pageNum, pageSize, appid);
    }

    @Override
    public List<UserHotDTO> getBestCompanies(IndexParam indexParam, String clientIp) throws WebServiceException {
        return super.getBestCompanies(indexParam, clientIp);
    }

    @Override
    public List<UserHotDTO> getNewUsers(IndexParam indexParam, String clientIp) throws WebServiceException {
        return super.getNewUsers(indexParam, clientIp);
    }

    /**
     * 将渠道号转换成枚举类型
     *
     * @param channel
     * @return
     */
    private ChannelEnum toEnum(String channel) {
        ChannelEnum channelEnum;
        try {
            channelEnum = ChannelEnum.valueOf(channel);
        } catch (Exception e) {
            channelEnum = ChannelEnum.xbd;
        }
        return channelEnum;
    }
}
