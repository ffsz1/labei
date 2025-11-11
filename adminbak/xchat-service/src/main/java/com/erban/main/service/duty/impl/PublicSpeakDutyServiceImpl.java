package com.erban.main.service.duty.impl;

import com.erban.main.mybatismapper.duty.DutyDailyRecordMapper;
import com.erban.main.mybatismapper.duty.domain.DutyDailyRecord;
import com.erban.main.service.duty.DutyResultService;
import com.erban.main.service.duty.DutyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PublicSpeakDutyService")
public class PublicSpeakDutyServiceImpl implements DutyResultService {
    @Autowired
    private DutyDailyRecordMapper dailyRecordMapper;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        DutyDailyRecord record = dailyRecordMapper.getFreshDuty(uid, DutyType.speak_in_public.getDutyId());
        return record == null ? 1 : record.getUdStatus();
    }
}
