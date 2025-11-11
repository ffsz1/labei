package com.xchat.oauth2.service.infrastructure.myaccountmybatis;

import com.xchat.oauth2.service.model.SmsRecord;
import com.xchat.oauth2.service.model.SmsRecordExample;
import java.util.List;

public interface SmsRecordMapper {
    int deleteByExample(SmsRecordExample example);

    int deleteByPrimaryKey(Long smsRecordId);

    int insert(SmsRecord record);

    int insertSelective(SmsRecord record);

    List<SmsRecord> selectByExample(SmsRecordExample example);

    SmsRecord selectByPrimaryKey(Long smsRecordId);

    int updateByPrimaryKeySelective(SmsRecord record);

    int updateByPrimaryKey(SmsRecord record);
}
