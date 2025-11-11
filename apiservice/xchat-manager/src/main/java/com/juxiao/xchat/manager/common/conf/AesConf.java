package com.juxiao.xchat.manager.common.conf;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import com.juxiao.xchat.base.utils.AESUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "common.aes")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class AesConf {
    private String key;
    private String iv;

    public String encryptData(String json) throws Exception {
        String encryptData = AESUtils.encrypt(json, key, iv);
        JSONObject body = new JSONObject();
        body.put("ed", encryptData);
        return body.toJSONString();
    }
}
