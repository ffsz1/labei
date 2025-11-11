package com.juxiao.xchat.service.api.guild.vo;

import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 17:42
 * @作者： carl
 */
@Data
public class GuildDetailVo {
    private GuildDTO guild;
    private GuildHallDTO myHall;
    private List<GuildHallDTO> halls;

    private boolean hasJoin; //是否已加入公会
}
