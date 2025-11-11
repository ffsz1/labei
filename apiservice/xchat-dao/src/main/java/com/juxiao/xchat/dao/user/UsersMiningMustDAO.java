package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author chris
 * @Title:
 * @date 2018/10/15
 * @time 16:36
 */
public interface UsersMiningMustDAO {

    /**
     * 根据uid更新全服配置为失效
     * @param uid
     * @return
     */
    @TargetDataSource
    @Update({"update users_mining_must set status = 2 where uid = #{uid}"})
    int updateByStatus(@Param("uid") Long uid);

}
