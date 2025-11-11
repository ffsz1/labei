package com.erban.admin.main.service;

import com.erban.main.model.StatShareCharge;
import com.erban.main.model.StatShareChargeExample;
import com.erban.main.mybatismapper.StatShareChargeMapper;
import com.xchat.common.utils.GetTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatShareChargeService {
    @Autowired
    private StatShareChargeMapper statShareChargeMapper;


    public List<StatShareCharge> getShareChargeLists() {
        Date date = new Date();
        StatShareChargeExample example = new StatShareChargeExample();
        example.createCriteria().andStatDateBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnights(date, 24));
        List<StatShareCharge> statShareChargeList = statShareChargeMapper.selectByExample(example);
        return statShareChargeList;
    }
}
