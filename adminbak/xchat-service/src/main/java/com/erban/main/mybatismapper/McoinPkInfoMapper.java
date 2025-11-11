package com.erban.main.mybatismapper;

import com.erban.main.model.McoinPkInfo;
import com.erban.main.model.McoinPkInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface McoinPkInfoMapper {
    int countByExample(McoinPkInfoExample example);

    int deleteByExample(McoinPkInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(McoinPkInfo record);

    int insertSelective(McoinPkInfo record);

    List<McoinPkInfo> selectByExample(McoinPkInfoExample example);

    McoinPkInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") McoinPkInfo record, @Param("example") McoinPkInfoExample example);

    int updateByExample(@Param("record") McoinPkInfo record, @Param("example") McoinPkInfoExample example);

    int updateByPrimaryKeySelective(McoinPkInfo record);

    int updateByPrimaryKey(McoinPkInfo record);

    List<McoinPkInfo> findMcoinPkInfo(@Param("term") int term, @Param("pkStatus") int pkStatus);

    McoinPkInfo findByPkStatus(byte pkStatus);
}
