package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.BannerManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.sysconf.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BannerServiceImpl implements BannerService {
    @Resource
    private BannerManager bannerManager;

    @Resource
    private UsersManager usersManager;

    @Resource
    private RedisManager redisManager;

    @Override
    public List<BannerDTO> findBannerList(Long uid, String os, String app) {
        String str = getListByOs(os, app);
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        List<BannerDTO> bannerVoList = bannerManager.getList(str);
        if (uid != null) {
            bannerVoList = removeByUid(bannerVoList, uid);
        }
        return bannerVoList;
    }

    public String getListByOs(String os, String app) {
        String jedisKey;
        if ("ios".equalsIgnoreCase(os)) {
            jedisKey = "0,2";
        } else if ("android".equalsIgnoreCase(os)) {
            jedisKey = "0,1";
        } else {
            jedisKey = "0,1,2";
        }
        String str = redisManager.hget(RedisKey.banner_list.getKey(), jedisKey);
        if (StringUtils.isBlank(str)) {
            str = bannerManager.refreshBannerListCache(RedisKey.banner_list.getKey(), jedisKey);
        }
        return str;
    }

    public List<BannerDTO> removeByUid(List<BannerDTO> bannerVoList, Long uid) {
        UsersDTO users = usersManager.getUser(uid);
        if (users == null || users.getCreateTime() == null) {
            return bannerVoList;
        }
        boolean isNew = false;
        Date date = DateTimeUtils.getLastDay(new Date(), 3);
        if (users.getCreateTime().getTime() - date.getTime() > 0) {
            isNew = true;
        }
        List<BannerDTO> bannerVos = Lists.newArrayList();
        for (BannerDTO bannerVo : bannerVoList) {
            if (isNew && (bannerVo.getIsNewUser().intValue() == 0 || bannerVo.getIsNewUser().intValue() == 1)) {
                bannerVos.add(bannerVo);
            } else if (!isNew && (bannerVo.getIsNewUser().intValue() == 0 || bannerVo.getIsNewUser().intValue() == 2)) {
                bannerVos.add(bannerVo);
            }
        }
        return bannerVos;
    }

    @Override
    public List<BannerDTO> findBannerListByTagId(Long uid, String os, Long tagId) {
        String str = getListByTagId(os, tagId);
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        List<BannerDTO> bannerVoList = bannerManager.getList(str);
        if (uid != null) {
            bannerVoList = removeByUid(bannerVoList, uid);
        }
        return bannerVoList;
    }

    @Override
    public List<BannerDTO> findDiscoverItemList(Long uid, String os, Long tagId) {
        String str = getListByTagId(os, tagId);
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        List<BannerDTO> bannerVoList = bannerManager.getList(str);
        if (uid != null) {
            bannerVoList = removeByUid(bannerVoList, uid);
        }
        return bannerVoList;
    }

    public String getListByTagId(String os, Long tagId) {
        String osType;
        if ("ios".equalsIgnoreCase(os)) {
            osType = "0,2";
        } else if ("android".equalsIgnoreCase(os)) {
            osType = "0,1";
        } else {
            osType = "0,1,2";
        }
        String jedisKey = tagId + "_" + osType;
        String str = redisManager.hget(RedisKey.banner_list.getKey(), jedisKey);
        if (StringUtils.isBlank(str)) {
            str = bannerManager.refreshBannerListByTagIdCache(RedisKey.banner_list.getKey(), jedisKey, osType, tagId);
        }
        return str;
    }

    @Override
    public List<BannerDTO> refreshValidateBannerList(String os) {
        String osType = "";
        if ("ios".equalsIgnoreCase(os)) {
            osType = "0,2";
        } else if ("android".equalsIgnoreCase(os)) {
            osType = "0,1";
        } else {
            osType = "0,1,2";
        }
        return bannerManager.refreshValidateBannerList(osType);
    }
}
