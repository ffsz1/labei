package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.vo.GiveHeadwearVo;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GiveHeadwearMapperMgr {
    List<GiveHeadwearVo> selectByParam(RoomRobotGroupParam roomRobotGroupParam);

    List<HeadwearRecordDTO> selectRecord(@Param("uids") List<Long> uids, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
