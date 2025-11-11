package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.room.domain.RoomVsTeamUserDO;

public interface RoomVsTeamUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(RoomVsTeamUserDO record);

    int insertSelective(RoomVsTeamUserDO record);

    RoomVsTeamUserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomVsTeamUserDO record);

    int updateByPrimaryKey(RoomVsTeamUserDO record);
}