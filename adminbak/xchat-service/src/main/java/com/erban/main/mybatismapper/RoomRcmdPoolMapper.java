package com.erban.main.mybatismapper;

import com.erban.main.model.RoomRcmdPool;
import com.erban.main.model.RoomRcmdPoolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoomRcmdPoolMapper {
    int countByExample(RoomRcmdPoolExample example);

    int deleteByExample(RoomRcmdPoolExample example);

    int insert(RoomRcmdPool record);

    int insertSelective(RoomRcmdPool record);

    int inserts(@Param("list") List<RoomRcmdPool> list);

    List<RoomRcmdPool> selectByExample(RoomRcmdPoolExample example);

    int updateByExampleSelective(@Param("record") RoomRcmdPool record, @Param("example") RoomRcmdPoolExample example);

    int updateByExample(@Param("record") RoomRcmdPool record, @Param("example") RoomRcmdPoolExample example);

    @Select("SELECT room_fk_id FROM room_rcmd_pool WHERE rcmd_id = #{rcmdId}")
    List<Long> listRoomFkId(@Param("rcmdId") Integer rcmdId);
}
