package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.item.dto.RoomDrawRecordDTO;
import com.juxiao.xchat.dao.user.domain.UserDrawGiftRoomDayDO;
import com.juxiao.xchat.dao.user.query.DrawGiftRecordQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房间捡海螺日流水
 */
public interface UserDrawGiftRoomDayDao {

    /**
     * 保存房间捡海螺日流水
     *
     * @param roomDayDo
     */
    @Insert("INSERT INTO `user_draw_gift_room_day`(`room_id`, `create_date`, `draw_count`, `gold_count`) VALUES (#{roomId}, #{createDate}, #{drawCount}, #{goldCount}) ON DUPLICATE KEY UPDATE `draw_count`=#{drawCount}, `gold_count`= #{goldCount}")
    void saveOrUpdate(UserDrawGiftRoomDayDO roomDayDo);

    /**
     * @param query
     * @return
     */
    @Select("SELECT `draw_count` as frequency, `gold_count` as totalGoldNum, `create_date` as createTime FROM `user_draw_gift_room_day` WHERE `room_id`=#{roomId} and `create_date` >= #{beginDate}")
    List<RoomDrawRecordDTO> listRoomDrawRecord(DrawGiftRecordQuery query);
}
