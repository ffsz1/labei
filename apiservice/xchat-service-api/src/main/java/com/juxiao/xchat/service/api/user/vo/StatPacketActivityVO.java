package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatPacketActivityVO {
    private Long uid;

    private Integer shareCount;

    private Double packetNum;

    private Integer packetCount;

    private Integer registerCout;

    private Double chargeBonus;

    private Integer todayRegisterCount;

}
