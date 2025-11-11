package com.juxiao.xchat.service.api.guild.vo;

import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/16 11:44
 * @作者： carl
 */
@Data
public class GuildHallDetailVo {

    private GuildHallTurnoverVo hall;

    private List<GuildDailyTurnoverReportDO> turnovers;
}
