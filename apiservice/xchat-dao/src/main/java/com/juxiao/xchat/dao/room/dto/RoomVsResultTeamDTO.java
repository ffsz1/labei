package com.juxiao.xchat.dao.room.dto;

import lombok.Data;

import java.util.List;

/**
 * 描述：房间PK结果的队伍信息
 *
 * @创建时间： 2020/10/28 20:40
 * @作者： carl
 */
@Data
public class RoomVsResultTeamDTO {

    private Integer teamIndex;

    private boolean isWinner;

    private List<RoomVsResultTeamUserDTO> users;
}
