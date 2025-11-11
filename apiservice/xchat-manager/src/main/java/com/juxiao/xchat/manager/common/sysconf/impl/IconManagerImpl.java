package com.juxiao.xchat.manager.common.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.domain.Icon;
import com.juxiao.xchat.dao.sysconf.dto.IconDTO;
import com.juxiao.xchat.manager.common.base.CacheBaseManager;
import com.juxiao.xchat.manager.common.sysconf.IconManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @class: IconManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class IconManagerImpl extends CacheBaseManager<Icon, IconDTO> implements IconManager {
    @Autowired
    private Gson gson;

    @Override
    public IconDTO getOneByJedisId(String jedisId) {
        return getOne(RedisKey.icon.getKey(), jedisId, "select * from icon where icon_id = ? and status = 1 ", jedisId);
    }

    @Override
    public IconDTO entityToCache(Icon entity) {
        IconDTO cache = new IconDTO();
        cache.setTitle(entity.getName());
        cache.setSubtitle(entity.getSubtitle());
        cache.setPic(entity.getPic());
        cache.setActivity(entity.getActivity());
        cache.setIosActivity(entity.getIosActivity());
        cache.setUrl(entity.getIconUrl());
        cache.setSkipType(entity.getSkipType());
        cache.setSkipUri(entity.getSkipUri());
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(entity.getKey1())){
            map.put(entity.getKey1(), entity.getValue1());
        }
        if(!StringUtils.isEmpty(entity.getKey2())){
            map.put(entity.getKey2(), entity.getValue2());
        }
        if(!StringUtils.isEmpty(entity.getKey3())){
            map.put(entity.getKey3(), entity.getValue3());
        }
        cache.setParams(gson.toJson(map));
        return cache;
    }

    /**
     * 刷新icon缓存
     */
    @Override
    public String refreshIconListCache(String jedisCode, String types) {
        return refreshListCacheByCode(null, jedisCode, "getIconId", "select * from icon where status = 1 and type in (?) order by seq asc ", types);
    }

}
