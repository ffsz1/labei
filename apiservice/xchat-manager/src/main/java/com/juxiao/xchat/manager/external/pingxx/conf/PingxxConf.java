package com.juxiao.xchat.manager.external.pingxx.conf;


import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @class: PingxxConfiger.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.pingxx")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class PingxxConf {
    private String appId;
    private String apiKey;
    private String publicKey;
    private String privateKeyPath;
}
