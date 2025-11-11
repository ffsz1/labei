package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatPacketActivityDO {
    private Long uid;
    private Integer shareCount;
    private Integer sharePacketCount;
    private Date latestShareDate;
    private Integer packetCount;
    private Integer registerCout;
    private Integer todayRegisterCount;
    private Date latestRegisterDate;
    private Double chargeBonus;
    private Double todayChargeBonus;
    private Date latestChargeBonusDate;
    private Date createTime;
    private Date updateTime;
}