package com.juxiao.xchat.manager.common.draw.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "common.giftdraw")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class GiftDrawConf {
    private int ticketGiftId;   // 人气票礼物
    private int defaultGiftId;  // 默认礼物
    private int fullGiftId;     // 全服礼物

    /**
     * 海螺
     */
    private int[] drawGifts;
    private double[] firstDrawRates;// 首次礼物概率
    private double[] highDrawRates; // 高概率礼物概率
    private double[] lowDrawRates;  // 低概率礼物概率
    /**
     * 相亲
     */
    private int[] xqDrawGifts;
    private double[] xqFirstDrawRates;  // 首次礼物概率
    private double[] xqLowDrawRates;    // 高概率礼物概率
    private double[] xqHighDrawRates;   // 低概率礼物概率
    /**
     * 活动
     */
    private int[] hdDrawGifts;
    private double[] hdFirstDrawRates;  // 首次礼物概率
    private double[] hdLowDrawRates;    // 高概率礼物概率
    private double[] hdHighDrawRates;   // 低概率礼物概率

    private int[] giftPrices;
    private int[] tmpDrawGifts;     // 临时捡海螺礼物列表
    private int[] tmpDrawGiftsPrice; // 临时捡海螺礼物价格
    private Map<Integer, DrawTypeConf> drawTypes; // 所有捡海螺类型
}
