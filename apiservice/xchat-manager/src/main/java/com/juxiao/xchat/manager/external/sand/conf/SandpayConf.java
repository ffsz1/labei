package com.juxiao.xchat.manager.external.sand.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "external.sandpay")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class SandpayConf {
    private String publicKey;           // 杉德公钥
    private String privateKey;          // 商户号对应的私钥和密码
    private String privateKeyPassword;  // 私钥密码
    private String merchMid;            // 商户基础信息
    private String merchPlmid;          // 商户基础信息
    private String merchProductcode;    // 商户基础信息
    private String gateWayUrl;
    private String gateWayFrontUrl;     // 商户前台跳转地址
    private String gateWayBacknoticeUrl; // 杉德公钥
}
