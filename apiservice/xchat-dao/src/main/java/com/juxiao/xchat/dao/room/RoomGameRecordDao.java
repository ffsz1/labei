package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.room.domain.RoomGameRecordDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoomGameRecordDao {

    @Select("SELECT * FROM room_game_record WHERE uid = #{uid} AND room_id = #{roomId} AND game_status = 0 ")
    List<RoomGameRecordDO> listRecord(@Param("uid") Long uid, @Param("roomId") Long roomId);

    @Insert("INSERT INTO room_game_record ( " +
            "   uid, room_id, type, result, game_status, create_time " +
            ") VALUES ( " +
            "   #{uid}, #{roomId}, #{type}, #{result}, #{gameStatus}, #{createTime} " +
            ") ")
    @Options(useGeneratedKeys = true, keyProperty = "recordId", keyColumn = "record_id")
    int insert(RoomGameRecordDO roomGameRecordDO);

    @Update("UPDATE room_game_record SET game_status = #{gameStatus} WHERE record_id = #{recordId} ")
    int updateStatus(RoomGameRecordDO roomGameRecordDO);
}
