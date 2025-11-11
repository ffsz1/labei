package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserGiftBonusPerDay {
    private Integer bonusId;
    private Long uid;
    private Double curDiamondNum;
    private Double forecastDiamondNum;
    private Double bonusDiamondNum;
    private Boolean todayHasFinishBonus;
    private Date statDate;
    private Date bonusFinishDate;
}