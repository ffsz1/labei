package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UserRealNameDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 15:33
 */
public interface UserRealNameDao {

    @TargetDataSource
    @Insert("REPLACE INTO `user_real_name`" +
            "(`uid`, `real_name`, `id_card_no`, `id_card_front`, `id_card_opposite`, `id_card_handheld`, " +
            "`audit_status`, `create_date`, `phone`) " +
            "VALUES " +
            "(#{uid}, #{realName}, #{idCardNo}, #{idCardFront}, #{idCardOpposite}, #{idCardHandheld}, 0, " +
            "#{createDate}, #{phone})")
    @Options(useGeneratedKeys = true)
    int save(UserRealNameDTO UserRealNameDto);

    /**
     * 查询
     * @param idCardNo
     * @return int
     */
    @TargetDataSource
    @Select("select count(1) from user_real_name where `id_card_no` = #{idcardNo}")
    int countIdCardNo(@Param("idcardNo") String idCardNo);
}
