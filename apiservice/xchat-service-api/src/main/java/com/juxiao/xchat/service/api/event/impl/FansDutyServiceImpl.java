package com.juxiao.xchat.service.api.event.impl;

import com.juxiao.xchat.dao.user.FansDao;
import com.juxiao.xchat.service.api.event.DutyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关注任务
 *
 * @class: FansDutyServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
@Service("FansDutyService")
public class FansDutyServiceImpl implements DutyResultService {
    @Autowired
    private FansDao fansDao;


    @Override
    public Byte checkUserDutyStatus(Long uid) {
        int followCount = fansDao.countUserFollow(uid);
        return followCount > 0 ? (byte) 2 : (byte) 1;
    }
}
