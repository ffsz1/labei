package com.juxiao.xchat.manager.external.qiniu.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
        import lombok.Getter;
        import lombok.Setter;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.context.annotation.PropertySource;
        import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.qiniu")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class QiniuConf {
    private String accessUrl;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
