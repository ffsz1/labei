package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UserWxappDrawDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserWxappDrawDao {

    @Update("UPDATE `user_wxapp_draw` SET `left_draw_count`=`left_draw_count` - 1 WHERE uid = #{uid}")
    void updateReduceLeftDrawCount(@Param("uid") Long uid);

    @Update("UPDATE `user_wxapp_draw` SET `win_draw_count`=`win_draw_count` + 1 WHERE uid = #{uid}")
    void updateAddWinDrawCount(@Param("uid") Long uid);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT uid as uid, total_draw_count as totalDrawCount, left_draw_count as leftDrawCount, win_draw_count as winDrawCount, update_time as updateTime FROM `user_wxapp_draw` where uid = #{uid}")
    UserWxappDrawDTO getUserWxappDraw(@Param("uid") Long uid);
}
