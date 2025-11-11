package com.juxiao.xchat.dao.guild.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GuildHallDO {
    private Long id;

    private Long guildId;

    private Long roomId;

    private Date createTime;

    private Date updateTime;

    private Boolean isDel;
}