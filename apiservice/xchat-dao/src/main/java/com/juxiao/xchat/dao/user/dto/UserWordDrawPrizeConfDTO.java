package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserWordDrawPrizeConfDTO {

    private Integer id;

    private Integer activityType;

    private Integer prizeType;

    private Integer prizeObjId;

    private Integer prizeGoldAmount;

    private Integer status;


}
