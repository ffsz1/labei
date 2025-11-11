package com.juxiao.xchat.manager.common.sysconf.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.SysNoticeDao;
import com.juxiao.xchat.dao.sysconf.dto.SysNoticeDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.SysNoticeManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysNoticeManagerImpl implements SysNoticeManager {
    @Autowired
    private SysNoticeDao noticeDao;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisManager redisManager;


    @Override
    public List<SysNoticeDTO> getAllNoticeList() {

        String strList = redisManager.get(RedisKey.sys_notice_list.getKey());
        if (StringUtils.isNotBlank(strList)) {
            return gson.fromJson(strList, new TypeToken<List<SysNoticeDTO>>(){}.getType());
        }
        List<SysNoticeDTO> list = noticeDao.findAll();
        if (list != null) {
            redisManager.set(RedisKey.sys_notice_list.getKey(), gson.toJson(list));
        }
        return list;
    }
}
