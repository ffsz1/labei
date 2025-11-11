package com.juxiao.xchat.manager.common.sysconf.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.sysconf.SysConfDao;
import com.juxiao.xchat.dao.sysconf.dto.SplashScreenDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.dto.TopTabDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @class: SysConfManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class SysConfManagerImpl implements SysConfManager {
    @Resource
    private SysConfDao sysconfDao;

    @Resource
    private RedisManager redisManager;

    @Resource
    private Gson gson;

    @Override
    public SysConfDTO getSysConf(String configId) {
        String confStr = redisManager.hget(RedisKey.sys_conf.getKey(), configId);
        if (StringUtils.isNotBlank(confStr)) {
            return gson.fromJson(confStr, SysConfDTO.class);
        }
        SysConfDTO sysConf = sysconfDao.getSysConf(configId);
        if (sysConf != null) {
            redisManager.hset(RedisKey.sys_conf.getKey(), configId, gson.toJson(sysConf));
        }
        return sysConf;
    }

    @Override
    public String getSysConfValue(String configId) {
        SysConfDTO confDto = this.getSysConf(configId);
        if (confDto == null) {
            return null;
        }
        return confDto.getConfigValue();
    }

    /**
     * @see com.juxiao.xchat.manager.common.sysconf.SysConfManager#getSysConf(SysConfigId)
     */
    @Override
    public SysConfDTO getSysConf(SysConfigId configId) {
        String confStr = redisManager.hget(RedisKey.sys_conf.getKey(), configId.name());
        if (StringUtils.isNotBlank(confStr)) {
            return gson.fromJson(confStr, SysConfDTO.class);
        }

        SysConfDTO sysConf = sysconfDao.getSysConf(configId.name());
        if (sysConf != null) {
            redisManager.hset(RedisKey.sys_conf.getKey(), configId.name(), gson.toJson(sysConf));
        }
        return sysConf;
    }

    @Override
    public SplashScreenDTO getSplashScreen(Integer userType) {
        return sysconfDao.getSplashScreen(userType);
    }

    @Override
    public void setConfValueById(SysConfigId configId, String value) {
        SysConfDTO sysConfDTO = getSysConf(configId);
        if (sysConfDTO == null) {
            return;
        }
        sysConfDTO.setConfigValue(value);
        redisManager.hset(RedisKey.sys_conf.getKey(), configId.name(), gson.toJson(sysConfDTO));
        sysconfDao.updateConfValue(value, configId.name());
    }

    /**
     * 根据命名空间以及状态获取系统配置
     * @param nameSpace
     * @param status
     * @return
     */
	@Override
	public List<TopTabDTO> getSysConfigByNameSpace(String nameSpace, Integer status) {
		String sysConfigStr=redisManager.get(RedisKey.sys_namespace.getKey()+"_"+nameSpace);
		if(StringUtils.isNotBlank(sysConfigStr)) {
			List<TopTabDTO> list=gson.fromJson(sysConfigStr, new TypeToken<List<TopTabDTO>>() {
            }.getType());
			return list;
		}else {
			List<TopTabDTO> list=sysconfDao.getSysConfigByNameSpace(nameSpace, status);
			redisManager.set(RedisKey.sys_namespace.getKey()+"_"+nameSpace, gson.toJson(list));
			return list;
		}
	}

}
