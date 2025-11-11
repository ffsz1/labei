package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.UserConsumeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/9 11:55
 */
public interface UserRoomTaskDAO {

    /**
     * 统计房间的消费信息
     * @param roomUid
     * @param beginDate
     * @param endDate
     * @return
     */
    List<UserConsumeDTO> countUserConsumeByRoom(@Param("roomUid") Long roomUid,
                                                @Param("beginDate") String beginDate,
                                                @Param("endDate") String endDate);

}
