package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.UserWxappDrawDao;
import com.juxiao.xchat.dao.user.dto.UserWxappDrawDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserWxappDrawManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWxappDrawManagerImpl implements UserWxappDrawManager {
    @Autowired
    private Gson gson;
    @Autowired
    private UserWxappDrawDao wxappDrawDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void redueceUserWxappDraw(Long uid) {
        wxappDrawDao.updateReduceLeftDrawCount(uid);

        redisManager.hdel(RedisKey.wxapp_draw_count.getKey(), String.valueOf(uid));
    }

    @Override
    public UserWxappDrawDTO getUserWxappDraw(Long uid) {
        String json = redisManager.hget(RedisKey.wxapp_draw_count.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(json)) {
            try {
                return gson.fromJson(json, UserWxappDrawDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.wxapp_draw_count.getKey(), String.valueOf(uid));
            }
        }
        UserWxappDrawDTO wxappDrawDto = wxappDrawDao.getUserWxappDraw(uid);
        if (wxappDrawDto != null) {
            redisManager.hset(RedisKey.wxapp_draw_count.getKey(), String.valueOf(uid), gson.toJson(wxappDrawDto));
        }

        return wxappDrawDto;
    }
}
