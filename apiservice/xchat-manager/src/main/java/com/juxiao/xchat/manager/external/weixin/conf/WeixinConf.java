package com.juxiao.xchat.manager.external.weixin.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 微信配置信息
 *
 * @class: WeixinConf.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.weixin")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class WeixinConf {
    private String appid;
    private String secret;
    private String mchid;
    private String paykey;
    private String notifyUrl;
    private String wxappId;
    private String wxSecret;
    private String encodingAesKey;
}
