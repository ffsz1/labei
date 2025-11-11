package com.erban.main.service.duty.impl;

import com.erban.main.model.Users;
import com.erban.main.service.duty.DutyResultService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
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
    private UsersService usersService;

    @Override
    public Byte checkUserDutyStatus(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        if (StringUtils.isBlank(users.getUserDesc())) {
            return 1;
        }
        return 2;
    }
}
