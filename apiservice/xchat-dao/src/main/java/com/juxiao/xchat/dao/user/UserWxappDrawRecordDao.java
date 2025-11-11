package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UserTodayShareDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserWxappDrawRecordDao {

    @TargetDataSource(name = "ds2")
    @Select("SELECT `target_uid` as uid FROM `user_wxapp_draw_record` WHERE `uid` = #{uid} AND `share_type` = 1 AND `share_date`=CURRENT_DATE()")
    List<UserTodayShareDTO> listTodayShareUser(@Param("uid") Long uid);
}
