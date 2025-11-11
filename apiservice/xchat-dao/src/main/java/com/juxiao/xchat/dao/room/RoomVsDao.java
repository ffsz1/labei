package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.room.domain.RoomVsDO;

public interface RoomVsDao {
    int deleteByPrimaryKey(Long id);

    int insert(RoomVsDO record);

    int insertSelective(RoomVsDO record);

    RoomVsDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomVsDO record);

    int updateByPrimaryKey(RoomVsDO record);
}