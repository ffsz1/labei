package com.erban.main.service.statis;

import com.erban.main.model.Statis;
import com.erban.main.mybatismapper.StatisMapper;
import com.xchat.common.UUIDUitl;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liuguofu on 2017/7/20.
 */
@Service
public class StatisService {
    @Autowired
    private StatisMapper statisMapper;
    public BusiResult saveLoginInfoStatic(Long uid,String url){
        Statis statis=new Statis();
        statis.setStatisId(UUIDUitl.get());
        statis.setUid(uid);
        statis.setFromLoginUrl(url);
        statis.setCreateTime(new Date());
        statisMapper.insert(statis);
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
