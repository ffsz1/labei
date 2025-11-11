package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserConfigureDO {
    private Long uid;
    private Byte superiorBouns;
    private Byte occupationRatio;
    private String bankCard;
    private String cardholder;
    private Byte greenRecom;
}