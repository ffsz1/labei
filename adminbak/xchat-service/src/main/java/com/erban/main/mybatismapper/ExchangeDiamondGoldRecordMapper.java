package com.erban.main.mybatismapper;

import com.erban.main.model.ExchangeDiamondGoldRecord;
import com.erban.main.model.ExchangeDiamondGoldRecordExample;
import com.erban.main.param.admin.ChargeRecordParam;
import com.erban.main.vo.admin.ExchangeDiamondVo;

import java.util.List;

public interface ExchangeDiamondGoldRecordMapper {
    int deleteByPrimaryKey(String recordId);

    int insert(ExchangeDiamondGoldRecord record);

    int insertSelective(ExchangeDiamondGoldRecord record);

    List<ExchangeDiamondGoldRecord> selectByExample(ExchangeDiamondGoldRecordExample example);

    ExchangeDiamondGoldRecord selectByPrimaryKey(String recordId);

    int updateByPrimaryKeySelective(ExchangeDiamondGoldRecord record);

    int updateByPrimaryKey(ExchangeDiamondGoldRecord record);

    List<ExchangeDiamondGoldRecord> sumExechangeDiamond(List<Long> uids);

    List<ExchangeDiamondVo> selectByQuery(ChargeRecordParam chargeRecordParam);
}
