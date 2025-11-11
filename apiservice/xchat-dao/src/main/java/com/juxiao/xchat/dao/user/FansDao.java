package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.FansDO;
import com.juxiao.xchat.dao.user.dto.FansDTO;
import com.juxiao.xchat.dao.user.dto.FansFollowDTO;
import com.juxiao.xchat.dao.user.query.UserFansQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 粉丝
 *
 * @class: FansDao.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface FansDao {

    /**
     * 保存喜欢用户
     *
     * @param fansDo fansDo
     */
    @TargetDataSource
    @Insert("INSERT IGNORE INTO `fans` (`like_uid`,`liked_uid`,`create_time`) VALUES (#{likeUid,jdbcType=BIGINT}, #{likedUid,jdbcType=BIGINT},#{createTime,jdbcType=TIMESTAMP})")
    @Options(useGeneratedKeys = true, keyProperty = "fanId", keyColumn = "fan_id")
    void save(FansDO fansDo);

    /**
     * 删除记录
     *
     * @param fanId
     */
    @TargetDataSource
    @Delete("DELETE FROM `fans` WHERE `fan_id` = #{fanId}")
    void delete(@Param("fanId") Long fanId);

    /**
     * 查询用户关注数量
     *
     * @param likeUid
     * @return
     */
    @TargetDataSource
    @Select("SELECT COUNT(*) FROM `fans` WHERE `like_uid`=#{likeUid}")
    int countUserFollow(@Param("likeUid") Long likeUid);

    /**
     * 更新用户的粉丝数量
     *
     * @param likedUid
     * @return
     */
    @TargetDataSource
    @Select("SELECT COUNT(*) FROM `fans` WHERE `liked_uid`=#{likedUid}")
    int countUserFans(@Param("likedUid") Long likedUid);

    /**
     * @param likeUid
     * @param likedUid
     * @return
     */
    @TargetDataSource
    @Select("SELECT COUNT(*) FROM `fans` WHERE `like_uid`=#{likeUid} AND `liked_uid`=#{likedUid}")
    int countLikeBetween(@Param("likeUid") Long likeUid, @Param("likedUid") Long likedUid);

    /**
     * 查看用户是否喜欢另一个
     *
     * @param likeUid  用户
     * @param likedUid 喜欢的用户
     * @return
     */
    @TargetDataSource
    FansDTO getUserLike(@Param("likeUid") Long likeUid, @Param("likedUid") Long likedUid);

    /**
     * 统计
     *
     * @param query
     * @return
     */
    int coutFollowOrFans(UserFansQuery query);

    /**
     * 查询我关注的用户列表
     *
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<FansFollowDTO> listFollowOrFans(UserFansQuery query);
}