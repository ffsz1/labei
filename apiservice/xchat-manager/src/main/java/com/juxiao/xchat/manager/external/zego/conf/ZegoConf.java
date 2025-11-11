package com.juxiao.xchat.manager.external.zego.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.zego")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class ZegoConf {

    private String appId;

    private String secret;

    private String tokenUrl;
}
