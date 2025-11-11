package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.UserIntoRoomRecordDO;
import org.apache.ibatis.annotations.Insert;

/**
 * 用户进房记录DAO
 */
public interface UserIntoRoomRecordDao {
    @TargetDataSource
    @Insert("INSERT INTO `user_into_room_record` (`uid`, `room_uid`, `create_date`, `type`) VALUES (#{uid}, #{roomUid}, #{createDate}, #{type});")
    int save(UserIntoRoomRecordDO recordDo);
}
