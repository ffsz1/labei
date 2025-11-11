package com.juxiao.xchat.service.api.guild.vo;

import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/16 15:29
 * @作者： carl
 */
@Data
public class GuildHallMemberInfoVo {
    private GuildHallMemberDTO memberInfo;

    private int memberType;
}
