package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.room.domain.RoomVsTeamDO;

public interface RoomVsTeamDao {
    int deleteByPrimaryKey(Long id);

    int insert(RoomVsTeamDO record);

    int insertSelective(RoomVsTeamDO record);

    RoomVsTeamDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomVsTeamDO record);

    int updateByPrimaryKey(RoomVsTeamDO record);
}