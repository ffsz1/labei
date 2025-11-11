package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.StatBasicUsersDO;
import org.apache.ibatis.annotations.*;

public interface StatBasicUsersDao {

    @TargetDataSource
    @Insert("INSERT INTO `stat_basic_users` (uid, room_uid, create_time) VALUES (#{uid}, #{roomUid}, #{createTime})")
    @Options(useGeneratedKeys = true)
    int save(StatBasicUsersDO basicUsersDTO);

    @TargetDataSource
    @Delete("DELETE from `stat_basic_users` WHERE create_time <DATE_SUB(CURDATE(), INTERVAL 3 DAY)")
    void deleteUnuse();

    /**
     * 查询最新更新的一条数据
     *
     * @param roomUid
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select * from `stat_basic_users` where uid=#{uid} AND room_uid = #{roomUid} ORDER BY create_time DESC LIMIT 1")
    StatBasicUsersDO getLastUpdateTime(@Param("roomUid") Long roomUid, @Param("uid") Long uid);
}
