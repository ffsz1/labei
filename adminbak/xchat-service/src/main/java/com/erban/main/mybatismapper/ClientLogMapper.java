package com.erban.main.mybatismapper;

import com.erban.main.model.ClientLog;
import com.erban.main.model.ClientLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientLogMapper {
    int countByExample(ClientLogExample example);

    int deleteByExample(ClientLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ClientLog record);

    int insertSelective(ClientLog record);

    List<ClientLog> selectByExample(ClientLogExample example);

    ClientLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ClientLog record, @Param("example") ClientLogExample example);

    int updateByExample(@Param("record") ClientLog record, @Param("example") ClientLogExample example);

    int updateByPrimaryKeySelective(ClientLog record);

    int updateByPrimaryKey(ClientLog record);
}
