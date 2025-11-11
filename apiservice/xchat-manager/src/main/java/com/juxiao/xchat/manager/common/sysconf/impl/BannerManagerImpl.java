package com.juxiao.xchat.manager.common.sysconf.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.sysconf.domain.Banner;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.manager.common.base.CacheBaseManager;
import com.juxiao.xchat.manager.common.sysconf.BannerManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @class: IconManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class BannerManagerImpl extends CacheBaseManager<Banner, BannerDTO> implements BannerManager {
    @Override
    public BannerDTO getOneByJedisId(String jedisId) {
        return getOne(RedisKey.banner.getKey(), jedisId, "select * from banner where banner_id = ? and banner_status " +
                "= 1 ", jedisId);
    }

    @Override
    public BannerDTO entityToCache(Banner entity) {
        BannerDTO bannerVo = new BannerDTO();
        bannerVo.setBannerId(entity.getBannerId());
        bannerVo.setBannerName(entity.getBannerName());
        bannerVo.setBannerPic(entity.getBannerPic());
        bannerVo.setSeqNo(entity.getSeqNo());
        bannerVo.setSkipType(entity.getSkipType());
        bannerVo.setSkipUri(entity.getSkipUri());
        bannerVo.setIsNewUser(entity.getIsNewUser());
        bannerVo.setViewType(entity.getViewType());
        return bannerVo;
    }

    /**
     * 刷新icon缓存
     */
    @Override
    public String refreshBannerListCache(String jedisCode, String jedisKey) {
        return refreshListCacheByKey(null, jedisCode, jedisKey, "getBannerId", "select * from banner where os_type in" +
                " (" + jedisKey + ") and banner_status = 1 order by seq_no asc ");
    }

    /**
     * 刷新Banner缓存
     */
    @Override
    public String refreshBannerListByTagIdCache(String jedisCode, String jedisKey, String osType, Long tagId) {
        return refreshBannerListCacheByKey(null, jedisCode, jedisKey, "getBannerId", "select * from banner where " +
                "os_type in (" + osType + ") and banner_status = 1 and view_type = ? order by seq_no asc ", tagId);
    }

    @Override
    public List<BannerDTO> refreshValidateBannerList(String osType) {
        String day = DateTimeUtils.getTodayStr();
        Integer times = DateTimeUtils.getLastSecond().intValue();
        String jediskey = RedisKey.eff_banner.getKey() + "_" + day;
        List<BannerDTO> list = super.findList(jediskey, times, "select * from banner where os_type in (" + osType +
                ") and banner_status = 1  order by seq_no asc", null);
        return list;
    }
}
