package com.juxiao.xchat.manager.external.netease.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "external.caih")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class CaihConf {
    private String clientId;

    private String smsSendUrl;

    private String password;

    private String smsTemplate;

    private int deviceIdDayLimit;

    private int uidDayLimit;



    public String content(String smsCode) {
        return String.format(smsTemplate, smsCode);
    }
}
