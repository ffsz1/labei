package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRecord;
import com.erban.main.model.NobleRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleRecordMapper {
    int countByExample(NobleRecordExample example);

    int deleteByExample(NobleRecordExample example);

    int deleteByPrimaryKey(String recordId);

    int insert(NobleRecord record);

    int insertSelective(NobleRecord record);

    List<NobleRecord> selectByExample(NobleRecordExample example);

    NobleRecord selectByPrimaryKey(String recordId);

    int updateByExampleSelective(@Param("record") NobleRecord record, @Param("example") NobleRecordExample example);

    int updateByExample(@Param("record") NobleRecord record, @Param("example") NobleRecordExample example);

    int updateByPrimaryKeySelective(NobleRecord record);

    int updateByPrimaryKey(NobleRecord record);
}
