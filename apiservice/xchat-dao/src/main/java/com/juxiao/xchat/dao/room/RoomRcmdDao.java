package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomRcmdDTO;
import com.juxiao.xchat.dao.room.query.RoomRcmdQuery;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoomRcmdDao {

    /**
     * 查询有当前有效的推荐房间
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT * FROM room_rcmd WHERE start_date <= #{startDate} AND end_date >= #{endDate}")
    List<RoomRcmdDTO> listRcmd(RoomRcmdQuery query);
}
