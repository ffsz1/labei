package com.erban.main.mybatismapper;

import com.erban.main.model.WxRecord;
import com.erban.main.model.WxRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxRecordMapper {
    int countByExample(WxRecordExample example);

    int deleteByExample(WxRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(WxRecord record);

    int insertSelective(WxRecord record);

    List<WxRecord> selectByExample(WxRecordExample example);

    WxRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") WxRecord record, @Param("example") WxRecordExample example);

    int updateByExample(@Param("record") WxRecord record, @Param("example") WxRecordExample example);

    int updateByPrimaryKeySelective(WxRecord record);

    int updateByPrimaryKey(WxRecord record);
}
