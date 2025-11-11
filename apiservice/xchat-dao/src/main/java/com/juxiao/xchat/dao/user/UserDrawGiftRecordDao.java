package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.dto.RoomDrawGiftDayDTO;
import com.juxiao.xchat.dao.item.dto.RoomDrawRecordDTO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRankDO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRecordDO;
import com.juxiao.xchat.dao.user.query.DrawGiftRecordQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @class: UserDrawGiftRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface UserDrawGiftRecordDao {
    /**
     * 保持抽奖记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void save(UserDrawGiftRecordDO recordDo);

    /**
     * 查询房间流水
     *
     * @param query
     * @return
     */
    List<RoomDrawRecordDTO> listRoomDraw(DrawGiftRecordQuery query);

    /**
     * 按时间段查询房间捡海螺流水
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT r.room_id as roomId, count(*) as drawCount, count(*) * 20 as goldCount FROM user_draw_gift_record r WHERE r.`create_time` >= #{startDate} AND r.create_time <= #{endDate} GROUP BY r.`room_id`")
    List<RoomDrawGiftDayDTO> listDayRoomDrawGift(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 批量保存
     *
     * @param list
     * @return
     */
    @TargetDataSource
    int saveMany(List<UserDrawGiftRecordDO> list);

    /**
     * 查询房间玩法榜单
     * @return
     */
    @TargetDataSource
    List<UserDrawGiftRankDO> listRoomRankByGiftType(@Param("giftType") Integer giftType, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
