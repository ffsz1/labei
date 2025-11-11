package com.juxiao.xchat.dao.user;


import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UserDrawPrettyErbanNoDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @class: UserDrawPrettyErbanNoDao.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface UserDrawPrettyErbanNoDao {

    /**
     * @param erbanNo
     */
    @TargetDataSource
    @Update("UPDATE `user_draw_pretty_erban_no` SET `use_status`='2' WHERE pretty_erban_no=#{erbanNo}")
    void updateUsedPrettyErbanNo(@Param("erbanNo") String erbanNo);

    @TargetDataSource(name = "ds2")
    @Select("SELECT pretty_erban_no AS prettyErbanNo, `type` AS type, use_status AS `useStatus`, `use_erban_no` AS useErbanNo, seq AS seq, create_time AS createTime FROM `user_draw_pretty_erban_no` WHERE use_status = 1 AND `type` = #{type} LIMIT 1")
    UserDrawPrettyErbanNoDTO getNotUsePrettyErbanNo(@Param("type") Byte type);
}