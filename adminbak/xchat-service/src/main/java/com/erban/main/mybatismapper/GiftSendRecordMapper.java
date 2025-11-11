package com.erban.main.mybatismapper;

import com.erban.main.model.GiftSendRecord;
import com.erban.main.model.GiftSendRecordExample;
import java.util.List;

public interface GiftSendRecordMapper {
    int deleteByPrimaryKey(Long sendRecordId);

    int insert(GiftSendRecord record);

    int insertSelective(GiftSendRecord record);

    List<GiftSendRecord> selectByExample(GiftSendRecordExample example);

    GiftSendRecord selectByPrimaryKey(Long sendRecordId);

    int updateByPrimaryKeySelective(GiftSendRecord record);

    int updateByPrimaryKey(GiftSendRecord record);
}
