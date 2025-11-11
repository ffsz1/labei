package com.erban.main.mybatismapper;

import com.erban.main.model.HeadwearGetRecord;
import com.erban.main.model.HeadwearGetRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HeadwearGetRecordMapper {
    int countByExample(HeadwearGetRecordExample example);

    int deleteByExample(HeadwearGetRecordExample example);

    int deleteByPrimaryKey(Long recordId);

    int insert(HeadwearGetRecord record);

    int insertSelective(HeadwearGetRecord record);

    List<HeadwearGetRecord> selectByExample(HeadwearGetRecordExample example);

    List<HeadwearGetRecord> selectRecord(@Param("uids") List<Long> uids, @Param("startDate") String startDate, @Param("endDate") String endDate);

    HeadwearGetRecord selectByPrimaryKey(Long recordId);

    int updateByExampleSelective(@Param("record") HeadwearGetRecord record, @Param("example") HeadwearGetRecordExample example);

    int updateByExample(@Param("record") HeadwearGetRecord record, @Param("example") HeadwearGetRecordExample example);

    int updateByPrimaryKeySelective(HeadwearGetRecord record);

    int updateByPrimaryKey(HeadwearGetRecord record);
}
