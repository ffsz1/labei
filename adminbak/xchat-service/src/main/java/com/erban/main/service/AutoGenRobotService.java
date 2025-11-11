package com.erban.main.service;

import com.erban.main.model.AutoGenRobot;
import com.erban.main.model.AutoGenRobotExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.AutoGenRobotMapper;
import com.erban.main.service.user.UsersService;
import com.xchat.common.UUIDUitl;
import com.xchat.common.netease.neteaseacc.result.FileUploadRet;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.oauth2.service.core.encoder.MD5;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.account.ErBanNoService;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/8/10.
 */
@Service
public class AutoGenRobotService {
    @Autowired
    private AutoGenRobotMapper autoGenRobotMapper;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private NetEaseService netEaseService;
    @Autowired
    private ErBanNoService erBanNoService;
    @Autowired
    private UsersService usersService;

    public void saveName(String name) {
        AutoGenRobot autoGenRobot = autoGenRobotMapper.selectByPrimaryKey(name);
        autoGenRobot = new AutoGenRobot();
        autoGenRobot.setNick(name);
        autoGenRobotMapper.insert(autoGenRobot);
//        if (autoGenRobot == null) {
//            autoGenRobot = new AutoGenRobot();
//            autoGenRobot.setNick(name);
//            autoGenRobotMapper.insert(autoGenRobot);
//        }
    }

    public void uploadAvatar(File file) throws Exception {
        AutoGenRobotExample AutoGenRobotExample = new AutoGenRobotExample();
        AutoGenRobotExample.createCriteria().andAvatarIsNull();
        List<AutoGenRobot> autoGenRobotList = autoGenRobotMapper.selectByExample(AutoGenRobotExample);

        File[] files = file.listFiles();
        int filesLength = files.length;
        int robcount = 0;
        for (File file2 : files) {
            FileUploadRet fileUploadRet = erBanNetEaseService.uploadFile(file2);
            if (fileUploadRet.getCode() == 200) {
                AutoGenRobot autoGenRobot = autoGenRobotList.get(robcount);
                String fileUrl = fileUploadRet.getUrl();
                autoGenRobot.setAvatar(fileUrl);
                autoGenRobotMapper.updateByPrimaryKey(autoGenRobot);
                robcount++;
            }
        }

    }

    public void batchGenRobAccount() throws Exception {
        AutoGenRobotExample AutoGenRobotExample = new AutoGenRobotExample();
        AutoGenRobotExample.createCriteria().andAvatarIsNotNull().andIsUsedEqualTo(new Byte("1"));
        List<AutoGenRobot> autoGenRobotList = autoGenRobotMapper.selectByExample(AutoGenRobotExample);
        Date date = new Date();
        for (int i = 0; i < autoGenRobotList.size(); i++) {
            AutoGenRobot autoGenRobot = autoGenRobotList.get(i);
            Long erbanNo = erBanNoService.getErBanNo();
            String password = "1234567890.";
            if (autoGenRobot.getNick().length() > 15) {
                continue;
            }

            Account account = new Account();
            account.setPhone(erbanNo.toString());
            account.setPassword(encryptPassword(password));
            account.setNeteaseToken(UUIDUitl.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setSignTime(date);
            account.setErbanNo(erbanNo);
            accountMapper.insert(account);
            String uidStr = String.valueOf(account.getUid());

            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            Users users = new Users();
            users.setUid(account.getUid());
            users.setAvatar(autoGenRobot.getAvatar());
            users.setNick(autoGenRobot.getNick());
            users.setDefUser(new Byte("3"));
            users.setGender(autoGenRobot.getGender());
            usersService.saveOrUpdateUserByUidV2(users,null);
            erBanNetEaseService.updateUserGenderOnly(account.getUid().toString(),autoGenRobot.getGender());
        }
    }

    private String encryptPassword(String password) {
        return MD5.getMD5(password);
    }

    public static void main(String args[]) throws Exception {
        AutoGenRobotService AutoGenRobotService = new AutoGenRobotService();
        File filePic = new File("D:\\beatu");//Text文件
        AutoGenRobotService.uploadAvatar(filePic);
    }
}
