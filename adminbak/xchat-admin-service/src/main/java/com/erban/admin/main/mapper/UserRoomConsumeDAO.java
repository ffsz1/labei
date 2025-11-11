package com.erban.admin.main.mapper;

import com.erban.admin.main.domain.UserRoomConsume;
import com.erban.admin.main.dto.UserRoomConsumeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/9 11:55
 */
public interface UserRoomConsumeDAO {

    /**
     * 保存房间消费记录
     * @param roomConsume
     * @return
     */
    int saveConsume(UserRoomConsume roomConsume);

    /**
     * 统计房间用户消费记录
     * @param beginDate
     * @param endDate
     * @return
     */
    List<UserRoomConsumeDTO> countUserConsume(@Param("roomUid") Long roomUid, @Param("beginDate") String beginDate,
                                              @Param("endDate") String endDate);
    
}
