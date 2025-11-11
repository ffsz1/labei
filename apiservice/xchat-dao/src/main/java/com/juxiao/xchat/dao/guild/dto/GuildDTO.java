package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 20:27
 * @作者： carl
 */
@Data
public class GuildDTO {
    private Long id;

    private String guildNo;

    private String name;

    private String logoUrl;

    private Long presidentUid;

    private Integer hallCount;

    private Integer memberCount;

    private Date createTime;

    private Long gold;
}
