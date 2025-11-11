package com.juxiao.xchat.dao.event;

import com.juxiao.xchat.dao.event.domain.UserSignCardDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserSignCardDAO {

    @Select("SELECT * FROM user_sign_card WHERE uid = #{uid} AND chip_id = #{chipId} ")
    List<UserSignCardDO> listChip(@Param("uid") Long uid, @Param("chipId") Long chipId);

    @Insert("INSERT INTO user_sign_card (" +
            "   uid, chip_id, chip_num, create_date" +
            ") VALUES (" +
            "   #{uid}, #{chipId}, #{chipNum}, #{createDate}" +
            ")")
    int insert(UserSignCardDO cardDO);

    @Update("UPDATE user_sign_card SET chip_num = #{num} WHERE uid = #{uid} AND chip_id = #{chipId} ")
    int updateChipNum(@Param("uid") Long uid, @Param("chipId") Long chipId, @Param("num") Integer num);
}
