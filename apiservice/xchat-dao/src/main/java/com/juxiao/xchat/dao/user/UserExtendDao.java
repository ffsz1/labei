package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserPacketDO;
import com.juxiao.xchat.dao.user.dto.UserExtendDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserExtendDao {


    @TargetDataSource
    @Insert("insert into user_extend(uid, liveness)value(#{uid}, #{liveness})")
    void insert(UserExtendDTO userExtend);

    /**
     * 增加活跃值
     * @param uid
     * @param increaseNum
     */
    @TargetDataSource
    @Insert("update user_extend set liveness = liveness + #{increaseNum} where uid = #{uid}")
    void increaseLiveness(@Param("uid") Long uid, @Param("increaseNum") Integer increaseNum);

    @TargetDataSource(name = "ds2")
    @Select("select * from user_extend where uid=#{uid}")
    UserExtendDTO getByUid(@Param("uid") Long uid);

}
