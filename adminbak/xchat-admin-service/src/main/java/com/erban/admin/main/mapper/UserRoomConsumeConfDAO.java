package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.UserRoomDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/9 11:55
 */
public interface UserRoomConsumeConfDAO {

    List<Long> listRoomUid();

    Long getByUid(Long roomUid);

    int insert(Long roomUid);

    List<UserRoomDTO> listRoom(@Param("erbanNo") Long erbanNo);

    long count();

    int delete(Long roomUid);
}
