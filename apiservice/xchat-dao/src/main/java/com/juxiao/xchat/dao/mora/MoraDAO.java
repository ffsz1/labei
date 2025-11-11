package com.juxiao.xchat.dao.mora;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mora.dto.MoraAwardDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-03
 * @time 14:20
 */
public interface MoraDAO {

    /**
     * 根据概率获取奖励信息
     * @param probability 概率(1.高 2.中 3.低)
     * @return MoraAwardDTO
     */
    @TargetDataSource(name = "ds2")
    @Select("select id,json,probability,is_use as isUse,grade,create_time as createTime from mora_award where probability = #{probability}")
    MoraAwardDTO selectByProbability(Integer probability);

    /**
     * 获取概率
     * @return List
     */
    @TargetDataSource(name = "ds2")
    @Select("select id,json,probability,is_use as isUse,grade,create_time as createTime from mora_award where is_use = 1")
    List<MoraAwardDTO> selectByMoraAward();
}
