package com.erban.main.mybatismapper;

import com.erban.main.model.FamilyTeamRecord;
import com.erban.main.model.FamilyTeamRecordExample;
import com.erban.main.model.RoomFlowWeekVo;
import com.erban.main.param.admin.FamilyFlowParam;
import com.erban.main.vo.admin.StatFamilyFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FamilyTeamRecordMapper {
    int countByExample(FamilyTeamRecordExample example);

    int deleteByExample(FamilyTeamRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FamilyTeamRecord record);

    int insertSelective(FamilyTeamRecord record);

    List<FamilyTeamRecord> selectByExample(FamilyTeamRecordExample example);

    FamilyTeamRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FamilyTeamRecord record, @Param("example") FamilyTeamRecordExample example);

    int updateByExample(@Param("record") FamilyTeamRecord record, @Param("example") FamilyTeamRecordExample example);

    int updateByPrimaryKeySelective(FamilyTeamRecord record);

    int updateByPrimaryKey(FamilyTeamRecord record);

    List<StatFamilyFlowVo> statFamilyFlow(FamilyFlowParam familyFlowParam);

    List<RoomFlowWeekVo> sumByTowWeeks();
}
