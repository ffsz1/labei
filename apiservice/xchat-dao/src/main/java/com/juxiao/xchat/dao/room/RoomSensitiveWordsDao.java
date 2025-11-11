package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomSensitiveWordsDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 敏感词
 */
public interface RoomSensitiveWordsDao {

    /**
     * 根据类型查询 1 房间名, 2 聊天
     * @param type
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * from room_sensitive_words where type = #{type}")
    List<RoomSensitiveWordsDO> listByType(Integer type);
}
