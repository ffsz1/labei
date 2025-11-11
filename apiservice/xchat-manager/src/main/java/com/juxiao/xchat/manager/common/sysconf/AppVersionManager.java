package com.juxiao.xchat.manager.common.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.AppVersionDTO;

public interface AppVersionManager {

    /**
     * 根据版本号及os获取版本详情
     *
     * @param os
     * @param appid
     * @param version
     * @return
     */
    AppVersionDTO getAppVersion(String os, String appid, String version);

    /**
     * 查看该版本是否在审核中
     *
     * @param os
     * @param appid
     * @param appVersion
     * @param ip
     * @return
     */
    boolean checkAuditingVersion(String os, String appid, String appVersion, String ip, Long uid);
}
