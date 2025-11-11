package com.erban.main.mybatismapper;

import com.erban.main.model.RoomRobotGroupRela;
import com.erban.main.model.RoomRobotGroupRelaExample;
import java.util.List;

public interface RoomRobotGroupRelaMapper {
    int deleteByExample(RoomRobotGroupRelaExample example);

    int deleteByPrimaryKey(Integer relaId);

    int insert(RoomRobotGroupRela record);

    int insertSelective(RoomRobotGroupRela record);

    List<RoomRobotGroupRela> selectByExample(RoomRobotGroupRelaExample example);

    RoomRobotGroupRela selectByPrimaryKey(Integer relaId);

    int updateByPrimaryKeySelective(RoomRobotGroupRela record);

    int updateByPrimaryKey(RoomRobotGroupRela record);
}
