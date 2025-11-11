package com.erban.main.mybatismapper;

import com.erban.main.model.ChargeRecord;
import com.erban.main.model.ChargeRecordExample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.erban.main.param.admin.ChargeRecordParam;
import com.erban.main.vo.admin.ChargeRecordVo;
import org.apache.ibatis.annotations.Param;

public interface ChargeRecordMapper {
    int countByExample(ChargeRecordExample example);

    int deleteByExample(ChargeRecordExample example);

    int deleteByPrimaryKey(String chargeRecordId);

    int insert(ChargeRecord record);

    int insertSelective(ChargeRecord record);

    List<ChargeRecord> selectByExample(ChargeRecordExample example);

    ChargeRecord selectByPrimaryKey(String chargeRecordId);

    int updateByExampleSelective(@Param("record") ChargeRecord record, @Param("example") ChargeRecordExample example);

    int updateByExample(@Param("record") ChargeRecord record, @Param("example") ChargeRecordExample example);

    int updateByPrimaryKeySelective(ChargeRecord record);

    int updateByPrimaryKey(ChargeRecord record);

    List<ChargeRecordVo> selectByQuery(ChargeRecordParam chargeRecordParam);

    List<ChargeRecordVo> queryUserCount(ChargeRecordParam chargeRecordParam);

    BigDecimal queryTotalAmount(ChargeRecordParam chargeRecordParam);

    List<ChargeRecord>  sumAmount(List<Long> uids);
}
