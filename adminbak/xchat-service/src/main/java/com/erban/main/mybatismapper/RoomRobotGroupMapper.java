package com.erban.main.mybatismapper;

import com.erban.main.model.RoomRobotGroup;
import com.erban.main.model.RoomRobotGroupExample;
import java.util.List;

public interface RoomRobotGroupMapper {
    int deleteByExample(RoomRobotGroupExample example);

    int deleteByPrimaryKey(Integer groupId);

    int insert(RoomRobotGroup record);

    int insertSelective(RoomRobotGroup record);

    List<RoomRobotGroup> selectByExample(RoomRobotGroupExample example);

    RoomRobotGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(RoomRobotGroup record);

    int updateByPrimaryKey(RoomRobotGroup record);
}
