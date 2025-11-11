package com.juxiao.xchat.service.api.guild;

import com.juxiao.xchat.service.api.guild.vo.GuildHallMemberInfoVo;
import com.juxiao.xchat.service.api.guild.vo.GuildMemberPageInfoVo;

public interface GuildHallMemberService {

    GuildHallMemberInfoVo getGuildMemberCommonInfo(Long uid) throws Exception;

    GuildMemberPageInfoVo getGuildMemberPageInfo(Long uid) throws Exception;
}
