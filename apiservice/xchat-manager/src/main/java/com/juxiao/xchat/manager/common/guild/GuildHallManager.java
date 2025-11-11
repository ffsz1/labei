package com.juxiao.xchat.manager.common.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;

import java.util.Date;
import java.util.List;

public interface GuildHallManager {

    GuildHallDTO getHall(Long id);

    /**
     * 获取公会的厅列表，按当日流水倒序排序
     * @param guildId
     * @return
     */
    List<GuildHallDTO> getHallListOrderByGoldDesc(Long guildId, Date date);

    /**
     * 根据成员uid获取厅信息
     * @param uid
     * @return
     */
    GuildHallDTO getHallByMemberUid(Long uid) throws WebServiceException;
}
