package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.user.domain.UserWordGetRecordDO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawPrizeConfDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserWordDrawPrizeConfDao {

    /**
     * 根据获取类型获取
     * @param activityType
     * @return
     */
    @Select("select * from user_word_draw_prize_conf where activity_type = #{activityType} and `status` = 1")
    List<UserWordDrawPrizeConfDTO> queryByActivityType(@Param("activityType") Integer activityType);





}
