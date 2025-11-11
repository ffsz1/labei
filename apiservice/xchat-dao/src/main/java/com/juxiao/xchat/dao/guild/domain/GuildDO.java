package com.juxiao.xchat.dao.guild.domain;

import lombok.Data;

import java.util.Date;

@Data
public class GuildDO {
    private Long id;

    private String guildNo;

    private String name;

    private String logoUrl;

    private Long presidentUid;

    private Date createTime;

    private Date updateTime;

    private Boolean isDel;
}