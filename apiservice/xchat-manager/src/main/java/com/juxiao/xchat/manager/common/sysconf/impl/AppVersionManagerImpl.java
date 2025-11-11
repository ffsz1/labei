package com.juxiao.xchat.manager.common.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.dao.sysconf.AppVersionDao;
import com.juxiao.xchat.dao.sysconf.dto.AppVersionDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.resp.IpMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AppVersionManagerImpl implements AppVersionManager {
    @Autowired
    private Gson gson;

    @Autowired
    private SystemConf systemConf;
    @Autowired
    private AppVersionDao appVersionDao;
    @Autowired
    private RedisManager redisManager;

    @Override
    public AppVersionDTO getAppVersion(String os, String appid, String version) {
        os = os.toLowerCase();
        String result = redisManager.hget(RedisKey.app_version.getKey(), version + os + appid);
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, AppVersionDTO.class);
        }

        AppVersionDTO versionDto = appVersionDao.listAppVersions(version, os, appid);
        if (versionDto != null) {
            redisManager.hset(RedisKey.app_version.getKey(), version + os + appid, gson.toJson(versionDto));
        }
        return versionDto;
    }

    @Override
    public boolean checkAuditingVersion(String os, String appid, String appVersion, String ip, Long uid) {
        if (uid != null && systemConf.getAuditAccountList().contains(String.valueOf(uid))) {
            return true;
        }

        if (StringUtils.isBlank(appVersion)) {
            return false;
        }

        if (!"ios".equalsIgnoreCase(os)) {
            return false;
        }

        try {
//            String str = redisManager.hget(RedisKey.client_ip.getKey(), ip);
//            if (StringUtils.isBlank(str)) {
//                String url = "https://api01.aliyun.venuscn.com/ip";
//                String result = HttpUtils.get(url, "ip=" + ip, "37774f940dee4b45b29f744fd4e58869");
//                IpMessageResult ret = gson.fromJson(result, IpMessageResult.class);
//                //解析相应内容（转换成json对象
//                if (ret.getRet() == 200) {
//                    Map<String, Object> map = ret.getData();
//                    if (map.get("country") != null) {
//                        str = map.get("country").toString();
//                        redisManager.hset(RedisKey.client_ip.getKey(), ip, str);
//                    }
//                } else {
//                    log.error("[ 查询ip信息失败 ]resp:{},msg:{},data:{}", ret.getRet(), ret.getMsg(), ret.getData());
//                }
//            }
//
//            if ("美国".equals(str) || "香港".equals(str) || "台湾".equals(str)) {
//                return true;
//            }
        } catch (Exception e) {
            log.warn("判断ip失败", e);
        }

        AppVersionDTO versionDto = getAppVersion(os, appid, appVersion);
        return versionDto != null && versionDto.getStatus() != null && versionDto.getStatus() == 2;
    }


}
