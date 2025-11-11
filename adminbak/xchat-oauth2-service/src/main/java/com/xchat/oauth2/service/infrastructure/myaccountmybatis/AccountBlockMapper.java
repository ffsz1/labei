package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.AccountBlock;
import com.xchat.oauth2.service.model.AccountBlockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountBlockMapper {
    int countByExample(AccountBlockExample example);

    int deleteByExample(AccountBlockExample example);

    int deleteByPrimaryKey(Integer blockId);

    int insert(AccountBlock record);

    int insertSelective(AccountBlock record);

    List<AccountBlock> selectByExample(AccountBlockExample example);

    AccountBlock selectByPrimaryKey(Integer blockId);

    int updateByExampleSelective(@Param("record") AccountBlock record, @Param("example") AccountBlockExample example);

    int updateByExample(@Param("record") AccountBlock record, @Param("example") AccountBlockExample example);

    int updateByPrimaryKeySelective(AccountBlock record);

    int updateByPrimaryKey(AccountBlock record);
}
