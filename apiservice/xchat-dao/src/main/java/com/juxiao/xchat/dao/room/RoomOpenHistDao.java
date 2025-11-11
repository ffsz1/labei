package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.dto.RoomOpenHistDTO;
import org.apache.ibatis.annotations.Insert;

public interface RoomOpenHistDao {

    @TargetDataSource
    @Insert("INSERT INTO room_open_hist(hist_id, uid, room_id, meeting_name, `type`, reward_money, serv_dura, close_type, open_time, close_time, dura) VALUES (#{histId}, #{uid}, #{roomId}, #{meetingName}, #{type}, #{rewardMoney}, #{servDura}, #{closeType}, #{openTime}, #{closeTime}, #{dura})")
    void insert(RoomOpenHistDTO record);
}
