package com.juxiao.xchat.service.api.guild.vo;

import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import com.juxiao.xchat.dao.guild.dto.TurnoverDWADto;
import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/23 15:18
 * @作者： carl
 */
@Data
public class GuildHallTurnoverVo {
    private Long guildId;
    private Long hallId;
    private Long hallUid;
    private Long erbanNo;
    private int memberCount;

    private Long roomId;
    private String roomTitle;
    private String roomTag;
    private String roomAvatar;

    private Long gold;

    public GuildHallTurnoverVo() {}

    public GuildHallTurnoverVo(GuildHallDTO guildHallDTO) {
        this.setGuildId(guildHallDTO.getGuildId());
        this.setHallId(guildHallDTO.getHallId());
        this.setHallUid(guildHallDTO.getHallUid());
        this.setErbanNo(guildHallDTO.getErbanNo());
        this.setMemberCount(guildHallDTO.getMemberCount());
        this.setRoomId(guildHallDTO.getRoomId());
        this.setRoomTitle(guildHallDTO.getRoomTitle());
        this.setRoomTag(guildHallDTO.getRoomTag());
        this.setRoomAvatar(guildHallDTO.getRoomAvatar());
        this.setGold(guildHallDTO.getGold());
    }

    /**
     * 厅流水数据
     */
    private TurnoverDWADto hallTurnover;
}
