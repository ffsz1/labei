package com.juxiao.xchat.dao.room.dto;

import lombok.Data;

import java.util.List;

/**
 * 描述：房间PK的结果信息
 *
 * @创建时间： 2020/10/28 20:37
 * @作者： carl
 */
@Data
public class RoomVsResultDTO {

    private Long id;

    private List<RoomVsResultTeamDTO> teams;

}
