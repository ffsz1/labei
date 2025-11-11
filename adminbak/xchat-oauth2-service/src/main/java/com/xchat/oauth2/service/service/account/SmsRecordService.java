package com.xchat.oauth2.service.service.account;

import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.SmsRecordMapper;
import com.xchat.oauth2.service.model.SmsRecord;
import com.xchat.oauth2.service.model.SmsRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/10/26.
 */
@Service
public class SmsRecordService {
    @Autowired
    private SmsRecordMapper smsRecordMapper;
    public static int maxSmsPerIpCount = 10;

    public List<SmsRecord> getSmsRecordListByIp(String ipAddress) {
        SmsRecordExample smsRecordExample = new SmsRecordExample();
        Date date = new Date();
        smsRecordExample.createCriteria().andIpEqualTo(ipAddress).andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnights(date, 24));
        List<SmsRecord> smsRecords = smsRecordMapper.selectByExample(smsRecordExample);
        return smsRecords;
    }

    public boolean checkIsTooOftenIp(String ipAddress) {
        boolean result = false;
        List<SmsRecord> smsRecords = getSmsRecordListByIp(ipAddress);
        if (CollectionUtils.isEmpty(smsRecords)) {
            result = false;
        }
        if (smsRecords.size() >= maxSmsPerIpCount) {
            return true;
        }
        return result;
    }

    public void saveSmsRecord(String phone, String ip, String deviceId, String imei, String os, String osversion,
                              String channel, String appVersion, String model, String smsCode, Byte smsType) {
        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setPhone(phone);
        smsRecord.setIp(ip);
        smsRecord.setDeviceId(deviceId);
        smsRecord.setImei(imei);
        smsRecord.setOs(os);
        smsRecord.setOsversion(osversion);
        smsRecord.setChannel(channel);
        smsRecord.setAppVersion(appVersion);
        smsRecord.setModel(model);
        smsRecord.setSmsCode(smsCode);
        smsRecord.setSmsType(smsType);
        smsRecordMapper.insert(smsRecord);

    }

    public static void main(String args[]) {

        Date date = new Date();
        System.out.println(GetTimeUtils.getTimesnights(date, 0) + "--" + GetTimeUtils.getTimesnights(date, 24));
    }

}
