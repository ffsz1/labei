package com.juxiao.xchat.service.common.sysconf.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.sysconf.AppVersionDao;
import com.juxiao.xchat.dao.sysconf.dto.AppVersionDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.AppVersionStatus;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import com.juxiao.xchat.service.common.sysconf.vo.AppVersionVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @class: AppVersionServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {
    private final Logger logger = LoggerFactory.getLogger(AppVersionServiceImpl.class);

    @Autowired
    private AppVersionDao appVersionDao;
    @Autowired
    private AppVersionManager appVersionManager;
    @Autowired
    private SysConfManager sysconfManager;

    @Override
    public WebServiceCode checkVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return WebServiceCode.VERSION_IS_NULL;
        }

        return WebServiceCode.SUCCESS;
    }


    @Override
    public AppVersionVO getAppVersionInfo(String appVersion, String os, String appid) throws WebServiceException {
        if (StringUtils.isBlank(appVersion) || StringUtils.isBlank(os)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        AppVersionDTO appVersionDTO = appVersionManager.getAppVersion(os, appid, appVersion);
        if (appVersionDTO == null) {
            throw new WebServiceException(WebServiceCode.NOT_EXISTS);
        }
        AppVersionVO versionVo = checkVersion(appVersionDTO);
        SysConfDTO confDTO = sysconfManager.getSysConf(SysConfigId.kick_waiting);
        versionVo.setKickWaiting(confDTO == null ? 0 : Integer.valueOf(confDTO.getConfigValue()));
        confDTO = sysconfManager.getSysConf(SysConfigId.newest_version);
        versionVo.setNewestVersion(confDTO == null ? 1 : Integer.valueOf(confDTO.getConfigValue().replaceAll("\\.", "")));
        return versionVo;
    }

    /**
     * 检查版本信息
     *
     * @param appVersion 版本信息
     * @return
     * @throws WebServiceException
     */
    private AppVersionVO checkVersion(AppVersionDTO appVersion) throws WebServiceException {
        AppVersionVO vo = new AppVersionVO();
        BeanUtils.copyProperties(appVersion, vo);
        // 该版本状态是否处于建议更新或强制更新状态
        boolean flag = appVersion.getStatus() == AppVersionStatus.force_update.getStatus() ||
                AppVersionStatus.recomm_update.getStatus() == appVersion.getStatus();
        if (flag) {
            // 需要更新
            AppVersionDTO versionDto = appVersionDao.listAppVersions("", appVersion.getOs(), appVersion.getAppId());
            // 获取当前os最新的可用版本
            if (appVersion.getVersion().equals(versionDto.getVersion())) {
                logger.error("Not found newestVersion. The version: " + appVersion.getVersion() + ", os: " + appVersion.getOs());
                vo.setStatus((byte) 1);
//                throw new WebServiceException("Not found newestVersion.");
            }
            // 将最新的版本及描述加入Vo
            vo.setUpdateVersion(versionDto.getVersion());
            vo.setUpdateVersionDesc(versionDto.getVersionDesc());
            vo.setDownloadUrl(versionDto.getDownloadUrl());
        } else {
            vo.setUpdateVersion("");
        }
        return vo;
    }
}
