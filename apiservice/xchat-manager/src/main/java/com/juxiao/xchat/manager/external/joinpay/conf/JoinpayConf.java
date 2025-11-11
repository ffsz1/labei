package com.juxiao.xchat.manager.external.joinpay.conf;


import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "external.joinpay")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class JoinpayConf {
    private String merchantNo;
    private String notifyUrl;
    private String wxAppId;
    private String gzhAppId;
    private String tradeMerchantNo;
    private String tradeMerchantNoAPP3; // app+报备商户号
}
