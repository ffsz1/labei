package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.GeneralReviewWhitelist;
import com.juxiao.xchat.dao.sysconf.dto.ReviewConfigDTO;

import java.util.List;
import java.util.Set;

/**
 * @author chris
 * @Title:
 * @date 2018/10/30
 * @time 11:20
 */
public interface ReviewConfigService {

    /**
     * 获取缓存审核配置信息
     * @return
     */
    List<ReviewConfigDTO> selectByCacheList();

    /**
     * 审核白名单
     * @return
     */
    List<GeneralReviewWhitelist> getGeneralReviewWhitelistByList();

    /**
     * 获取需要移除的审核配置信息标签
     * @param channel 渠道
     * @param uid uid
     * @param os 系统
     * @param appVersion APP版本号
     * @return hashSet
     */
    Set<String> getRemoveTags(String channel, Long uid, String os, String appVersion);
}
