package com.xchat.oauth2.service.service;

import com.xchat.oauth2.service.domain.user.User;
import com.xchat.oauth2.service.service.exceptions.InsertException;
import com.xchat.oauth2.service.service.exceptions.UpdateException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author liuguofu
 */
public interface UserService extends UserDetailsService {

    User loadUserByUid(Long uid);
    User queryUserByUsername(String username);

    String loadPasswordByUsername(String username);

    void updatePasswordByUsername(String username,String password) throws UpdateException;

    void updatePasswordByUid(Long uid,String password) throws UpdateException;

    boolean hasUser(String username);

    void saveUser(User user) throws InsertException;

    void updateUser(User user) throws UpdateException;

    User loadUserByQQOpenId(String openId);

    User loadUserByWeiXinOpenId(String openId);

    User loadUserByWeiBoOpenId(String openId);

    void updateQQBindInfoByUid(long uid,String openId,String qqNick);

    void updateWeiboBindInfoByUid(long uid,String openId,String weiboNick);

    void updateWeixinBindInfoByUid(long uid,String openId,String weixinNick);

    void updatePhoneBindInfoByUid(long uid,String phone,String password);


}
