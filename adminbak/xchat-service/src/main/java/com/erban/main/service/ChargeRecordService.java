package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.ChargeRecord;
import com.erban.main.model.ChargeRecordExample;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.xchat.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeRecordService {

    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

    public List<ChargeRecord> getAllUnRecharge() {
        ChargeRecordExample example = new ChargeRecordExample();
        List<Byte> list = Lists.newArrayList();
        list.add(Constant.ChargeRecordStatus.finish);
        list.add(Constant.ChargeRecordStatus.error);
        example.createCriteria().andChargeStatusNotIn(list);
        List<ChargeRecord> chargeRecordList = chargeRecordMapper.selectByExample(example);
        return chargeRecordList;
    }

    public void UpdateChargeRecord(ChargeRecord record) {
        chargeRecordMapper.updateByPrimaryKey(record);
    }

}
