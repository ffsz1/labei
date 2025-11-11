package com.erban.admin.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.AutoGenRobot;
import com.erban.main.model.AutoGenRobotExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.AutoGenRobotMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.im.bo.ImRoomMemberBO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.RegexUtil;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.common.utils.CommonUtil;
import com.xchat.common.utils.StringUtils;
import com.xchat.common.utils.UUIDUtils;
import com.xchat.oauth2.service.common.KeyStore;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.core.encoder.MD5;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountExample;
import com.xchat.oauth2.service.service.account.NetEaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AutoGenRobotAdminService {
    private Logger logger = LoggerFactory.getLogger(AutoGenRobotAdminService.class);

    @Autowired
    private AutoGenRobotMapper autoGenRobotMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private NetEaseService netEaseService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ImRoomManager imRoomManager;

    /**
     * 获取生成机器人列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return
     */
    public PageInfo<AutoGenRobot> getList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AutoGenRobot> autoGenRobots = autoGenRobotMapper.selectByExample(null);
        return new PageInfo<>(autoGenRobots);
    }

    /**
     * 根据ID获取机器人信息
     *
     * @param id 机器人ID
     * @return
     */
    public AutoGenRobot getOne(int id) {
        return autoGenRobotMapper.selectById(id);
    }

    /**
     * 批量生成机器人账户
     *
     * @throws Exception
     */
    public void batchGenRobAccount(Integer type) throws Exception {
        Gson gson = new Gson();
        AutoGenRobotExample autoGenRobotExample = new AutoGenRobotExample();
        autoGenRobotExample.createCriteria().andAvatarIsNotNull();
        autoGenRobotExample.createCriteria().andIsUsedGreaterThan(new Byte("1"));
        List<AutoGenRobot> autoGenRobots = autoGenRobotMapper.selectByExample(autoGenRobotExample);
        Date date = new Date();
        for (int i = 0; i < autoGenRobots.size(); i++) {
            AutoGenRobot autoGenRobot = autoGenRobots.get(i);
            Long erbanNo = getErBanNo();
            String pwd = "";
            if (autoGenRobot.getNick().length() > 15) {
                continue;
            }
            if (!StringUtils.isBlank(autoGenRobot.getPassword()) && type == 2) {
                pwd = autoGenRobot.getPassword();
            } else {
                pwd = "123456789.";
            }
            Account account = new Account();
            account.setPassword(encryptPassword(pwd));
            account.setNeteaseToken(UUIDUtils.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setSignTime(date);
            account.setErbanNo(erbanNo);
            if (type == 2) {
                account.setPhone("13888888888");
                account.setIsShuijun((byte) 1);
            } else if (type == 3) {
                account.setPhone("13888888888");
                account.setIsShuijun((byte) 2);
            } else {
                account.setPhone(erbanNo.toString());
            }
            accountMapper.insert(account);
            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            logger.info("[调用云信 createNetEaseAcc]接口返回:{}", gson.toJson(tokenRet));
            Users users = new Users();
            users.setErbanNo(erbanNo);
            users.setPhone(erbanNo.toString());
            users.setUid(account.getUid());
            users.setAvatar(autoGenRobot.getAvatar());
            users.setNick(autoGenRobot.getNick());
            if (type == 2) {
                users.setDefUser(new Byte("1"));
            } else {
                users.setDefUser(new Byte("3"));
            }
            users.setGender(autoGenRobot.getGender());
            users.setCreateTime(new Date());
            usersMapper.insertSelective(users);
            BaseNetEaseRet baseNetEaseRet = netEaseService.updateUserInfo(uidStr, autoGenRobot.getNick(),
                    autoGenRobot.getAvatar());
            logger.info("[调用云信 updateUserInfo]接口返回:{}", gson.toJson(baseNetEaseRet));
        }
    }

    /**
     * 创建或修改机器人账户
     *
     * @param autoGenRobot 自动生成机器人信息
     * @param isEdit       是否修改
     * @return
     */
    public int createOrModifyRobot(AutoGenRobot autoGenRobot, boolean isEdit) {
        if (isEdit) {
            return autoGenRobotMapper.updateByPrimaryKeySelective(autoGenRobot);
        } else {
            return autoGenRobotMapper.insertSelective(autoGenRobot);
        }
    }

    /**
     * 批量修改机器人密码
     *
     * @param robotIds 机器人ID
     * @return
     */
    public int modifiedPassword(String robotIds, String password) {
        String[] robotIdArray = robotIds.split(",");
        List<Integer> robotIdList = Lists.newArrayList();
        for (int i = 0; i < robotIdArray.length; i++) {
            robotIdList.add(Integer.valueOf(robotIdArray[i]));
        }
        return autoGenRobotMapper.modifiedRobotPassword(robotIdList, password);
    }

    public void test() throws Exception {
        Long erbanNo = getErBanNo();
        String pwd = "123456";
        Gson gson = new Gson();
        String password = DESUtils.DESAndBase64Decrypt(pwd, KeyStore.DES_ENCRYPT_KEY);
        Account account = new Account();
        account.setPhone(erbanNo.toString());
        account.setPassword(password);
        account.setNeteaseToken(UUIDUtils.get());
        account.setLastLoginTime(new Date());
        account.setUpdateTime(new Date());
        account.setSignTime(new Date());
        account.setErbanNo(erbanNo);
        accountMapper.insert(account);
        String uidStr = String.valueOf(account.getUid());
        TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
        logger.info("[调用云信 createNetEaseAcc]接口返回:{}", gson.toJson(tokenRet));
        Users users = new Users();
        users.setErbanNo(erbanNo);
        users.setPhone(erbanNo.toString());
        users.setUid(account.getUid());
        users.setAvatar("http://res.91fb.com/FnRrvo_KPXKFyt5ZzgbqBBIyZmRl?imageslim");
        users.setNick("官方小秘书");
        users.setDefUser(new Byte("3"));
        users.setGender((byte) 1);
        users.setCreateTime(new Date());
        usersMapper.insertSelective(users);
        BaseNetEaseRet baseNetEaseRet = netEaseService.updateUserInfo(uidStr, "官方小秘书", "http://res.91fb" +
                ".com/FnRrvo_KPXKFyt5ZzgbqBBIyZmRl?imageslim");
        logger.info("[调用云信 updateUserInfo]接口返回:{}", gson.toJson(baseNetEaseRet));
    }

    /**
     * 生成 erbanNo
     *
     * @return long
     * @throws Exception
     */
    private Long getErBanNo() throws Exception {
        int digit = 7;
        Long erBanNo = generalNotPrettyId(digit);
        int num = 0;
        while (isExsisErbanAccount(erBanNo)) {
            num++;
            erBanNo = generalNotPrettyId(digit);
            if (num == 3) {
                digit++;
            } else if (num == 6) {
                digit++;
            } else if (num == 10) {
                throw new Exception("拉贝号生成异常!");
            }
        }
        return erBanNo;
    }

    private Long generalNotPrettyId(int digit) throws Exception {
        String generalId = "";
        boolean isPrettyFilter = true;
        int numFilter = 0;
        int condition = 5;
        while (isPrettyFilter) {
            numFilter++;
            generalId = CommonUtil.getRandomNumStr(digit);
            boolean dumpNumber = CommonUtil.checkMaxDumpNumber(generalId, condition);
            isPrettyFilter = RegexUtil.checkPretty(generalId);
            if (!isPrettyFilter && !dumpNumber) {
                break;
            }

            if (numFilter == 3) {
                condition--;
            } else if (numFilter == 6) {
                condition--;
            } else if (numFilter == 10) {
                throw new Exception("拉贝号生成异常!");
            }
            isPrettyFilter = true;

        }
        return Long.valueOf(generalId);
    }

    private boolean isExsisErbanAccount(Long erBanNo) {
        boolean flag = false;
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andErbanNoEqualTo(erBanNo);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (accounts != null && accounts.size() > 0) {
            flag = true;
        }
        return flag;
    }

    // 密码MD5加密
    private String encryptPassword(String password) {
        return MD5.getMD5(password);
    }
}
