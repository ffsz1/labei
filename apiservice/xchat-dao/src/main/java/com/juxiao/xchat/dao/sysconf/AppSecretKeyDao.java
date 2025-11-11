package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @class: AppSecretKeyDao.java
 * @author: chenjunsheng
 * @date 2018/7/16
 */
public interface AppSecretKeyDao {

    /**
     * 根据版本号获取加密key值
     *
     * @param appVersion
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT `sign_key` FROM `app_secret_key` WHERE `os` = #{os} AND `app_version` = #{appVersion}")
    String getAppSignKey(@Param("os") String os, @Param("appVersion") String appVersion);
}
