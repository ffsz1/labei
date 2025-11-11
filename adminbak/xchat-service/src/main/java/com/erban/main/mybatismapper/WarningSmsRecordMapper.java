package com.erban.main.mybatismapper;

import com.erban.main.dto.WarningSmsRecordDTO;
import com.erban.main.model.WarningSmsRecord;
import com.erban.main.model.WarningSmsRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WarningSmsRecordMapper {
    int countByExample(WarningSmsRecordExample example);

    int deleteByExample(WarningSmsRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(WarningSmsRecord record);

    int insertSelective(WarningSmsRecord record);

    List<WarningSmsRecord> selectByExample(WarningSmsRecordExample example);

    WarningSmsRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") WarningSmsRecord record, @Param("example") WarningSmsRecordExample example);

    int updateByExample(@Param("record") WarningSmsRecord record, @Param("example") WarningSmsRecordExample example);

    int updateByPrimaryKeySelective(WarningSmsRecord record);

    int updateByPrimaryKey(WarningSmsRecord record);

    List<WarningSmsRecordDTO> selectByPage(@Param("search") String searchText);

}
