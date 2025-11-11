package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserGiftPurseDTO {
    private Integer giftPurseId;
    private Long uid;
    private Integer giftId;
    private Integer countNum;   // 礼物数量
    private Date createTime;
}