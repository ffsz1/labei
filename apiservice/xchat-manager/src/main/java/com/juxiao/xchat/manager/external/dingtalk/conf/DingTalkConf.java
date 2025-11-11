package com.juxiao.xchat.manager.external.dingtalk.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chris
 * @Title: 钉钉机器人配置信息
 * @date 2018/10/8
 * @time 09:55
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.dingtalk")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class DingTalkConf {

    private String developChatbot;

    private String reportWebHook;

    private List<String> pm;

    private List<String> report;

    private List<String> programmer;
}
