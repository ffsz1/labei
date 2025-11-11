package com.xchat.oauth2.service.service.impl;

import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.common.exceptions.HaveMultiAccountException;
import com.xchat.oauth2.service.common.exceptions.UserHasSignUpException;
import com.xchat.oauth2.service.core.util.JsonMapper;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.domain.user.AccountNameInfo;
import com.xchat.oauth2.service.domain.user.AccountNameInfoRepository;
import com.xchat.oauth2.service.domain.user.User;
import com.xchat.oauth2.service.domain.user.UserRepository;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.UserService;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.service.exceptions.InsertException;
import com.xchat.oauth2.service.service.exceptions.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author liuguofu
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    //    @Autowired
    private UserRepository userRepository;

    //    @Autowired
    private AccountNameInfoRepository accountNameInfoRepository;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException, HaveMultiAccountException {
        Account account = accountService.getAccountByPhone(phone);
        if (account == null) {
            if (!CommonUtil.checkValidPhone(phone)) {
                if (CommonUtil.checkNumberOnly(phone)) {
                    account = accountService.getAccountByErBanNo(Long.valueOf(phone));
                }
            }
            if (account == null) {
                throw new UsernameNotFoundException("Not found any user for phone[" + phone + "]" + !CommonUtil.checkValidPhone(phone) + ":" + CommonUtil.checkNumberOnly(phone));
            }
        }
        return new AccountDetails(account);
    }

    public User queryUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(phone);
        if (user == null) {
            throw new UsernameNotFoundException("Not found any user for phone[" + phone + "]");
        }

        return user;
    }

    @Override
    public User loadUserByUid(Long uid) {
        return userRepository.findByUid(uid);
    }

    @Override
    public String loadPasswordByUsername(String username) {
        UserDetails user = loadUserByUsername(username);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    @Override
    public void updatePasswordByUsername(String username, String password) throws UpdateException {
        try {
            userRepository.updatePasswordByUsername(username, password);
        } catch (Exception e) {
            throw new UpdateException("failed update password", e);
        }
    }

    @Override
    public void updatePasswordByUid(Long uid, String password) throws UpdateException {
        try {
            userRepository.updatePasswordByUid(uid, password);
        } catch (Exception e) {
            throw new UpdateException("failed update password", e);
        }
    }

    @Override
    public boolean hasUser(String username) {
        try {
            AccountNameInfo accountNameInfo = accountNameInfoRepository.loadByAccountName(username);
            if (accountNameInfo != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void saveUser(User user) throws InsertException {
        try {
            //TODO 检查事务是否生效
            AccountNameInfo accountNameInfo = new AccountNameInfo();
            accountNameInfo.setAccountName(user.username());
            accountNameInfoRepository.saveAccountNameInfo(accountNameInfo);
            userRepository.saveUser(user);
            jedisService.hwrite("account", String.valueOf(user.uid()), JsonMapper.nonDefaultMapper().toJson(user));
        } catch (Exception e) {
            throw new InsertException("failed insert user", e);
        }
    }

    @Override
    public void updateUser(User user) throws UpdateException {
        try {
            userRepository.updateUser(user);
        } catch (Exception e) {
            throw new UpdateException("failed update user", e);
        }


    }

    @Override
    public User loadUserByQQOpenId(String openId) {
        return userRepository.findByQQOpenId(openId);
    }

    @Override
    public User loadUserByWeiXinOpenId(String openId) {
        return userRepository.findByWeiXinOpenId(openId);
    }

    @Override
    public User loadUserByWeiBoOpenId(String openId) {
        return userRepository.findByWeiBoOpenId(openId);
    }

    @Override
    public void updateQQBindInfoByUid(long uid, String openId, String qqNick) {
        userRepository.updateQQBindInfoByUid(uid, openId, qqNick);
    }

    @Override
    public void updateWeiboBindInfoByUid(long uid, String openId, String weiboNick) {
        userRepository.updateWeiboBindInfoByUid(uid, openId, weiboNick);
    }

    @Override
    public void updateWeixinBindInfoByUid(long uid, String openId, String weixinNick) {
        userRepository.updateWeixinBindInfoByUid(uid, openId, weixinNick);
    }

    @Override
    public void updatePhoneBindInfoByUid(long uid, String phone, String password) {
        if (accountNameInfoRepository.loadByAccountName(phone) != null) {
            throw new UserHasSignUpException("the phone have been sign up");
        }
        User user = loadUserByUid(uid);
        if (phone == null) {
            accountNameInfoRepository.removeAccountNameInfo(user.phone());
        } else {
            if (StringUtils.isBlank(user.phone())) {
                AccountNameInfo accountNameInfo = new AccountNameInfo();
                accountNameInfo.setAccountName(phone);
                accountNameInfoRepository.saveAccountNameInfo(accountNameInfo);
            } else {
                accountNameInfoRepository.updateAccountNameInfo(user.phone(), phone);
            }
        }
        userRepository.updatePhoneBindInfoByUid(uid, phone, password);
    }
}
