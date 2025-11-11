package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.manager.common.sysconf.IconManager;
import com.juxiao.xchat.service.api.sysconf.IconService;
import com.juxiao.xchat.dao.sysconf.dto.IconDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IconServiceImpl implements IconService {
    @Autowired
    private IconManager iconManager;
    @Autowired
    private RedisManager redisManager;

    private final long LIMIT_VERSION = Utils.version2long("1.0.3");

    private final long IOS_LIMIT_VERSION = Utils.version2long("1.0.1");

    /**
     * 获取首页icon
     */
    @Override
    public List<IconDTO> findIconList(boolean isCheck, String app, String appVersion,String os) {
        String str;
//        if (isCheck) {
//            //TODO 企业包
//            str = redisManager.get(RedisKey.icon_index_v.getKey());
//        } else {
//            str = redisManager.get(RedisKey.icon_index.getKey());
//        }
        if(StringUtils.isNotBlank(appVersion)){
            if("android".equalsIgnoreCase(os) && Utils.version2long(appVersion) >= LIMIT_VERSION){
                str = redisManager.get(RedisKey.icon_index_news.getKey());
            }else if("iOS".equalsIgnoreCase(os) && Utils.version2long(appVersion) >= IOS_LIMIT_VERSION){
                str = redisManager.get(RedisKey.icon_index_news.getKey());
            }else{
                str = redisManager.get(RedisKey.icon_index.getKey());
            }
        }else{
            str = redisManager.get(RedisKey.icon_index.getKey());
        }

        if (StringUtils.isNotBlank(str)) {
            return iconManager.getList(str.split(","));
        }else{
            if(StringUtils.isNotBlank(appVersion)) {
                if (Utils.version2long(appVersion) >= LIMIT_VERSION) {
                    str = iconManager.refreshIconListCache(RedisKey.icon_index_news.getKey(), "0");
                } else {
                    str = iconManager.refreshIconListCache(RedisKey.icon_index.getKey(), "0");
                }
            }else{
                str = iconManager.refreshIconListCache(RedisKey.icon_index.getKey(), "0");
            }
        }

        if (StringUtils.isEmpty(str)) {
            return Lists.newArrayList();
        }

        return iconManager.getList(str.split(","));
    }

}
