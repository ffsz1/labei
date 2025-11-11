package com.juxiao.xchat.service.api.guild.vo;

import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 20:48
 * @作者： carl
 */
@Data
public class GuildMemberPageInfoVo {

    private GuildHallMemberDTO memberInfo;

    private List<GuildDailyTurnoverReportDO> turnovers;

    private int memberType;
}
