package com.juxiao.xchat.service.api.event.dto;

import lombok.Data;

@Data
public class NoviceFirstChargeCheckInDTO {

    /**
     * uid
     */
    private Long uid;

    /**
     * 次数
     */

    private Integer num;
    /**
     * 充值时间
     */
    private String time;


}
