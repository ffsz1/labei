package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.UserWordDrawDO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawDTO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface UserWordDrawDao {

    @Insert("insert into user_word_draw (uid, word, activity_type, is_use, create_time, update_time)" +
        "value(#{uid}, #{word}, #{activityType}, #{isUse}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userWordDrawId", keyColumn = "user_word_draw_id")
    void insert(UserWordDrawDO entity);

    @Update("update user_word_draw set is_use = #{isUse}, update_time = #{updateTime} where user_word_draw_id = #{userWordDrawId}")
    int updateIsUseById(@Param("userWordDrawId")Integer userWordDrawId, @Param("isUse")Boolean isUse, @Param("updateTime")Date updateTime);

    @Update("update user_word_draw set is_use = #{isUse}, update_time = #{updateTime} where uid = #{uid} and activity_type = #{activityType}")
    int updateIsUseByUidActivityType(@Param("uid") Long uid, @Param("activityType") Integer activityType,
                                     @Param("isUse")Boolean isUse, @Param("updateTime")Date updateTime);


    @Select("select * from user_word_draw where uid = #{uid} and activity_type = #{activityType} and is_use = #{isUse} ")
    List<UserWordDrawDTO> queryListBy(@Param("uid") Long uid, @Param("activityType") Integer activityType, @Param("isUse") Boolean isUse);


    @Select("select * from user_word_draw where uid = #{uid} and activity_type = #{activityType} and word = #{word} limit 1")
    UserWordDrawDTO queryByWord(@Param("uid") Long uid, @Param("activityType") Integer activityType, @Param("word") String word);




}
