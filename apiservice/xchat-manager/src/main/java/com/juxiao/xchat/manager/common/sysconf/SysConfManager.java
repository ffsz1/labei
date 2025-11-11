package com.juxiao.xchat.manager.common.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.SplashScreenDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.dto.TopTabDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;

import java.util.List;

/**
 * @class: SysConfManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface SysConfManager {

    /**
     * @param configId
     * @return
     */
    SysConfDTO getSysConf(String configId);

    /**
     * @param configId
     * @return
     */
    String getSysConfValue(String configId);

    /**
     * 获取系统配置
     *
     * @param configId 配置ID
     * @return
     */
    SysConfDTO getSysConf(SysConfigId configId);

    /**
     * 获取闪屏配置
     *
     * @param userType 用户类型 [0.全部; 1.新用户; 2.老用户]
     * @return
     */
    SplashScreenDTO getSplashScreen(Integer userType);

    void setConfValueById(SysConfigId configId, String value);

    /**
     * 根据命名空间以及状态获取系统配置
     *
     * @param nameSpace
     * @param status
     * @return
     */
    List<TopTabDTO> getSysConfigByNameSpace(String nameSpace, Integer status);
}
