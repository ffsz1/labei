package com.erban.main.service.statis;

import com.erban.main.model.StatPacketRegister;
import com.erban.main.model.StatPacketRegisterExample;
import com.erban.main.mybatismapper.StatPacketRegisterMapper;
import com.erban.main.mybatismapper.UserPacketRecordExpand;
import com.erban.main.mybatismapper.UserPacketRecordMapper;
import com.xchat.common.UUIDUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


@Service
public class StatPacketRegisterService {

    @Autowired
    private StatPacketRegisterMapper statPacketRegisterMapper;


    /**
     * 注册红包统计
     * @param uid 分享人uid
     * @param registerUid 被分享人uid
     * @return
     */
    public int saveStatPacketRegister(Long uid,Long registerUid){
        int count=0;
        StatPacketRegisterExample statPacketRegisterExample=new StatPacketRegisterExample();
        statPacketRegisterExample.createCriteria().andUidEqualTo(uid).andRegisterUidEqualTo(registerUid);
        List<StatPacketRegister> statPacketRegisterList=statPacketRegisterMapper.selectByExample(statPacketRegisterExample);
        if(CollectionUtils.isEmpty(statPacketRegisterList)){
            StatPacketRegister statPacketRegister=new StatPacketRegister();
            statPacketRegister.setRegisterId(UUIDUitl.get());
            statPacketRegister.setUid(uid);
            statPacketRegister.setRegisterUid(registerUid);
            statPacketRegister.setCreateTime(new Date());
            statPacketRegisterMapper.insert(statPacketRegister);
            count=1;
        }else{
            count=0;
        }
        return count;
    }
    public StatPacketRegister getShareRegisterByRegisterUid(Long registerUid){
        StatPacketRegisterExample statPacketRegisterExample=new StatPacketRegisterExample();
        statPacketRegisterExample.createCriteria().andRegisterUidEqualTo(registerUid);
        List<StatPacketRegister> statPacketRegisterList=statPacketRegisterMapper.selectByExample(statPacketRegisterExample);
        if(!CollectionUtils.isEmpty(statPacketRegisterList)){
            return statPacketRegisterList.get(0);
        }else{
            return null;
        }
    }


}
