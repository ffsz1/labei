package com.erban.main.mybatismapper;

import com.erban.main.model.McoinDrawPrize;
import com.erban.main.model.McoinDrawPrizeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface McoinDrawPrizeMapper {
    int countByExample(McoinDrawPrizeExample example);

    int deleteByExample(McoinDrawPrizeExample example);

    int deleteByPrimaryKey(Long issueId);

    int insert(McoinDrawPrize record);

    int insertSelective(McoinDrawPrize record);

    List<McoinDrawPrize> selectByExample(McoinDrawPrizeExample example);

    McoinDrawPrize selectByPrimaryKey(Long issueId);

    int updateByExampleSelective(@Param("record") McoinDrawPrize record, @Param("example") McoinDrawPrizeExample example);

    int updateByExample(@Param("record") McoinDrawPrize record, @Param("example") McoinDrawPrizeExample example);

    int updateByPrimaryKeySelective(McoinDrawPrize record);

    int updateByPrimaryKey(McoinDrawPrize record);
}
