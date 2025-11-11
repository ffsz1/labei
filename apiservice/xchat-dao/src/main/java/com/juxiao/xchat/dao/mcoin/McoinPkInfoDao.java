package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.dto.McoinPkInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface McoinPkInfoDao {

    @TargetDataSource(name = "ds2")
    @Select("SELECT `id` as id, `term` as term, `title` as title, `red_answer` as redAnswer, `blue_answer` as blueAnswer, `red_pic` as redPic, `blue_pic` as bluePic, `red_polls` as redPolls, `blue_polls` as bluePolls, `lottery_time` as lotteryTime, `carve_up_mcoin_num` as carveUpMcoinNum, `pk_status` as pkStatus, `create_time` as createTime, `update_time` as updateTime FROM `mcoin_pk_info` WHERE `pk_status`=#{pkStatus}")
    McoinPkInfoDTO getForStatusNormal(Byte pkStatus);

    @TargetDataSource
    @Update("UPDATE `mcoin_pk_info` SET `red_polls` = #{redPolls},`blue_polls` = #{bluePolls},`carve_up_mcoin_num` = #{carveUpMcoinNum},`pk_status` = 2, `update_time` = now() WHERE `id`=#{id} AND `term` >= #{term}")
    int updatePoolsAndNum(@Param("redPolls") Integer redPolls, @Param("bluePolls") Integer bluePolls, @Param("carveUpMcoinNum") Integer carveUpMcoinNum, @Param("id") Long id, @Param("term") Integer term);

    @TargetDataSource
    @Update("UPDATE `mcoin_pk_info` SET `pk_status` = #{pkStatus}, `update_time` = now() WHERE `id`=#{id} AND `term` >= #{term}")
    int updatePkStatus(@Param("id") Long id, @Param("term") Integer term, @Param("pkStatus") Byte pkStatus);

    @TargetDataSource(name = "ds2")
    @Select("SELECT `id` as id, `term` as term, `title` as title, `red_answer` as redAnswer, `blue_answer` as blueAnswer, `red_pic` as redPic, `blue_pic` as bluePic, `red_polls` as redPolls, `blue_polls` as bluePolls, `lottery_time` as lotteryTime, `carve_up_mcoin_num` as carveUpMcoinNum, `pk_status` as pkStatus, `create_time` as createTime, `update_time` as updateTime FROM `mcoin_pk_info` WHERE `term`=#{term}")
    McoinPkInfoDTO getByTerm(Integer term);

    @TargetDataSource
    @Update("UPDATE mcoin_pk_info SET pk_status = 1, push_msg_status = 1, update_time = NOW() WHERE term = #{term}")
    Integer updateNextTermBeginning(Integer term);

    @TargetDataSource
    @Select("SELECT term FROM mcoin_pk_info WHERE pk_status = 0 order by term asc limit 1")
    int findByNextTerm();

    /**
     * 根据期号获取活动正在进行的活动推送状态
     * @param term
     * @return
     */
    @TargetDataSource
    @Select("SELECT push_msg_status as pushMsgStatus FROM mcoin_pk_info WHERE pk_status = 1 AND term = #{term}")
    Byte getPushMsgStatusByTerm(int term);

    /**
     * 获取活动正在进行且未推送全服的活动期号
     * @return
     */
    @TargetDataSource
    @Select("SELECT term FROM mcoin_pk_info WHERE pk_status = 1 and push_msg_status = 0 order by term asc limit 1")
    Integer getTermByPushMsgAndPkStatus();
}
