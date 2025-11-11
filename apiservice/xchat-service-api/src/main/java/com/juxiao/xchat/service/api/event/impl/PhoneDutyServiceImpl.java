package com.juxiao.xchat.service.api.event.impl;

import com.juxiao.xchat.base.utils.DataValidationUtils;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.DutyResultService;
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
    private UsersManager usersManager;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            return 1;
        }
        if (DataValidationUtils.validatePhone(usersDto.getPhone())) {
            return 2;
        }
        return 1;
    }
}
