package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UserConfigureDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @class: UserConfigureDao.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserConfigureDao {

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT " +
            "   uid AS uid, " +
            "   superior_bouns AS superiorBouns, " +
            "   occupation_ratio AS occupationRatio, " +
            "   bank_card AS bankCard, " +
            "   cardholder AS cardholder, " +
            "   green_recom AS greenRecom," +
            "   new_users AS newUsers," +
            "   game_room AS gameRoom " +
            "FROM user_configure " +
            "WHERE uid = #{uid,jdbcType=BIGINT}")
    UserConfigureDTO getUserConfigure(@Param("uid") Long uid);
}