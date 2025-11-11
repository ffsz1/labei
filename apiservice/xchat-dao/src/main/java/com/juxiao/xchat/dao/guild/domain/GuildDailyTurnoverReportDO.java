package com.juxiao.xchat.dao.guild.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GuildDailyTurnoverReportDO {
    private Long id;

    private Long guildId;

    private Long hallId;

    private Long roomId;

    private Long memberId;

    private Long gold;

    private Date reportDate;

    private Date createTime;
}