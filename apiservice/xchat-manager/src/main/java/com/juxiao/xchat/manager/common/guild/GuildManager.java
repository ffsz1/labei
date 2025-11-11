package com.juxiao.xchat.manager.common.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;

import java.util.List;


public interface GuildManager {

    GuildDTO getGuild(Long id);

    List<GuildDO> getGuildValidList();
}
