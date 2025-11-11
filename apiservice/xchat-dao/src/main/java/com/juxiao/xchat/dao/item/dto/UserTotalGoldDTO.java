package com.juxiao.xchat.dao.item.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserTotalGoldDTO {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 用户总金币
     */
    private Long totalGoldNum;
}
