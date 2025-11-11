package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.PrettyErbanNo;
import com.xchat.oauth2.service.model.PrettyErbanNoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PrettyErbanNoMapper {
    int countByExample(PrettyErbanNoExample example);

    int deleteByExample(PrettyErbanNoExample example);

    int deleteByPrimaryKey(Integer prettyId);

    int insert(PrettyErbanNo record);

    int insertSelective(PrettyErbanNo record);

    List<PrettyErbanNo> selectByExample(PrettyErbanNoExample example);

    PrettyErbanNo selectByPrimaryKey(Integer prettyId);

    int updateByExampleSelective(@Param("record") PrettyErbanNo record, @Param("example") PrettyErbanNoExample example);

    int updateByExample(@Param("record") PrettyErbanNo record, @Param("example") PrettyErbanNoExample example);

    int updateByPrimaryKeySelective(PrettyErbanNo record);

    int updateByPrimaryKey(PrettyErbanNo record);
}
