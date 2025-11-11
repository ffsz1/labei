package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserConfigureDTO {
    private Long uid;
    private Byte superiorBouns;
    private Byte occupationRatio;
    private String bankCard;
    private String cardholder;
    private Byte greenRecom;
    private Byte newUsers;
    private Byte gameRoom;
}