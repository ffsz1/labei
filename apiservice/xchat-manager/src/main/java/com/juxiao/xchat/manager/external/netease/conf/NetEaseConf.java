package com.juxiao.xchat.manager.external.netease.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.netease")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class NetEaseConf {
    private String appKey;

    private String appSecret;

    private String smsAppKey;

    private String smsAppSecret;

    private String smsTemplateid;

    private String alarmSmsTemplateid;
}
