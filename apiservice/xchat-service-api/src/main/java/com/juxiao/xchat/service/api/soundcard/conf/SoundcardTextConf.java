package com.juxiao.xchat.service.api.soundcard.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @class: SoundcardTextConf.java
 * @author: chenjunsheng
 * @date 2018/5/28
 */
@Getter
@Setter
@Component
@Configuration
@PropertySource("classpath:soundcard.properties")
@ConfigurationProperties(prefix = "service.soundcard.text")
public class SoundcardTextConf {

    List<String> males;

    List<String> females;
}
