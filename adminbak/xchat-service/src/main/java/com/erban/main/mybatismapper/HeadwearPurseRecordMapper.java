package com.erban.main.mybatismapper;

import com.erban.main.model.HeadwearPurseRecord;
import com.erban.main.model.HeadwearPurseRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HeadwearPurseRecordMapper {
    int countByExample(HeadwearPurseRecordExample example);

    int deleteByExample(HeadwearPurseRecordExample example);

    int deleteByPrimaryKey(Long recordId);

    int insert(HeadwearPurseRecord record);

    int insertSelective(HeadwearPurseRecord record);

    List<HeadwearPurseRecord> selectByExample(HeadwearPurseRecordExample example);

    HeadwearPurseRecord selectByPrimaryKey(Long recordId);

    int updateByExampleSelective(@Param("record") HeadwearPurseRecord record, @Param("example") HeadwearPurseRecordExample example);

    int updateByExample(@Param("record") HeadwearPurseRecord record, @Param("example") HeadwearPurseRecordExample example);

    int updateByPrimaryKeySelective(HeadwearPurseRecord record);

    int updateByPrimaryKey(HeadwearPurseRecord record);
}
