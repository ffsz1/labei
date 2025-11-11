package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.PrettyErbanNoRecord;
import com.xchat.oauth2.service.model.PrettyErbanNoRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PrettyErbanNoRecordMapper {
    int countByExample(PrettyErbanNoRecordExample example);

    int deleteByExample(PrettyErbanNoRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(PrettyErbanNoRecord record);

    int insertSelective(PrettyErbanNoRecord record);

    List<PrettyErbanNoRecord> selectByExample(PrettyErbanNoRecordExample example);

    PrettyErbanNoRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") PrettyErbanNoRecord record, @Param("example") PrettyErbanNoRecordExample example);

    int updateByExample(@Param("record") PrettyErbanNoRecord record, @Param("example") PrettyErbanNoRecordExample example);

    int updateByPrimaryKeySelective(PrettyErbanNoRecord record);

    int updateByPrimaryKey(PrettyErbanNoRecord record);
}
