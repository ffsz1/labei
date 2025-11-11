package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRecomRecord;
import com.erban.main.model.NobleRecomRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleRecomRecordMapper {
    int countByExample(NobleRecomRecordExample example);

    int deleteByExample(NobleRecomRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NobleRecomRecord record);

    int insertSelective(NobleRecomRecord record);

    List<NobleRecomRecord> selectByExample(NobleRecomRecordExample example);

    NobleRecomRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NobleRecomRecord record, @Param("example") NobleRecomRecordExample example);

    int updateByExample(@Param("record") NobleRecomRecord record, @Param("example") NobleRecomRecordExample example);

    int updateByPrimaryKeySelective(NobleRecomRecord record);

    int updateByPrimaryKey(NobleRecomRecord record);
}
