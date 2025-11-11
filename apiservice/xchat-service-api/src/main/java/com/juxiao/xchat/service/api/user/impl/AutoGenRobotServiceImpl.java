package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.sysconf.domain.AutoGenRobot;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.AutoGenRobotDao;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.manager.common.erbanno.ErBanNoManager;
import com.juxiao.xchat.manager.external.netease.NetEaseAccManager;
import com.juxiao.xchat.service.api.user.AutoGenRobotService;
import com.juxiao.xchat.service.api.user.UsersService;
import com.juxiao.xchat.service.api.user.bo.UserUpdateBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AutoGenRobotServiceImpl implements AutoGenRobotService {
    @Autowired
    private AutoGenRobotDao autoGenRobotDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private NetEaseAccManager netEaseAccManager;
    @Autowired
    private ErBanNoManager erBanNoManager;
    @Autowired
    private UsersService usersService;

    @Override
    public void batchGenRobAccount() throws Exception {
        List<AutoGenRobot> autoGenRobotList = autoGenRobotDao.list();
        Date date = new Date();
        for (int i = 0; i < autoGenRobotList.size(); i++) {
            AutoGenRobot autoGenRobot = autoGenRobotList.get(i);
            Long erbanNo = erBanNoManager.getErBanNo();
            String password = "1234567890.";
            if (autoGenRobot.getNick().length() > 15) {
                continue;
            }
            AccountDTO account = new AccountDTO();
            account.setPhone(erbanNo.toString());
            account.setPassword(encryptPassword(password));
            account.setNeteaseToken(UUIDUtils.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setSignTime(date);
            account.setErbanNo(erbanNo);
            accountDao.insert(account);
            String uidStr = String.valueOf(account.getUid());

            netEaseAccManager.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            UserUpdateBO users = new UserUpdateBO();
            users.setUid(account.getUid());
            users.setAvatar(autoGenRobot.getAvatar());
            users.setNick(autoGenRobot.getNick());
            users.setDefUser(new Byte("3"));
            users.setGender(autoGenRobot.getGender());
            usersService.saveOrUpdateUser(users, null);
        }
    }

    private String encryptPassword(String password) {
        return MD5Utils.getMD5(password);
    }

}
