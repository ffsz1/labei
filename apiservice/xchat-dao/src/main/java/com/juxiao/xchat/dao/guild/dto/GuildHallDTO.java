package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

/**
 * 描述：厅的相关信息
 *
 * @创建时间： 2020/10/10 18:41
 * @作者： carl
 */
@Data
public class GuildHallDTO {
    private Long guildId;
    private Long hallId;
    private Long hallUid;
    private Long erbanNo;
    private int memberCount;

    private Long roomId;
    private String roomTitle;
    private String roomTag;
    private String roomAvatar;

    private Long gold;
}
