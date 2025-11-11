package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserGiftPurseDO;
import com.juxiao.xchat.dao.user.dto.UserGiftPurseDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserGiftPurseDao {

    /**
     * @param purseDo
     */
    @TargetDataSource
    void save(UserGiftPurseDO purseDo);

    /**
     * @param purseDo
     */
    @TargetDataSource
    void update(UserGiftPurseDO purseDo);

    @TargetDataSource
    @Update("UPDATE `user_gift_purse` SET `count_num` = `count_num` - #{countNum} WHERE uid = #{uid} AND gift_id = #{giftId} AND count_num - #{countNum} >=0")
    int updateReduceCountNum(UserGiftPurseDO purseDo);

    /**
     * 获取用户的礼品余额
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT `gift_purse_id` AS giftPurseId,`uid` AS uid,`gift_id` AS giftId,`count_num`AS countNum,`create_time`AS createTime FROM user_gift_purse WHERE uid = #{uid} and gift_id = #{giftId} ")
    UserGiftPurseDTO getUserGiftPurse(@Param("uid") Long uid, @Param("giftId") Integer giftId);

    @TargetDataSource(name = "ds2")
    Double sumGiftDiamond(@Param("giftType") Integer giftType);
}