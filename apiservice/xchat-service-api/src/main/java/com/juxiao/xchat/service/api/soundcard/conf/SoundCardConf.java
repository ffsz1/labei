package com.juxiao.xchat.service.api.soundcard.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @class: SoundCardConf.java
 * @author: chenjunsheng
 * @date 2018/5/25
 */
@Getter
@Setter
@Component
@Configuration
@PropertySource("classpath:soundcard.properties")
@ConfigurationProperties(prefix = "service.soundcard")
public class SoundCardConf {
    private List<SoundCardTimbres> males;

    private List<SoundCardTimbres> females;
}