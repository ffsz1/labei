package com.erban.main.mybatismapper.dao;

import com.erban.main.model.domain.HotManualRuleDO;
import com.erban.main.model.dto.HotManualRuleDTO;
import com.erban.main.mybatismapper.query.HotManualRuleQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 上热门的时间配置规则
 */
public interface HotManualRuleDAO {

    @Insert("INSERT INTO hot_manual_rule ( " +
            "   start_date, end_date, week_days, create_date, status, uid, seq_no, view_type " +
            ") VALUES ( " +
            "   #{startDate}, #{endDate}, #{weekDays}, #{createDate}, #{status}, #{uid}, #{seqNo}, #{viewType} " +
            ")")
    @Options(useGeneratedKeys = true)
    int insert(HotManualRuleDO ruleDO);

    @Update("UPDATE " +
            "   hot_manual_rule " +
            "SET " +
            "   start_date = #{startDate}, " +
            "   end_date = #{endDate}, " +
            "   week_days = #{weekDays}, " +
            "   status = #{status}, " +
            "   uid = #{uid}, " +
            "   seq_no = #{seqNo}," +
            "   view_type = #{viewType} " +
            "WHERE " +
            "   id = #{id}")
    int update (HotManualRuleDO ruleDO);

    @Select("UPDATE hot_manual_rule SET status = #{status} WHERE id = #{id} ")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM hot_manual_rule WHERE id = #{id}")
    int delete(Long id);

    HotManualRuleDTO getById(Long id);

    List<HotManualRuleDTO> listByParam(HotManualRuleQuery query);

    /**
     * 查询今天需要上热门的房间
     * @return
     */
    List<HotManualRuleDO> listByToday(HotManualRuleQuery query);
}
