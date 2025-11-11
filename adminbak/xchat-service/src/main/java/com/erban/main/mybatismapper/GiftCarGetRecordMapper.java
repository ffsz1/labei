package com.erban.main.mybatismapper;

import com.erban.main.model.GiftCarGetRecord;
import com.erban.main.model.GiftCarGetRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiftCarGetRecordMapper {
    int countByExample(GiftCarGetRecordExample example);

    int deleteByExample(GiftCarGetRecordExample example);

    int deleteByPrimaryKey(Long recordId);

    int insert(GiftCarGetRecord record);

    int insertSelective(GiftCarGetRecord record);

    List<GiftCarGetRecord> selectByExample(GiftCarGetRecordExample example);

    List<GiftCarGetRecord> selectRecord(@Param("uids") List<Long> uids, @Param("startDate") String startDate, @Param("endDate") String endDate);

    GiftCarGetRecord selectByPrimaryKey(Long recordId);

    int updateByExampleSelective(@Param("record") GiftCarGetRecord record, @Param("example") GiftCarGetRecordExample example);

    int updateByExample(@Param("record") GiftCarGetRecord record, @Param("example") GiftCarGetRecordExample example);

    int updateByPrimaryKeySelective(GiftCarGetRecord record);

    int updateByPrimaryKey(GiftCarGetRecord record);
}
