package com.erban.main.mybatismapper;

import com.erban.main.model.GiftCarPurseRecord;
import com.erban.main.model.GiftCarPurseRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GiftCarPurseRecordMapper {
    int countByExample(GiftCarPurseRecordExample example);

    int deleteByExample(GiftCarPurseRecordExample example);

    int deleteByPrimaryKey(Long recordId);

    int insert(GiftCarPurseRecord record);

    int insertSelective(GiftCarPurseRecord record);

    List<GiftCarPurseRecord> selectByExample(GiftCarPurseRecordExample example);

    GiftCarPurseRecord selectByPrimaryKey(Long recordId);

    int updateByExampleSelective(@Param("record") GiftCarPurseRecord record, @Param("example") GiftCarPurseRecordExample example);

    int updateByExample(@Param("record") GiftCarPurseRecord record, @Param("example") GiftCarPurseRecordExample example);

    int updateByPrimaryKeySelective(GiftCarPurseRecord record);

    int updateByPrimaryKey(GiftCarPurseRecord record);
}
