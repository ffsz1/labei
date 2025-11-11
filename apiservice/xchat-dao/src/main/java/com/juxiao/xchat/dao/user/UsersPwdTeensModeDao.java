package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UsersPwdTeensMode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 青少年模式Dao
 * @author chris
 * @date 2019-07-03
 */
public interface UsersPwdTeensModeDao {
    /**
     * 插入青少年模式信息或更新青少年模式密码
     *
     * @param usersPwdTeensMode 青少年模式信息
     * @return int 返回受影响行数
     */
    @TargetDataSource
    @Insert("INSERT INTO `users_pwd_teens_mode`(`uid`, `pwd`, `create_date`) VALUES (#{uid}, #{pwd}, #{createDate}) " +
            "ON DUPLICATE KEY UPDATE `pwd` = #{pwd}")
    int saveOrUpdate(UsersPwdTeensMode usersPwdTeensMode);

    /**
     * 根据用户UID查询青少年模式信息
     *
     * @param uid 用户UID
     * @return UsersPwdTeensMode
     */
    @TargetDataSource(name = "ds2")
    @Select("select uid,pwd from `users_pwd_teens_mode` where uid = #{uid}")
    UsersPwdTeensMode selectUsersPwdTeensModeByUid(@Param("uid") Long uid);

    /**
     * 删除该用户的青少年模式信息
     *
     * @param uid 用户UID
     * @return int 返回受影响行数
     */
    @TargetDataSource
    @Delete("delete from users_pwd_teens_mode where uid = #{uid}")
    int deleteTeensMode(@Param("uid") Long uid);
}
