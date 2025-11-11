package com.juxiao.xchat.manager.common.item.conf;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 送礼爆出神秘礼物的概率
 * @Date: 2018/9/13 19:26
 */
@Data
@Component
@ConfigurationProperties(prefix = "gift.mystic")
public class MysticGiftConfig {

    /** 能爆出神秘礼物的, 礼物列表 */
    private List<Integer> giftList = Lists.newArrayList();
    /** 神秘礼物ID */
    private Integer giftId;
    /** 送礼爆出神秘礼物开关 */
    private boolean open = false;
    /** 爆出礼物的倍数 */
    private int pro = 1;

}
