package com.juxiao.xchat.dao.guild.dto;

import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/12 20:18
 * @作者： carl
 */
@Data
public class GuildHallMemberListDTO {
    private Long uid;
    private Long erbanNo;
    private String nick;
    private String avatar;
    private int type;  //成员类型：0（会长），1（厅主），2（厅成员）
}
