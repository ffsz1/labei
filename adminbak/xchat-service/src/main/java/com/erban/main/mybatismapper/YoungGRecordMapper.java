package com.erban.main.mybatismapper;

import com.erban.main.model.YoungGRecord;
import com.erban.main.model.YoungGRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YoungGRecordMapper {
    int countByExample(YoungGRecordExample example);

    int deleteByExample(YoungGRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(YoungGRecord record);

    int insertSelective(YoungGRecord record);

    List<YoungGRecord> selectByExample(YoungGRecordExample example);

    YoungGRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") YoungGRecord record, @Param("example") YoungGRecordExample example);

    int updateByExample(@Param("record") YoungGRecord record, @Param("example") YoungGRecordExample example);

    int updateByPrimaryKeySelective(YoungGRecord record);

    int updateByPrimaryKey(YoungGRecord record);
}
