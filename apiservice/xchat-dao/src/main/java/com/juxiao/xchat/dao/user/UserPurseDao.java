package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserPurseDO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UserPurseSumDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户钱包操作接口
 *
 * @class: UserPurseDao.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface UserPurseDao {

    /**
     * 插入用户钱包数据
     *
     * @param purseDo
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource
    void save(UserPurseDO purseDo);

    /**
     * 砖石兑换金币
     *
     * @param uid
     * @param goldAmount
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `gold_num`=`gold_num`+#{goldAmount},`charge_gold_num`=`charge_gold_num` + #{goldAmount},`diamond_num`=`diamond_num`-#{diamondCost},`update_time`=now() WHERE `uid`=#{uid} and `diamond_num`-#{diamondCost} >= 0")
    int updateAddGoldReduceDiamond(@Param("uid") Long uid, @Param("goldAmount") Long goldAmount, @Param("diamondCost") Double diamondCost);

    /**
     * 更新用户金币增加
     *
     * @param uid
     * @param goldAmount
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET is_first_charge = 0, `gold_num`=`gold_num`+#{goldAmount},`charge_gold_num`=`charge_gold_num` + #{goldAmount},`update_time`=now() WHERE `uid`=#{uid}")
    int updateGoldAmount(@Param("uid") Long uid, @Param("goldAmount") Long goldAmount);

    /**
     * 更新用户金币消耗
     *
     * @param uid
     * @param goldCost
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `gold_num`=`gold_num`-#{goldCost},`charge_gold_num`=`charge_gold_num` - #{goldCost},`update_time`=now() WHERE `uid`=#{uid} AND `gold_num` - #{goldCost} >= 0")
    int updateGoldCost(@Param("uid") Long uid, @Param("goldCost") Long goldCost);


    /**
     * 更新用户金币消耗和用户海螺次数增加
     *
     * @param uid
     * @param conchAmount
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `gold_num`=`gold_num`-#{goldCost},`charge_gold_num`=`charge_gold_num` - #{goldCost}, `conch_num`=`conch_num`+#{conchAmount}, `update_time`=now() WHERE `uid`=#{uid} AND `gold_num` - #{goldCost} >= 0")
    int updateGoldCostConchAmount(@Param("uid") Long uid, @Param("goldCost") Long goldCost , @Param("conchAmount") Long conchAmount);


    /**
     * 更新用户海螺次数减少
     *
     * @param uid
     * @param conchCost
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `conch_num`=`conch_num`-#{conchCost},`update_time`=now() WHERE `uid`=#{uid} AND `conch_num` - #{conchCost} >= 0")
    int updateConchCost(@Param("uid") Long uid, @Param("conchCost") Long conchCost);


    @TargetDataSource
    @Update("UPDATE `user_purse` SET `trycoin_num`=`trycoin_num`-#{trycoinCost},`update_time`=now() WHERE `uid`=#{uid} AND `trycoin_num` - #{trycoinCost} >= 0")
    int updateTryCoinCost(@Param("uid") Long uid, @Param("trycoinCost") Long trycoinCost);

    /**
     * 更新用户钻石增加
     *
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `diamond_num`=`diamond_num`+#{diamondAmount},`update_time`=now() WHERE `uid`=#{uid}")
    int updateDiamondAmount(@Param("uid") Long uid, @Param("diamondAmount") Double diamondAmount);

    /**
     * 更新用户钻石消耗
     *
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_purse` SET `diamond_num`=`diamond_num`-#{diamondCost},`update_time`=now() WHERE `uid`=#{uid} and `diamond_num`-#{diamondCost} >= 0")
    int updateDiamondCost(@Param("uid") Long uid, @Param("diamondCost") Double diamondCost);

    /**
     * 获取用户钱包值
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    @TargetDataSource(name = "ds2")
    UserPurseDTO getUserPurse(@Param("uid") Long uid);

    @TargetDataSource(name = "ds2")
    @Select("SELECT SUM(gold_num) as goldSum, SUM(diamond_num) as diamondSum FROM user_purse")
    UserPurseSumDTO sumUserPurse();
}
