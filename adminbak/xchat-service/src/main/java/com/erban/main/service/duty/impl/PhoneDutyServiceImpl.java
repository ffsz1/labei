package com.erban.main.service.duty.impl;

import com.erban.main.model.Users;
import com.erban.main.service.duty.DutyResultService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: PhoneDutyServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
@Service("PhoneDutyService")
public class PhoneDutyServiceImpl implements DutyResultService {
    @Autowired
    private UsersService usersService;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        String phone = users.getPhone();
        if (CommonUtil.checkValidPhone(phone)) {
            return 2;
        }
        return 1;
    }
}
