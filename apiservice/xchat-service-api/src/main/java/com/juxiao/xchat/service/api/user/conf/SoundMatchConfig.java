package com.juxiao.xchat.service.api.user.conf;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 声音匹配配置
 * @Author: alwyn
 * @Date: 2018/11/26 16:29
 */
@Data
@Component
@ConfigurationProperties(prefix = "user.sound.match")
public class SoundMatchConfig {
    /**
     * 打招呼的礼物列表
     */
    private List<Integer> giftList = Lists.newArrayList(1, 70);

    /** 每页的条数 */
    private int randomPageSize = 20;

    /** 等级限制的最小等级 */
    private int leftLevel = 5;

    /** 低于等级限制的人,可以喜欢的人数 */
    private int likeNum = 10;
}
