package com.xchat.oauth2.service.service.account;

import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.infrastructure.mybatis.SysConfMapperMybatis;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.vo.SysConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthSysConfService {
    private final Gson gson = new Gson();
    @Autowired
    private SysConfMapperMybatis sysConfMapper;
    @Autowired
    private JedisService jedisService;


    public SysConf getSysConfById(String configId) {
        String confStr = jedisService.hget(RedisKey.sys_conf.getKey(), configId);
        if (StringUtils.isNoneBlank(confStr)) {
            try {
                return gson.fromJson(confStr, SysConf.class);
            } catch (Exception e) {
                jedisService.hdel(RedisKey.sys_conf.getKey(), configId);
            }
        }

        SysConf sysConf = sysConfMapper.selectByPrimaryKey(configId);
        if (sysConf != null) {
            jedisService.hset(RedisKey.sys_conf.getKey(), configId, gson.toJson(sysConf));
        }
        return sysConf;
    }
}
