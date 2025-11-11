package com.juxiao.xchat.service.common.sysconf;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.common.sysconf.vo.AppVersionVO;

/**
 * @class: AppVersionService.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface AppVersionService {

    /**
     * 检查版本
     *
     * @param version
     * @return
     */
    WebServiceCode checkVersion(String version);

    /**
     * 获取版本信息(附带版本更新信息)
     *
     * @param appVersion app版本
     * @param os         系统版本
     * @param appid
     * @return
     */
    AppVersionVO getAppVersionInfo(String appVersion, String os, String appid) throws WebServiceException;
}
