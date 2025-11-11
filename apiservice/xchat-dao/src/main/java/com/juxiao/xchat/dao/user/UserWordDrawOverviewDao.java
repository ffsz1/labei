package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.UserWordDrawOverviewDO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawOverviewDTO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface UserWordDrawOverviewDao {

    @Insert("insert into user_word_draw_overview (uid, activity_type, left_draw_num, total_draw_num, win_draw_num, create_time, update_time)" +
        "value(#{uid},  #{activityType}, #{leftDrawNum}, #{totalDrawNum}, #{winDrawNum}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(UserWordDrawOverviewDO entity);

    int save(UserWordDrawOverviewDO entity);

    @Select("select * from user_word_draw_overview where uid = #{uid} and activity_type = #{activityType}")
    UserWordDrawOverviewDTO queryByUidActivityType(@Param("uid")Long uid, @Param("activityType")Integer activityType);





}
