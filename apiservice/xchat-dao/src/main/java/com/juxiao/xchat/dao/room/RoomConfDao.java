package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.room.dto.RoomConfDTO;
import org.apache.ibatis.annotations.Param;

public interface RoomConfDao {

    /**
     * 查询房间配置信息
     *
     * @param roomUid
     * @return
     */
    RoomConfDTO getRoomConf(@Param("roomUid") Long roomUid);
}
