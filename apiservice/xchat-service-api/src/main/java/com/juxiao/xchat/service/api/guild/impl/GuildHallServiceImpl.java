package com.juxiao.xchat.service.api.guild.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import com.juxiao.xchat.manager.common.guild.GuildHallManager;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import com.juxiao.xchat.manager.common.guild.GuildManager;
import com.juxiao.xchat.service.api.guild.GuildHallService;
import com.juxiao.xchat.service.api.guild.vo.GuildHallDetailVo;
import com.juxiao.xchat.service.api.guild.vo.GuildHallTurnoverVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/16 11:49
 * @作者： carl
 */
@Service
public class GuildHallServiceImpl implements GuildHallService {

    @Autowired
    private GuildManager guildManager;

    @Autowired
    private GuildHallManager guildHallManager;

    @Autowired
    private GuildHallMemberManager guildHallMemberManager;

    @Override
    public GuildHallDetailVo getHallDetail(Long hallId, Long uid) throws WebServiceException {
        if (hallId == null || uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        GuildHallDetailVo vo = new GuildHallDetailVo();

        GuildHallDTO hallDTO = guildHallManager.getHall(hallId);
        if (hallDTO == null) {
            throw new WebServiceException(WebServiceCode.GUILD_HALL_NOT_EXIST);
        }
        GuildHallTurnoverVo hallTurnoverVo = new GuildHallTurnoverVo(hallDTO);

        // 当前用户是会长或厅主才显示厅流水
        if (uid.compareTo(hallDTO.getHallUid()) == 0) {
            hallTurnoverVo.setHallTurnover(guildHallMemberManager.getTurnover_room(hallDTO.getGuildId(), hallDTO.getHallId()));
        }
        else {
            GuildDTO guildDTO = guildManager.getGuild(hallDTO.getGuildId());
            if (uid.compareTo(guildDTO.getPresidentUid()) == 0) {
                hallTurnoverVo.setHallTurnover(guildHallMemberManager.getTurnover_room(hallDTO.getGuildId(), hallDTO.getHallId()));
            }
        }

        vo.setHall(hallTurnoverVo);
        if (hallTurnoverVo.getHallUid().compareTo(uid) == 0) {
            List<GuildDailyTurnoverReportDO> turnovers = guildHallMemberManager.getMemberTurnovers(uid, hallId);
            vo.setTurnovers(turnovers);
        }

        return vo;
    }
}
