package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.RoomMasterDto;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import com.erban.admin.main.vo.RoomRobotGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomRobotGroupMapperMgr {
    List<RoomRobotGroupVo> selectByParam(RoomRobotGroupParam roomRobotGroupParam);

    List<RoomRobotGroupVo> selectRoomOnlineNumByParam(RoomRobotGroupParam roomRobotGroupParam);

    List<RoomMasterDto> queryRoomMasterAccountByParams(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
