package com.juxiao.xchat.service.api.guild.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import com.juxiao.xchat.service.api.guild.GuildHallMemberService;
import com.juxiao.xchat.service.api.guild.vo.GuildHallMemberInfoVo;
import com.juxiao.xchat.service.api.guild.vo.GuildMemberPageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 16:18
 * @作者： carl
 */
@Service
public class GuildHallMemberServiceImpl implements GuildHallMemberService {

    @Autowired
    private GuildHallMemberManager guildHallMemberManager;


    @Override
    public GuildHallMemberInfoVo getGuildMemberCommonInfo(Long uid) throws Exception {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        GuildHallMemberDTO guildHallMemberDTO = guildHallMemberManager.getCommonInfo(uid);

        GuildHallMemberInfoVo vo = new GuildHallMemberInfoVo();
        vo.setMemberInfo(guildHallMemberDTO);
        vo.setMemberType(this.getMemberType(uid, guildHallMemberDTO));

        return vo;
    }

    @Override
    public GuildMemberPageInfoVo getGuildMemberPageInfo(Long uid) throws Exception {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        GuildHallMemberDTO memberInfo = guildHallMemberManager.getCommonInfo(uid);
        if (memberInfo == null) {
            throw new WebServiceException(WebServiceCode.GUILD_MEMBER_NOT_EXIST);
        }

        List<GuildDailyTurnoverReportDO> turnovers = guildHallMemberManager.getMemberTurnovers(uid, memberInfo.getHallId());

        GuildMemberPageInfoVo vo = new GuildMemberPageInfoVo();
        vo.setMemberInfo(memberInfo);
        vo.setTurnovers(turnovers);
        vo.setMemberType(this.getMemberType(uid, memberInfo));

        return vo;
    }

    private int getMemberType(Long uid, GuildHallMemberDTO guildHallMemberDTO) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (guildHallMemberDTO == null) {
            return 3;
        }

        return guildHallMemberDTO.getMemberType().intValue();
    }
}
