package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.FamilySendGiftRecordDTO;
import com.erban.main.model.RoomFlowWeekVo;
import com.erban.main.param.admin.FamilyFlowParam;
import com.erban.main.vo.admin.StatFamilyFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.mapper
 * @date 2018/9/4
 * @time 18:08
 */
public interface FamilyGiftRecordMapper {

    /**
     * 查询
     * @return
     */
    List<RoomFlowWeekVo> sumByTowWeeks();

    List<StatFamilyFlowVo> statFamilyFlow(FamilyFlowParam familyFlowParam);

    int deleteByTeamId(@Param("teamId") Long teamId);

    List<Long> selectTeamIdByFamilyGiftRecordList(@Param("teamId") Long teamId);

    /**
     * 根据家族ID统计消费人数
     * @param teamId
     * @param startTime
     * @param endTime
     * @return
     */
    int countByConsumers(@Param("familyId") Long teamId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据家族编号ID查询礼物记录
     * @param teamId
     * @param startTime
     * @param endTime
     * @return
     */
    List<FamilySendGiftRecordDTO> selectByList(@Param("teamId") Long teamId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
