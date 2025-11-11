package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
/**
 * 礼物排名
 */
public class RankActDO implements Serializable{
    private long uid;
    private long erbanNo;
    private String nick;
    private String avatar;
    // 等级值
    private Integer experLevel;
    // 魅力值
    private Integer charmLevel;
    // 数量
    private long total;

}
