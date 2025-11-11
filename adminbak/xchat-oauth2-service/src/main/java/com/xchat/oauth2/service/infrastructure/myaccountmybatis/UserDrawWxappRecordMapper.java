package com.xchat.oauth2.service.infrastructure.myaccountmybatis;


import com.xchat.oauth2.service.model.UserDrawWxappRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
public interface UserDrawWxappRecordMapper {

    @Insert("INSERT INTO `user_wxapp_draw_record`(`uid`, `target_uid`, `share_type`, `share_date`, `create_time`, `update_time`) VALUES (#{uid}, #{targetUid}, #{shareType}, #{shareDate}, NOW(), NOW());")
    int save(UserDrawWxappRecord record);

    @Insert("INSERT INTO `user_wxapp_draw`(`uid`,`total_draw_count`,`left_draw_count`,`update_time`) VALUES (#{uid}, 1, 1, NOW()) ON DUPLICATE KEY UPDATE `total_draw_count` = `total_draw_count` + 1, `left_draw_count` = `left_draw_count` + 1;")
    int saveOrUpdateUserWxappDraw(@Param("uid") Long uid);
}
