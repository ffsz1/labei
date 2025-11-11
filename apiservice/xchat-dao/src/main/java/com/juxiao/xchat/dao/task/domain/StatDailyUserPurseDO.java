package com.juxiao.xchat.dao.task.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatDailyUserPurseDO {
    private Long id;
    private Long goldSum;
    private Double diamondSum;
    private Double giftNomalDiamondSum;
    private Double giftDrawDiamondSum;
}
