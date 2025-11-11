package com.juxiao.xchat.service.api.sysconf.impl;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.dto.GeneralReviewWhitelist;
import com.juxiao.xchat.dao.sysconf.dto.ReviewConfigDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.sysconf.ReviewConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chris
 * @Title:
 * @date 2018/10/30
 * @time 11:22
 */
@Service
public class ReviewConfigServiceImpl implements ReviewConfigService {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    /**
     * 获取缓存审核配置信息
     *
     * @return
     */
    @Override
    public List<ReviewConfigDTO> selectByCacheList() {
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.review_config.getKey())).orElse(new HashMap<>());
        if(!CollectionUtils.isEmpty(result)) {
            List<ReviewConfigDTO> list = new ArrayList<>();
            for(Map.Entry<String, String> entry : result.entrySet()) {
                ReviewConfigDTO reviewConfigDTO = gson.fromJson(entry.getValue(), ReviewConfigDTO.class);
                list.add(reviewConfigDTO);
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 审核白名单
     *
     * @return
     */
    @Override
    public List<GeneralReviewWhitelist> getGeneralReviewWhitelistByList() {
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.general_review_whitelist.getKey())).orElse(new HashMap<>());
        if(!CollectionUtils.isEmpty(result)) {
            List<GeneralReviewWhitelist> list = new ArrayList<>();
            for(Map.Entry<String, String> entry : result.entrySet()) {
                GeneralReviewWhitelist reviewConfigDTO = gson.fromJson(entry.getValue(), GeneralReviewWhitelist.class);
                list.add(reviewConfigDTO);
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 获取需要移除的审核配置信息标签
     * @param channel    渠道
     * @param uid        uid
     * @param os         系统
     * @param appVersion APP版本号
     * @return Set<String>
     */
    @Override
    public Set<String> getRemoveTags(String channel, Long uid, String os, String appVersion) {
        List<ReviewConfigDTO> reviewConfigDTOS = selectByCacheList();
        Set<String> removeTag = new HashSet<>();
        if (reviewConfigDTOS.size() > 0) {
            String channels = Optional.ofNullable(channel).orElse("appstore");
            Integer chargeTotal = Optional.ofNullable(redisManager.hget(RedisKey.user_charge.getKey(), uid.toString()))
                    .map(Integer::parseInt)
                    .orElse(0);
            removeTag = reviewConfigDTOS.stream().filter(item -> os.equals(item.getSystem())
                    && ("all".equals(item.getChannel()) || channels.equals(item.getChannel()))
                    && ("all".equals(item.getVersions()) || appVersion.equals(item.getVersions())) && chargeTotal < item.getRechargeAmount())
                    .map(ReviewConfigDTO::getTagName)
                    .collect(Collectors.toSet());
        }
        return removeTag;
    }
}
