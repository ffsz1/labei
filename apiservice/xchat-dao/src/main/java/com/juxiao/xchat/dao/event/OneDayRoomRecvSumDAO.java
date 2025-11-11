package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OneDayRoomRecvSumDAO {
    /**
     * 获取最新变动的魅力数据
     *
     * @param offset
     * @param limit
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select distinct (recv_uid) as uid from one_day_room_recv_sum order by ctrb_id desc limit #{offset}, #{limit}")
    List<Long> queryLastData(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 根据UID获取最新变动的魅力数据
     *
     * @param offset
     * @param limit
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("select distinct (recv_uid) as uid from one_day_room_recv_sum where recv_uid <> #{uid} order by ctrb_id desc limit #{offset}, #{limit}")
    List<Long> queryLastsData(@Param("offset") Integer offset, @Param("limit") Integer limit, @Param("uid") Long uid);
}
