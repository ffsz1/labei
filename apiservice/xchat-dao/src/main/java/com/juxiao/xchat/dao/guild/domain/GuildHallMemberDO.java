package com.juxiao.xchat.dao.guild.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GuildHallMemberDO {
    private Long id;

    private Long hallId;

    private Long guildId;

    private Long uid;

    private Date createTime;

    private Date updateTime;

    private Boolean isDel;

    private Integer memberType;     // 成员类型：0（会长），1（厅主），2（厅成员）；
}