package com.juxiao.xchat.dao.room;

import org.apache.ibatis.annotations.Update;

public interface RoomGameConfigDAO {

    /**
     *  更新配置状态
     * @param id
     * @param status
     * @return
     */
    @Update("UPDATE room_game_config SET status = #{status} WHERE id = #{id} ")
    int updateByStatus(Integer id,Integer status);
}
