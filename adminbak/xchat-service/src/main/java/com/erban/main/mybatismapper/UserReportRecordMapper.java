package com.erban.main.mybatismapper;

import com.erban.main.model.UserReportRecord;
import com.erban.main.model.dto.UserReportRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserReportRecordMapper {

    int insert(UserReportRecord reportRecord);

    List<UserReportRecordDTO> selectRoomIdByList(@Param("roomId") Long roomId,@Param("beginDate")String beginDate, @Param("endDate")String endDate,@Param("page")Integer page,@Param("size")Integer size);

    List<UserReportRecordDTO> selectRoomId(@Param("roomId") Long roomId);
}
