package com.erban.main.mybatismapper;

import com.erban.main.model.RedeemCodeRecord;
import com.erban.main.model.RedeemCodeRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RedeemCodeRecordMapper {
    int countByExample(RedeemCodeRecordExample example);

    int deleteByExample(RedeemCodeRecordExample example);

    int deleteByPrimaryKey(String recordId);

    int insert(RedeemCodeRecord record);

    int insertSelective(RedeemCodeRecord record);

    List<RedeemCodeRecord> selectByExample(RedeemCodeRecordExample example);

    RedeemCodeRecord selectByPrimaryKey(String recordId);

    int updateByExampleSelective(@Param("record") RedeemCodeRecord record, @Param("example") RedeemCodeRecordExample example);

    int updateByExample(@Param("record") RedeemCodeRecord record, @Param("example") RedeemCodeRecordExample example);

    int updateByPrimaryKeySelective(RedeemCodeRecord record);

    int updateByPrimaryKey(RedeemCodeRecord record);
}
