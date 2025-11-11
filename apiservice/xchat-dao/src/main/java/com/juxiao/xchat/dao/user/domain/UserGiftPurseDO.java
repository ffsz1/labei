package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserGiftPurseDO {
    private Integer giftPurseId;
    private Long uid;
    private Integer giftId;
    private Integer countNum;
    private Date createTime;
}