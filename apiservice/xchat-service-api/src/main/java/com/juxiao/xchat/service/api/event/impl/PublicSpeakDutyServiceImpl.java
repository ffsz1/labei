package com.juxiao.xchat.service.api.event.impl;

import com.juxiao.xchat.dao.event.DutyDailyRecordDao;
import com.juxiao.xchat.dao.event.dto.DutyDailyRecordDTO;
import com.juxiao.xchat.service.api.event.DutyResultService;
import com.juxiao.xchat.service.api.event.DutyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PublicSpeakDutyService")
public class PublicSpeakDutyServiceImpl implements DutyResultService {
    @Autowired
    private DutyDailyRecordDao dailyRecordDao;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        DutyDailyRecordDTO record = dailyRecordDao.getFreshDuty(uid, DutyType.speak_in_public.getDutyId());
        return record == null ? 1 : record.getUdStatus();
    }
}
