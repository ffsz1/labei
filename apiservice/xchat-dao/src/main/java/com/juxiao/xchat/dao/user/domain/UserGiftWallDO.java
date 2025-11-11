package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserGiftWallDO {
    private Long giftWallId;
    private Long uid;
    private Integer giftId;
    private String giftName;
    private String picUrl;
    private Integer reciveCount;
    private Date createTime;
}