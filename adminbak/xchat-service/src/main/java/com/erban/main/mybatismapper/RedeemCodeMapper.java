package com.erban.main.mybatismapper;

import com.erban.main.model.RedeemCode;
import com.erban.main.model.RedeemCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RedeemCodeMapper {
    int countByExample(RedeemCodeExample example);

    int deleteByExample(RedeemCodeExample example);

    int deleteByPrimaryKey(String code);

    int insert(RedeemCode record);

    int insertSelective(RedeemCode record);

    List<RedeemCode> selectByExample(RedeemCodeExample example);

    RedeemCode selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") RedeemCode record, @Param("example") RedeemCodeExample example);

    int updateByExample(@Param("record") RedeemCode record, @Param("example") RedeemCodeExample example);

    int updateByPrimaryKeySelective(RedeemCode record);

    int updateByPrimaryKey(RedeemCode record);
}
