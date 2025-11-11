package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.UserMcoinPurseDO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMcoinPurseDao {

    /**
     * 保存用户的萌币余额
     *
     * @param purseDo
     */
    @TargetDataSource
    @Insert("INSERT IGNORE INTO `user_mcoin_purse`(`uid`, `mcoin_num`, `purse_status`, `create_time`, `update_time`) VALUES (#{uid}, #{mcoinNum}, 1, #{createTime}, #{updateTime})")
    void save(UserMcoinPurseDO purseDo);

    /**
     * 更新用户金币增加
     *
     * @param uid
     * @param mcoinAmount
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_mcoin_purse` SET `mcoin_num` = `mcoin_num` + #{mcoinAmount}, `update_time` = now() WHERE `uid`=#{uid}")
    int updateMcoinAmount(@Param("uid") Long uid, @Param("mcoinAmount") Integer mcoinAmount);

    /**
     * 更新用户金币消耗
     *
     * @param uid
     * @param mcoinCost
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `user_mcoin_purse` SET `mcoin_num` = `mcoin_num` - #{mcoinCost}, `update_time` = now() WHERE `uid`=#{uid} AND `mcoin_num` >= #{mcoinCost}")
    int updateMcoinCost(@Param("uid") Long uid, @Param("mcoinCost") Integer mcoinCost);

    /**
     * 查询用户的萌币余额
     *
     * @param uid
     * @return
     */
    @TargetDataSource
    @Select("SELECT `uid` as uid, `mcoin_num` as mcoinNum, `purse_status` as purseStatus, `create_time` as createTime, `update_time` as updateTime FROM `user_mcoin_purse` WHERE `uid`=#{uid}")
    UserMcoinPurseDTO getUserMcoinPurse(@Param("uid") Long uid);
}
