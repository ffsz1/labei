package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserGiftWallDO;
import com.juxiao.xchat.dao.user.dto.UserGiftWallDTO;
import com.juxiao.xchat.dao.user.query.UserGiftWallOrderTypeQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @class: UserGiftWallDao.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserGiftWallDao {

    /**
     * 保存用户礼品墙
     *
     * @param wallDo
     */
    @TargetDataSource
    void save(UserGiftWallDO wallDo);

    /**
     * 更新
     *
     * @param wallDo
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_gift_wall` SET `recive_count` =`recive_count`+#{reciveCount,jdbcType=INTEGER} WHERE  `uid` = #{uid,jdbcType=BIGINT} AND `gift_id` = #{giftId,jdbcType=INTEGER}")
    int updateGiftWallReciveCount(UserGiftWallDO wallDo);

    /**
     * 查询用户礼品墙
     *
     * @param uid
     * @param giftId
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT COUNT(8) FROM `user_gift_wall` WHERE  `uid` = #{uid,jdbcType=BIGINT} AND `gift_id` = #{giftId,jdbcType=INTEGER}")
    int countUserGiftWall(@Param("uid") Long uid, @Param("giftId") Integer giftId);

    /**
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<UserGiftWallDTO> listUserGiftOrderTypeWall(UserGiftWallOrderTypeQuery query);
}