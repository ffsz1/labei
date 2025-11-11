package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.GiftCarRecordDTO;
import com.erban.admin.main.dto.HeadwearRecordDTO;
import com.erban.admin.main.vo.GiveGiftcarVo;
import com.erban.admin.main.vo.RoomRobotGroupParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GiveGiftcarMapperMgr {
    List<GiveGiftcarVo> selectByParam(RoomRobotGroupParam roomRobotGroupParam);

    List<GiftCarRecordDTO> selectRecord(@Param("uids") List<Long> uids, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
