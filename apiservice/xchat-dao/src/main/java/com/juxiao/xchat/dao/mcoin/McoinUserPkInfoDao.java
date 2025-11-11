package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.McoinUserPkInfoDO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserPkInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface McoinUserPkInfoDao {

    @TargetDataSource
    @Insert("INSERT IGNORE INTO `mcoin_user_pk_info`(`id`,`uid`, `term`, `red_polls`, `blue_polls`, `pay_mcoin_number`, `carve_up_mcoin_num`, `arrival_mcoin`, `create_time`) VALUES (#{id},#{uid}, #{term}, #{redPolls}, #{bluePolls}, #{payMcoinNumber}, #{carveUpMcoinNum}, #{arrivalMcoin}, #{createTime})")
    void save(McoinUserPkInfoDO userPkInfoDO);
    /**
     * 根据期数和uid查询用户的萌币PK信息
     * @param term
     * @param uid
     * @return
     */
    @TargetDataSource
    @Select("SELECT `id` as id,`uid` as uid, `term` as term, `red_polls` as redPolls, `blue_polls` as bluePolls, `pay_mcoin_number` as payMcoinNumber, `carve_up_mcoin_num` as carveUpMcoinNum, `arrival_mcoin` as arrivalMcoin, `create_time` as createTime FROM `mcoin_user_pk_info` WHERE `term`=#{term} AND `uid`=#{uid}")
    McoinUserPkInfoDTO getForTermAndUid(@Param("term") Integer term, @Param("uid") Long uid);

    /**
     * 根据期数查询用户的萌币PK信息列表
     * @param term
     * @return
     */
    @TargetDataSource
    @Select("SELECT `id` as id,`uid` as uid, `term` as term, `red_polls` as redPolls, `blue_polls` as bluePolls, `pay_mcoin_number` as payMcoinNumber, `carve_up_mcoin_num` as carveUpMcoinNum, `arrival_mcoin` as arrivalMcoin, `create_time` as createTime FROM `mcoin_user_pk_info` WHERE `term`=#{term}")
    List<McoinUserPkInfoDTO> getForTerm(@Param("term") Integer term);

    /**
     * 根据uid查询用户的萌币PK信息列表
     * @param uid
     * @return
     */
    @TargetDataSource
    @Select("SELECT `id` as id,`uid` as uid, `term` as term, `red_polls` as redPolls, `blue_polls` as bluePolls, `pay_mcoin_number` as payMcoinNumber, `carve_up_mcoin_num` as carveUpMcoinNum, `arrival_mcoin` as arrivalMcoin, `create_time` as createTime FROM `mcoin_user_pk_info` WHERE `uid` = #{uid}")
    List<McoinUserPkInfoDTO> getForUid(@Param("uid") Long uid);

    /**
     * 更新支持数量和瓜分萌币数
     * @param redPolls
     * @param bluePolls
     * @param payMcoinNumber
     * @param carveUpMcoinNum
     * @param arrivalMcoin

     * @param uid
     * @param term
     * @return
     */
    @TargetDataSource
    @Update("UPDATE `mcoin_user_pk_info` SET `red_polls` = #{redPolls},`blue_polls` = #{bluePolls},`pay_mcoin_number` = #{payMcoinNumber},`carve_up_mcoin_num` = #{carveUpMcoinNum},`arrival_mcoin` = #{arrivalMcoin}, `update_time` = now() WHERE `uid`=#{uid} AND `term` >= #{term}")
    int updatePollsAndMcoin(@Param("redPolls") Integer redPolls, @Param("bluePolls") Integer bluePolls, @Param("payMcoinNumber") Integer payMcoinNumber, @Param("carveUpMcoinNum") Integer carveUpMcoinNum, @Param("arrivalMcoin") Byte arrivalMcoin, @Param("uid") Long uid, @Param("term") Integer term);
}
