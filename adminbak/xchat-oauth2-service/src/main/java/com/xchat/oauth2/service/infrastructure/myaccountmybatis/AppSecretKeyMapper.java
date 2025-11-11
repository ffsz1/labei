package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @class: AppSecretKeyMapper.java
 * @author: chenjunsheng
 * @date 2018/7/16
 */
public interface AppSecretKeyMapper {

    /**
     * 根据版本号获取加密key值
     *
     * @param appVersion
     * @return
     */
    @Select("SELECT `sign_key` FROM `app_secret_key` WHERE `os` = #{os} AND `app_version` = #{appVersion}")
    String getAppSignKey(@Param("os") String os, @Param("appVersion") String appVersion);
}
