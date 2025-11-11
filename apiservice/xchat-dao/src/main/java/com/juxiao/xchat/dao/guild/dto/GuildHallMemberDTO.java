package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 14:28
 * @作者： carl
 */
@Data
public class GuildHallMemberDTO {
    private Long guildId;   //公会id
    private String guildNo;
    private String guildName;
    private Long guildPresidentUid;   //会长uid
    private String guildLogoUrl;

    private Long hallId;   //厅id
    private Long hallUid;  //所属厅主uid
    private Long uid;
    private Long memberId;  // 成员id

    private String nick;
    private Long erbanNo;
    private String avatar;
    private Integer memberType;

    /**
     * 公会流水数据
     */
    private TurnoverDWADto guildTurnover;
    /**
     * 厅流水数据
     */
    private TurnoverDWADto hallTurnover;
    /**
     * 个人流水数据
     */
    private TurnoverDWADto memberTurnover;
}
