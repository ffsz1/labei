package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.SysConf;
import com.erban.main.model.SysConfExample;
import com.erban.main.mybatismapper.SysConfMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.vo.SplashVo;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SysConfService extends BaseService {
    @Autowired
    private SysConfMapper sysConfMapper;

    /**
     * 获取闪屏的配置信息
     *
     * @return
     */
    public SplashVo getSplashConf() {
        String splash = jedisService.get(RedisKey.sys_conf_splash.getKey());
        if (BlankUtil.isBlank(splash)) {
            SplashVo splashVo = new SplashVo();
            splashVo.setPict(getSysConfById(Constant.SysConfId.splash_pict).getConfigValue());
            splashVo.setLink(getSysConfById(Constant.SysConfId.splash_link).getConfigValue());
            String splashType = getSysConfById(Constant.SysConfId.splash_type).getConfigValue();
            if (!BlankUtil.isBlank(splashType)) {
                splashVo.setType(Integer.valueOf(splashType));
            }
            jedisService.set(RedisKey.sys_conf_splash.getKey(), gson.toJson(splashVo));
            return splashVo;
        }
        return gson.fromJson(splash, SplashVo.class);
    }

    public SysConf getSysConfById(String configId) {
        String sysConfStr = jedisService.hget(RedisKey.sys_conf.getKey(), configId);
        if (StringUtils.isBlank(sysConfStr)) {
            SysConf sysConf = sysConfMapper.selectByPrimaryKey(configId);
            if (sysConf != null) {
                saveSysConfCache(sysConf);
            }
            return sysConf;
        }
        return gson.fromJson(sysConfStr, SysConf.class);
    }

    public void setConfValueById(String configId, String configValue) {
        SysConf sysConf = getSysConfById(configId);
        if (sysConf != null) {
            sysConf.setConfigValue(configValue);
            save(sysConf, true);
        }
    }

    public void refreshSysConf(String configId, String configValue) {
        SysConf sysConf = new SysConf();
        sysConf.setConfigId(configId);
        sysConf.setConfigValue(configValue);
        sysConfMapper.updateByPrimaryKeySelective(sysConf);
        jedisService.hdel(RedisKey.sys_conf.getKey(), configId);
    }

    public String getSysConfValueById(String configId) {
        SysConf sysConf = getSysConfById(configId);
        if (sysConf != null) {
            return sysConf.getConfigValue();
        }
        return "";
    }

    public void addSysConf(SysConf sysConf) {
        int count = sysConfMapper.insert(sysConf);
        if (count > 0) {
            saveSysConfCache(sysConf);
        }
    }

    public void saveSysConfCache(SysConf sysConf) {
        if (sysConf == null) {
            return;
        }
        jedisService.hwrite(RedisKey.sys_conf.getKey(), sysConf.getConfigId(), gson.toJson(sysConf));
    }


    /**
     * 获取配置列表(redis)
     *
     * @return
     */
    public List<SysConf> getList() {
        Map<String, String> result = jedisService.hgetAll(RedisKey.sys_conf.getKey());
        if (!CollectionUtils.isEmpty(result)) {
            List<SysConf> list = Lists.newArrayList(result.size());
            for (Map.Entry<String, String> entry : result.entrySet()) {
                SysConf sysConf = gson.fromJson(entry.getValue(), SysConf.class);
                list.add(sysConf);
            }
            return list;
        }
        return Collections.emptyList();
    }

    public int save(SysConf sysConf, boolean isEdit) {
        int result = 0;
        if (isEdit) {
            result = sysConfMapper.updateByPrimaryKeySelective(sysConf);
        } else {
            result = sysConfMapper.insert(sysConf);
        }
        if (result > 0) {
            saveSysConfCache(sysConf);
            if ("splash_screen".equals(sysConf.getNameSpace())) {
                jedisService.del(RedisKey.sys_conf_splash.getKey());
                jedisService.del(RedisKey.sys_conf_splash.getKey("mmyy"));
                jedisService.del(RedisKey.sys_conf_splash.getKey("mmjy"));
                jedisService.del(RedisKey.sys_conf_splash.getKey("mmxq"));
                jedisService.del(RedisKey.sys_conf_splash.getKey("xchat"));
            }
        }
        return result;
    }

    /**
     * 根据id统计配置列表记录数
     *
     * @param configId
     * @return
     */
    public int countById(String configId) {
        SysConfExample example = new SysConfExample();
        example.createCriteria().andConfigIdEqualTo(configId);
        return sysConfMapper.countByExample(example);
    }

    /**
     * 根据configId获取配置
     *
     * @param id
     * @return
     */
    public SysConf getById(String id) {
        String result = jedisService.hget(RedisKey.sys_conf.getKey(), id);
        return gson.fromJson(result, SysConf.class);
    }

    public int deleteByIds(String[] ids) {
        int count = 0;
        for (String id : ids)
            count += deleteById(id);
        return count;
    }

    public int deleteById(String id) {
        int count = sysConfMapper.deleteByPrimaryKey(id);
        //TODO 批量删除redis
        if (count > 0)
            jedisService.hdel(RedisKey.sys_conf.getKey(), id);
        return count;
    }

    public List<SysConf> getByList(String searchText) {
        String result = jedisService.hget(RedisKey.sys_conf.getKey(), searchText);
        return Arrays.asList(gson.fromJson(result, SysConf.class));
    }
}
