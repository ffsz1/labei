package com.erban.main.service.duty.impl;

import com.erban.main.model.Fans;
import com.erban.main.service.duty.DutyResultService;
import com.erban.main.service.user.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private FansService fansService;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        List<Fans> list = null;
        try {
            list = fansService.getFollowingListByUid(uid, 10, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        if (list == null || list.size() == 0) {
            return 1;
        }
        return 2;
    }
}
