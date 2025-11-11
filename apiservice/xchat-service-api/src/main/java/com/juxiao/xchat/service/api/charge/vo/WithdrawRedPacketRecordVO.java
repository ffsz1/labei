package com.juxiao.xchat.service.api.charge.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawRedPacketRecordVO {

    private Long uid;
    private String packetName;
    private Double packetNum;
    private Date createTime;
}
