package com.juxiao.xchat.manager.common.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.dto.*;

import java.util.List;

public interface GuildHallMemberManager {

    GuildHallMemberDTO getCommonInfo(Long uid);

    boolean hasJoinGuild(Long uid);

    List<GuildDailyTurnoverReportDO> getMemberTurnovers(Long uid, Long hallId);

    List<GuildHallMemberListDTO> getGuildMembers(Long guildId) throws Exception;

    List<GuildHallMemberListDTO> getHallMembers(Long hallId) throws Exception;

    TurnoverDWADto getTurnover_president(Long guildId);

    TurnoverDWADto getTurnover_room(Long guildId, Long hallId);

    TurnoverDWADto getTurnover_user(Long guildId, Long hallId, Long memberId);
}
