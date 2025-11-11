package com.xchat.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
@Lazy(value = false)
@Order(-1)
public class GlobalConfig {
    public static String sysEnv;
    public static String appName;
    public static String appSecretName;
    public static String envName;

    @Value("${SYS_ENV}")
    public void setSysEnv(String sysEnv) {
        GlobalConfig.sysEnv = sysEnv;
    }

    @Value("${app_name}")
    public void setAppName(String appName) {
        GlobalConfig.appName = appName;
    }

    @Value("${app_secretary_name}")
    public void setAppSecretName(String appSecretName) {
        GlobalConfig.appSecretName = appSecretName;
    }

    @Value("${admin_env_name}")
    public void setEnvName(String envName) {
        GlobalConfig.envName = envName;
    }
}
