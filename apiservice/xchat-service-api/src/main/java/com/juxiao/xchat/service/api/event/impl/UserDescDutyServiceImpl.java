package com.juxiao.xchat.service.api.event.impl;

import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.DutyResultService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传5张图片
 *
 * @class: UserDescDutyServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/8/9
 */
@Service("UserDescDutyService")
public class UserDescDutyServiceImpl implements DutyResultService {
    @Autowired
    private UsersManager usersManager;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        UsersDTO users = usersManager.getUser(uid);
        if (StringUtils.isBlank(users.getUserDesc())) {
            return 1;
        }
        return 2;
    }
}
