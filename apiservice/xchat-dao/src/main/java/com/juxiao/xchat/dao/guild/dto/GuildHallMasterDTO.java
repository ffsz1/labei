package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 10:13
 * @作者： carl
 */
@Data
public class GuildHallMasterDTO {
    private Long guildId;

    private Long hallId;   //厅id
    private String hallNo;
    private String hallTitle;
    private Long uid;
    private Integer memberCount;
    private String hallLogoUrl;

    /**
     * 厅流水数据
     */
    private TurnoverDWADto hallTurnover;
}
