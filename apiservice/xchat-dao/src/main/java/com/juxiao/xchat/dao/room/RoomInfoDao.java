package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomInfoDO;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: alwyn
 * @Description: 房间信息
 * @Date: 2018/9/4 12:46
 */
public interface RoomInfoDao {

    @Select("REPLACE INTO room_info " +
            "   (room_uid, play_info) " +
            "VALUES " +
            "   (#{roomUid}, #{playInfo})")
    void saveOrUpdate(RoomInfoDO infoDO);

    @TargetDataSource(name = "ds2")
    @Select("SELECT room_uid as roomUid,play_info as playInfo from room_info where room_uid = #{roomUid}")
    RoomInfoDO selectByRoomUid(Long roomUid);
}
