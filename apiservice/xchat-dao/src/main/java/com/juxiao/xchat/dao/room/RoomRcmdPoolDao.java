package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoomRcmdPoolDao {

    @TargetDataSource(name = "ds2")
    @Select("SELECT room_fk_id FROM room_rcmd_pool WHERE rcmd_id = #{rcmdId}")
    List<Long> listRoomFkId(@Param("rcmdId") Integer rcmdId);
}
