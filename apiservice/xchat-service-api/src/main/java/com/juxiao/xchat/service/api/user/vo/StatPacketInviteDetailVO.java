package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatPacketInviteDetailVO {
    private Long uid;
    private Integer todayRegisterCount;
    private Integer totalRegisterCount;
    private Double packetNum;
    private Byte superiorBouns;
    private Integer directlyNum;
    private Integer lowerNum;
}
